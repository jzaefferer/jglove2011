package jGlove;

public class WLANGloveBoxTest {
	
	public static void main(String[] args) throws Exception {
		GloveBox gb = new WLANGloveBox();
		int[] channels = gb.getAllChannels();
		System.out.println(channels[0]);
		System.out.println(channels[1]);
		System.out.println(channels[2]);
		System.out.println(channels[3]);
		System.out.println(channels[4]);
	}
}
