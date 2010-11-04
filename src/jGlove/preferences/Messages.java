package jGlove.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.preferences.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String MidiPreferences_description;

    public static String MidiPreferences_mididevice;

    public static String PerformancePreferences_description;

    public static String PerformancePreferences_coreperformance;

    public static String PerformancePreferences_displaymode;

    public static String PerformancePreferences_graph_numberofvalues;

    public static String PerformancePreferences_graph_interval;
}
