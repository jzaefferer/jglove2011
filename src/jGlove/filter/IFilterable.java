package jGlove.filter;

/**
 * An IFilterable can store several <code>IFilter</code>s and provides
 * methods for adding, removing and configuring filters.
 * <p>
 * An abstract implementation is provided by <code>Source</code>
 * @see jGlove.shared.Source
 * @see jGlove.filter.FilterList
 * @see jGlove.filter.IFilter
 */
public interface IFilterable {

	/**
	 * Returns the FilterList of this IFilterable
	 * @return the filterList of this IFilterable
	 */
	public FilterList getFilterList();
	
	/**
	 * Sets the FilterList for this IFilterable
	 * @param filterList the FilterList to set
	 */
    public void setFilterList(FilterList filterList);
	
	/**
	 * Returns all filters associated with this IFilterable object
	 * @return all filters as an array
	 */
	public IFilter[] getFilters();
	
}
