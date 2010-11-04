package jGlove.mouse;

import jGlove.filter.FilterViewer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * An implementation of a wizardpage that displays
 * a filterviewer, allowing the user to configure
 * filters for the mousesensor.
 */
public class MouseWizardOptionsPage extends WizardPage {

	private Composite container;
	
	private MouseSensor sensor;
	
	
	protected MouseWizardOptionsPage(MouseSensor sensor) {
		super(Messages.MouseWizardOptionsPage_pagename);
		setTitle(Messages.MouseWizardOptionsPage_pagetitle);
		setDescription(Messages.MouseWizardOptionsPage_pagedescription);
		this.sensor = sensor;
	}
	
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

        Label filterLabel = new Label(container, SWT.NULL);
        filterLabel.setText(Messages.MouseWizardOptionsPage_filter);
        GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_END);
        filterLabel.setLayoutData(gd);
        
        Composite filterTableComposite = new FilterViewer(container, sensor);
        gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        filterTableComposite.setLayoutData(gd);
		
		setPageComplete(true);
		
		setControl(container);
	}

}
