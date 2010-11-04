package jGlove.shared;

import java.util.EventObject;

/**
 * An event fired when a property changes, containing
 * information about the source, the name of the property
 * and the new value.
 */
public class PropertyChangeEvent extends EventObject {

	private static final long serialVersionUID = 1033406561005382742L;

	/**
     * The name of the changed property.
     */
    private String propertyName;

    /**
     * The new value of the changed property, or <code>null</code> if
     * not known or not relevant.
     */
    private Object newValue;

    /**
     * Creates a new property change event.
     *
     * @param source the object whose property has changed
     * @param property the property that has changed (must not be <code>null</code>)
     * @param newValue the new value of the property, or <code>null</code> if none
     */
    public PropertyChangeEvent(Object source, String property, Object newValue) {
        super(source);
        if (property == null) {
    	    throw new NullPointerException();
    	}
        this.propertyName = property;
        this.newValue = newValue;
    }

    /**
     * Returns the new value of the property.
     *
     * @return the new value, or <code>null</code> if not known
     *  or not relevant (for instance if the property was removed).
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * Returns the name of the property that changed.
     * <p>
     * Warning: there is no guarantee that the property name returned
     * is a constant string.  Callers must compare property names using
     * equals, not ==.
     * </p>
     *
     * @return the name of the property that changed
     */
    public String getProperty() {
        return propertyName;
    }
}
