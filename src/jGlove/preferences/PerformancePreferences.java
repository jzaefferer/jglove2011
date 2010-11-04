package jGlove.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;

/**
 * This class represents the performance preference page that
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
public class PerformancePreferences extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PerformancePreferences() {
		super(GRID);
		setPreferenceStore(JGlovePlugin.getDefault().getPreferenceStore());
		setDescription(Messages.PerformancePreferences_description);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new RadioGroupFieldEditor(
			IJGloveConstants.P_CORE_PERFORMANCE,
			Messages.PerformancePreferences_coreperformance,
			1,
			IJGloveConstants.PERFORMANCE_LABELS_VALUES,
			getFieldEditorParent()
		));
		addField(new RadioGroupFieldEditor(
				IJGloveConstants.P_DISPLAY_MODE,
				Messages.PerformancePreferences_displaymode,
				1,
				IJGloveConstants.MODE_LABELS_VALUES,
				getFieldEditorParent()
			));
		addField(new IntegerFieldEditor(
			IJGloveConstants.P_GRAPH_SCALE,
			Messages.PerformancePreferences_graph_numberofvalues,
			getFieldEditorParent()
		));
		addField(new IntegerFieldEditor(
			IJGloveConstants.P_GRAPH_INTERVAL,
			Messages.PerformancePreferences_graph_interval,
			getFieldEditorParent()
		));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}