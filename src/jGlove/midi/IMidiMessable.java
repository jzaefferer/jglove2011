package jGlove.midi;

/**
 * An IMidiMessable is capable of getting and setting a
 * MidiMessage and knows what to do when asked to check
 * the MidiMessage.
 * <p>
 * A abstract implementation is provided by <code>Source</code>.
 * @see jGlove.shared.Source
 * @see jGlove.midi.MidiMessage
 */
public interface IMidiMessable {
	
	/**
	 * check if it is necessary to send a midi message
	 * <p>
	 * Any registered midi listeneres have to be noticed
	 * about the result
	 */
	public void checkMidiMessage();
	
	/**
	 * Set the MidiMessage object for this IMidiMessable
	 * @param midi the midiMessage to be set
	 */
	public void setMidiMessage(MidiMessage midi);
	
	/**
	 * Returns the MidiMessage of this IMidiMessable
	 * @return the MidiMessage of this IMidiMessable
	 */
	public MidiMessage getMidiMessage();
	
}
