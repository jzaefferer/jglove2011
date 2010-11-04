package jGlove.monitor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.monitor.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String Graph_error_nothingtomonitor;
    public static String MonitorView_globalmidi_on;
    public static String MonitorView_globalmidi_off;
    public static String MonitorView_graphicalmonitor_text;
    public static String MonitorView_graphicalmonitor_tooltip;
    public static String MonitorView_error_nothingtomonitor;
    public static String MonitorView_setupbutton_text;
    public static String MonitorView_setupbutton_tooltip;
    public static String MonitorView_midibutton_text;
    public static String MonitorView_midibutton_tooltip;
}
