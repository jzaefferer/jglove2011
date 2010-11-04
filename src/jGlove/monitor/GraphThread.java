package jGlove.monitor;

import jGlove.Core;
import jGlove.shared.ISource;

import org.eclipse.swt.widgets.Composite;

/**
 * This thread creates and repaints the graph as defined
 * in the performance-preferences, and stores the values
 * that are retrieved from the Core.
 */
public class GraphThread extends Thread {

	private Graph g;
	private ISource[] source;
	private int interval;
	private int[][] values;
	

	/**
	 * @param parent the parent composite
	 * @param scale number of values that are display at the same time
	 * @param interval the interval the graph is updated
	 */
	public GraphThread(Composite parent, int scale, int interval) {
        setName("GraphThread"); //$NON-NLS-1$
		this.interval = interval;
		source = Core.getDefault().getActives();
		g = new Graph(parent,scale, getLabels());// Gegebenenfalls Resolution mit übergeben
		values = new int[source.length][g.getLength()];
	}
	
	private String[] getLabels() {
		String[] text = new String[source.length];
		for(int i=0; i<source.length; i++){
			text[i] = source[i].getName();
		}
		return text;
	}
	
	protected void init(int scale, int interval) {
		this.interval = interval;
		g.setLength(scale);
		init();
	}
	
	protected void init() {
		source = Core.getDefault().getActives();
		values = new int[source.length][g.getLength()];
		g.setText(getLabels());
		g.update();
		g.updatePaint(true);
		
	}
	
	/**
	 * Run-method that repaints every Thread.sleep(n)-interval.
	 * If an error occurs the application will interrupt.
	 */
	public void run() {
		while(!isInterrupted()) {		
			try {
				Thread.sleep(interval);
			} catch(InterruptedException e) {
				interrupt();
			}
			update();
		}
	}
	
	/**
	 * The method creates seperate graph-lines for each given signal.
	 * If a new signal reached the method, the value-array will be re-arranged by
	 * deleting the oldest value and setting the newest at the first array-position
	 * (zero/0).
	 *
	 */
	private void update() {
		for(int i=0; i<source.length; i++) {
			int value1F2 = source[i].getValueMidi();
			for (int j = 0; j < values[i].length; j++) {
				int value2F2 = values[i][j];
				values[i][j] = value1F2;
				value1F2 = value2F2;

			}
		}
		g.update(values);
	}
	
}
