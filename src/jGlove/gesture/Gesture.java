package jGlove.gesture;

import jGlove.filter.IFilter;
import jGlove.sensor.Sensor;
import jGlove.shared.ISource;
import jGlove.shared.Source;

/**
 * An implementation of <code>Source</code>, providing
 * either simple or approximated gesture recognition.
 * <p>
 * The gesture recognition methods return a
 * double value between 0 and 1. 
 */
public class Gesture extends Source {

	private static final long serialVersionUID = 8532954597115684823L;

	/**
     * The array of sensors that this gesture depends on, must be at least
     * one.
     */
	private Sensor[] sensors;
	
	/**
	 * The target sensor values, used when calculating the gesture.
     * <p>
     * If all target values equal the current values of the sensor, the
     * gesture is 100% correct. 
	 */
	private int[] values;
	
	/**
	 * The tolerance value of this gesture (default is 50)
     * <p>
     * A higher tolerance makes it easier to reach the gesture. 
	 */
	private int tolerance = 50;
	
	/**
	 * If true, an approximation value is calculated instead of only
     * true or false when the gesture is calculated.
	 */
	private boolean calculateApproximation;
	
	/**
	 * The last calculated value of this gesture, if approximation is
     * false, it is either 0 or 1, if approximation is true, it is
     * between 0 and 1
	 */
	private double calculation;

	/**
     * Creates a new gesture with the given Sensor objects
     * @param sensors the array of sensors that this gesture depends 
     *        on, must be at least one
	 */
	public Gesture(Sensor[] sensors) {
		this.sensors = sensors;
		setType(ISource.TYPE_GESTURE);
	}
	
	/**
     * Returns the target values of this gesture
     * @return the target values of this gesture, at least one
	 */
	public int[] getValues() {
		return values;
	}
	
	/**
     * Returns the sensors that this gesture depends on
     * @return the sensors that this gesture depends on
	 */
	public Sensor[] getSensors() {
		return sensors;
	}
	
	/**
     * Returns wheather approximation is calculated for this
     * gesture or not
     * @return true, if approximation is calculated
	 */
	public boolean isApproximated() {
		return calculateApproximation;
	}
	
	/**
     * Sets the target values for this gesture
     * @param values the target values, if all match the current
     *        gesture value, the gesture is 100% correct
	 */
	public void setValues(int[] values) {
		this.values = values;
	}
	
	/**
	 * setter tolerance value
	 * 
	 * @param tolerance value 
	 */
	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}
	
	/**
     * If true, an approximation value is calculated instead of only
     * true or false when the gesture is calculated.
     * @param calculateApproximation if true, a approximation is calculated
	 */
	public void setCalculateApproximation(boolean calculateApproximation) {
		this.calculateApproximation = calculateApproximation;
	}
	
    /**
     * The tolerance value of this gesture (default is 50)
     * <p>
     * A higher tolerance makes it easier to reach the gesture.
     * @return the tolerance of this gesture
     */
	public int getTolerance() {
		return tolerance;
	}
	
	/**
	 * Returns the last calculated value 
	 * @see #calculate()
	 * @return  0 or 1 (no approximation) or x for 0 <= x <= 1
	 */
	public int getValue() {
		return (int)Math.round(calculation*100);
	}
	
	public double getCalculation() {
		return calculation;
	}
	
	/**
	 * Returns the last calculated value in midi range
	 * @see #calculate()
	 * @return 0 or 127 (no approximation) or x for 0 <= x <= 127
	 */
	public int getValueMidi() {
		return (int)(127*calculation);
	}
	
	/**
	 * Returns the last calculated value in 8-bit mode
	 * @see #calculate()
	 * @return 0 or 255 (no approximation) or x for 0 <= x <= 255
	 */
	public int getValueScaled() {
		return getValue();
	}
	
	/**
	 * Reset the gesture
	 * <p>
	 * use this method to prevent the check() returning true
	 * when the gesture is not updated anymore
	 */
	public void reset() {
		calculation = 0;
	}
	
	/**
	 * calculate the gesture values
	 * 
	 * should be invoked in a certain interval
	 * 
	 * @see #getValueMidi() 
	 */
	public void calculate() {
		if(values == null) {
			return;
		}
        // advanced gesture check
		if (calculateApproximation) {
			double value = 0.0;
			// calculate tolerance approximation value for every gesture sensor and add value
			for (int i = 0; i < sensors.length; i++) {
				int diff = Math.abs(values[i] - sensors[i].getValue()); // calculate difference
				if (diff < tolerance) { // check if sensor value is within tolerance range
					int q = tolerance - diff;
					double p = (double) q / tolerance; 
					value += p / (double) sensors.length;
				}
			}		
			// retrieve filters and filter the calculated value
			IFilter[] filters = getFilters();
			if(filters != null) {
				for(int i=0; i<filters.length; i++) {
					value = filters[i].filter(value);
				}
			}
			if(calculation != value) {
				calculation = value;
				getPropertyChangeSupport().firePropertyChange("value", new Double(calculation)); //$NON-NLS-1$
			}
	    } 
        // simple gesture check
		else {
			int value = 1;
	    	
			for (int i = 0; i < sensors.length; i++) {
				// check if sensor value is not within tolerance range
				if(Math.abs(values[i] - sensors[i].getValue()) > tolerance ) {
					value = 0; // no gesture
				}
			}
			if(value != calculation) {
				calculation = value;
				getPropertyChangeSupport().firePropertyChange("value", new Double(calculation)); //$NON-NLS-1$
			}
	    }
	}
	
	/**
	 * Returns wheather the gesture is recognized or not
	 * @return true, if the gesture is recognized
	 */
	public boolean check() {
		return calculation > 0.0;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(getName() != null) sb.append(getName());
		if(values != null) {
			sb.append("\n[ "); //$NON-NLS-1$
			for(int i=0; i<values.length; i++) {
				sb.append(values[i]);
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append("]"); //$NON-NLS-1$
		}
		if(sensors != null) {
			sb.append("\n[ "); //$NON-NLS-1$
			for(int i=0; i<sensors.length; i++) {
				sb.append(sensors[i].toString());
				sb.append(", "); //$NON-NLS-1$
			}
			sb.append("]"); //$NON-NLS-1$
		}
		
		sb.append("\nValue: "); //$NON-NLS-1$
		sb.append(getValue());
		return sb.toString();
	}
}
