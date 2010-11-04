package jGlove.monitor;

import jGlove.Core;
import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.PropertyChangeEvent;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that displays the currently active <code>ISource</code>
 * objects available.
 */
public class GraphicalMonitorView extends ViewPart {

	public static final String ID = "jGlove.app.views.graphicalMonitorView"; //$NON-NLS-1$
	
	private GraphThread gt;
	
	private int scale;
	private int interval;
	
	private IPreferenceStore store;
	
	private IPropertyChangeListener activeListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			gt.init();
		}
	};
	
	private org.eclipse.jface.util.IPropertyChangeListener storeListener = new org.eclipse.jface.util.IPropertyChangeListener() {
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			scale = getValue(store, IJGloveConstants.P_GRAPH_SCALE);
			interval = getValue(store, IJGloveConstants.P_GRAPH_INTERVAL);
			gt.init(scale, interval);
		}
	};
	
	public GraphicalMonitorView() {
		store = JGlovePlugin.getDefault().getPreferenceStore();
		scale = getValue(store, IJGloveConstants.P_GRAPH_SCALE);
		interval = getValue(store, IJGloveConstants.P_GRAPH_INTERVAL);
		store.addPropertyChangeListener(storeListener);
	}
	
	private int getValue(IPreferenceStore store, String name) {
		String mode = store.getString(name);
		return Integer.valueOf(mode).intValue();
	}
	
	public void createPartControl(Composite parent) {
		gt = new GraphThread(parent, scale, interval);
		gt.start();
		Core.getDefault().getPropertyChangeSupport().addPropertyChangeListener("active", activeListener); //$NON-NLS-1$
	}

	public void setFocus() {
	}
	
	public void dispose() {
		gt.interrupt();
		store.removePropertyChangeListener(storeListener);
		Core.getDefault().getPropertyChangeSupport().removePropertyChangeListener("active", activeListener); //$NON-NLS-1$
		super.dispose();
	}
}
