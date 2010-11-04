/**
 * 
 */
package jGlove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jd2xx.JD2XX;

/**
 * Provides an interface to the hardware glovebox.
 * Connection to the hardware is established via the D2XX
 * wrapper class (JD2XX), the rest of the communication
 * is done by sending and reading bytes.    
 * 
 * Usage example:
 * <code>
 * GloveBox gb = new GloveBox();
 * int[] channels = gb.getAllChannels();
 * String firmware = gb.getFirmware();
 * </code>
 * 
 */
public class J2DXXGloveBox extends GloveBox {
	
	/**
	 * the connection to the device
	 */
	private JD2XX jd2xxConnection = null;
	
	/**
	 * firmware version of the connected glovebox
	 */
	private String firmware = null;
	
	/**
	 * hardware version of the connected glovebox
	 */
	private String hardware = null;

	/**
	 * product id of the connected glovebox
	 */
	private String productID = null;
	
	
	/** connects to avaible devices
	 * @return true, if the attempt to connect was successful
	 */
	private boolean connectGloveBox() throws Exception {

		jd2xxConnection = new JD2XX();
		if (jd2xxConnection == null) {
			throw new Exception(Messages.GloveBox_error_nodriver);
		}


		Object[] devs;
		try {
			devs = jd2xxConnection.listDevicesByDescription();
		} catch (IOException e) {
			if (JGlovePlugin.getDefault() != null) {
				JGlovePlugin.log(e);
			}
			throw new Exception(Messages.GloveBox_error_deviceinuse);
		}
		
		String description = ""; //$NON-NLS-1$
		
		if (devs.length == 1) {
			/*
			 * only one device found
			 */
			description = String.valueOf(devs[0]);
		} 
		else if (devs.length > 1) {
			/*
			 * more then one found
			 * TODO configure/choose device
			 */
			description = String.valueOf(devs[0]);
		}
		else {
			/*
			 * no device found
			 */
			throw new Exception(Messages.GloveBox_error_nodevice);
		}
		/*
		 * connect and set up options
		 */
		try {
			jd2xxConnection.openByDescription(description);
			jd2xxConnection.setBaudRate(1000000);
			jd2xxConnection.setFlowControl(JD2XX.FLOW_NONE, 0, 0);
			jd2xxConnection.setTimeouts(30, 1000);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * constructor
	 */
	// FIXME throw something less generic
	public J2DXXGloveBox() throws Exception {
		if(!connectGloveBox()) {
			throw new Exception(Messages.GloveBox_error_unexpected);
		}
	}
	
	/**
	 * returns the values of all channels
	 * 
	 * 
	 * @return array of integers
	 */
	public int[] getAllChannels() {
		try {
			jd2xxConnection.write(CMD_GET_ALL_CHANNELS);
			int[] channels = descrambleData(jd2xxConnection.read(38));
			int[] copy = new int[channels.length-1];
			// cut of first value, as it is alway zero
			System.arraycopy(channels, 1, copy, 0, channels.length-1);
			return copy;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	/**
	 * if the hardware version was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the hardware version of the connected GloveBox
	 */
	public String getHardware() {
		if(hardware == null) {
			byte[] hardwareBytes = null;
			try {
				jd2xxConnection.write(CMD_GET_HARDWARE_VERSION);
				hardwareBytes = jd2xxConnection.read(40);
			} catch (IOException e) {
				e.printStackTrace();
			}
			hardware = new String(hardwareBytes);
		}
        return hardware;
	}
	
	/**
	 * if the firmware version was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the firmware version of the connected GloveBox
	 */
	public String getFirmware() {
		if(firmware == null) {
			byte[] firmwareBytes = null;
			try {
				jd2xxConnection.write(CMD_GET_FIRMWARE_VERSION);
				firmwareBytes = jd2xxConnection.read(40);
			} catch (IOException e) {
				e.printStackTrace();
			}
			firmware = new String(firmwareBytes);
		}
        return firmware;
	}
	
	/**
	 * if the productID was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the productID of the connected GloveBox
	 */
	public String getProductID() {
		if(productID == null) {
			byte[] productIDBytes = null;
			try {
				jd2xxConnection.write(CMD_GET_PRODUCT_ID);
				productIDBytes = jd2xxConnection.read(40);
			} catch (IOException e) {
				e.printStackTrace();
			}
			productID = new String(productIDBytes);
		}
        return productID;
	}
	
	public void debug(int anz) {
		try {
			System.out.println("CMD_GET_ALL_CHANNELS: "+jd2xxConnection.write(CMD_GET_ALL_CHANNELS)); //$NON-NLS-1$
			byte[] bytes = jd2xxConnection.read(38);
			for(int i=0; i<anz; i++) {
				String b = Integer.toBinaryString((int)bytes[i]);
				while(b.length() < 8) {
					b = "0"+b; //$NON-NLS-1$
				}
				System.out.println("Byte Nr. "+i); //$NON-NLS-1$
				System.out.println("base10: "+bytes[i]); //$NON-NLS-1$
				System.out.println("base2 : "+b.substring(b.length()-8)); //$NON-NLS-1$
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void close() {
		try {
			jd2xxConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
			JGlovePlugin.log(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		GloveBox gloveBox = new J2DXXGloveBox();
		while (true) {
			List<Integer> channels = new ArrayList<Integer>();
			for(int i : gloveBox.getAllChannels()) {
				channels.add(i);
			}
			System.out.println("channel: " + channels);
		}
	}
}
