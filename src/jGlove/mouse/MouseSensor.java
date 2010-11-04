package jGlove.mouse;

import jGlove.filter.IFilter;
import jGlove.shared.ISource;
import jGlove.shared.Source;

/**
 * An implementation of the abstract class<code>Source</code>, provides
 * an mouseaxis or -button as an input.
 */
public class MouseSensor extends Source implements MouseSensorListener {

	private static final long serialVersionUID = -5628485181343165801L;
	
    /**
     * the current value of this mousesensor
     */
	private int value;
    
    /**
     * the type of this mousesensor
     */
	private int type;
	
	public MouseSensor(int type) {
		setType(ISource.TYPE_MOUSE);
		this.type = type;
		switch(type) {
		case MouseSensorEvent.EVENT_X:
			setName(Messages.MouseSensor_mouse_x);
			break;
		case MouseSensorEvent.EVENT_Y:
			setName(Messages.MouseSensor_mouse_y);
			break;
		case MouseSensorEvent.EVENT_BUTTON1:
			setName(Messages.MouseSensor_mouse_b1);
			break;
		case MouseSensorEvent.EVENT_BUTTON2:
			setName(Messages.MouseSensor_mouse_b2);
			break;
		case MouseSensorEvent.EVENT_BUTTON3:
			setName(Messages.MouseSensor_mouse_b3);
			break;
		case MouseSensorEvent.EVENT_BUTTON4:
		    setName(Messages.MouseSensor_mouse_b4);
		    break;
		case MouseSensorEvent.EVENT_BUTTON5:
		    setName(Messages.MouseSensor_mouse_5);
		    break;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/* (non-Javadoc)
	 * @see jGlove.shared.ISource#getValue()
	 */
	public int getValue() {
		return value;
	}
	
	private void setValue(int value) {
		IFilter[] filters = getFilters();
		if(filters != null) {
			for(int i=0; i<filters.length; i++) {
				value = filters[i].filter(value);
			}
		}
		if(this.value != value) {
			this.value = value;
			getPropertyChangeSupport().firePropertyChange("value", value); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see jGlove.shared.ISource#getValueMidi()
	 */
	public int getValueMidi() {
		return value;
	}

	/* (non-Javadoc)
	 * @see jGlove.shared.ISource#getValueScaled()
	 */
	public int getValueScaled() {
		return value;
	}
	
	public void mouseSensorChanged(MouseSensorEvent event) {
		if(event.getType() == type) {
			setValue(event.getValue());
		}
	}
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getClass() + " " + getName() + " " + getValue(); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
