package jGlove.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiDevice.Info;

/**
 * A <code>MidiReceiver</code> is a wrapper for the javax MIDI api
 * providing a simple method to retrieve available midi devices 
 * and send midimessages.
 * <p>
 * For creating a MIDI-object the name of the chosen synth-device is needed
 * as a Info-object.
 * <p>
 * The static method getDevices() shows all reachable midi-synthesizer on the current system.
 * <p>
 * With send() Midi-messages could be send.
 * <p>
 * To closure the connection to the synth-device use close() 
 * 
 * <p>
 * Example for usage:<code><pre>
 * 
 * // Get all reachable Midi-devices
 * Info[] mySynths = Midi.getDevices();
 * 
 * // Creates a Midi-Object with the fourth found synth-device.
 * myMidi = new Midi(mySynths[3]);
 * 
 * // Before closing program
 * myMidi.close();
 * 
 * </pre></code>
 * 
 */
public class MidiReceiver {

	/**
	 * Stores the receiver for the connection to the synth-device
	 */
	private Receiver receiver;

	/**
	 * Saves the midi-device (the synthesizer)
	 */
	private MidiDevice device;
	
	/**
	 * Static method that shows all midi-devices.
	 * Returns an array of Info with all devices found.
	 * <p>
	 * 
	 * @return array of Info with all synth-devices found on the current system
	 */
	public static Info[] getDevices() {
		return MidiSystem.getMidiDeviceInfo();
	}
	
	/**
	 * Standardconstructor that connects to the synthesizer
	 * The constructor awaits as delivery the data of a synthesizer
	 * <p>
	 * If an error occurs, an at this point unhandled exception will be thrown.
	 * <p>
	 * 
	 * @param info
	 * @throws MidiUnavailableException
	 */
	public MidiReceiver(Info info) throws MidiUnavailableException {
		device = MidiSystem.getMidiDevice(info);
		if (!device.isOpen()) {
			device.open();
		}
		receiver = device.getReceiver();
	}

	/**
	 * Emits the signal to the receiver
	 * 
	 * @param msg
	 */
	public void send(ShortMessage msg) {
		receiver.send(msg, -1);
	}

	/**
	 * Closes the connection to the synth-device
	 * 
	 */
	public void close() {
		receiver.close();
		receiver = null;
		device.close();
		device = null;
	}
}