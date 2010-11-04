package jGlove.filter;

/**
 * An abstract base implementation of <code>IFilter</code>.
 */
public abstract class Filter implements IFilter {

	private static final long serialVersionUID = -2779313600514337142L;

	/**
     * The name of this filter (no default)
     */
	protected String name;
	
    /**
     * The active state of this filter (default is true)
     */
    private boolean active = true;
    
    /**
     * The optionlist of this filter (no default options)
     */
    private OptionList options;
    
    /* (non-Javadoc)
     * @see jGlove.filter.IFilter#getOptions()
     */
    public OptionList getOptions() {
        return options;
    }
    
    /* (non-Javadoc)
     * @see jGlove.filter.IFilter#setOptions(jGlove.filter.OptionList)
     */
    public void setOptions(OptionList options) {
        this.options = options;
    }
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#setActive(boolean)
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return active;
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see jGlove.filter.IFilter#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

    /* (non-Javadoc)
     * @see jGlove.filter.IFilter#getMoreRuns()
     */
    public int getMoreRuns() {
    	return 0;
    }
}
