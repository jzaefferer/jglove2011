package jGlove.shared;

import jGlove.filter.IFilterable;
import jGlove.midi.IMidiMessable;

import java.io.Serializable;

/**
 * An ISource object is the most basic input class. It
 * provides an active state, a name, a type,
 * getters for raw, scaled and midi values and a
 * PropertyChangeSupport.
 * <p>
 * The class <code>Source</code> provides an abstract
 * implementation of this interface. 
 * @see jGlove.shared.Source
 */
public interface ISource extends Serializable, IMidiMessable, IFilterable  {

	/**
	 * These fields define constants for the several types.
	 */
	public final static int
		TYPE_BEND = 0,
		TYPE_FORCE = 1,
		TYPE_TILT = 2,
		TYPE_GESTURE = 3,
		TYPE_MOUSE = 4;
	
	/**
	 * Returns the raw value of this ISource.<br>
	 * There are no predefined ranges.
	 * @return the raw value
	 */
	public int getValue();
	
	/**
	 * @return the 7bit (midi) value (between 0 and 127)
	 */
	public int getValueMidi();
	
	/**
	 * @return the 8bit value (between 0 and 255)
	 */
	public int getValueScaled();
	
	/**
	 * Returns the type, one of the TYPE_XXX constant
	 * @return the type of this ISource
	 */
	public int getType();
	
	/**
	 * Sets the type of this ISource
	 * @param type one of the TYPE_XXX constants
	 */
	public void setType(int type);
	
	/**
	 * Returns the name of this ISource.<br>
	 * A ISource should have a name to represent it.
	 * @return the name of this ISource
	 */
	public String getName();
	
	/**
	 * Sets the name of this ISource<br>
	 * An ISource should have a unique name.
	 * @param name the name to set
	 */
	public void setName(String name);
	
	/**
	 * Returns if this ISource is activated.
	 * @return true if a component is active, false if not
	 */
	public boolean getActive();
	
	/**
	 * Sets the active state of this ISource
	 * @param active true, if this ISource is active, otherwise false
	 */
	public void setActive(boolean active);
	
	/**
	 * Returns the PropertyChangeSupport of this ISource
	 * <p>
	 * Any listeners who want to get informed about
	 * PropertyChangeEvents should use this method and
	 * add themselves as listeners.
	 * @return the PropertyChangeSupport for this ISource
	 */
    public PropertyChangeSupport getPropertyChangeSupport();
}
