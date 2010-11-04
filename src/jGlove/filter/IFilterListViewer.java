package jGlove.filter;

/**
 * An IFilterListViewer is informed when an IFilter is added, removed
 * or updated.
 */
public interface IFilterListViewer {
	
    /**
	 * Update the view to reflect the fact that a filter was added 
	 * to the filter list
	 * 
	 * @param filter
	 */
	public void addFilter(IFilter filter);
	
	/**
	 * Update the view to reflect the fact that a filter was removed 
	 * from the filter list
	 * 
	 * @param filter
	 */
	public void removeFilter(IFilter filter);
	
	/**
	 * Update the view to reflect the fact that one of the filters
	 * was modified 
	 * 
	 * @param filter
	 */
	public void updateFilter(IFilter filter);
}