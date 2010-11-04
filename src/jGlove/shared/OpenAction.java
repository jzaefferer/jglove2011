package jGlove.shared;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * A simple abstract Action that implements
 * <code>IWorkbenchWindowActionDelegate</code>.
 */
public abstract class OpenAction implements IWorkbenchWindowActionDelegate {

	protected IWorkbenchWindow window;
	
	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
