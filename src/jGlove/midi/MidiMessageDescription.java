package jGlove.midi;

import java.util.ArrayList;

/**
 * A MidiMessageDescription contains <code>Descriptor</code>s
 * for the midimessage itself and its options like triggers, values
 * etc.
 */
public class MidiMessageDescription {

	private int id;
	private Descriptor descriptor;
	private ArrayList options;

	public MidiMessageDescription(int id, Descriptor descriptor, ArrayList options) {
		this.id = id;
		this.descriptor = descriptor;
		this.options = options;
	}
	
	public MidiMessageDescription() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Descriptor getDescriptor() {
		return descriptor;
	}
	
	public void setDescriptor(Descriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public ArrayList getOptions() {
		return options;
	}
	
	public void setOptions(ArrayList options) {
		this.options = options;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer(); 
		sb.append(super.toString());
		sb.append("\n"); sb.append(descriptor); sb.append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		if(options != null) {
			for(int i=0; i<options.size(); i++) {
				sb.append((Descriptor)options.get(i));
				sb.append("\n"); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}
	
}
