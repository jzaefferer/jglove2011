package jGlove.gesture;

import jGlove.filter.FilterViewer;
import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * An implementation of <code>WizardPage</code> that allows
 * the user to configure the name, tolerance, approximation
 * and filters of a <code>Gesture</code>.
 */
public class GestureWizardOptionsPage extends WizardPage {

	private Composite container;
	private Text nameText;
	private Spinner toleranceSpinner;
	private Button approximationButton;
	private ProgressBar recognizedProgressBar;
	private Label recognizedLabel;
	
	private WizardPage page;
	
	private Gesture gesture;
	
	
	protected GestureWizardOptionsPage() {
		super(Messages.GestureWizardOptionsPage_pagename);
		setTitle(Messages.GestureWizardOptionsPage_pagetitle);
		setDescription(Messages.GestureWizardOptionsPage_pagedescription);
		page = this;
	}
	
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		GestureWizard wizard = (GestureWizard)page.getWizard();
		gesture = wizard.getGesture();
		
		createLabel(container, Messages.GestureWizardOptionsPage_name_text, Messages.GestureWizardOptionsPage_name_tooltip);
		nameText = new Text(container, SWT.BORDER);
		nameText.setText(gesture.getName());
		nameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				gesture.setName(((Text)e.getSource()).getText());
			}
		});
		nameText.setLayoutData(getFillGridData());
		
		createLabel(container, Messages.GestureWizardOptionsPage_tolerance_text, Messages.GestureWizardOptionsPage_tolerance_tooltip);
		toleranceSpinner = new Spinner(container, SWT.BORDER);
		toleranceSpinner.setMinimum(0);
		toleranceSpinner.setMaximum(500);
		toleranceSpinner.setSelection(gesture.getTolerance());
		toleranceSpinner.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				gesture.setTolerance(((Spinner)e.getSource()).getSelection());
			}
		});
		
		new Label(container, SWT.NULL);
		approximationButton = new Button(container, SWT.CHECK);
		approximationButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				gesture.setCalculateApproximation(((Button)e.getSource()).getSelection());
			}
		});
		approximationButton.setText(Messages.GestureWizardOptionsPage_approximation_text);
		approximationButton.setToolTipText(Messages.GestureWizardOptionsPage_approximation_tooltip);
		approximationButton.setSelection(gesture.isApproximated());
		
		createLabel(container, Messages.GestureWizardOptionsPage_recognition_text, Messages.GestureWizardOptionsPage_recognition_tooltip);
		
		new GestureMonitor(container);

        Label filterLabel = new Label(container, SWT.NULL);
        filterLabel.setText(Messages.GestureWizardOptionsPage_filter_text);
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_END);
        filterLabel.setLayoutData(gd);
        
        Composite filterTableComposite = new FilterViewer(container, gesture);
        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        filterTableComposite.setLayoutData(gd);
		
		setPageComplete(true);
		
		setControl(container);
	}

	
	private GridData getFillGridData() {
		return new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
	}
	
	private Label createLabel(Composite parent, String text, String tooltip) {
		Label l = new Label(parent, SWT.RIGHT);
		l.setText(text);
		l.setToolTipText(tooltip);
		l.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		return l;
	}
	
	private class GestureMonitor extends Composite implements IPropertyChangeListener {

		private Composite parent;
		
		public GestureMonitor(Composite parent) {
			super(parent, SWT.NULL);
			this.parent = this;
			
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			setLayout(layout);
			setLayoutData(getFillGridData());
			
			gesture.getPropertyChangeSupport().addPropertyChangeListener(this);
			
			createControl();
		}
		
		public void dispose() {
			gesture.getPropertyChangeSupport().removePropertyChangeListener(this);
			super.dispose();
		}
		
		private void createControl() {
			recognizedLabel = new Label(parent, SWT.CENTER | SWT.BORDER);
			recognizedLabel.setText(String.valueOf(gesture.getValue()));
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
			gd.widthHint = 30;
			recognizedLabel.setLayoutData(gd);
			
			recognizedProgressBar = new ProgressBar(parent, SWT.SMOOTH);
			recognizedProgressBar.setMinimum(0);
			recognizedProgressBar.setMaximum(127);
			recognizedProgressBar.setSelection(gesture.getValue());
			recognizedProgressBar.setLayoutData(getFillGridData());
		}
		
        public void propertyChange(PropertyChangeEvent event) {
            valueChanged();
        }
        
		public void valueChanged() {
			if(!parent.isDisposed()) {
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						if(parent.getDisplay() != null && !recognizedLabel.isDisposed() && !recognizedProgressBar.isDisposed())
						recognizedProgressBar.setSelection(gesture.getValueMidi());
						recognizedLabel.setText(String.valueOf(gesture.getValue()));
					}
				});
			}
		}
	}
	
}
