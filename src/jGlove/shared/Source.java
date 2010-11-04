package jGlove.shared;

import jGlove.Core;
import jGlove.filter.FilterList;
import jGlove.filter.IFilter;
import jGlove.midi.MidiMessage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

/**
 * An abstract base implementation of a source. A typical client
 * class subclasses <code>Source</code> to implement a particular
 * source.
 * <p>
 * Subclasses may call the following methods to configure the source:
 * <ul>
 * <li><code>setType<code></li>
 * <li><code>setName<code></li>
 * <li><code>setActive<code></li>
 * </ul>
 * <p>
 * Subclasses may override these methods if required:
 * <ul>
 * <li>reimplement <code>getType<code></li>
 * </ul>
 * @see jGlove.sensor.Sensor
 * @see jGlove.mouse.MouseSensor
 * @see jGlove.gesture.Gesture
 */
public abstract class Source implements ISource {
	
	private static final long serialVersionUID = -8422526704354907816L;

	/**
	 * the name of this source
	 */
	private String name;
	
	/**
	 * the state of this source<br>
	 * either active or inactive
	 */
	private boolean active;
	
	/**
	 * the type of this source<br>
	 * one of the ISource.TYPE_XXX constants
	 */
	private int type;
	
	/**
	 * the MidiMessage associated with this source
	 */
	private MidiMessage midiMessage;
	
	/**
	 * the list of filters used by this source
	 */
	private FilterList filterList;
    
	/**
	 * the number of additional "runs" necessary
	 * <p>
	 * This number allows caching and "active" filters
	 * at the same time.
	 */
	private transient int moreRunsNeeded;
	
	/**
	 * 
	 */
	private transient PropertyChangeSupport propertyChangeSupport;
	
    /**
     * Returns the PropertyChangeSupport of this Source
     * <p>
     * Any listeners who want to get informed about
     * PropertyChangeEvents should use this method and
     * add themselves as listeners.
     * @return the PropertyChangeSupport for this Source
     */
	public PropertyChangeSupport getPropertyChangeSupport() {
		if(propertyChangeSupport == null) {
			propertyChangeSupport = new PropertyChangeSupport(this);
			if(midiMessage != null) {
				propertyChangeSupport.addPropertyChangeListener("midimessage", Core.getDefault().getMidiListener()); //$NON-NLS-1$
			}
		}
		return propertyChangeSupport;
	}
	
    /**
     * Returns the FilterList of this Source
     * @return the filterList of this Source
     */
    public FilterList getFilterList() {
    	if(filterList == null) {
    		filterList = new FilterList();
    	}
        return filterList;
    }
    
    /**
     * Sets the FilterList for this Source
     * @param filterList the FilterList to set
     */
    public void setFilterList(FilterList filterList) {
        this.filterList = filterList;
    }
    
    /**
     * Returns all filters associated with this Source
     * @return all filters as an array
     */
    public IFilter[] getFilters() {
        return getFilterList().getActiveFilters();
    }
    
    /**
     * Returns if this Source is activated.
     * @return true if a component is active, false if not
     */
	public boolean getActive() {
		return active;
	}
	
    /**
     * Sets the active state of this Source
     * @param active true, if this Source is active, otherwise false
     */
	public void setActive(boolean active) {
        if(this.active != active) {
            this.active = active;
            getPropertyChangeSupport().firePropertyChange("active", active); //$NON-NLS-1$
            if(active) {
                Core.getDefault().addActive(this, true);
            } else {
                Core.getDefault().removeActive(this, true);
            }
        }
	}
	
    /**
     * Sets the name of this Source<br>
     * A Source should have a unique name.
     * @param name the name to set
     */
	public void setName(String name) {
        if(this.name == null || !this.name.equals(name)) {
            this.name = name;
            getPropertyChangeSupport().firePropertyChange("name", name); //$NON-NLS-1$
        }
	}
	
    /**
     * Returns the name of this Source.<br>
     * A Source should have a name to represent it.
     * @return the name of this ISource
     */
	public String getName() {
		return name;
	}

    /**
     * Returns the type, one of the ISource.TYPE_XXX constant
     * @return the type of this Source
     */
	public int getType() {
		return type;
	}
	
    /**
     * Sets the type of this Source
     * @param type one of the ISource.TYPE_XXX constants
     */
	public void setType(int type) {
		this.type = type;
	}
	
    /**
     * check if it is necessary to send a midi message
     * <p>
     * Any registered midi listeneres have to be noticed
     * about the result
     */
	public void checkMidiMessage() {
		if(midiMessage == null) {
			return;
		}
		if(midiMessage.check(getValueMidi())) {
			ShortMessage sm = null;
			try {
				sm = midiMessage.getShortMessage();
			} catch(InvalidMidiDataException e) {
			}
			if(sm != null) {
				getPropertyChangeSupport().firePropertyChange("midimessage", sm); //$NON-NLS-1$
			}
		}
	}

    /**
     * Set the MidiMessage object for this Source
     * @param midi the midiMessage to be set
     */
	public void setMidiMessage(MidiMessage midi) {
		this.midiMessage = midi;
	}
	
    /**
     * Returns the MidiMessage of this Source
     * @return the MidiMessage of this Source
     */
	public MidiMessage getMidiMessage() {
		return midiMessage;
	}
	
	/**
	 * workaround to allow "active" filters and caching
	 * at the same time
	 * @param changed true, if the new value was not the same as the
	 * 			old value
	 * @return 0, if no more runs are needed
	 */
	public int getMoreRunsNeeded(boolean changed) {
		if(changed) {
			IFilter[] filters = getFilters();
			if(filters != null) {
				for(int i=0; i<filters.length; i++) {
					moreRunsNeeded = filters[i].getMoreRuns();
				}
			}
			return 1;
		}
		if(moreRunsNeeded == -1) {
			return 1;
		}
		return (moreRunsNeeded > 0) ? moreRunsNeeded-- : 0;
	}
}
