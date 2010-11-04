package jGlove.gesture;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "jGlove.gesture.messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String GestureWizard_create_title;

	public static String GestureWizard_defaultgesturename;

	public static String GestureWizard_modify_title;

	public static String GestureView_warning_nothingavailable;

	public static String GestureView_value_tooltip;

	public static String GestureView_calibrate_tooltip;

	public static String GestureView_deletebutton_text;

	public static String GestureView_deletebutton_tooltip;

	public static String GestureView_delete_confirm;

	public static String GestureView_delete_text_prefix;

	public static String GestureView_delete_text_postfix;

	public static String GestureWizardOptionsPage_pagename;

	public static String GestureWizardOptionsPage_pagetitle;

	public static String GestureWizardOptionsPage_pagedescription;

	public static String GestureWizardOptionsPage_name_text;

	public static String GestureWizardOptionsPage_name_tooltip;

	public static String GestureWizardOptionsPage_tolerance_text;

	public static String GestureWizardOptionsPage_tolerance_tooltip;

	public static String GestureWizardOptionsPage_approximation_text;

	public static String GestureWizardOptionsPage_approximation_tooltip;

	public static String GestureWizardOptionsPage_recognition_text;

	public static String GestureWizardOptionsPage_recognition_tooltip;

	public static String GestureWizardOptionsPage_filter_text;

	public static String GestureWizardCalibratePage_pagename;

	public static String GestureWizardCalibratePage_pagetitle;

	public static String GestureWizardCalibratePage_pagedescription;

	public static String GestureWizardCalibratePage_startbutton;

	public static String GestureWizardCalibratePage_advice_start;

	public static String GestureWizardCalibratePage_advice_end;
}
