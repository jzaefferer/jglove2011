package jGlove.shared;

import org.eclipse.jface.action.Action;

/**
 * A simple abstract action that has an on/off state.
 */
public abstract class SwitchAction extends Action {

	protected boolean state;
	
	public SwitchAction(String text, int type) {
		super(text, type);
		switchTooltip();
	}
	
	public boolean getState() {
		return state;
	}
	
	public abstract void switchTooltip();
	public abstract void run();
	
}
