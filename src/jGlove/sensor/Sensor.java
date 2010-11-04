package jGlove.sensor;

import jGlove.filter.IFilter;
import jGlove.shared.Source;

/**
 * A Sensor object is bound to a channel from the GloveBox
 * and provides methods to calibrate this channel.
 */
public class Sensor extends Source {
	
	private static final long serialVersionUID = -2285964678446822764L;

    /**
     * The index of this Sensor is the index of the channel that it
     * is associated with<br>
     * The index cannot be changed after the object is created.
     */
	private int index;
	
    private int calibrationHigh = 1024;
    private int calibrationLow = 0;
    
    /**
     * the raw, non-calibrated-value of this sensor
     */
    private int valueRaw;
    
    /**
     * the scaled (8bit) value of this sensor 
     */
    private int valueScaled;
	
    /**
     * the calibration-factor is calculated when setting the
     * calibration and is used to calculate the "scaled" (8bit)
     * value
     */
	private double calibrationFactor = 1.0;
	
    /**
     * Creates a new Sensor
     * @param index the index of this sensor, it cannot be changed later
     * @param value a starting rawvalue for this sensor
     */
	public Sensor(int index, int value) {
		this.index = index;
		setValueRaw(value);
		setActive(false);
		setName(Messages.Sensor_defaultname_prefix +index);
		setType(0);
	}
	
	/**
	 * Filters the given value, checks wheather it has changed, if
     * it has changed, the scaled value is calculated and a "value"
     * PropertyChangeEvent is fired.
	 * @param valueRaw the raw value to check
	 */
	public void setValueRaw(int valueRaw) {
		IFilter[] filters = getFilters();
		if(filters != null) {
			for(int i=0; i<filters.length; i++) {
				valueRaw = filters[i].filter(valueRaw);
			}
		}
		if(this.valueRaw != valueRaw) {
			this.valueRaw = valueRaw;
			if(valueRaw > calibrationHigh) {
				valueScaled = 255;
			} else if(valueRaw < calibrationLow) {
				valueScaled = 0;
			} else {
				valueScaled = (int)Math.round((valueRaw - calibrationLow) / calibrationFactor);
			}
			getPropertyChangeSupport().firePropertyChange("value", valueRaw); //$NON-NLS-1$
		}
	}
	
	public int getValue() {
		return valueRaw;
	}
	
	public int getValueScaled() {
		return valueScaled;
	}
	
	public int getValueMidi() {
		int midiValue = valueScaled / 2;
		midiValue = Math.min(127, midiValue);
		midiValue = Math.max(0, midiValue);
		return midiValue;
	}
	
	public int getCalibrationHigh() {
		return calibrationHigh;
	}
	
	public int getCalibrationLow() {
		return calibrationLow;
	}
	
	/**
	 * calculates the calibration factor from the lower
	 * and higher calibration values<br>
     * It does not matter which one of the two is higher
     * or lower.
	 * @param first one of the two calibration values
	 * @param second the other one of the two calibration values
	 */
	public void setCalibration(int first, int second) {
		if(first > second) {
			calibrationHigh = first;
			calibrationLow = second;
		} else {
			calibrationHigh = second;
			calibrationLow = first;
		}
		calibrationFactor = (calibrationHigh - calibrationLow) / 255.0;
	}
	
    
	/**
     * Returns the index of this Sensor
	 * @return  the index of the channel that this sensor
     *          is associated with
	 */
	public int getIndex() {
		return index;
	}
	
	public String toString() {
		return "i: "+index+", valueMidi: "+ getValueMidi() +", valueRaw: "+valueRaw+", valueScaled: "+valueScaled; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
