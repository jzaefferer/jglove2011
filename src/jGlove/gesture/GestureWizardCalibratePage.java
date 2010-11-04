package jGlove.gesture;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.sensor.Sensor;
import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * An implementation of <code>WizardPage</code> that allows
 * the user to calibrate a <code>Gesture</code>.
 */
public class GestureWizardCalibratePage extends WizardPage {

	private Display display;
	
	private Composite container;
	private Button createButton;
	private Label commandLabel;
	
	private SensorMonitor[] monitors;
	
	private Sensor[] sensors;
	
	private WizardPage page;
	
	private boolean modify;
	private Font bigFont;
	
	/**
	 * you cant finish the wizard as long as no calibration is done
	 * <p>
	 * to override this, set modfiy to true
	 * 
	 * @param sensors
	 * @param modify
	 */
	protected GestureWizardCalibratePage(Sensor[] sensors, boolean modify) {
		super(Messages.GestureWizardCalibratePage_pagename);
		setTitle(Messages.GestureWizardCalibratePage_pagetitle);
		setDescription(Messages.GestureWizardCalibratePage_pagedescription);
		this.sensors = sensors;
		this.modify = modify;
		
		
		monitors = new SensorMonitor[sensors.length];
		page = this;
	}
	
	public SensorMonitor[] getMonitors() {
		return monitors;
	}

	public void dispose() {
		if(bigFont != null) {
			bigFont.dispose();
		}
	}
	
	public void createControl(Composite parent) {
		display = parent.getDisplay();
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		
		for(int i=0; i<sensors.length; i++) {
			monitors[i] = new SensorMonitor(container, sensors[i]);
			monitors[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		}
		
		createButton = new Button(container, SWT.PUSH);
		createButton.setText(Messages.GestureWizardCalibratePage_startbutton);
		createButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				startCalibration(1000);
			}
		});
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);
		createButton.setLayoutData(gd);
		
		commandLabel = new Label(container, SWT.CENTER | SWT.WRAP);
		FontData fd = commandLabel.getFont().getFontData()[0];
		fd.setHeight(20);
		bigFont = new Font(container.getDisplay(), fd);
		commandLabel.setFont(bigFont);
		gd = getFillCenterSpanGridData();
		gd.widthHint = 350;
		commandLabel.setLayoutData(gd);
		
		setPageComplete(modify);
		
		setControl(container);
	}
	
	private void startCalibration(int interval) {
		int start = 0;
		setPageComplete(false);
		for(int i = 3; i>0; i--) {
			display.timerExec(start++*interval, showText(Messages.GestureWizardCalibratePage_advice_start + i));
		}
		display.timerExec(start*interval, new Runnable() {
			public void run() {
				if(getControl().isDisposed()) {
					return;
				}
				int[] values = new int[sensors.length];

				for(int i=0; i<sensors.length; i++) {
					values[i] = sensors[i].getValue();
				}
				commandLabel.setText(Messages.GestureWizardCalibratePage_advice_end);
				
				GestureWizard wizard = (GestureWizard)page.getWizard();
				wizard.getGesture().setValues(values);
				
				setPageComplete(true);
			}
		});
	}
	
	private Runnable showText(String text) {
		final String ftext = text;
		return new Runnable() {
			public void run() {
				if(commandLabel.isDisposed()) {
					return;
				}
				commandLabel.setText(ftext);
			}
		};
	}
	
	private final static int[] GRID_WIDTH = {18, 60, 25};
	private final static int[] GRID_HEIGHT = {18, 16, 16};

	private static GridData getGridData(int i) {
		GridData g = new GridData();
		g.widthHint = GRID_WIDTH[i];
		g.heightHint = GRID_HEIGHT[i];
		g.horizontalAlignment = GridData.CENTER;
		return g;
	}
	
	private class SensorMonitor extends Composite implements IPropertyChangeListener {
		
		private Sensor sensor;
		
		private Composite parent;
		
		private Label typeLabel;
		private Label nameLabel;
		private Label valueLabel;
		private ProgressBar valueBar;
		
		public SensorMonitor(Composite parent, Sensor sensor) {
			super(parent, SWT.NULL);
			this.parent = parent;
			this.sensor = sensor;
			sensor.getPropertyChangeSupport().addPropertyChangeListener(this);
			createControl(this);
		}
		
		public void dispose() {
			sensor.getPropertyChangeSupport().removePropertyChangeListener(this);
			super.dispose();
		}
		
		private void createControl(Composite parent) {
			GridLayout g = new GridLayout();
			g.numColumns = 4;
			g.horizontalSpacing = 4;
			g.verticalSpacing = 1;
			parent.setLayout(g);
			
			typeLabel = new Label(parent, SWT.BORDER);
			typeLabel.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SOURCE_TYPES[sensor.getType()]));
			typeLabel.setLayoutData(getGridData(0));
			
			nameLabel = new Label(parent, SWT.BORDER);
			nameLabel.setText(sensor.getName());
			nameLabel.setLayoutData(getGridData(1));
			
			valueLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
			valueLabel.setText(String.valueOf(sensor.getValue()));
			valueLabel.setLayoutData(getGridData(2));
			
			valueBar = new ProgressBar(parent, SWT.SMOOTH);
			valueBar.setMinimum(sensor.getCalibrationLow());
			valueBar.setMaximum(sensor.getCalibrationHigh());
			valueBar.setSelection(sensor.getValue());
			valueBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		}

        public void propertyChange(PropertyChangeEvent event) {
            valueChanged();
        }
        
		public void valueChanged() {
			if (parent != null && !parent.isDisposed()) {
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						if (!valueLabel.isDisposed()
								&& !valueBar.isDisposed()) {
							valueLabel.setText(""+sensor.getValue()); //$NON-NLS-1$
							valueBar.setSelection(sensor.getValue());
							int low = sensor.getCalibrationLow();
							int high = sensor.getCalibrationHigh();
							if(low != valueBar.getMinimum() || high != valueBar.getMaximum()) {
								valueBar.setMinimum(low);
								valueBar.setMaximum(high);
							}
						}
					}
				});
			}
		}

	}
	
	private static GridData getFillCenterSpanGridData() {
		GridData gd = getFillCenterGridData();
		gd.horizontalSpan = 2;
		return gd;
	}
	private static GridData getFillCenterGridData() {
		return new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL | GridData.CENTER);
	}
	
}
