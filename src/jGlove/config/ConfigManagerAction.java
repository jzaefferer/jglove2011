package jGlove.config;

import jGlove.shared.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

/**
 * An abstract base Action that allows the creation
 * and selection of <code>Config</code>s. 
 */
public abstract class ConfigManagerAction extends Action implements IMenuCreator {
    
    private Menu fMenu;
    private ConfigManager manager;
    private Shell parentShell;
    
    public ConfigManagerAction(String title, String tooltip, ConfigManager manager, Shell parentShell) {
        setText(title);
        setToolTipText(tooltip);
//        setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_SAMPLE));
        setMenuCreator(this);
        this.manager = manager;
        this.parentShell = parentShell;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuCreator#dispose()
     */
    public void dispose() {
        if (fMenu != null) {
            fMenu.dispose();
        }
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
        String[] labels = manager.getLabels();
        for (int i = 0; i < labels.length; i++) {
            addActionToMenu(labels[i], i);
            
        }
        Action newPresetAction = new Action(Messages.ConfigManagerAction_newpreset) {
            public void run() {
                InputDialog dialog = new InputDialog(parentShell, Messages.ConfigManagerAction_newpresetnamedialog_title, Messages.ConfigManagerAction_newpresetnamedialog_text, Messages.ConfigManagerAction_newpresetnamedialog_default, new IInputValidator() {
                    public String isValid(String newText) {
                        if(newText.length() == 0) {
                            return Messages.ConfigManagerAction_newpresetnamedialog_error;
                        }
                        return null;
                    }
                });
                dialog.setBlockOnOpen(true);
                if(dialog.open() == InputDialog.OK) {
                    String label = dialog.getValue();
                    Config config = getConfig();
                    manager.add(label, config);
                    int current = manager.getCount();
                    setCurrent(current-1);
                    addActionToMenu(label, current);
                }
            }
            
        };
        new ActionContributionItem(newPresetAction).fill(fMenu, fMenu.getItemCount());
        
        Action deleteAction = new Action(Messages.ConfigManagerAction_removepreset) {
            public void run() {
                boolean ok = MessageDialog.openConfirm(parentShell, Messages.ConfigManagerAction_removepresetdialog_title, Messages.ConfigManagerAction_removepresetdialog_text);
                if(ok) {
                    System.out.println("remove preset"); //$NON-NLS-1$
                    fMenu.getItem(getCurrent()).dispose();
                    manager.remove(getCurrent());
                    setCurrent(-1);
                }
            }
        };
        new ActionContributionItem(deleteAction).fill(fMenu, fMenu.getItemCount());
        
        return fMenu;
    }
    
    private void addActionToMenu(String label, final int index) {
        Action action = new Action(label) {
            public void run() {
                setConfig(manager.get(index));
                setCurrent(index);
            }
        };
        action.setChecked(getCurrent() == index);
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(fMenu, index);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        int current = getCurrent() + 1;
        if(current == manager.getLabels().length) {
            current = 0;
        }
        setConfig(manager.get(current));
        setCurrent(current);
    }
    
    /**
     * Is called when a <code>Config</code> is selected.
     * @param config the selected <code>Config</code>
     */
    public abstract void setConfig(Config config);
    
    /**
     * Is called when a new <code>Config</code> is
     * created and added to the menu.
     * @return the current config
     */
    public abstract Config getConfig();
    
    /**
     * Is called when the currently selected config
     * is requested.
     * @return the index of the current selected config
     */
    public abstract int getCurrent();
    
    /**
     * Is called to change the current selection
     * @param current the index of the current selection
     */
    public abstract void setCurrent(int current);
    
}
