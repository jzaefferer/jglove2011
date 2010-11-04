package jGlove.shared;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.shared.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String ConfigManagerAction_newpreset;

    public static String ConfigManagerAction_newpresetnamedialog_title;

    public static String ConfigManagerAction_newpresetnamedialog_text;

    public static String ConfigManagerAction_newpresetnamedialog_default;

    public static String ConfigManagerAction_newpresetnamedialog_error;

    public static String ConfigManagerAction_removepreset;

    public static String ConfigManagerAction_removepresetdialog_title;

    public static String ConfigManagerAction_removepresetdialog_text;

    public static String Form_activebutton_text;

    public static String Form_activebutton_tooltip;

    public static String Form_calibratebutton_text;
}
