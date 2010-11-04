package jGlove.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

/**
 * A very simple view that can display helptext.
 */
public class HelpView extends ViewPart {

	public static final String ID = "jGlove.app.views.helpView"; //$NON-NLS-1$
	
	private Text helpText;
	
	public Text getHelpText() {
		return helpText;
	}
	
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		toolkit.adapt(parent);
		parent.setLayout(new FillLayout());
		helpText = toolkit.createText(parent, Messages.HelpView_default, SWT.WRAP);
	}

	public void setFocus() {
	}
}
