package jGlove.sensor;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class DisplayModeDropdownAction extends Action implements IMenuCreator {

	private SensorView fView;
	private Menu fMenu;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
		}
		fView = null;
	}
	
	public DisplayModeDropdownAction(SensorView view) {
		fView= view;
		setText(Messages.DisplayModeDropdownAction_text);
		setToolTipText(Messages.DisplayModeDropdownAction_tooltip);
		setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_MONITOR));
		setMenuCreator(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		if (fMenu != null) {
			fMenu.dispose();
		}
		
		fMenu = new Menu(parent);
		String[] labels = IJGloveConstants.MODE_LABELS;
		int current = fView.getDisplayMode();
		for (int i = 0; i < labels.length; i++) {
//			Action action = new SetAction(fView, i, labels[i]);
			final int mode = i;
			Action action = new Action(labels[i]) {
				public void run() {
					if (mode != fView.getDisplayMode()) {
						fView.setDisplay(mode);
					}
				}
			};
			action.setChecked(current == i);
			addActionToMenu(fMenu, action, i + 1);
		}
		return fMenu;
	}
	
	private void addActionToMenu(Menu parent, Action action, int accelerator) {
	    if (accelerator < 10) {
		    StringBuffer label= new StringBuffer();
			//add the numerical accelerator
			label.append('&');
			label.append(accelerator);
			label.append(' ');
			label.append(action.getText());
			action.setText(label.toString());
		}
		ActionContributionItem item= new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
        int current = fView.getDisplayMode();
        if(current >= IJGloveConstants.MODE_LABELS.length-1) {
        	current = 0;
        } else {
        	current++;
        }
        fView.setDisplay(current);
	}
	
}
