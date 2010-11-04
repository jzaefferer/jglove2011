package jGlove.mouse;

import jGlove.filter.FilterList;

import org.eclipse.jface.wizard.Wizard;

/**
 * An implementation of a wizard that allows configuration
 * of a mousesensor.
 */
public class MouseWizard extends Wizard {

	private MouseSensor sensor;
	
	private FilterList filterList;
	
	public void addPages() {
		addPage(new MouseWizardOptionsPage(sensor));
	}
	
	public MouseWizard(MouseSensor sensor) {
		this.sensor = sensor;
		filterList = (FilterList)sensor.getFilterList().clone();
		setWindowTitle(Messages.MouseWizard_windowtitle);
	}
	
	public boolean performFinish() {
		return true;
	}

	public boolean performCancel() {
		sensor.setFilterList(filterList);
		return true;
	}
}
