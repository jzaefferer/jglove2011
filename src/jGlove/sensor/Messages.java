package jGlove.sensor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.sensor.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String DisplayModeDropdownAction_text;

    public static String DisplayModeDropdownAction_tooltip;

    public static String CalibrationWizard_windowtitle;

    public static String Sensor_defaultname_prefix;

    public static String CalibrationWizardPage_pagename;

    public static String CalibrationWizardPage_pagetitle_prefix;

    public static String CalibrationWizardPage_description_prefix1;

    public static String CalibrationWizardPage_description_prefix2;

    public static String CalibrationWizardPage_name_text;

    public static String CalibrationWizardPage_name_tooltip;

    public static String CalibrationWizardPage_type_text;

    public static String CalibrationWizardPage_type_tooltip;

    public static String CalibrationWizardPage_filter_text;

    public static String CalibrationWizardPage_fullrangebar;

    public static String CalibrationWizardPage_calibratedrangebar;

    public static String CalibrationWizardPage_calibratebutton;

    public static String CalibrationWizardPage_setmaxposition_prefix;

    public static String CalibrationWizardPage_setminpositon_prefix;

    public static String CalibrationWizardPage_succesful;

    public static String SensorView_selection_text;

    public static String SensorView_selection_tooltip;

    public static String SensorView_preset_text;

    public static String SensorView_preset_tooltip;

    public static String SensorView_calibrate_text;

    public static String SensorView_calibrate_tooltip;

    public static String SensorView_creategesture_text;

    public static String SensorView_creategesture_tooltip;

    public static String SensorView_noselectionwarning_title;

    public static String SensorView_noselectionwarning_text;

    public static String SensorView_name;

    public static String SensorView_value;

    public static String SensorView_calibratebutton;
}
