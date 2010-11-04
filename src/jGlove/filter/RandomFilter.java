package jGlove.filter;

/**
 * An implementation of <code>Filter</code> that adds and subtracts 
 * random values.
 * TODO improve "randomness"
 * TODO add Options to make this filter configurable
 */
public class RandomFilter extends Filter {

    private static final long serialVersionUID = -6639874658168992624L;

    public final static String FILTER_NAME = Messages.getString("RandomFilter.filtername"); //$NON-NLS-1$
    
    public RandomFilter() {
        setName(FILTER_NAME);
    }
    
    protected void init() {
    	// do nothing
    }
    
    public int filter(int value) {
        value += Math.random() * 50 + Math.random() * 10;
        value += Math.random() * 50 + Math.random() * 10;
        value -= Math.random() * 50 + Math.random() * 10;
        if (Math.random() * 10 < 5) value += 20;
        if ((int) (Math.random() * 10) == 2 || (int) (Math.random() * 10) == 9) {
            value += 18;
        }
        if ((int) (Math.random() * 10) == 4 || (int) (Math.random() *10) == 8) {
            if(Math.random() < 5) value += (Math.random()*10);
            if(Math.random() > 5) value -= (Math.random()*10);
        }
        
        return value;
    }

    public double filter(double value) {
        value += Math.random() * 0.2 + Math.random() * 0.15;
        if (Math.random() * 10 < 5) value += 0.2;
        if ((int) (Math.random() * 10) == 2 || (int) (Math.random() * 10) == 9) {
            value += 0.18;
        }
        if ((int) (Math.random() * 10) == 4 || (int) (Math.random() *10) == 8) {
            if(Math.random() < 5) value += Math.random();
            if(Math.random() > 5) value -= Math.random();
        }
        return value;
    }

    public int getMoreRuns() {
    	return -1;
    }
    
}
