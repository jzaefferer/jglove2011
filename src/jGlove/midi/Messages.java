package jGlove.midi;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.midi.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String MidiConfigView_showhelp_text;

    public static String MidiConfigView_showhelp_tooltip;

    public static String MidiConfigView_generic_viewtitlte;

    public static String MidiConfigView_typesection_text;

    public static String MidiConfigView_typesection_description;

    public static String MidiConfigView_type_none;

    public static String MidiConfigView_generalsection_text;

    public static String MidiConfigView_generalsection_description;

    public static String MidiConfigView_invertbutton;

    public static String MidiConfigView_invert_help;

    public static String MidiConfigView_channel;

    public static String MidiConfigView_channel_tooltip;

    public static String MidiConfigView_channel_help1;

    public static String MidiConfigView_channel_help2;

    public static String MidiConfigView_optionssection_text;

    public static String MidiConfigView_optionssection_description;

    public static String MidiConfigView_octave;

    public static String MidiConfigView_note;
}
