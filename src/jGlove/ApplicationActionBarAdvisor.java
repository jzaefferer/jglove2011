package jGlove;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction quitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction preferenceAction;
    private IWorkbenchAction helpAction;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {    	
    	quitAction = ActionFactory.QUIT.create(window);
    	register(quitAction);
    	
    	preferenceAction = ActionFactory.PREFERENCES.create(window);
    	register(preferenceAction);
    	
    	aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        helpAction = ActionFactory.HELP_CONTENTS.create(window);
        register(helpAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager fileMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_menu_file, IWorkbenchActionConstants.M_FILE);
    	fileMenu.add(quitAction);

    	MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_menu_help, IWorkbenchActionConstants.M_HELP);
    	helpMenu.add(aboutAction);
    	helpMenu.add(helpAction);
    	
    	MenuManager windowMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_menu_window, IWorkbenchActionConstants.M_WINDOW);
//    	MenuManager openMenu = menuBar.get
    	windowMenu.add(preferenceAction);
    	
    	menuBar.add(fileMenu);
    	menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    	menuBar.add(windowMenu);
    	menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    	menuBar.add(helpMenu);
    	
    }

}
