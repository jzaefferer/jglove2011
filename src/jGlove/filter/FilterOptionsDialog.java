package jGlove.filter;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * A dialog to configure all <code>Option</code>s of a IFilter.
 */
public class FilterOptionsDialog extends Dialog {

	private Label message;
	
	private List<Option> options;
	
	protected FilterOptionsDialog(Shell parentShell, List<Option> options) {
		super(parentShell);
		this.options = options;
	}

	protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite)super.createDialogArea(parent);
        GridLayout layout = (GridLayout)composite.getLayout();
        layout.numColumns = 2;
        
        message = new Label(composite, SWT.NULL);
        message.setText(Messages.getString("FilterOptionsDialog.messagetext")); //$NON-NLS-1$
        GridData data = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_CENTER);
        data.horizontalSpan = 2;
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        message.setLayoutData(data);
        message.setFont(parent.getFont());
        
        for (final Option option : options) {
        	Label label = new Label(composite, SWT.NULL);
        	label.setText(option.getLabel());
        	data = new GridData(GridData.HORIZONTAL_ALIGN_END);
        	label.setLayoutData(data);
        	
        	final Spinner spinner = new Spinner(composite, SWT.BORDER);
        	spinner.setSelection(option.getValue());
        	spinner.setMinimum(option.getMin());
        	spinner.setMaximum(option.getMax());
        	spinner.addModifyListener(new ModifyListener(){
        		public void modifyText(ModifyEvent e) {
        			option.setValue(spinner.getSelection());
        		}
        	});
        	spinner.addSelectionListener(new SelectionAdapter(){
        		public void widgetSelected(SelectionEvent e) {
        			option.setValue(spinner.getSelection());
        		}
        	});
        }

        return composite;
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID,
                IDialogConstants.OK_LABEL, true);
    }
    
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("FilterOptionsDialog.dialogtext")); //$NON-NLS-1$
    }
	
}
