package jGlove.gesture;

import jGlove.Core;
import jGlove.filter.FilterList;
import jGlove.sensor.Sensor;

import org.eclipse.jface.wizard.Wizard;

/**
 * An implementation of <code>Wizard</code> that allows the user
 * to create or modify a <code>Gesture</code>.
 */
public class GestureWizard extends Wizard {

	private Sensor[] sensors;
	private Gesture gesture;
	private boolean modify = false;
	
	private GestureWizardOptionsPage gestureWizardOptionsPage;
	private GestureWizardCalibratePage gestureWizardCalibratePage;
	
	private String name;
	private boolean approximation;
	private int tolerance;
	private int[] values;
	private FilterList filterList;
	
	public void addPages() {
		gestureWizardCalibratePage = new GestureWizardCalibratePage(gesture.getSensors(), modify);
		gestureWizardOptionsPage = new GestureWizardOptionsPage(); // gesture.getSensors()
		if(modify == true) {
			addPage(gestureWizardOptionsPage);
			addPage(gestureWizardCalibratePage);
		} else {
			addPage(gestureWizardCalibratePage);
			addPage(gestureWizardOptionsPage);
		}

	}
	
	public GestureWizard(Sensor[] sensors) {
		this.sensors = sensors;
		setWindowTitle(Messages.GestureWizard_create_title);
		gesture = new Gesture(sensors);
		gesture.setName(Messages.GestureWizard_defaultgesturename);
		gesture.setActive(true);
		Core.getDefault().addGesture(gesture);
	}
	
	public GestureWizard(Gesture gesture) {
		setWindowTitle(Messages.GestureWizard_modify_title);
		modify = true;
		this.gesture = gesture;
		name = gesture.getName();
		approximation = gesture.isApproximated();
		tolerance = gesture.getTolerance();
		values = gesture.getValues();
		filterList = (FilterList)gesture.getFilterList().clone();
	}
	
	public boolean performFinish() {
		Core.getDefault().getPropertyChangeSupport().firePropertyChange("active", null); //$NON-NLS-1$
		return true;
	}

	public boolean performCancel() {
		// remove the gesture in case it was not a modified, but a new one
		if(sensors != null) {
			Core.getDefault().removeGesture(gesture);
		} else {
			gesture.setName(name);
			gesture.setCalculateApproximation(approximation);
			gesture.setTolerance(tolerance);
			gesture.setValues(values);
			gesture.setFilterList(filterList);
		}
		return true;
	}
	
	public Gesture getGesture() {
		return gesture;
	}
	
	protected void setGesture(Gesture gesture) {
		this.gesture = gesture;
	}
}
