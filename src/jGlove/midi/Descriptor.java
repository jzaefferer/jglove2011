package jGlove.midi;

/**
 * A description of a part of a configurable midimessage, including label,
 * description, help text.
 */
public class Descriptor {

	private String label;
	private String description;
	private String help;
	private String type;
	private int value;
	private int id = 0xff;
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setHelp(String help) {
		this.help = help;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getHelp() {
		return help;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getType() {
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("\nLabel       :"); //$NON-NLS-1$
		sb.append(getLabel());
		sb.append("\nDescription :"); //$NON-NLS-1$
		sb.append(getDescription());
		sb.append("\nHelp        :"); //$NON-NLS-1$
		sb.append(getHelp());
		sb.append("\nValue       :"); //$NON-NLS-1$
		sb.append(getValue());
		sb.append("\nType        :"); //$NON-NLS-1$
		sb.append(getType());
		sb.append("\nID          :"); //$NON-NLS-1$
		sb.append(getId());
		return sb.toString();
	}
}
