package jGlove.mouse;

import jGlove.shared.OpenAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * A IWorkbenchWindowActionDelegate action
 * that simply opens the <code>MouseView</code>.
 */
public class OpenMouseViewAction extends OpenAction {

	public void run(IAction action) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.showView(MouseView.ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
