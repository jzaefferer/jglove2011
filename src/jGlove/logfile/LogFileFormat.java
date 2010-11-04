package jGlove.logfile;

/**
 * A logfileformat describes the path of the logfile, the format of
 * header and the format of the data rows.
 */
public class LogFileFormat {
   
	/**
	 * wheather to write a timestamp in every line or not
	 * (default is true)
	 */
	public boolean timestamp = true;
	
	/**
	 * wheather to put the timestamp at the end of 
	 * the line (true) or at the start (false)
	 * (default is false)
	 */
	private boolean timestampEnd=false;
	
	/**
	 * wheather to display the name of the source in header (default is false)
	 */
	private boolean sourceName=true;
	
	/**
	 * which format to use:
	 * <p>
	 * 0 - 10bit
	 * <p>
	 * 1 - 8bit
	 * <p>
	 * 2 - 7bit
	 * <p>
	 * (default is 0)
	 */
	private int format=0;
	
	/**
	 * csv cell speratator (default is ";")
	 */
	private String separator = Messages.LogFileFormat_seperator_default;
	
	/**
	 * wheather to display any gestures or not
	 * <p>
	 * (default is false)
	 */
	private boolean gesture;
	
	/**
	 * path of the log file, required
	 */
	private String path;
	
	/**
	 * delay time before writing logfile (seconds)
	 */
//	private int startDelay;
	
	/**
	 * time logfile is written
	 */
//	private int time;
//	
//	private int seconds = 0;
//	
//	private int minutes = 1;
//	
//	private int hours = 0;
	
	
	public int interval;
	
	/**
	 * default constructor
	 *
	 */
	public LogFileFormat() {
	}
	
	/*
	 * setter
	 */
	public void setTimestamp(boolean timestamp) {
		this.timestamp = timestamp;
	}
	public void setTimestampEnd(boolean timestampEnd) {
		this.timestampEnd = timestampEnd;
	}
	public void setSourceName(boolean sensorName) {
		this.sourceName = sensorName;
	}
	public void setFormat(int format) {
		this.format = format;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public void setGesture(boolean gesture) {
		this.gesture = gesture;
	}
	public void setPath(String path) {
		this.path = path;
	}
//	public void setStartDelay(int startDelay) {
//		this.startDelay = startDelay;
//	}
//	public void setSeconds(int seconds) {
//		this.seconds = seconds;
//	}
//	public void setMinutes(int minutes) {
//		this.minutes = minutes;
//	}
//	public void setHours(int hours) {
//		this.hours = hours;
//	}
	
	/*
	 * getter
	 */
	public boolean isTimestamp() {
		return timestamp;
	}
	public boolean isTimestampEnd() {
		return timestampEnd;
	}
	public boolean isSourceName() {
		return sourceName;
	}
	public int getFormat() {
		return format;
	}
	public String getSeparator() {
		return separator;
	}
	public boolean isGesture() {
		return gesture;
	}
	public String getPath() {
		return path;
	}
	
//	public int getStartDelay() {
//		return startDelay;
//	}
//	
//	public int getSeconds() {
//		return seconds;
//	}
//	public int getMinutes() {
//		return minutes;
//	}
//	public int getHours() {
//		return hours;
//	}
	
	/**
	 * returns delay time in milliseconds
	 */
//	public int getMilliDelay() {
//		return startDelay*1000;
//	}
	
	/**
	 * returns time in milliseconds
	 */
//	public int getTime() {
//		time = (hours*60*60*1000)+(minutes*60*1000)+(seconds*1000);
//		return time;
//	}

	/**
	 * toString method
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass());
		sb.append("\nPath: "); //$NON-NLS-1$
		sb.append(path);	
		return sb.toString();
	}
}
