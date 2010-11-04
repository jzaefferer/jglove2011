package jGlove.monitor;

import jGlove.Core;
import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.gesture.Gesture;
import jGlove.gesture.GestureWizard;
import jGlove.midi.MidiConfigView;
import jGlove.midi.MidiMessage;
import jGlove.mouse.MouseSensor;
import jGlove.mouse.MouseWizard;
import jGlove.sensor.CalibrationWizard;
import jGlove.sensor.Sensor;
import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.ISource;
import jGlove.shared.PropertyChangeEvent;
import jGlove.shared.SwitchAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that displays all active <code>ISource</code>s
 * and provides a setup and a midiconfig button for each.
 */
public class MonitorView extends ViewPart {

	public static final String ID = "jGlove.app.views.monitorView"; //$NON-NLS-1$
	
	private final static int[] GRID_WIDTH = {18, 80, 20, 100};
	private final static int[] GRID_HEIGHT = {18, 18, 18, 20};
	
	private FormToolkit toolkit;
	private ScrolledForm form;
	private Composite body;
	
	private MonitorElement[] monitorElements;
	
	private Label nothingLabel;
	
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }
    
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		
		body = form.getBody();
		
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 4;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		body.setLayout(layout);
		
		SwitchAction globalMidiOnOffAction = new SwitchAction("", Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void switchTooltip() {
				if(state) {
					setText(Messages.MonitorView_globalmidi_on);
					setToolTipText(Messages.MonitorView_globalmidi_on);
				} else {
					setText(Messages.MonitorView_globalmidi_off);
					setToolTipText(Messages.MonitorView_globalmidi_off);
				}
			}
			public void run() {
				switchTooltip();
				state = !state;
				Core.getDefault().setGlobalMidiOnOff(state);
				System.out.println("GLOBAL MIDI ON/OFF "+ Core.getDefault().getGlobalMidiOnOff()); //$NON-NLS-1$
			};
		};
		globalMidiOnOffAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_MIDI));

		Action showGraphAction = new Action(Messages.MonitorView_graphicalmonitor_text, Action.AS_PUSH_BUTTON) {
			public void run() {
				IWorkbenchPage page = getSite().getPage();
				try {
					page.showView(GraphicalMonitorView.ID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		showGraphAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_GRAPH_MONITOR));
		showGraphAction.setToolTipText(Messages.MonitorView_graphicalmonitor_tooltip);
		
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		toolbar.add(globalMidiOnOffAction);
		toolbar.add(showGraphAction);
		
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		menu.add(globalMidiOnOffAction);
		menu.add(showGraphAction);
		
		createMonitorElements();
		
		Core.getDefault().getPropertyChangeSupport().addPropertyChangeListener("active", new IPropertyChangeListener() { //$NON-NLS-1$
			public void propertyChange(PropertyChangeEvent event) {
				updateView();
			}
		});
		
		toolkit.paintBordersFor(body);

	}

	public void updateView() {
		if(nothingLabel != null) {
			nothingLabel.dispose();
			nothingLabel = null;
		}
		if(monitorElements != null) {
			for(int i=0; i<monitorElements.length; i++) {
				monitorElements[i].dispose();
			}			
		}
		createMonitorElements();
	}
	
	private void createMonitorElements() {
		ISource[] source = Core.getDefault().getActives();
		if(source.length == 0) {
			nothingLabel = toolkit.createLabel(body, Messages.MonitorView_error_nothingtomonitor);
		} else {
			monitorElements = new MonitorElement[source.length];
			for(int i=0; i<source.length; i++) {
				monitorElements[i] = new MonitorElement(source[i]);
			}
		}
		body.layout();
		form.reflow(true);
	}
	
	public void setFocus() {
		body.setFocus();
	}
	
	private static GridData getGridData(int i) {
		GridData g = new GridData();
		g.widthHint = GRID_WIDTH[i];
		g.heightHint = GRID_HEIGHT[i];
		g.horizontalAlignment = GridData.CENTER;
		return g;
	}
	
	private class MonitorElement implements IPropertyChangeListener {
		
		private ISource source;
		
		private Composite container;
		
		private Label valueLabel;
		private ProgressBar valueProgressBar;
		private Button setupButton;
		private Button midiConfigButton;
		
		public MonitorElement(ISource source) {
			this.source = source;
			
			if(source != null) {
				source.getPropertyChangeSupport().addPropertyChangeListener(this);
			} else {
				System.out.println(source == null);
			}
            
            
			container = toolkit.createComposite(body);
			GridLayout g = new GridLayout();
			g.numColumns = 6;
			g.horizontalSpacing = 4;
			g.verticalSpacing = 1;
			container.setLayout(g);
			
			createControl();
		}
		
		public void dispose() {
			source.getPropertyChangeSupport().removePropertyChangeListener(this);
			container.dispose();
		}

		private void createControl() {
			Label typeLabel = toolkit.createLabel(container, ""); //$NON-NLS-1$
			typeLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
			typeLabel.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SOURCE_TYPES[source.getType()]));
			typeLabel.setLayoutData(getGridData(0));
			typeLabel.setToolTipText(IJGloveConstants.TYPE_LABELS[source.getType()]);
			
			Label nameLabel = toolkit.createLabel(container, source.getName());
			nameLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
			nameLabel.setLayoutData(getGridData(1));
			
			valueLabel = toolkit.createLabel(container, String.valueOf(source.getValueMidi()));
			valueLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
			valueLabel.setLayoutData(getGridData(2));
			
			valueProgressBar = new ProgressBar(container, SWT.SMOOTH);
			valueProgressBar.setMinimum(0);
			valueProgressBar.setMaximum(127);
			valueProgressBar.setSelection(source.getValueMidi());
			valueProgressBar.setLayoutData(getGridData(3));
			
			setupButton = toolkit.createButton(container, Messages.MonitorView_setupbutton_text, SWT.PUSH);
			setupButton.setToolTipText(Messages.MonitorView_setupbutton_tooltip);
			setupButton.setLayoutData(getGridData(0));
			setupButton.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SETUP));
			setupButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					try {
						Wizard wizard = null;
						if (source instanceof Gesture) {
							wizard = new GestureWizard((Gesture) source);
						} else if (source instanceof Sensor) {
							wizard = new CalibrationWizard((Sensor) source);
						} else if (source instanceof MouseSensor) {
							wizard = new MouseWizard((MouseSensor) source);
						}
						if (wizard != null) {
							WizardDialog dialog = new WizardDialog(container
									.getShell(), wizard);
							if (dialog.open() == 0) {
								updateView();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			
			midiConfigButton = toolkit.createButton(container, Messages.MonitorView_midibutton_text, SWT.PUSH);
			midiConfigButton.setLayoutData(getGridData(0));
			midiConfigButton.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_MIDI));
			midiConfigButton.setToolTipText(Messages.MonitorView_midibutton_tooltip);
			midiConfigButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					ISource midiSource = (ISource)source;
					MidiMessage midiMessage = midiSource.getMidiMessage();
					if(midiMessage == null) {
						midiMessage = new MidiMessage();
						midiSource.setMidiMessage(midiMessage);
						Core.getDefault().addMidi(midiSource);
						midiSource.getPropertyChangeSupport().addPropertyChangeListener("midimessage", Core.getDefault().getMidiListener()); //$NON-NLS-1$
						System.out.println("lege neue MidiMessage an f�r " //$NON-NLS-1$
								+ source.getName());
					}
					try {
						MidiConfigView mcv = (MidiConfigView)getViewSite().getPage().showView(MidiConfigView.ID, midiMessage.getId(), IWorkbenchPage.VIEW_ACTIVATE);
						mcv.secondaryInit(source.getName(), midiMessage);
					} catch (PartInitException e1) {
						e1.printStackTrace();
						JGlovePlugin.log(e1);
					}
				}
			});
			
			toolkit.paintBordersFor(container);
		}
		
        public void propertyChange(PropertyChangeEvent event) {
            if(container != null && !container.isDisposed()) {
                container.getDisplay().syncExec(new Runnable(){
                    public void run() {
                        if(!valueLabel.isDisposed() && !valueProgressBar.isDisposed()) {
                            valueLabel.setText(String.valueOf(source.getValueMidi()));
                            valueProgressBar.setSelection(source.getValueMidi());
                        }
                    }
                });
            }
        }

	}
}
