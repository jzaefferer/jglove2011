package jGlove;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Provides <code>Image</code>s and <code>ImageDescriptor</code>s for
 * all IJGloveConstants.IMG_XXX constants.
 */
public class JGlovePluginImages {
    
	/** 
	 * The image registry containing <code>Image</code>s and the <code>ImageDescriptor</code>s.
	 */
	private static ImageRegistry imageRegistry;
		
	/* Declare Common paths */
	private static URL ICON_BASE_URL = null;

	static {
		String pathSuffix = "icons/"; //$NON-NLS-1$
		ICON_BASE_URL = JGlovePlugin.getDefault().getBundle().getEntry(pathSuffix);
	}
	
	/**
	 * Declare all images
	 */
	private static void declareImages() {
		// Global Icons
		declareRegistryImage(IJGloveConstants.IMG_GESTURE, "thumbV2.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_SETUP, "0gif_newbutton_pure_callibr.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_BEND, "button_bend.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_FORCE, "button_force.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_MIDI, "midiconfig.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_SAMPLE, "sample.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_SAMPLE2, "sample2.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_SAMPLE3, "sample3.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_STOP, "stop.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_TILT, "button_tilt.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_MONITOR, "monitor.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_CHECKED, "checked.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_UNCHECKED, "unchecked.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_MOUSE, "mices_mouse.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_RECORD, "record_toggle1.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_GRAPH_MONITOR, "monitor.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_MONITOR, "monitor2.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_HELP, "help_topic.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_SELECTION, "selection.gif"); //$NON-NLS-1$
		declareRegistryImage(IJGloveConstants.IMG_PRESET, "selectionsternchen.gif"); //$NON-NLS-1$
	}

	/**
	 * Declare an Image in the registry table.
	 * @param key 	The key to use when registering the image
	 * @param path	The path where the image can be found. This path is relative to where
	 *				this plugin class is found (i.e. typically the packages directory)
	 */
	private final static void declareRegistryImage(String key, String path) {
		ImageDescriptor desc= ImageDescriptor.getMissingImageDescriptor();
		try {
			desc= ImageDescriptor.createFromURL(makeIconFileURL(path));
		} catch (MalformedURLException me) {
			JGlovePlugin.log(me);
		}
		imageRegistry.put(key, desc);
	}
	
	/**
	 * Returns the ImageRegistry.
	 */
	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			initializeImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 *	Initialize the image registry by declaring all of the required
	 *	graphics. This involves creating JFace image descriptors describing
	 *	how to create/find the image should it be needed.
	 *	The image is not actually allocated until requested.
	 *	@see org.eclipse.jface.resource.ImageRegistry
	 */
	public static ImageRegistry initializeImageRegistry() {
		imageRegistry= new ImageRegistry(JGlovePlugin.getStandardDisplay());
		declareImages();
		return imageRegistry;
	}

	/**
	 * Returns the <code>Image<code> identified by the given key,
	 * or <code>null</code> if it does not exist.
	 */
	public static Image getImage(String key) {
		return getImageRegistry().get(key);
	}
	
	/**
	 * Returns the <code>ImageDescriptor<code> identified by the given key,
	 * or <code>null</code> if it does not exist.
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}
	
	private static URL makeIconFileURL(String iconPath) throws MalformedURLException {
		if (ICON_BASE_URL == null) {
			throw new MalformedURLException();
		}
			
		return new URL(ICON_BASE_URL, iconPath);
	}
}
