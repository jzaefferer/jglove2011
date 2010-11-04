package jGlove.filter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This dialog shows a list of available filters and lets the user
 * choose one of them.
 */
public class ChooseFilterDialog extends Dialog {

    protected ChooseFilterDialog(Shell parentShell) {
        super(parentShell);
        setBlockOnOpen(true);
    }

    private int selection;
    
    private Label message;
    private List list;
    private Text errorMessageText;

    private Button okButton;
    
    public int getSelection() {
        return selection;
    }
    
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite)super.createDialogArea(parent);
        message = new Label(composite, SWT.NULL);
        message.setText(Messages.getString("ChooseFilterDialog.messagetext")); //$NON-NLS-1$
        GridData data = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        message.setLayoutData(data);
        message.setFont(parent.getFont());
        
        list = new List(composite, SWT.SINGLE | SWT.BORDER);
        list.setItems(FilterList.FILTERS);
        list.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        list.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                selection = list.getSelectionIndex();
                setErrorMessage(isValid(selection));
            }
        });
        list.addMouseListener(new MouseAdapter(){
           public void mouseDoubleClick(MouseEvent e) {
               selection = list.getSelectionIndex();
               close();
            } 
        });
        
        errorMessageText = new Text(composite, SWT.READ_ONLY);
        errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        errorMessageText.setBackground(errorMessageText.getDisplay()
                .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        
        return composite;
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        okButton = createButton(parent, IDialogConstants.OK_ID,
                IDialogConstants.OK_LABEL, true);
        okButton.setEnabled(false);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
        list.setFocus();
    }
    
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ChooseFilterDialog.dialogtext")); //$NON-NLS-1$
    }

    private String isValid(int selection) {
        if(selection >= 0) {
            return null;
        }
        return Messages.getString("ChooseFilterDialog.errortext"); //$NON-NLS-1$
    }
    
    public void setErrorMessage(String errorMessage) {
        errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
        okButton.setEnabled(errorMessage == null);
        errorMessageText.getParent().update();
    }
    
}
