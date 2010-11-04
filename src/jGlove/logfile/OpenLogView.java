package jGlove.logfile;

import jGlove.shared.OpenAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * A IWorkbenchWindowActionDelegate action
 * that simply opens the <code>LogView</code>.
 */
public class OpenLogView extends OpenAction {

	public void run(IAction action) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.showView(LogView.ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}