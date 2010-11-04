package jGlove;

public abstract class GloveBox implements IGloveBox {

	/**
	 * commandos to interact with the device
	 */
	public static final int CMD_GET_FIRMWARE_VERSION = 0x11;
	public static final int CMD_GET_HARDWARE_VERSION = 0x12;
	public static final int CMD_GET_PRODUCT_ID = 0x13;
	public static final int CMD_GET_STATUS_PORT0 = 0x14;
	public static final int CMD_GET_STATUS_PORT1 = 0x15;
	public static final int CMD_SET_SWITCH_MODE = 0x17;
	public static final int CMD_GET_SPIKE_FILTER_THRESHOLD = 0x26;
	
	public static final int CMD_GET_ALL_CHANNELS = 0x35;
	public static final int CMD_GET_ALL_CHANNELS_BURST = 0x36;
	public static final int CMD_GET_ALL_MIDI_CHANNELS = 0x4A;
	
	public static final byte BUTTON_MODE_MIDI = 0x00;
	public static final byte BUTTON_MODE_RAW = 0x01;
	public static final byte BUTTON_MODE_DISABLED = 0x02;
	/**
	 * maximum number of channels
	 */
	private int maxchannels = 25;
	/**
	 * helper method, converts (signed) byte to unsigned int
	 * 
	 * @param value a signed byte value
	 * @return a unsigned int value
	 */
	private int byteToUnsignedInt(byte value) {
		return (value & 0x7F) + (value < 0 ? 128 : 0);
	}
	/** 
	 * descrambles the raw data
	 * 
	 * @param scrambled_data byte array, signed bytes from Java perspective
	 * @return array of descrambled channels
	 */
	protected int[] descrambleData(byte[] scrambled_data) {
		
		// converts the signed bytes to unsigned integers
		int[] data = new int[scrambled_data.length];
		for (int i = 0; i < scrambled_data.length; i++) {
			data[i] = byteToUnsignedInt(scrambled_data[i]);
		}
	
		/*
		 * calculates the necessary loops to process the scrambled data
		 */ 
		int mainLoopCounter = Math.round(data.length / 3);
	
		/*
		 * create array to save the descrambled data
		 */
		int[] channels = new int[maxchannels];
	
		/* loops through the scrambled data
		 * two datarows are processed to two channels per cycle
		 * 
		 * one channel consists of one and a half data row
		 */
		for (int resultIndex = 0, dataIndex = 0; resultIndex < mainLoopCounter; resultIndex++) {
			// first channel, composed of the first row and first half of second row
			channels[resultIndex * 2 + 1] = data[dataIndex] + ((data[dataIndex + 1] & 0xf0) << 4);
			// second channel, composed of third row and second half of second row
			channels[resultIndex * 2 + 2] = data[dataIndex + 2] + ((data[dataIndex + 1] & 0x0f) << 8);
			// three rows processed, increased dataIndex by three
			dataIndex += 3;
		}
	
		return channels;
	}

	

}
