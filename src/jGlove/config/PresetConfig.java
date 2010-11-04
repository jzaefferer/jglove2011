package jGlove.config;

/**
 * Configuration for a calibration preset.
 * Contains a selection, description and type for every sensor.
 */
public class PresetConfig extends Config {

	private static final long serialVersionUID = 7711698080493266741L;
	
	private boolean[] selection;
	private String[] description;
	private int[] type;
	
	public PresetConfig(boolean[] selection, String[] description, int[] type) {
		this.selection = selection;
		this.description = description;
		this.type = type;
	}
	
	public boolean[] getSelection() {
		return selection;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public int[] getType() {
		return type;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<selection.length; i++) {
			sb.append("row "); //$NON-NLS-1$
			sb.append(i);
			sb.append(" ["); //$NON-NLS-1$
			sb.append(selection[i]);
			sb.append(", "); //$NON-NLS-1$
			sb.append(description[i]);
			sb.append(", "); //$NON-NLS-1$
			sb.append(type[i]);
			sb.append("]\n"); //$NON-NLS-1$
		}
		return sb.toString();
	}
	
}
