package jGlove.monitor;

import jGlove.shared.OpenAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;

public class OpenGraphicalMonitorViewAction extends OpenAction {

	public void run(IAction action) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.showView(GraphicalMonitorView.ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
