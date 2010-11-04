package jGlove;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class WLANGloveBox extends GloveBox {
	
	private Socket connection;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public WLANGloveBox(String host, int port) throws UnknownHostException, IOException {
		init(host, port);
	}
	
	public WLANGloveBox() throws UnknownHostException, IOException {
		File config = new File("wlanconfig.properties");
		Properties wlanProps = new Properties();
		wlanProps.load(new FileInputStream(config));
		String host = wlanProps.getProperty("host");
		int port = Integer.parseInt(wlanProps.getProperty("port"));
		init(host, port);
	}

	private void init(String host, int port) throws UnknownHostException, IOException {
		connection = new Socket(host, port);
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		writer = new PrintWriter(connection.getOutputStream());
	}

	public int[] getAllChannels() {
		try {
			writer.write(CMD_GET_ALL_CHANNELS);
			writer.flush();
			byte[] data = new byte[38];
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) reader.read();
			}
			int[] channels = descrambleData(data);
			int[] copy = new int[channels.length - 1];
			// cut of first value, as it is alway zero
			System.arraycopy(channels, 1, copy, 0, channels.length - 1);
			return copy;
		} catch (IOException ex) {
			ex.printStackTrace();
			JGlovePlugin.log(ex);
		}
		return null;
	}

	public String getHardware() {
		// TODO implement
		return null;
	}

	public String getFirmware() {
		// TODO implement
		return null;
	}

	public String getProductID() {
		// TODO implement
		return null;
	}

	public void debug(int anz) {
		// TODO implement
	}

	public void close() {
		try {
			reader.close();
			writer.close();
			connection.close();
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	

}
