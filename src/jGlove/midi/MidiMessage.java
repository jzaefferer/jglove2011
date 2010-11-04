/**
 * class MidiMessage
 * class provides methodes to create shortMessages
 * method check() 
 * Attribut MidiMessage
 */

package jGlove.midi;

import java.io.Serializable;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

/**
 * A <code>MidiMessage</code> describes the type, channel and 
 * several optional attributes required to send a certain 
 * midimessage, eg. a pitchbend.
 */
public class MidiMessage implements Serializable {

	private static final long serialVersionUID = -84450122665483394L;

	public final static int 
		MIDI_SENSOR_CONTROL_CHANGE = 0,
		MIDI_SENSOR_PITCH_BENDER = 1, 
		MIDI_TOGGLE_CONTROL_CHANGE = 2, 
		MIDI_TOGGLE_NOTE_ON_OFF = 3,
		MIDI_TOGGLE_PROGRAM_CHANGE = 4,
		MIDI_TRIGGER_CONTROL_CHANGE = 5, 
		MIDI_TRIGGER_PROGRAM_CHANGE = 6,
		MIDI_TRIGGER_START_STOP = 7, 
		MIDI_SENSOR_CONTROL_VOLUME = 8,
		MIDI_SENSOR_TRIGGER_VOLUME = 9;

	public final static int[] MIDI_MESSAGES = { 
		MIDI_SENSOR_CONTROL_CHANGE,
		MIDI_SENSOR_PITCH_BENDER, 
		MIDI_TOGGLE_CONTROL_CHANGE, 
		MIDI_TOGGLE_NOTE_ON_OFF,
		MIDI_TOGGLE_PROGRAM_CHANGE,
		MIDI_TRIGGER_CONTROL_CHANGE,
		MIDI_TRIGGER_PROGRAM_CHANGE,
		MIDI_TRIGGER_START_STOP,
		MIDI_SENSOR_CONTROL_VOLUME,
		MIDI_SENSOR_TRIGGER_VOLUME,
	};

    /**
     * @see #getId()
     */
	private String id;
	
    /**
     * When first requested, a mostly unique id is generated
     * by using the current time in milliseconds. This id
     * is used by the application to display the midimessage
     * in its own view.
     * @return the (mostly) unique id of this midimessage
     */
	public String getId() {
		if(id == null) {
			id = String.valueOf(System.currentTimeMillis());
		}
		return id;
	}
	
	/**
	 * the midi channel of this message (default is 1)
	 */
	private int channel = 1;

	/**
	 * either the low cut or low trigger of this message, depends on type
	 */
	private int low = -1;

	/**
	 * either the high cut or high trigger of this message, depends on type
	 */
	private int high = -1;

	/**
	 * type of this message (default is -1)
	 */
	private int type = -1;

	/**
	 * options for this type of message
	 */
	private int[] options;

	/**
	 * wheather the value is inverted or not
	 */
	private boolean invert;

	/**
	 * the last value checked
	 */
	private transient int value;

	/**
	 * the last original sensor value checked
	 */
	private transient int valueRange;

	/**
	 * value for volume Trigger
	 */
	private transient int valueTrigger;

	/**
	 * used within getShortMessage()
	 */
	private transient boolean flag;
	
	/**
	 * help value for MIDI Note On/Off - makes sure that Note off will be sent only one time
	 */
	private transient boolean isNoteOn;
	
	/**
	 * MidiMessage information string
	 */
	private String infoMidiMessage;
	
	/**
	 * lenghth of MIDI message info array
	 */
	private int arrayLength;
	
	/**
	 * MIDI message info array
	 */
	private String[] arrayMidiMessage = new String[arrayLength];
	
	/**
	 * creates a new midi message
	 * 
	 * @param channel
	 *            the midi channel of this message
	 * @param low
	 *            either low trigger or low cut
	 * @param high
	 *            either high trigger or high cut
	 * @param type
	 *            the type of this message
	 * @param options
	 *            any options required for this type of message
	 * @param invert
	 *            wheather the value must be inverted or not
	 */
	public MidiMessage(int channel, int low, int high, int type, int[] options,
			boolean invert) {
		this.channel = channel;
		this.low = low;
		this.high = high;
		this.type = type;
		this.options = options;
		this.invert = invert;
	}
	
	public MidiMessage() {
		
	}
	/**
	 * setter MIDI channel
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * setter high value (high cut or high trigger)
	 * @param high
	 */
	public void setHigh(int high) {
		this.high = high;
	}
	
	/**
	 * setter low value (low cut or low trigger)
	 * @param low
	 */
	public void setLow(int low) {
		this.low = low;
	}
	
	/**
	 * setter invert mode
	 * @param invert
	 */
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
	
	/**
	 * setter MIDI message type
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * setter options parameters
	 * @param options
	 */
	public void setOptions(int[] options) {
		this.options = options;
	}
    
	/**
	 * setter MidiMessageInfo
	 * info message for Midi Message Monitor
	 * @param midiMessageInfo
	 */
    public void setInfoMidiMessage(String midiMessageInfo) {
		infoMidiMessage = midiMessageInfo;
	}
    
	/**
	 * getter MIDI channel
	 * @return the channel number of this midimessage
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * getter low value (low cut or low trigger)
	 * @return 0..127
	 */
	public int getLow() {
		return low;
	}

	/**
	 * getter high value (high value or high trigger)
	 * @return 0..127
	 */
	public int getHigh() {
		return high;
	}

	/**
	 * getter MIDI message type
	 * @return 0..127
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * getter option parameters
	 * @return int[]
	 */
	public int[] getOptions() {
		return options;
	}

	/**
	 * getter invert mode value
	 * @return true or false
	 */
	public boolean getInvert() {
		return invert;
	}
	
	/**
	 * creates a full range (7Bit) if low and high cut is set und full range
	 * mode is activated
	 * 
	 * @param value
	 *            0..127 or 0..255
	 */
	private int getFullrange(int value) {
		double faktor = 127.0 / (high - low);
		int new_value = (int) ((value - low) * faktor);
		return new_value;
	}
	
	/**
	 * getter MidiMessageInfo
	 * @return String
	 */
    public String getInfoMidiMessage() {
		return infoMidiMessage;
	}
	
    /**
     * method return the last five MidiMessage info objects
     * 
     * @return ArrayMidiMessage
     */
	public String[] getArrayMidiMessage() {
		
		if (!getInfoMidiMessage().equals(arrayMidiMessage[0])) {
			for (int i=1;i<arrayMidiMessage.length;i++) {
				arrayMidiMessage[i]=arrayMidiMessage[i-1];
			}
			arrayMidiMessage[0]=getInfoMidiMessage();
		}
		return arrayMidiMessage;	
	}
	
	
    /**
     * method checks if a midi signal has to be sent
     * 
     * @param value
     * @return midi signal boolean
     */
	public boolean check(int value) {
		if (this.value == value) {
			this.value = value;
			return false;
		}

		// inverting value if option is clicked
		if (invert) {
			value = 127 - value;
		}

		switch (type) {

		/*
		 * if the current value is between the low und high range the midi
		 * control change will be send
		 * 
		 * channel
		 * high
		 * low
		 * options[0] full range (!=0)
		 * options[1] controller
		 */
		case MIDI_SENSOR_CONTROL_CHANGE:
			if ((value >= low) && (value < high)) {

				// value conversion if full range mode is activated
				if (options != null && options[0] != 0) {
					this.valueRange = value;
					this.value = getFullrange(value);
				} else
					this.value = value;
				return true;
			}

			/*
			 * if value is lower than low cut the value will set to low or 0
			 * (full range mode)
			 */
			if (value < low) {
				if (options != null && options[0] != 0) {
					if (this.valueRange >= low) {
						this.value = 0;
						this.valueRange = -1;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
				else {
					if (this.value > low) {
						this.value = low;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}

			/*
			 * if value is higher than high cut the value will set high or 127
			 * (full range mode)
			 */
			if (value >= high) {
				if (options != null && options[0] != 0) {
					if (this.valueRange <= high) {
						this.value = 127;
						this.valueRange = 128;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}

				}
				else {
					if (this.value < high) {
						this.value = high;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}
			this.value = value;
			break;

		/*
		 * if the current value is between the low und high range the midi
		 * pitch_bender will be send
		 * 
		 * channel
		 * high
		 * low
		 * options[0] full range (!=0)
		 */
		case MIDI_SENSOR_PITCH_BENDER:
			if ((value >= low) && (value < high)) {

				// value converstion if full range mode is activated
				if (options != null && options[0] != 0) {
					this.valueRange = value;
					this.value = getFullrange(value);
				} else
					this.value = value;
				return true;
			}

			/*
			 * if value is lower than low cut the value will set to low or 0
			 * (full range mode)
			 */
			if (value < low) {
				if (options != null && options[0] != 0) {
					if (this.valueRange >= low) {
						this.value = 0;
						this.valueRange = -1;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
				else {
					if (this.value > low) {
						this.value = low;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}

			/*
			 * if value is higher than high cut the value will set high or 127
			 * (full range mode)
			 */
			if (value >= high) {
				if (options != null && options[0] != 0) {
					if (this.valueRange <= high) {
						this.value = 127;
						this.valueRange = 128;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}

				}
				else {
					if (this.value < high) {
						this.value = high;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi control_change signal for
		 * controller2 will be send if the low trigger is passed, the midi
		 * control_change signal for controller2 will be send
		 * 
		 * channel
		 * high
		 * low
		 * options[0] - controller 1 
		 * options[1] - value controller 1 
		 * options[2] - controller 2 
		 * options[3] - value controller 2
		 */
		case MIDI_TOGGLE_CONTROL_CHANGE:
			if ((this.value < high) && (value >= high)) {
				flag = true; // = controller 2
				this.value = value;
				return true;
			} else if ((value <= low) && (this.value > low)) {
				flag = false; // = controller 1
				this.value = value;
				return true;
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi noteOn signal will be send if
		 * the low trigger is passed, the midi noteoff signal will be send
		 * 
		 * channel
		 * high
		 * low
		 * options[0] note
		 * options[1] velocity
		 */
		case MIDI_TOGGLE_NOTE_ON_OFF:
			if ((this.value < high) && (value >= high)) {
				flag = true; //  = send MIDI NoteOn
				this.value = value;
				isNoteOn=true;
				return true;
			} else if ((value <= low) && (this.value > low) && (isNoteOn==true)) {
				flag = false; // = send MIDI NoteOff
				this.value = value;
				isNoteOn=false;
				return true;
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi programm_change signal for
		 * program2 will be send if the low trigger is passed, the midi
		 * programm_change signal for programm1 will be send
		 * 
		 * channel
		 * high
		 * low
		 * options[0] program1
		 * options[1] program2
		 */
		case MIDI_TOGGLE_PROGRAM_CHANGE:
			if ((this.value < high) && (value >= high)) {
				flag = true; // = program 2
				this.value = value;
				return true;
			} else if ((value <= low) && (this.value > low)) {
				flag = false; // = program 1
				this.value = value;
				return true;
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi control_change signal will be
		 * sended
		 * 
		 * channel
		 * high
		 * options[0] controller
		 * options[1] value controller
		 */
		case MIDI_TRIGGER_CONTROL_CHANGE:
			if ((this.value < high) && (value >= high)) {
				this.value = value;
				return true;
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi program-change signal will be
		 * sended
		 * 
		 * channel
		 * high
		 * options[0] controller*
		 */
		case MIDI_TRIGGER_PROGRAM_CHANGE:
			if ((this.value < high) && (value >= high)) {
				// flag = true;
				this.value = value;
				return true;
			}
			this.value = value;
			break;

		/*
		 * if the high trigger is passed, the midi start/stop signal will be
		 * send
		 * 
		 * channel
		 * high
		 * options[0] only start = 0 
		 * options[0] only stop = 1
		 * options[0] start/stop switch mode = 2
		 * options[0] start/stop alternation mode otherwise
		 */
		case MIDI_TRIGGER_START_STOP:

			// increase var high
			if (high==0)
				high++;
			
            // alternation mode
			if (options[0] == 1) {
				if ((this.value < high) && (value >= high)) {

					if (this.flag == true)
						this.flag = false; // start mode
					else
						this.flag = true; // stop mode
					this.value = value;
					return true;
				}
			}
			
            // switch mode
            if (options[1] == 1) {
				
				if (high==127)
					high--;
				
				if ((this.value < high) && (value >= high)) {
					this.flag = false; // start mode
					this.value = value;
					return true;
				}
				if ((this.value > high) && (value <= high)) {
					this.flag = true; // stop mode
					this.value = value;
					return true;
				}
			}
			
            // start mode
			if (options[2] == 1) {
				if ((this.value < high) && (value >= high)) {
					this.flag = false; // start mode
					this.value = value;
					return true;
				}

			}

            // stop mode
			if (options[3] == 1) {
				if ((this.value < high) && (value >= high)) {
					this.flag = true; // stop mode
					this.value = value;
					return true;
				}

			}
			this.value = value;
			break;

		/*
		 * if the current value is between the low und high range the midi
		 * volume_change will be send
		 * 
		 * channel high low options[0] full range (!=0)
		 */
		case MIDI_SENSOR_CONTROL_VOLUME:

			if ((value >= low) && (value < high)) {
				this.value = value;
				// value converstion if full range mode is activated
				if (options != null && options[0] != 0) {
					this.valueRange = value;
					this.value = getFullrange(value);
				}
				return true;
			}

			/*
			 * if value is lower than low cut the value will set to 0
			 */
			if (value < low) {
				if (options != null && options[0] != 0) {

					if (this.valueRange >= low) {
						this.value = 0;
						this.valueRange = -1;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
				else {
					if (this.value >= low) {
						this.value = 0;
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}

			/*
			 * if value is higher than high cut the value will set high or 127
			 * (full range mode)
			 */
			if (value > high) {
				if (options != null && options[0] != 0) {
					if (this.valueRange <= high) {
						this.value = 127;
						this.valueRange = 128;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}
				} else {
					if (this.value <= high-1) {
						this.value = high;
						// System.out.println("new_value="+value);
						return true;
					} else {
						this.value = value;
						break;
					}
				}
			}
			break;

		/*
		 * if the high trigger is passed the volume will set to high velocity
		 * level if the low trigger is passed the volume will set to low
		 * velocity level
		 * 
		 * channel
		 * high
		 * low
		 * options[0] high velocity
		 * options[1] low velocity
		 */
		case MIDI_SENSOR_TRIGGER_VOLUME:

			if ((this.value < high) && (value >= high)) {
				this.valueTrigger = options[0];
				this.value = value;
				return true;
			} else if ((value <= low) && (this.value > low)) {
				this.valueTrigger = options[1];
				this.value = value;
				return true;
			}
			this.value = value;
			break;
		}
		return false;
	}
	
    /**
     * method creates a MIDI shortMessage depending on the MIDI message type
     * 
     * @return ShortMessage
     * @throws InvalidMidiDataException
     */
	public ShortMessage getShortMessage() throws InvalidMidiDataException {
		ShortMessage sm = new ShortMessage();

		switch (type) {

		case MIDI_SENSOR_CONTROL_CHANGE:
			setInfoMidiMessage("Control change - controller "+options[1]+" - channel "+channel+" - value "+value); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, options[1],
					value);
			break;

		case MIDI_SENSOR_PITCH_BENDER:
			setInfoMidiMessage("Pitch change - channel "+channel+" - value "+value); //$NON-NLS-1$ //$NON-NLS-2$
			sm.setMessage(ShortMessage.PITCH_BEND, channel, value);
			break;

		case MIDI_TOGGLE_CONTROL_CHANGE:

			if (flag == true) {
				setInfoMidiMessage("Control change - controller "+options[2]+" - channel "+channel+" - value "+options[3]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, options[2],options[3]);
			} 
			else {
				setInfoMidiMessage("Control change - controller "+options[0]+" - channel "+channel+" - value "+options[1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, options[0],options[1]);
			}
			break;

		case MIDI_TOGGLE_NOTE_ON_OFF:

			if (flag == true) {
				setInfoMidiMessage("Note on - channel "+channel+" - note "+options[0]+" value "+options[1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				sm.setMessage(ShortMessage.NOTE_ON, channel, options[0],options[1]);
			} else {
				setInfoMidiMessage("Note off - channel "+channel+" - note "+options[0]); //$NON-NLS-1$ //$NON-NLS-2$
				sm.setMessage(ShortMessage.NOTE_OFF, channel, options[0],0);
			}
			break;

		case MIDI_TOGGLE_PROGRAM_CHANGE:
			if (flag == true) {
				setInfoMidiMessage("Programm change - channel "+channel+" - number "+options[0]); //$NON-NLS-1$ //$NON-NLS-2$
				sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, options[0],0);;
			} else {
				setInfoMidiMessage("Programm change - channel "+channel+" - number "+options[1]); //$NON-NLS-1$ //$NON-NLS-2$
				sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, options[1],0);;
			}
			break;

		case MIDI_TRIGGER_CONTROL_CHANGE:
			setInfoMidiMessage("Control change - controller "+options[0]+" - channel "+channel+" - value "+options[1]); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, options[0], options[1]);
			break;

		case MIDI_TRIGGER_PROGRAM_CHANGE:
			setInfoMidiMessage("Programm change - channel "+channel+" - number "+options[0]); //$NON-NLS-1$ //$NON-NLS-2$
			sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, options[0], 0);
			break;

		case MIDI_TRIGGER_START_STOP:
			/*
			 * logic for start_stop
			 */
			if (this.flag == true) {
				setInfoMidiMessage("Midi stop"); //$NON-NLS-1$
				sm.setMessage(ShortMessage.STOP);
			} 
			else {
				setInfoMidiMessage("Midi start"); //$NON-NLS-1$
				sm.setMessage(ShortMessage.START);
			}
			break;

		case MIDI_SENSOR_CONTROL_VOLUME:
			setInfoMidiMessage("Volume change - channel "+channel+" - value "+value); //$NON-NLS-1$ //$NON-NLS-2$
			sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, value);
			break;

		case MIDI_SENSOR_TRIGGER_VOLUME:
			setInfoMidiMessage("Volume change - channel "+channel+" - value "+valueTrigger); //$NON-NLS-1$ //$NON-NLS-2$
			sm.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, valueTrigger);
			break;
		}
		return sm;
	}
	
	/**
	 * toString method for "show Midi Message" option 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass());
		sb.append("\nType: "); sb.append(type); //$NON-NLS-1$
		sb.append("\nLow: "); sb.append(low); //$NON-NLS-1$
		sb.append("\nHigh: "); sb.append(high); //$NON-NLS-1$
		sb.append("\nchannel: "); sb.append(channel); //$NON-NLS-1$
		sb.append("\ninvert: "); sb.append(invert); //$NON-NLS-1$
		if(options != null) {
			for(int i=0; i<options.length; i++) {
				sb.append("\noptions "); sb.append(i); //$NON-NLS-1$
				sb.append(": "); sb.append(options[i]); //$NON-NLS-1$
			}
		} else {
			sb.append("\noptions: null"); //$NON-NLS-1$
		}
		return sb.toString();
	}
}// end class MidiMessage
