package jGlove;

import java.io.File;

public class FileLister {

	private void listFiles(File file) {
		if(file == null) {
			return;
		}
		File[] files =  file.listFiles();
		if(files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			File file2 = files[i];
			if(file2.isDirectory()) {
				listFiles(file2);
			} else {
				String temp = file2.getAbsolutePath();
				if(temp.matches(".*jd2xx.*")) continue;
				if(temp.matches(".*essages.*")) continue;
				System.out.println(temp.substring(28, temp.length()-5));
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("src");
		new FileLister().listFiles(file);
	}

}
