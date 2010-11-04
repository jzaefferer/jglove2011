package jGlove.filter;


import java.io.Serializable;

/**
 * A IFilter provides methods to filter int and double values and can have
 * a name, active state and several <code>Option</code>s.
 * <p>
 * Clients should extend the base implementation <code>Filter</code>.
 * <p>
 * Never use a IFilter object twice - caching values necessary for
 * interpolation are stored inside the IFilter object and results are
 * unpredictable, if an IFilter object is used more then once
 * 
 * @see IFilterable
 */
public interface IFilter extends Serializable {
    
	/**
	 * Filter the given int value and return it
	 * @param value the raw value
	 * @return the filtered value
	 */
	public int filter(int value);
	
	/**
	 * Filter the given double value and return it
	 * @param value the raw value
	 * @return the filtered value
	 */
	public double filter(double value);
	
    /**
     * Returns the name for this IFilter
     * @return the name of this IFilter
     */
	public String getName();
    
    /**
     * Sets the name for this IFilter
     * @param name the name for this IFilter
     */
	public void setName(String name);
    
    /**
     * Returns the active state for this IFitler
     * @return the active state for this IFitler
     */
	public boolean getActive();
    
    /**
     * Sets the active state for this IFitler
     * @param active the active state for this IFitler
     */
	public void setActive(boolean active);


    /**
     * Returns the OptionList for this IFilter
     * @return the OptionList for this IFilter
     */
    public OptionList getOptions();
    
    /**
     * Sets the OptionList for this IFilter
     * @param options the OptionList for this IFilter
     */
    public void setOptions(OptionList options);
    
    /**
     * override this method if your filter is a synthetic filter
     * <p>
     * if your filter generates values all the time, return -1
     * <p>
     * if your filter needs a limited number of additional
     * calculation to display a useful value, return this number
     * <p>
     * Default should return 0, resulting in only changed values being filtered
     * 
     */
    public int getMoreRuns();
}
