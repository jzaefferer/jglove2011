package jGlove.monitor;

import jGlove.shared.ISource;

/**
 * The graphic-processor
 * <p>
 * This class delivers given worths to the graphic-engine.
 * It also re-arranges the value-array.
 * 
 * </pre></code>
 * @version 1.0
 * @deprecated
 */
public class GraphProcessor {

	private Graph g;

	private int[][] values;
	
	
	private ISource[] source;
	
	/**
	 * Constructor, creates a value-array for source.length devices with
	 * g.getLength() individual values.
	 * 
	 * @param g
	 * @param source
	 */
	public GraphProcessor(Graph g, ISource[] source) {
		this.g = g;
		this.source = source;
		values = new int[source.length][g.getLength()];
	}
	
	/**
	 * The method creates seperate graph-lines for each given signal.
	 * If a new signal reached the method, the value-array will be re-arranged by
	 * deleting the oldest value and setting the newest at the first array-position
	 * (zero/0).
	 *
	 */
	public void update() {
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
