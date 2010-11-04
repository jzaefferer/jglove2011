package jGlove;

public interface IGloveBox {

	/**
	 * returns the values of all channels
	 * 
	 * 
	 * @return array of integers
	 */
	public int[] getAllChannels();

	/**
	 * if the hardware version was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the hardware version of the connected GloveBox
	 */
	public String getHardware();

	/**
	 * if the firmware version was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the firmware version of the connected GloveBox
	 */
	public String getFirmware();

	/**
	 * if the productID was not requested before, it will be
	 * retrieved from the connected GloveBox
	 * @return the productID of the connected GloveBox
	 */
	public String getProductID();

	public void debug(int anz);

	public void close();

}