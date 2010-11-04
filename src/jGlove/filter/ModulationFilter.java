package jGlove.filter;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * An implementation of <code>Filter</code> that adds a configurable 
 * sinus wave.
 */
public class ModulationFilter extends Filter implements FilterOptionChangedListener {

	private static final long serialVersionUID = 6680026977927039000L;

    public final static String FILTER_NAME = Messages.getString("ModulationFilter.filtername"); //$NON-NLS-1$
    
    /**
	 * int value array
	 */
	private int[] ival;
	
	/**
	 * double value array;
	 */
	private double[] dval;
	
    /**
     * storing parameter (default is 40)
     */
	private Option durationOption;
	
	/**
     * modulation range (default is 64)
     */
	private Option rangeOption;
	
	/**
	 * factor
	 */
	private double factor;
	
	/**
	 * value loop
	 */
	private double loopValue;
	
	/**
	 * value maximum
	 */
	private int max=0;
	
	/**
	 * value minimum  
	 */
	private int min=1024;
	
	/**
	 * default constructor
	 *
	 */
	public ModulationFilter() {
		setName(FILTER_NAME);
        durationOption = new Option(Messages.getString("ModulationFilter.optionname_duration"), 40, 4, 2000); //$NON-NLS-1$
        durationOption.addFilterOptionChangeListener(this);
        rangeOption = new Option(Messages.getString("ModulationFilter.optionname_range"), 64, 3, 127); //$NON-NLS-1$
        rangeOption.addFilterOptionChangeListener(this);
        setOptions(new OptionList(new Option[]{rangeOption, durationOption}));
	}
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // ListenerList is not serializable, restore listeners
        durationOption.addFilterOptionChangeListener(this);
        rangeOption.addFilterOptionChangeListener(this);
    }
	
	/**
	 * constructor
	 *
	 * @param duration
	 */
	public ModulationFilter(int duration) {
		this();
		durationOption.setValue(duration);
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(int)
	 */
	public int filter(int value) {
		int newValue;
		
		factor = 6.3 / (double) durationOption.getValue(); 
		
		if(ival == null) {
			ival = new int[durationOption.getValue()]; 
			}
		loopValue+=factor; //add factor
		
		if (value > max)   //set maximum
			max = value;
			
		if (value < min)   //set minimum
			min = value;
			
		if (loopValue >= 6.266 )
			loopValue = 0; //reset loop value
		
		if  ( max-min > rangeOption.getValue()/2 ) {
			if ( value > (max-rangeOption.getValue()/2)) {
				value = max-rangeOption.getValue()/2;  // reset upper border
			}
			if (value < (min+rangeOption.getValue()/2)) {
				value = min+rangeOption.getValue()/2;  // reset lower border
			}
		int sinus = (int)( ((Math.sin(loopValue))* rangeOption.getValue()/2 )); //calculate sinus
		newValue = value+sinus;
		}
		else 
			newValue=value;
		return newValue;
	}
	
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(double)
	 */
	public double filter(double value) {
		
		factor = 6.3 / (double) durationOption.getValue(); 
		
		if(dval == null) {
			dval = new double[durationOption.getValue()];
		}
		double newDValue=value;
		
		loopValue += factor; //add factor
		if (loopValue >= 6.266 )
			loopValue = 0; //reset loopvalue
		
		double drange = ((double)rangeOption.getValue()/127.0)/2;
		
		//System.out.println("drange="+drange+"  rangeOption.getValue()"+rangeOption.getValue());
		if (value > 1-drange)
				newDValue = 1-drange; // border high 
		
		if (value<drange)
		        newDValue = drange; // border low
		
		double sinus = ( ((Math.sin(loopValue))* rangeOption.getValue()/(2*127) )); //calculate sinus
		//System.out.println("Sinus="+sinus);
		return newDValue+sinus;
	}
	
	public String toString() {
		return getClass()+" duration: "+durationOption.getValue(); //$NON-NLS-1$
	}

	public void optionChanged(Option option) {
		if(option.equals(durationOption)) {
			dval = null;
			ival = null;
		}
	}
	
	public int getMoreRuns() {
		return -1;
	}
	
}
