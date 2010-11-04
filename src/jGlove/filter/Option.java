package jGlove.filter;

import java.io.Serializable;

import org.eclipse.core.runtime.ListenerList;

/**
 * An <code>Option</code> is a configurable part of an <code>IFilter</code>
 * with a label and current, minimum and maximum value.
 */
public class Option implements Serializable {

    private static final long serialVersionUID = -1048707193155512458L;
    
    private String label;
    private int value;
    private int max;
    private int min;
    
    private transient ListenerList listenerList;

    
    public Option(String label, int value, int min, int max) {
        this.label = label;
        this.max = max;
        this.min = min;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
    	if(this.value != value) {
    		this.value = value;
    		Object[] listeners = getListenerList().getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				((FilterOptionChangedListener) listeners[i]).optionChanged(this);
			}
    	}
    }
    
    public ListenerList getListenerList() {
        if(listenerList == null) {
            listenerList = new ListenerList();
        }
        return listenerList;
    }
    
    public void addFilterOptionChangeListener(FilterOptionChangedListener listener) {
        getListenerList().add(listener);
	}
	
	public void removeFilterOptionChangeListener(FilterOptionChangedListener listener) {
        getListenerList().remove(listener);
	}
    
    public String toString() {
        return label+": "+value; //$NON-NLS-1$
    }
}
