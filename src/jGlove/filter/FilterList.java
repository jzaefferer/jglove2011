package jGlove.filter;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * A filterlist can store several <code>IFilter</code>s and <code>IFilterListViewer</code>s
 * as listeners.
 */
public class FilterList implements Serializable, Cloneable {

    private static final long serialVersionUID = 7816627724721655646L;
    
    /**
     * global list of avaible filters
     * <p>
     * add new filters here!
     */
    public final static String[] FILTERS = {
        TiltFilter.FILTER_NAME,
        SmoothingFilter.FILTER_NAME,
        RandomFilter.FILTER_NAME,
        ModulationFilter.FILTER_NAME,
    };
    
    public static IFilter createFilter(int index) {
        IFilter filter = null;
        switch(index) {
        case 0:
            filter = new TiltFilter();
            break;
        case 1:
            filter = new SmoothingFilter();
            break;
        case 2:
            filter = new RandomFilter();
            break;
        case 3:
            filter = new ModulationFilter();
            break; 
        }
        return filter;
    }
    
    private Vector filters;
    
    private static final int COUNT = 3;
	
	private transient Set changeListeners;
	
	private transient IFilter[] filtersCache;
	
	/**
	 * Return the collection of tasks
	 */
	public List getFilters() {
		if(filters == null) {
            filters = new Vector(COUNT);
        }
		return filters;
	}
    
	public Object clone() {
		FilterList filterList = new FilterList();
		if(filters != null) {
			filterList.filters = (Vector)filters.clone();
		}
		return filterList;
	}
	
	public IFilter[] getActiveFilters() {
		if(filtersCache == null) {
			IFilter[] allFilters = (IFilter[])getFilters().toArray(new IFilter[getFilters().size()]);
			int counter = 0;
			for(int i=0; i<allFilters.length; i++) {
				if(allFilters[i].getActive()) counter++;
			}
			IFilter[] activeFilters = new IFilter[counter];
			counter = 0;
			for(int i=0; i<allFilters.length; i++) {
				if(allFilters[i].getActive()) {
					activeFilters[counter++] = allFilters[i];
				}
			}
			filtersCache = activeFilters;
		}
		return filtersCache;
	}

    public Set getChangeListeners() {
    	if(changeListeners == null) {
    		changeListeners = new HashSet();
    	}
		return changeListeners;
	}
    
    public void addFilter(IFilter filter) {
        filtersCache = null;
        getFilters().add(filter);
        Iterator iterator = getChangeListeners().iterator();
        while (iterator.hasNext())
            ((IFilterListViewer) iterator.next()).addFilter(filter);
    }
    
	/**
	 * @param filter
	 */
	public void removeFilter(IFilter filter) {
		filtersCache = null;
		getFilters().remove(filter);
		Iterator iterator = getChangeListeners().iterator();
		while (iterator.hasNext())
			((IFilterListViewer) iterator.next()).removeFilter(filter);
	}

	/**
	 * @param filter
	 */
	public void filterChanged(IFilter filter) {
		filtersCache = null;
		Iterator iterator = getChangeListeners().iterator();
		while (iterator.hasNext())
			((IFilterListViewer) iterator.next()).updateFilter(filter);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IFilterListViewer viewer) {
		getChangeListeners().remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IFilterListViewer viewer) {
		getChangeListeners().add(viewer);
	}
}
