package jGlove.sensor;

import jGlove.IJGloveConstants;
import jGlove.filter.FilterViewer;
import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

/**
 * A page for the CalibrationWizard, that display the widgets to
 * calibrate a sensor by displaying both a full range bar and a
 * scaled bar. The scaled bar displays the same as the full range bar,
 * as long as no calibration has been done.
 * 
 */
public class CalibrationWizardPage extends WizardPage implements IPropertyChangeListener {

	private Sensor sensor;
	
	private Text nameText;
	private Combo typeCombo;
	
	private ProgressBar fullBar;
	private ProgressBar rangedBar;
	private Button caliButton;
	private Label commandLabel;
	
	private Font bigFont;
	
	private Composite container;
	private Display display;
	
	private int
		min,
		max;
	
	public CalibrationWizardPage(Sensor sensor) {
		super(Messages.CalibrationWizardPage_pagename+sensor.getIndex());
		setTitle(Messages.CalibrationWizardPage_pagetitle_prefix+sensor.getName());
		setDescription(Messages.CalibrationWizardPage_description_prefix1+sensor.getName()+Messages.CalibrationWizardPage_description_prefix2+IJGloveConstants.TYPE_LABELS[sensor.getType()]);
		this.sensor = sensor;
		
		sensor.getPropertyChangeSupport().addPropertyChangeListener(this);
	}
	
	public void dispose() {
		sensor.getPropertyChangeSupport().removePropertyChangeListener(this);
		if(bigFont != null) {
			bigFont.dispose();
		}
		super.dispose();
	}
	
	public void createControl(Composite parent) {
		display = parent.getDisplay();
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		new Label(container, SWT.NULL).setText(Messages.CalibrationWizardPage_name_text);
		nameText = new Text(container, SWT.BORDER);
		nameText.setText(sensor.getName());
		nameText.setToolTipText(Messages.CalibrationWizardPage_name_tooltip);
		nameText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		nameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				sensor.setName(nameText.getText());
			}
		});
		
		new Label(container, SWT.NULL).setText(Messages.CalibrationWizardPage_type_text);
		typeCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		typeCombo.setItems(IJGloveConstants.TYPE_LABELS);
		typeCombo.select(sensor.getType());
		typeCombo.setToolTipText(Messages.CalibrationWizardPage_type_tooltip);
		typeCombo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.GRAB_HORIZONTAL));
		typeCombo.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				sensor.setType(typeCombo.getSelectionIndex());
			}
		});
		
		Label filterLabel = new Label(container, SWT.NULL);
		filterLabel.setText(Messages.CalibrationWizardPage_filter_text);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		filterLabel.setLayoutData(gd);
		
		Composite filterTableComposite = new FilterViewer(container, sensor);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		filterTableComposite.setLayoutData(gd);
		
		
		createLabel(Messages.CalibrationWizardPage_fullrangebar, 2);
		fullBar = new ProgressBar(container, SWT.SMOOTH);
		fullBar.setLayoutData(getFillCenterSpanGridData());
		fullBar.setMinimum(0);
		fullBar.setMaximum(1024);
		
		createLabel(Messages.CalibrationWizardPage_calibratedrangebar, 2);
		rangedBar = new ProgressBar(container, SWT.SMOOTH);
		rangedBar.setLayoutData(getFillCenterSpanGridData());
		rangedBar.setMinimum(0);
		rangedBar.setMaximum(1024);
		
		caliButton = new Button(container, SWT.PUSH | SWT.CENTER);
		caliButton.setText(Messages.CalibrationWizardPage_calibratebutton);
		gd = getCenterSpanGridData();
		gd.widthHint = 150;
		gd.heightHint = 25;
		caliButton.setLayoutData(gd);
		caliButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				startCalibration(1000);
			}
		});
		
		commandLabel = new Label(container, SWT.CENTER | SWT.WRAP);
		FontData fd = commandLabel.getFont().getFontData()[0];
		fd.setHeight(20);
		bigFont = new Font(container.getDisplay(), fd);
		commandLabel.setFont(bigFont);
		gd = getFillCenterSpanGridData();
		gd.widthHint = 350;
		commandLabel.setLayoutData(gd);
		
		setControl(container);
	}
	
	private void startCalibration(int interval) {
		int start = 0;
		setPageComplete(false);
		caliButton.setEnabled(false);
		for(int i = 3; i>0; i--) {
			display.timerExec(start++*interval, showText(Messages.CalibrationWizardPage_setmaxposition_prefix + i));
		}
		display.timerExec(start*interval, new Runnable() {
			public void run() {
				if(getControl().isDisposed()) {
					return;
				}
				max = sensor.getValue();
			}
		});
		for(int i = 3; i>0; i--) {
			display.timerExec(start++*interval, showText(Messages.CalibrationWizardPage_setminpositon_prefix + i));
		}
		display.timerExec(start*interval, new Runnable() {
			public void run() {
				if(getControl().isDisposed()) {
					return;
				}
				min = sensor.getValue();
				sensor.setCalibration(min, max);
				commandLabel.setText(Messages.CalibrationWizardPage_succesful);
				setPageComplete(true);
				caliButton.setEnabled(true);
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
	
	private void createLabel(String text, int horizontalSpan) {
		Label label = new Label(container, SWT.NULL);
		label.setText(text);
		if(horizontalSpan > 1) {
			GridData gd = new GridData();
			gd.horizontalSpan = horizontalSpan;
			label.setLayoutData(gd);
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
	private static GridData getCenterSpanGridData() {
		GridData gd = getCenterGridData();
		gd.horizontalSpan = 2;
		return gd;
	}
	private static GridData getCenterGridData() {
		return new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.GRAB_HORIZONTAL);
	}

	public void propertyChange(PropertyChangeEvent event) {
        valueChanged();
	}
    
	public void valueChanged() {
		if (container != null && !container.isDisposed()) {
			container.getDisplay().syncExec(new Runnable() {
				public void run() {
					if (fullBar != null && !fullBar.isDisposed()
							&& rangedBar != null && !rangedBar.isDisposed()) {
						fullBar.setSelection(sensor.getValue());
						rangedBar.setSelection(sensor.getValue());
						int low = sensor.getCalibrationLow();
						int high = sensor.getCalibrationHigh();
						if(low != rangedBar.getMinimum() || high != rangedBar.getMaximum()) {
							rangedBar.setMinimum(low);
							rangedBar.setMaximum(high);
						}
					}
				}
			});
		}
	}
	
}
