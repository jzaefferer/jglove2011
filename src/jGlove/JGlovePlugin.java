package jGlove;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin.
 */
public class JGlovePlugin extends AbstractUIPlugin {

	private static final String ID = "jGlove.JGlovePlugin"; //$NON-NLS-1$
	
	//The shared instance.
	private static JGlovePlugin plugin;
	
	/**
	 * The constructor.
	 */
	public JGlovePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static JGlovePlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	/**
	 * Logs the specified throwable with this plug-in's log.
	 * 
	 * @param t throwable to log 
	 */
	public static void log(Throwable t) {
		log(newErrorStatus("Error logged from JGlove plug-in: ", t)); //$NON-NLS-1$
	}
	
	/**
	 * Returns a new error status for this plugin with the given message
	 * @param message the message to be included in the status
	 * @param exception the exception to be included in the status or <code>null</code> if none
	 * @return a new error status
	 */
	public static IStatus newErrorStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, ID, 1, message, exception);
	}
	
	/**
     * Returns the <code>Image</code> identified by the given key,
     * or <code>null</code> if it does not exist.
     * 
     * @return the <code>Image</code> identified by the given key,
     * or <code>null</code> if it does not exist
     * @since 3.1
     */
    public static Image getImage(String key) {
        return JGlovePluginImages.getImage(key);
    }
    
    /**
     * Returns the <code>ImageDescriptor</code> identified by the given key,
     * or <code>null</code> if it does not exist.
     * 
     * @return the <code>ImageDescriptor</code> identified by the given key,
     * or <code>null</code> if it does not exist
     * @since 3.1
     */
    public static ImageDescriptor getImageDescriptor(String key) {
        return JGlovePluginImages.getImageDescriptor(key);
    }
    
    /**
	 * Returns the standard display to be used. The method first checks, if
	 * the thread calling this method has an associated display. If so, this
	 * display is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay() {
		Display display= Display.getCurrent();
		if (display == null) {
			display= Display.getDefault();
		}
		return display;		
	}
	
	/**
	 * @param filename
	 * @return a new File object, created by the given filename
	 * @throws FileNotFoundException
	 */
	public static File getFile(String filename) {
		if(plugin == null) {
			return new File(filename);
		}
		Bundle bundle = getDefault().getBundle();
        File file = null;
        URL realUrl;
        try {
            realUrl = FileLocator.toFileURL(bundle.getEntry(filename));
            URI uri = new URI(realUrl.toString());
            file = new File(uri);
        } catch (IOException e) {
            e.printStackTrace();
            log(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log(e);
        }
        return file;
	}
}
