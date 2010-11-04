package jGlove.filter;

import java.io.IOException;
import java.io.ObjectInputStream;



/**
 * An implementation of <code>Filter</code> that stabilizes the signal
 * thorugh interpolation, causes a latency, depending on the
 * configuration.
 */
public class TiltFilter extends Filter implements FilterOptionChangedListener {
    
	private static final long serialVersionUID = 6680036977927039000L;

    public final static String FILTER_NAME = Messages.getString("TiltFilter.filtername"); //$NON-NLS-1$
    
	/**
	 * int value array
	 */
	private int[] ival;
	
	/**
	 * double value array;
	 */
	private double[] dval;
	
	/**
	 * array pointer
	 */
	private int x=0;
 
    /**
     * storing parameter (default is 6)
     */
//    private int duration = 6;
	private Option durationOption;
	
    /**
     * last filtered value
     */
	private double bufferValue;
	
	/**
	 * default constructor
	 *
	 */
	public TiltFilter() {
		setName(FILTER_NAME);
        durationOption = new Option(Messages.getString("TiltFilter.optionname_duration"), 6, 2, 500); //$NON-NLS-1$
        durationOption.addFilterOptionChangeListener(this);
        setOptions(new OptionList(new Option[]{durationOption}));
	}
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // ListenerList is not serializable, restore listeners
        durationOption.addFilterOptionChangeListener(this);
    }
	
	/**
	 * constructor
	 *
	 * @param duration
	 */
	public TiltFilter(int duration) {
		this();
		durationOption.setValue(duration);
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(int)
	 */
	public int filter(int value) {
		if(ival == null) {
			ival = new int[durationOption.getValue()];
		}
		
		// move array pointer
		if (x < durationOption.getValue() - 1)
			x++;
		else
			x = 0;
		
		// calculate interpolation value
		bufferValue = (bufferValue-(ival[x]/durationOption.getValue()*1.0)+(value/durationOption.getValue()*1.0)); 
		
		// store new filter value 
		int newValue = (int) (Math.round(bufferValue * 10) / 10);
		
		// store new value
        ival[x] = value;
		return newValue;
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(double)
	 */
	public double filter(double value) {
		if(dval == null) {
			dval = new double[durationOption.getValue()];
		}
		
		// move array pointer
		if (x < durationOption.getValue() - 1)
			x++;
		else
			x = 0;
		double newDValue = (bufferValue-(dval[x]/durationOption.getValue())+(value/durationOption.getValue()));
		// store new value
        dval[x] = value;
        
        // calculate and return interpolation value
		return newDValue;
	}
	
	public String toString() {
		return getClass()+" duration: "+durationOption.getValue(); //$NON-NLS-1$
	}

	public void optionChanged(Option option) {
		if(option.equals(durationOption)) {
			dval = null;
			ival = null;
			bufferValue = 0;
		}
	}
	
}

