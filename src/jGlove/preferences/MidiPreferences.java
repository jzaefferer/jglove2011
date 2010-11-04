package jGlove.preferences;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.midi.MidiReceiver;

import javax.sound.midi.MidiDevice.Info;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents the midi preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class MidiPreferences extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public MidiPreferences() {
		super(GRID);
		setPreferenceStore(JGlovePlugin.getDefault().getPreferenceStore());
		setDescription(Messages.MidiPreferences_description);
	}

	protected void createFieldEditors() {
		Info[] devices = MidiReceiver.getDevices();
		String[][] valueLabels = new String[devices.length][];
		for (int i = 0; i < devices.length; i++) {
			valueLabels[i] = new String[]{devices[i].getName(), String.valueOf(i)};
		}
		addField(new RadioGroupFieldEditor(
				IJGloveConstants.P_MIDI_DEVICE,
				Messages.MidiPreferences_mididevice,
				1,
				valueLabels,
				getFieldEditorParent()
		));
	}

	public void init(IWorkbench workbench) {
	}

}
