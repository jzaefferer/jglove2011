package jGlove.sensor;

import jGlove.Core;
import jGlove.Form;
import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.config.Config;
import jGlove.config.ConfigManager;
import jGlove.config.ConfigManagerAction;
import jGlove.config.PresetConfig;
import jGlove.config.SelectionConfig;
import jGlove.gesture.GestureView;
import jGlove.gesture.GestureWizard;
import jGlove.shared.ISource;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that displays all available <code>Sensor</code>s
 * and allows the user to select and configure them.
 *
 */
public class SensorView extends ViewPart {

	public static final String ID = "jGlove.app.views.calibrationView"; //$NON-NLS-1$
	
	private FormToolkit toolkit;
	
	private SensorForm[] sensorForms;
	private ConfigManager selectionManager;
	private ConfigManager presetManager;
	
	private ScrolledForm form;
	private Composite body;
	
	private Sensor[] sensors;
	
	private int displayMode = 0;
    private int currentSelection = -1;
    private int currentPreset = -1;
    
    public SensorView() {
    	IPreferenceStore store = JGlovePlugin.getDefault().getPreferenceStore();
    	String mode = store.getString(IJGloveConstants.P_DISPLAY_MODE);
    	displayMode =  Integer.valueOf(mode).intValue();
    	try {
			selectionManager = new ConfigManager(JGlovePlugin.getFile("/resource/selectionConfigs.dat")); //$NON-NLS-1$
			presetManager = new ConfigManager(JGlovePlugin.getFile("/resource/presetConfigs.dat")); //$NON-NLS-1$
			selectionManager.load();
			presetManager.load();
		} catch(Exception e) {
			e.printStackTrace();
			JGlovePlugin.log(e);
		}
    }
	
	public void createPartControl(Composite parent) {
        toolkit = new FormToolkit(parent.getDisplay());
		
		form = toolkit.createScrolledForm(parent);
		
		createToolbarActions();
		
		body = form.getBody();

		ColumnLayout layout = new ColumnLayout();
		layout.topMargin = 0;
		layout.maxNumColumns = 3;
		layout.horizontalSpacing = 4;
		layout.verticalSpacing = 4;
		body.setLayout(layout);

		createGestureMonitors();
	}
	
	private void updateView() {
		if(sensorForms != null) {
			for(int i=0; i<sensorForms.length; i++) {
				sensorForms[i].dispose();
			}
		}
		createGestureMonitors();
	}
	
	private void createGestureMonitors() {
		sensors = Core.getDefault().getSensors();
		if(sensors != null && sensors.length > 0) {
			sensorForms = new SensorForm[sensors.length]; 
			for(int i=0; i<sensorForms.length; i++) {
				sensorForms[i] = new SensorForm(body, sensors[i], toolkit);
			}
			
			toolkit.paintBordersFor(body);
			body.layout();
		}
	}
	
	private void createToolbarActions() {
        
		ConfigManagerAction selectionAction = new ConfigManagerAction(Messages.SensorView_selection_text, Messages.SensorView_selection_tooltip, selectionManager, getViewSite().getShell()) {
            public void setConfig(Config config) {
                setSelectionConfig(config);
            }
            public Config getConfig() {
                return getSelectionConfig();
            }
            public int getCurrent() {
                return currentSelection;
            }
            public void setCurrent(int current) {
                currentSelection = current;
            }
        };
        selectionAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_SELECTION));
        
        ConfigManagerAction presetAction = new ConfigManagerAction(Messages.SensorView_preset_text, Messages.SensorView_preset_tooltip, presetManager, getViewSite().getShell()) {
            public void setConfig(Config config) {
                setPresetConfig(config);
            }
            public Config getConfig() {
                return getPresetConfig();
            }
            public int getCurrent() {
                return currentPreset;
            }
            public void setCurrent(int current) {
                currentPreset = current;
            }
        };
        presetAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_PRESET));
		
		Action calibrationAction = new Action(Messages.SensorView_calibrate_text, Action.AS_PUSH_BUTTON) {
			public void run() {
				calibrateSelection();
			};
		};
		calibrationAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_SETUP));
		calibrationAction.setToolTipText(Messages.SensorView_calibrate_tooltip);

		Action createGestureAction = new Action(Messages.SensorView_creategesture_text, Action.AS_PUSH_BUTTON) {
			public void run() {
				createGesture();
			};
		};
		createGestureAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_GESTURE));
		createGestureAction.setToolTipText(Messages.SensorView_creategesture_tooltip);
		
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		toolbar.add(selectionAction);
		toolbar.add(presetAction);
		toolbar.add(new DisplayModeDropdownAction(this));
		toolbar.add(calibrationAction);
		toolbar.add(createGestureAction);
		
	}

	public void setFocus() {
		body.setFocus();
	}
	
	public void redrawForm(SensorForm sensorForm) {
		for(int i=0; i<sensorForms.length; i++) {
			if(sensorForms[i].equals(sensorForm)) {
				ISource source = sensorForm.getSource();
				sensorForms[i].dispose();
				sensorForms[i] = new SensorForm(body, source, toolkit);
				if(i != 0) {
					sensorForms[i].moveBelow(sensorForms[i-1]);
				} else {
					sensorForms[i].moveAbove(null);
				}
				break;
			}
		}
		body.layout();
	}
	
	public void dispose() {
		if(selectionManager != null) {
			selectionManager.save();
		}
		if(presetManager != null) {
			presetManager.save();			
		}
		for(int i=0; i<sensorForms.length; i++) {
			sensorForms[i].dispose();
		}
		form.dispose();
        toolkit.dispose();
		super.dispose();
	}
	
    private void setPresetConfig(Config config) {
        PresetConfig ps = (PresetConfig)config;

        for(int i=0; i<sensorForms.length; i++) {
            sensorForms[i].setActive(ps.getSelection()[i]);
            sensorForms[i].setName(ps.getDescription()[i]);
            sensorForms[i].setType(ps.getType()[i]);
        }
        Core.getDefault().getPropertyChangeSupport().firePropertyChange("active", null); //$NON-NLS-1$
    }
    
    private Config getPresetConfig() {
        boolean[] selection = new boolean[sensorForms.length];
        String[] description = new String[sensorForms.length];
        int[] type = new int[sensorForms.length];
        for(int i=0; i<sensorForms.length; i++) {
            selection[i] = sensorForms[i].getActive();
            description[i] = sensorForms[i].getSourceName();
            type[i] = sensorForms[i].getType();
        }
        return new PresetConfig(selection, description, type);
    }
	
    public void setSelectionConfig(Config config) {
        SelectionConfig ps = (SelectionConfig)config;

        for(int i=0; i<sensorForms.length; i++) {
            sensorForms[i].setActive(ps.getSelection()[i]);
        }
        Core.getDefault().getPropertyChangeSupport().firePropertyChange("active", null); //$NON-NLS-1$
    }
	
    private Config getSelectionConfig() {
        boolean[] selection = new boolean[sensorForms.length];
        for(int i=0; i<sensorForms.length; i++) {
            selection[i] = sensorForms[i].getActive();
        }
        return new SelectionConfig(selection);
    }
	
	public void setDisplay(int selection) {
		displayMode = selection;
		for(int i=0; i<sensorForms.length; i++) {
			sensorForms[i].setDisplayMode(selection);
		}
	}
	
	public int getDisplayMode() {
		return displayMode;
	}
	
	public Sensor[] getSelection() {
		int count = 0;
		for(int i=0; i<sensorForms.length; i++) 
			if(sensorForms[i].getActive()) 
				count++;
		
		if(count == 0) {
			MessageDialog.openWarning(body.getShell(), Messages.SensorView_noselectionwarning_title, Messages.SensorView_noselectionwarning_text);
			return null;
		}
		Sensor[] sensors = new Sensor[count];
		count = 0;
		for(int i=0; i<sensorForms.length; i++) {
			if(sensorForms[i].getActive()) {
				sensors[count++] = (Sensor)sensorForms[i].getSource();
			}
		}
		return sensors;
	}
	
	/**
	 * gathers all active (selected) sensors and trys to calibrate them via CalibrationWizard
	 */
	private void calibrateSelection() {
		try {
			Sensor[] sensors = getSelection();
			if(sensors != null) {
				CalibrationWizard wizard = new CalibrationWizard(sensors);
				WizardDialog dialog = new WizardDialog(body.getShell(), wizard);
				if(dialog.open() == 0) {
					updateView();
				}
			}
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	private void createGesture() {
		Sensor[] sensors = getSelection();
		if(sensors != null) {
			GestureWizard wizard = new GestureWizard(sensors);
			WizardDialog dialog = new WizardDialog(body.getShell(), wizard);
			if(dialog.open() == 0) {
				try {
					getViewSite().getPage().showView(GestureView.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
					JGlovePlugin.log(e);
				}
				GestureView gv = (GestureView)getViewSite().getPage().findView(GestureView.ID);
				if(gv != null) {
					gv.updateView();
				}
			}
		}
	}
	
	private final static int[] GRID_WIDTH = {18, 100, 25, 100, 20};
	private final static int[] GRID_HEIGHT = {18, 18, 18, 20, 20};
	
	private static GridData getGridData(int i) {
		GridData g = new GridData();
		g.widthHint = GRID_WIDTH[i];
		g.heightHint = GRID_HEIGHT[i];
		g.horizontalAlignment = GridData.CENTER;
		return g;
	}
	
	/**
	 * GUI Komponente
	 * contains all necessary widgets, to display a sensor in the
	 * calibration perspective
	 *
	 */
	private class SensorForm extends Form {

		private SensorForm myself;
		
		private int localDisplayMode;
		
		public SensorForm(Composite parent, ISource source, FormToolkit toolkit) {
			super(parent, source, toolkit);
			createControl(6);
			myself = this;
		}
		
		protected void createControl(int rows) {
			super.createControl(rows);
		
			typeLabel.setLayoutData(getGridData(0));
			typeLabel.setToolTipText(IJGloveConstants.TYPE_LABELS[source.getType()]);
			
			nameLabel.setToolTipText(Messages.SensorView_name);
			nameLabel.setLayoutData(getGridData(1));
			
			valueLabel.setToolTipText(Messages.SensorView_value);
			valueLabel.setLayoutData(getGridData(2));
			
			valueProgressBar.setLayoutData(getGridData(3));
			
			calibrateButton.setToolTipText(Messages.SensorView_calibratebutton);
			calibrateButton.setLayoutData(getGridData(4));
			calibrateButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					CalibrationWizard wizard = new CalibrationWizard(
							(Sensor) source);
					WizardDialog dialog = new WizardDialog(getShell(),
							wizard);
					if (dialog.open() == 0) {
						redrawForm(myself);
						Core.getDefault().getPropertyChangeSupport()
								.firePropertyChange("active", null); //$NON-NLS-1$
					}
				}
			});
			
			activeBackgroundColor = valueProgressBar.getBackground();
			nonactiveBackgroundColor = toolkit.getColors().getBackground();
			
			changeColors(source.getActive());
			
			setDisplayMode(displayMode);
			
			toolkit.paintBordersFor(this);
		}
		
		public void setDisplayMode(int mode) {
			int min = 0;
			int max = 1023;
			switch(mode) {
			case 1:
				max = 255;
				break;
			case 2:
				max = 127;
				break;
			}
			valueProgressBar.setMinimum(min);
			valueProgressBar.setMaximum(max);
			localDisplayMode = mode;
			valueChanged();
		}
		
        public void propertyChange(PropertyChangeEvent event) {
            valueChanged();
        }
        
		public void valueChanged() {
			int value = 0;
			switch(localDisplayMode) {
			case 0:
				value = source.getValue();
				break;
			case 1:
				value = source.getValueScaled();
				break;
			case 2:
				value = source.getValueMidi();
				break;
			}
			final int fvalue = value;
			if (!isDisposed()) {
				getDisplay().syncExec(new Runnable() {
					public void run() {
						if (!valueLabel.isDisposed() && !valueProgressBar.isDisposed()) {
							valueLabel.setText(String.valueOf(fvalue));
							valueProgressBar.setSelection(fvalue);
						}
					}
				});
			}
		}

	}
}
