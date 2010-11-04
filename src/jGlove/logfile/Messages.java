package jGlove.logfile;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.logfile.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String LogView_timestamp;

    public static String LogView_timestampend;

    public static String LogView_sourcenames;

    public static String LogView_includegestures;

    public static String LogView_seperator;

    public static String LogView_format;

    public static String LogView_preview;

    public static String LogView_filename;

    public static String LogView_error_choosefile;

    public static String LogView_choosebutton;

    public static String LogView_startstop;

    public static String LogView_startbutton;

    public static String LogView_status_logging;

    public static String LogView_status_stopped;

    public static String LogView_error_choosefile_beforestarting;

    public static String LogFileFormat_seperator_default;

    public static String LogFileWriter_header_timestamp;

    public static String LogFileWriter_header_gesture;

    public static String LogFileWriter_header_sensor;

}
