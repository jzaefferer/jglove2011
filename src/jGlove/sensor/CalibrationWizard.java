package jGlove.sensor;


import jGlove.filter.FilterList;

import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard to calibrate one or more sensors.
 * If only one sensor is given, there will be only one page displayed,
 * if there is more then one, every sensor will be displayed on its own page.
 * 
 * addPages() will store the calibration values of every sensor
 * When the user cancels the wizard, performCancel() restores the old
 * calibration values.
 *
 */
public class CalibrationWizard extends Wizard {

	private Sensor[] sensor;
	
	private String[] name;
	private int[] type;
	private int[] low;
	private int[] high;
	private FilterList[] filterList;
	
	private CalibrationWizard() {
		setWindowTitle(Messages.CalibrationWizard_windowtitle);
	}
	
	public CalibrationWizard(Sensor[] sensor) {
		this();
		this.sensor = sensor;
	}
	
	public CalibrationWizard(Sensor sensor) {
		this();
		this.sensor = new Sensor[1];
		this.sensor[0] = sensor;
	}
	
	public void addPages() {
		low = new int[sensor.length];
		high = new int[sensor.length];
		name = new String[sensor.length];
		type = new int[sensor.length];
		filterList = new FilterList[sensor.length];
		
		for(int i=0; i<sensor.length; i++) {
			addPage(new CalibrationWizardPage(sensor[i]));
			
			// save calibration values for restoring
			low[i] = sensor[i].getCalibrationLow();
			high[i] = sensor[i].getCalibrationHigh();
			name[i] = sensor[i].getName();
			type[i] = sensor[i].getType();
			filterList[i] = (FilterList)sensor[i].getFilterList().clone();
		}
		
	}
	
	
	
	public boolean performCancel() {
		// restore calibration values
		for(int i=0; i<sensor.length; i++) {
			sensor[i].setCalibration(low[i], high[i]);
			sensor[i].setName(name[i]);
			sensor[i].setType(type[i]);
			sensor[i].setFilterList(filterList[i]);
		}
		return true;
	}
	
	public boolean performFinish() {
		return true;
	}

}
