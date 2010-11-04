package jGlove.filter;



public class TiltFilterTest {
	 //testarray
	private static int[] test = {1,1,100,98,102,100,105,103,94,98,110,103,95,100,104,100,78,60,58,56,56,50,58,100,105,101,103,108,113,118,123,123,115,122,122,20,20,20,100};

	/**
	 * testapplication  
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
         
		TiltFilter tf = new TiltFilter(4);
		for (int i=0; i<test.length; i++){
			System.out.println (i+": O-Wert:"+test[i]+" Neu:"+tf.filter(test[i]));
		}
	}
}
