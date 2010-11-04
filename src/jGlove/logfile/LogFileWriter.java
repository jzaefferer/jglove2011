package jGlove.logfile;

import jGlove.Core;
import jGlove.JGlovePlugin;
import jGlove.shared.ISource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Provides methods to create a CSV conform header and data lines
 * with a format defined by a <code>LogFileFormat</code>, writes 
 * this data into the file when the <code>Thread</code> is started.
 */
public class LogFileWriter extends Thread {

	private ISource[] sc = Core.getDefault().getActives();
	
//	private int interval = 10;
	
	private LogFileFormat lff;
	
	/**
	 * invisible default constructor
	 */
	@SuppressWarnings("unused")
	private LogFileWriter() {}
	
	/**
	 * creates a new LogFileWriter with the given LogFileFormat
	 * @param logFileformat
	 */
	public LogFileWriter(LogFileFormat logFileformat) {
		lff = logFileformat;	
	}
	
     /**
       * formats strings into .csv compatible strings 
       * 
       * @return String
       */
    private String replaceString(String name) {		  
	   name = name.replaceAll("\"","\"\""); //$NON-NLS-1$ //$NON-NLS-2$
	   name = "\""+name+"\""; //$NON-NLS-1$ //$NON-NLS-2$
	   return name; 
    }
	   
    private OutputStream openOutputStream() {
    	File file = null;
    	file = new File(lff.getPath());
    	if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				JGlovePlugin.log(e1);
			}
    	try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JGlovePlugin.log(e);
		}
		return null;
    }
    
    /**
     * method returns header
     * 
     * @return String
     */
    public String createHeader() {
		
		   StringBuffer header = new StringBuffer();
		
		   if (lff.isTimestamp() && !(lff.isTimestampEnd()) ){
			   header.append(Messages.LogFileWriter_header_timestamp);
			   header.append(lff.getSeparator());
		   }
		   if (lff.isSourceName()){
			   for(int i=0; i<sc.length;i++){ 
                   int type = sc[i].getType();
				   if ( (type != 3) || (type == 3 && lff.isGesture()) ) {
				       header.append(replaceString(sc[i].getName()) );
				       header.append(lff.getSeparator()); 
				   }
			   }
		   }
		   else {
			   for(int i=0; i<sc.length;i++){ 
                   int type = sc[i].getType();
				   if ( (type != 3) || (type == 3 && lff.isGesture()) ) {
					   if (type == 3) {
						   if(lff.isGesture()) { 
						       header.append(Messages.LogFileWriter_header_gesture);
					           header.append(i);
					           header.append(lff.getSeparator());
					       }
					   }
					   else {
						   header.append(Messages.LogFileWriter_header_sensor);
			               header.append(i);
				           header.append(lff.getSeparator());
					   }
					}
			   }
		   }
		   if (lff.isTimestamp() && lff.isTimestampEnd() ){ 
			   header.append(Messages.LogFileWriter_header_timestamp);
		   }   
		   else { //delete last separator
               int length = header.length();
			   header.delete( (length - lff.getSeparator().length()), length);
		   }
    	return header.toString();
    }
    
    /**
     * method creates and returns a logfile line
     * 
     * @return String
     */
    public String createLine() {
      	
    	StringBuffer line = new StringBuffer();
		   //add timestamp at the beginning
		   if (lff.isTimestamp() && !(lff.isTimestampEnd()) ){
			   line.append(System.currentTimeMillis());
			   line.append(lff.getSeparator());
		   }
		   // add sensor names and values
		   for(int i=0; i<sc.length;i++){ 
		       int type = sc[i].getType();
			   if ( (type != 3) || (type == 3 && lff.isGesture()) ) {
				   
				   // add sensor value
				   switch (lff.getFormat()) {	   
					   case 0: 
						   line.append(sc[i].getValue());
					       break;
					   case 1:
						   line.append(sc[i].getValueScaled());
						   break;
					   case 2:
						   line.append(sc[i].getValueMidi());
						   break;
					   default: 
						   break;
				   }   
			   line.append(lff.getSeparator());
			   }
		   }
		  
		   //add timestamp at the end
		   if (lff.isTimestamp() && lff.isTimestampEnd() ){
			   line.append(System.currentTimeMillis());
		   }
		   else {//delete last separator
			   int length = line.length();
			   line.delete( (length - lff.getSeparator().length()), length);
		   }    	
    	return line.toString();
    }
    
    /**
     * Opens the file defined in the logfileformat, writes the header
     * (createHeader()) and, inside a loop, the datalines (createLine()).
     */
	public void run() {
		PrintStream stream = new PrintStream(openOutputStream());
		
		String header = createHeader();
		// write header
	    stream.println(header);
	    long time = 0;
		while(!isInterrupted()) {
			try {
				Thread.sleep(lff.interval);
			} catch(InterruptedException e) {
				interrupt();
			}
            
            // write line ( only one line per millisecond )
			   if ( System.currentTimeMillis() != time ){
                   String line = createLine();
				   stream.println(line);
	            // cache time to prevent spamming timestamp
				   time = System.currentTimeMillis();
			   }
		}
		stream.close();
	}
}	