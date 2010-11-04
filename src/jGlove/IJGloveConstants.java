package jGlove;

/**
 * Provides constants for images, some often used strings and preference fields.
 */
public interface IJGloveConstants {

	public static final String IMG_MONITOR = "IMG_MONITOR"; //$NON-NLS-1$
	public static final String IMG_GRAPH_MONITOR = "IMG_GRAPH_MONITOR"; //$NON-NLS-1$
	public static final String IMG_SETUP = "IMG_SETUP"; //$NON-NLS-1$
	public static final String IMG_GESTURE = "IMG_GESTURE"; //$NON-NLS-1$
	public static final String IMG_BEND = "IMG_BEND"; //$NON-NLS-1$
	public static final String IMG_FORCE = "IMG_FORCE"; //$NON-NLS-1$
	public static final String IMG_MOUSE = "IMG_MOUSE"; //$NON-NLS-1$
	public static final String IMG_TILT = "IMG_TILT"; //$NON-NLS-1$
	public static final String IMG_MIDI = "IMG_MIDI"; //$NON-NLS-1$
	public static final String IMG_STOP = "IMG_STOP"; //$NON-NLS-1$
	public static final String IMG_SAMPLE = "IMG_SAMPLE"; //$NON-NLS-1$
	public static final String IMG_SAMPLE2 = "IMG_SAMPLE2"; //$NON-NLS-1$
	public static final String IMG_SAMPLE3 = "IMG_SAMPLE3"; //$NON-NLS-1$
	public static final String IMG_CHECKED = "IMG_CHECKED"; //$NON-NLS-1$
	public static final String IMG_UNCHECKED = "IMG_UNCHECKED"; //$NON-NLS-1$
	public static final String IMG_RECORD = "IMG_RECORD"; //$NON-NLS-1$
	public static final String IMG_HELP = "IMG_HELP"; //$NON-NLS-1$
	public static final String IMG_SELECTION = "IMG_SELECTION"; //$NON-NLS-1$
	public static final String IMG_PRESET = "IMG_PRESET"; //$NON-NLS-1$
	
	
	public static final String[] IMG_SOURCE_TYPES = {IMG_BEND, IMG_FORCE, IMG_TILT, IMG_GESTURE, IMG_MOUSE};
	
	public static final String TYPE_BEND = Messages.IJGloveConstants_type_bend;
	public static final String TYPE_FORCE = Messages.IJGloveConstants_type_force;
	public static final String TYPE_TILT = Messages.IJGloveConstants_type_tilt;
	public static final String TYPE_GESTURE = Messages.IJGloveConstants_type_gesture;
	public static final String TYPE_MOUSE = Messages.IJGloveConstants_type_mouse;
	
	public static final String[] TYPE_LABELS = {TYPE_BEND, TYPE_FORCE, TYPE_TILT, TYPE_GESTURE, TYPE_MOUSE};
	
	public static final String MODE_10BIT = Messages.IJGloveConstants_mode_10bit;
	public static final String MODE_8BIT = Messages.IJGloveConstants_mode_8bit;
	public static final String MODE_7BIT = Messages.IJGloveConstants_mode_7bit;

	public static final String[] MODE_LABELS = {MODE_10BIT, MODE_8BIT, MODE_7BIT};
	public static final String[][] MODE_LABELS_VALUES = {
		{MODE_10BIT, "0"}, //$NON-NLS-1$
		{MODE_8BIT, "1"}, //$NON-NLS-1$
		{MODE_7BIT, "2"}, //$NON-NLS-1$
	};
	
	public static final String[][] PERFORMANCE_LABELS_VALUES = {
		{ Messages.IJGloveConstants_performance_high, "1" }, //$NON-NLS-1$
		{ Messages.IJGloveConstants_performance_medium, "10" }, //$NON-NLS-1$
		{ Messages.IJGloveConstants_performance_low, "50" },  //$NON-NLS-1$
	};
	
	public static final String P_CORE_PERFORMANCE = "corePerformancePreference"; //$NON-NLS-1$
	public static final String P_GRAPH_SCALE = "graphScalePreference"; //$NON-NLS-1$
	public static final String P_GRAPH_INTERVAL = "graphIntervalPreference"; //$NON-NLS-1$
	public static final String P_MIDI_DEVICE = "midiDevicePreference"; //$NON-NLS-1$
	public static final String P_DISPLAY_MODE = "displayModePreference"; //$NON-NLS-1$
	
}
