package jGlove;

import jGlove.help.HelpView;
import jGlove.monitor.GraphicalMonitorView;
import jGlove.monitor.MonitorView;
import jGlove.sensor.SensorView;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Defines the only Perspective in the JGlove Plugin.
 * <p>
 * Monitor and sensor are added as non-closeable views,
 * placeholders are provided for GraphicalMonitor and Help views.
 */
public class Perspective implements IPerspectiveFactory {

	public static final String ID = "jGlove.perspective"; //$NON-NLS-1$
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		
		layout.addView(SensorView.ID, IPageLayout.TOP, IPageLayout.RATIO_MAX, layout.getEditorArea());
		layout.getViewLayout(SensorView.ID).setCloseable(false);
		
		layout.addView(MonitorView.ID, IPageLayout.LEFT, 0.33f, SensorView.ID); //);
		layout.getViewLayout(MonitorView.ID).setCloseable(false);
		layout.addPlaceholder(GraphicalMonitorView.ID, IPageLayout.BOTTOM, 0.4f, MonitorView.ID);
		layout.addPlaceholder(HelpView.ID, IPageLayout.BOTTOM, 0.65f, GraphicalMonitorView.ID);
	}

}
