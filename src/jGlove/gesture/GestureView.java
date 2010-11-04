package jGlove.gesture;

import jGlove.Core;
import jGlove.Form;
import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.shared.ISource;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

/**
 * A view showing all available gestures and allowing the user
 * to active or deactive, modify or delete them.
 */
public class GestureView extends ViewPart {

public static final String ID = "jGlove.app.views.gestureView"; //$NON-NLS-1$
	
	private FormToolkit toolkit;
	private ScrolledForm form;
	private Composite body;
	
	private GestureMonitor[] gestureMonitors;
	
	private Label nothingLabel;
    
	public void createPartControl(Composite parent) {
        toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		
		body = form.getBody();

		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 4;
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		body.setLayout(layout);

		createGestureMonitors();
		
	}
	
	public void updateView() {
		if(gestureMonitors != null) {
			for(int i=0; i<gestureMonitors.length; i++) {
				gestureMonitors[i].dispose();
			}
			if(nothingLabel != null) {
				nothingLabel.dispose();
				nothingLabel = null;
			}
		}
		createGestureMonitors();
	}
	
	private void createGestureMonitors() {
		Gesture[] gestures = Core.getDefault().getGestures();
		if(gestures.length == 0) {
			nothingLabel = toolkit.createLabel(body, Messages.GestureView_warning_nothingavailable);
		} else {
			gestureMonitors = new GestureMonitor[gestures.length];
			for(int i=0; i<gestures.length; i++) {
				gestureMonitors[i] = new GestureMonitor(body, gestures[i], toolkit);
			}
		}
		toolkit.paintBordersFor(body);
		body.layout();
		
	}
	
	public void redrawMonitor(GestureMonitor gestureMonitor) {
		for(int i=0; i<gestureMonitors.length; i++) {
			if(gestureMonitors[i].equals(gestureMonitor)) {
				ISource source = gestureMonitor.getSource();
				gestureMonitors[i].dispose();
				gestureMonitors[i] = new GestureMonitor(body, source, toolkit);
				if(i != 0) {
					gestureMonitors[i].moveBelow(gestureMonitors[i-1]);
				} else {
					gestureMonitors[i].moveAbove(null);
				}
				body.layout();
				return;
			}
		}
	}
	
	public void setFocus() {
		body.setFocus();
	}
	
	public void dispose() {
		if(gestureMonitors != null) {
			for(int i=0; i<gestureMonitors.length; i++) {
				gestureMonitors[i].dispose();
			}			
		}
		toolkit.dispose();
		super.dispose();
	}

	private static final int[] GRID_WIDTH = {18, 100, 25, 20, 125};
	private static final int[] GRID_HEIGHT = {18, 18, 18, 20, 20};
	
	private static GridData getGridData(int i) {
		GridData g = new GridData();
		g.widthHint = GRID_WIDTH[i];
		g.heightHint = GRID_HEIGHT[i];
		g.horizontalAlignment = GridData.CENTER;
		return g;
	}

	private class GestureMonitor extends Form {

		private GestureMonitor myself;
		private Button deleteButton;
		
		public GestureMonitor(Composite parent, ISource source, FormToolkit toolkit) {
			super(parent, source, toolkit);
			createControl(7);
		}
		
		public void dispose() {
			super.dispose();
		}
		
		protected void createControl(int rows) {
			super.createControl(rows);
			myself = this;
			g.horizontalSpacing = 4;
			g.verticalSpacing = 1;
			
			typeLabel.setLayoutData(getGridData(0));
			typeLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
			
			nameLabel.setLayoutData(getGridData(1));
			
			valueLabel.setLayoutData(getGridData(2));
			valueLabel.setToolTipText(Messages.GestureView_value_tooltip);
			
			valueProgressBar.setMinimum(0);
			valueProgressBar.setMaximum(127);
			valueProgressBar.setLayoutData(getGridData(4));
			
			calibrateButton.setToolTipText(Messages.GestureView_calibrate_tooltip);
			calibrateButton.setLayoutData(getGridData(3));
			calibrateButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					GestureWizard wizard = new GestureWizard(
							(Gesture) source);
					WizardDialog dialog = new WizardDialog(getShell(),
							wizard);
					if (dialog.open() == 0) {
						redrawMonitor(myself);
					}
				}
			});
			
			deleteButton = toolkit.createButton(this, Messages.GestureView_deletebutton_text, SWT.PUSH);
			deleteButton.setToolTipText(Messages.GestureView_deletebutton_tooltip);
			deleteButton.setLayoutData(getGridData(3));
			deleteButton.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_STOP));
			deleteButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					boolean ok = MessageDialog.openConfirm(getShell(), Messages.GestureView_delete_confirm, Messages.GestureView_delete_text_prefix+source.getName() + Messages.GestureView_delete_text_postfix);
					if(ok) {
						Core.getDefault().removeGesture((Gesture)source);
						myself.dispose();
						body.layout();
						body.redraw();
					}
				}
			});
			
			changeColors(source.getActive());
			
			toolkit.paintBordersFor(this);
		}

        public void propertyChange(PropertyChangeEvent event) {
            if (!isDisposed()) {
                getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (!valueLabel.isDisposed()
                                && !valueProgressBar.isDisposed()) {
                            valueLabel.setText(String.valueOf(source.getValue()));
                            valueProgressBar.setSelection(source.getValueMidi());
                        }
                    }
                });
            }
        }
        
	}

}
