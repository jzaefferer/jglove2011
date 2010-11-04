package jGlove.gesture;

import jGlove.shared.OpenAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * A IWorkbenchWindowActionDelegate action
 * that simply opens the <code>GestureView</code>.
 */
public class OpenGestureViewAction extends OpenAction {

	public void run(IAction action) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.showView(GestureView.ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
