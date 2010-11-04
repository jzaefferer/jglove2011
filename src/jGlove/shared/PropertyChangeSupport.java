package jGlove.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * A manager for <code>PropertyChangeListener</code>s that
 * is able to fire <code>PropertyChangeEvent</code>s.
 * Implementation is based on the
 * java.beans PropertyChangeSupport.
 */
public class PropertyChangeSupport implements Serializable {

	private static final long serialVersionUID = 3829872588007373029L;

	/**
	 * Constructs a <code>PropertyChangeSupport</code> object.
	 * 
	 * @param source
	 *            The object to be given as the source for any events.
	 */

	public PropertyChangeSupport(Object source) {
		if (source == null) {
			throw new NullPointerException();
		}
		this.source = source;
	}

	/**
	 * Add a PropertyChangeListener to the listener list. The listener is
	 * registered for all properties.
	 * 
	 * @param listener
	 *            The PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		if (listeners == null) {
			listeners = new ArrayList();
		}
		listeners.add(listener);
	}

	/**
	 * Remove a PropertyChangeListener from the listener list. This removes a
	 * PropertyChangeListener that was registered for all properties.
	 * 
	 * @param listener
	 *            The PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}


	public IPropertyChangeListener[] getPropertyChangeListeners() {
		List returnList = new ArrayList();

		// Add all the PropertyChangeListeners
		if (listeners != null) {
			returnList.addAll(listeners);
		}

		// Add all the PropertyChangeListenerProxys
		if (children != null) {
			Iterator iterator = children.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				PropertyChangeSupport child = (PropertyChangeSupport) children
						.get(key);
				IPropertyChangeListener[] childListeners = child.getPropertyChangeListeners();
				for (int i = 0; i < childListeners.length; i++) {
					returnList.add(childListeners[i]);
				}
			}
		}
		return (IPropertyChangeListener[]) (returnList
				.toArray(new IPropertyChangeListener[returnList.size()]));
	}

	/**
	 * Add a PropertyChangeListener for a specific property. The listener will
	 * be invoked only when a call on firePropertyChange names that specific
	 * property.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on.
	 * @param listener
	 *            The PropertyChangeListener to be added
	 */

	public void addPropertyChangeListener(String propertyName,
			IPropertyChangeListener listener) {
		if (children == null) {
			children = new Hashtable();
		}
		PropertyChangeSupport child = (PropertyChangeSupport) children
				.get(propertyName);
		if (child == null) {
			child = new PropertyChangeSupport(source);
			children.put(propertyName, child);
		}
		child.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a PropertyChangeListener for a specific property.
	 * 
	 * @param propertyName
	 *            The name of the property that was listened on.
	 * @param listener
	 *            The PropertyChangeListener to be removed
	 */

	public void removePropertyChangeListener(String propertyName,
			IPropertyChangeListener listener) {
		if (children == null) {
			return;
		}
		PropertyChangeSupport child = (PropertyChangeSupport) children
				.get(propertyName);
		if (child == null) {
			return;
		}
		child.removePropertyChangeListener(listener);
	}

	/**
	 * Returns an array of all the listeners which have been associated with the
	 * named property.
	 * 
	 * @return all of the <code>PropertyChangeListeners</code> associated with
	 *         the named property or an empty array if no listeners have been
	 *         added
	 */
	public IPropertyChangeListener[] getPropertyChangeListeners(
			String propertyName) {
		ArrayList returnList = new ArrayList();

		if (children != null) {
			PropertyChangeSupport support = (PropertyChangeSupport) children
					.get(propertyName);
			if (support != null) {
				returnList.addAll(Arrays.asList(support
						.getPropertyChangeListeners()));
			}
		}
		return (IPropertyChangeListener[]) (returnList
				.toArray(new IPropertyChangeListener[returnList.size()]));
	}

	/**
	 * Report a bound property update to any registered listeners. No event is
	 * fired if old and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The programmatic name of the property that was changed.
	 * @param newValue
	 *            The new value of the property.
	 */
	public void firePropertyChange(String propertyName, Object newValue) {
		PropertyChangeEvent evt = new PropertyChangeEvent(source, propertyName,
				newValue);

		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				IPropertyChangeListener target = (IPropertyChangeListener) listeners
						.get(i);
				target.propertyChange(evt);
			}
		}
		
		PropertyChangeSupport child = null;
		if (children != null && propertyName != null) {
			child = (PropertyChangeSupport) children.get(propertyName);
		}
		if (child != null) {
			child.firePropertyChange(evt);
		}
	}

	/**
	 * Report an int bound property update to any registered listeners. No event
	 * is fired if old and new are equal and non-null.
	 * <p>
	 * This is merely a convenience wrapper around the more general
	 * firePropertyChange method that takes Object values.
	 * 
	 * @param propertyName
	 *            The programmatic name of the property that was changed.
	 * @param newValue
	 *            The new value of the property.
	 */
	public void firePropertyChange(String propertyName, int newValue) {
		firePropertyChange(propertyName, new Integer(newValue));
	}

	/**
	 * Report a boolean bound property update to any registered listeners. No
	 * event is fired if old and new are equal and non-null.
	 * <p>
	 * This is merely a convenience wrapper around the more general
	 * firePropertyChange method that takes Object values.
	 * 
	 * @param propertyName
	 *            The programmatic name of the property that was changed.
	 * @param newValue
	 *            The new value of the property.
	 */
	public void firePropertyChange(String propertyName, boolean newValue) {
		firePropertyChange(propertyName, Boolean.valueOf(newValue));
	}

	/**
	 * Fire an existing PropertyChangeEvent to any registered listeners. No
	 * event is fired if the given event's old and new values are equal and
	 * non-null.
	 * 
	 * @param evt
	 *            The PropertyChangeEvent object.
	 */
	public void firePropertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getProperty();

		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				IPropertyChangeListener target = (IPropertyChangeListener) listeners
						.get(i);
				target.propertyChange(evt);
			}
		}
		
		PropertyChangeSupport child = null;
		if (children != null && propertyName != null) {
			child = (PropertyChangeSupport) children.get(propertyName);
		}
		if (child != null) {
			child.firePropertyChange(evt);
		}
	}

	/**
	 * Check if there are any listeners for a specific property.
	 * 
	 * @param propertyName
	 *            the property name.
	 * @return true if there are ore or more listeners for the given property
	 */
	public boolean hasListeners(String propertyName) {
		if (listeners != null && !listeners.isEmpty()) {
			// there is a generic listener
			return true;
		}
		if (children != null) {
			PropertyChangeSupport child = (PropertyChangeSupport) children
					.get(propertyName);
			if (child != null && child.listeners != null) {
				return !child.listeners.isEmpty();
			}
		}
		return false;
	}

	/**
	 * lists all the generic listeners.
	 */
	private ArrayList listeners;

	/**
	 * Hashtable for managing listeners for specific properties. Maps property
	 * names to PropertyChangeSupport objects.
	 */
	private Hashtable children;

	/**
	 * The object to be provided as the "source" for any generated events.
	 */
	private Object source;

}
