package jGlove.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

/**
 * An implementation of <code>Filter</code> that smoothes the signal
 * with in- and outfadings without latency.
 */
public class SmoothingFilter extends Filter implements FilterOptionChangedListener {
    
	private static final long serialVersionUID = 3556508590705475915L;

    public final static String FILTER_NAME = Messages.getString("SmoothingFilter.filtername"); //$NON-NLS-1$
    
	/**
     * limit cut values
     */
    private Option cutOption;
	
    /**
     * storing parameter 
     */
	private Option durationOption;
    
	/**
	 * storing values for fade in+out
	 */
	private transient int
        ueber = 0,
        loop = 0,
	    buffer = 0,
        bufferValue,
        storedValue = 0;
	
	private transient double
        dueber = 0,
        dbuffer = 0,
        dbufferValue,
        dstoredValue = 0;
	 
	/**
	 * default constructor
	 *
	 */
	public SmoothingFilter() {
		setName(FILTER_NAME);
        durationOption = new Option(Messages.getString("SmoothingFilter.durationname"), 6, 1, 500); //$NON-NLS-1$
        durationOption.addFilterOptionChangeListener(this);
        cutOption = new Option(Messages.getString("SmoothingFilter.cutname"), 20, 0, 500); //$NON-NLS-1$
        cutOption.addFilterOptionChangeListener(this);
        setOptions(Arrays.asList(new Option[]{cutOption, durationOption}));
	}
	
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // ListenerList is not serializable, restore listeners
        durationOption.addFilterOptionChangeListener(this);
        cutOption.addFilterOptionChangeListener(this);
    }
    
	/**
	 * constructor with parameters
	 *
	 * @param cut must be one or greater
	 * @param duration must be one or greater
	 */
	public SmoothingFilter(int cut, int duration) {
		this();
		cutOption.setValue(cut);
		durationOption.setValue(duration);
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(int)
	 */
	public int filter(int value) {
		int newValue;
		boolean flag = false;
		
		// check if value is within "cut" range
		if ( ( Math.abs(storedValue-value) ) > cutOption.getValue() ) {
			buffer = value-storedValue; 
			ueber += (buffer / durationOption.getValue()); 
		    loop = durationOption.getValue();
            flag=true;    
		}
	    // set new value
		newValue = value;
			
		// correct new value if the variable ueber is set
		if (ueber!=0){
            if (!flag) { 
				buffer = value-bufferValue; 
				ueber = (buffer / loop);
	            }
			newValue = bufferValue+ueber;
		}
       
		// set loop parameter	
		if (loop > 1){ 
			loop--;
	       }
		else 
	       ueber=0; // reset var ueber
			  
	    // additional algotrithm logic
		if ( ( newValue>=value && ueber>0 ) ) {
			buffer = value-bufferValue;
			ueber  = (buffer / loop); 
			newValue=bufferValue+ueber;
			//System.out.println("1 Buffer:"+buffer+" NewValue:"+newValue+" Loop:"+loop+" Ueber:"+ueber);
		}
		else 
		if ( ( newValue<=value && ueber<0 ) ) {
			buffer = value-newValue;
			ueber  = (buffer / loop); 
			newValue=bufferValue+ueber;
			//System.out.println("2 Buffer:"+buffer+" NewValue:"+newValue+" Loop:"+loop+" Ueber:"+ueber);
		}
		
		// reset - possibly obsolete
		if (newValue==value) {
			ueber=0;}
		 
        // storing current value
		storedValue=value;
		
        // storing new value
		bufferValue=newValue;
	
		return newValue;	
	}

	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#filter(double)
	 */
	public double filter(double dvalue) {
		double newValue;
		boolean flag = false;
		
		// check if value is within "cut" range
		if ( ( Math.abs(dstoredValue-dvalue) ) > cutOption.getValue()/10.0 ) {
			dbuffer = dvalue-dstoredValue; 
			dueber += (dbuffer / durationOption.getValue()); 
		    loop = durationOption.getValue();
            flag=true;    
		}
	    // set new value
		newValue = dvalue;
			
		// correct new value if the variable ueber is set
		if (dueber!=0.0){
            if (!flag) { 
				dbuffer = dvalue-dbufferValue; 
				dueber = (dbuffer / (double)loop);
	            }
			newValue = dbufferValue+dueber;
		}
       
		// set loop parameter	
		if (loop > 1){ 
			loop--;
	       }
		else 
	       ueber=0; // reset var ueber
			  
	    // additional algotrithm logic
		if ( ( newValue>=dvalue && dueber>0 ) ) {
			dbuffer = dvalue-dbufferValue;
			dueber  = (dbuffer / (double)loop); 
			newValue=dbufferValue+dueber;
			//System.out.println("1 Buffer:"+dbuffer+" NewValue:"+newValue+" Loop:"+loop+" Ueber:"+dueber);
		}
		else 
		if ( ( newValue<=dvalue && dueber<0 ) ) {
			dbuffer = dvalue-newValue;
			dueber  = (dbuffer / (double)loop); 
			newValue=dbufferValue+dueber;
			//System.out.println("2 Buffer:"+dbuffer+" NewValue:"+newValue+" Loop:"+loop+" Ueber:"+dueber);
		}
		
		// reset - can maybe deleted...
		if (newValue==dvalue) {
			dueber=0;}
		 
        // storing current value
		dstoredValue=dvalue;
		
        // storing new value
		dbufferValue=newValue;
	
		return newValue;	
	}
	
	public String toString() {
		return getClass()+ " cut: "+cutOption.getValue()+", duration: "+durationOption.getValue(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void optionChanged(Option option) {
		if(option.equals(durationOption)) {
			dbufferValue = 0;
			bufferValue = 0;
		}
	}
	
	public int getMoreRuns() {
		return durationOption.getValue();
	}
	
}