package jGlove.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JGlovePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(IJGloveConstants.P_CORE_PERFORMANCE, "1"); //$NON-NLS-1$
		store.setDefault(IJGloveConstants.P_DISPLAY_MODE, 0);
		store.setDefault(IJGloveConstants.P_GRAPH_SCALE, 30);
		store.setDefault(IJGloveConstants.P_GRAPH_INTERVAL, 50);
		store.setDefault(IJGloveConstants.P_MIDI_DEVICE, "0"); //$NON-NLS-1$
	}

}
