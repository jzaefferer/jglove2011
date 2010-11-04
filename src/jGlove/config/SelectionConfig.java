package jGlove.config;

/**
 * Configuration for a selection preset.
 * Contains a boolean value for every sensor.
 */
public class SelectionConfig extends Config {

	private static final long serialVersionUID = -4778643326277363642L;
	
	private boolean[] selection;
	
	public SelectionConfig(boolean[] selection) {
		this.selection = selection;
	}
	
	public boolean[] getSelection() {
		return selection;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("selection preset ["); //$NON-NLS-1$
		for(int i=0; i<selection.length; i++) {
			sb.append(selection[i]);
			sb.append(", "); //$NON-NLS-1$
		}
		sb.append("]"); //$NON-NLS-1$
		return sb.toString();
	}
	
}
