package jGlove;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String GloveBox_error_nodriver;

    public static String GloveBox_error_deviceinuse;

    public static String GloveBox_error_nodevice;

    public static String GloveBox_error_unexpected;

    public static String ApplicationActionBarAdvisor_menu_file;

    public static String ApplicationActionBarAdvisor_menu_help;

    public static String ApplicationActionBarAdvisor_menu_window;

    public static String IJGloveConstants_type_bend;

    public static String IJGloveConstants_type_force;

    public static String IJGloveConstants_type_tilt;

    public static String IJGloveConstants_type_gesture;

    public static String IJGloveConstants_type_mouse;

    public static String IJGloveConstants_mode_10bit;

    public static String IJGloveConstants_mode_8bit;

    public static String IJGloveConstants_mode_7bit;

    public static String IJGloveConstants_performance_high;

    public static String IJGloveConstants_performance_medium;

    public static String IJGloveConstants_performance_low;

    public static String Core_error_midiconnect;

    public static String Core_error_midiunavailable;

	public static String Core_error_glovebox_title;
}
