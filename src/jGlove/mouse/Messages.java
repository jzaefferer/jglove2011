package jGlove.mouse;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "jGlove.mouse.messages"; //$NON-NLS-1$

    private Messages() {
    }

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String MouseSensor_mouse_x;

    public static String MouseSensor_mouse_y;

    public static String MouseSensor_mouse_b1;

    public static String MouseSensor_mouse_b2;

    public static String MouseSensor_mouse_b3;

    public static String MouseSensor_mouse_b4;

    public static String MouseSensor_mouse_5;

    public static String MouseWizardOptionsPage_pagename;

    public static String MouseWizardOptionsPage_pagetitle;

    public static String MouseWizardOptionsPage_pagedescription;

    public static String MouseWizardOptionsPage_filter;

    public static String MouseView_remove_tooltip;

    public static String MouseView_add_tooltip;

    public static String MouseView_advice;

    public static String MouseWizard_windowtitle;
}
