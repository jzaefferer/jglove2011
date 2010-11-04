package jGlove.filter;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * Provides methods to edit the fields in a FilterViewer.
 * @see org.eclipse.jface.viewers.ICellModifier
 */
public class FilterCellModifier implements ICellModifier {
    
	private FilterViewer filterViewer;
	
	/**
	 * Constructor 
	 */
	public FilterCellModifier(FilterViewer filterViewer) {
		this.filterViewer = filterViewer;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
        // check for OPTIONS_COLUMN
        int columnIndex = filterViewer.getColumnNamesAsList().indexOf(property);
        switch(columnIndex) {
        case 2:
            IFilter filter = (IFilter) element;
            return filter.getOptions() != null;
        }
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {

		// Find the index of the column
		int columnIndex = filterViewer.getColumnNamesAsList().indexOf(property);

		Object result = null;
		IFilter filter = (IFilter) element;

		switch (columnIndex) {
			case 0 : // ACTIVE_COLUMN 
				result = new Boolean(filter.getActive());
				break;
			case 1 : // NAME_COLUMN 
				result = filter.getName();
				break;
			case 2 : // OPTIONS_COLUMN 
			    result = filter.getOptions(); //.toString();
				break;
			default :
				result = ""; //$NON-NLS-1$
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	

		// Find the index of the column 
		int columnIndex	= filterViewer.getColumnNamesAsList().indexOf(property);
		TableItem item = (TableItem) element;
		IFilter filter = (IFilter) item.getData();
		String valueString;

		switch (columnIndex) {
			case 0 : // ACTIVE_COLUMN 
			    filter.setActive(((Boolean) value).booleanValue());
				break;
			case 1 : // NAME_COLUMN 
				valueString = ((String) value).trim();
				filter.setName(valueString);
				break;
			case 2 : // OPTIONS_COLUMN
                // modification is done via the FilterOptionDialog
				break;

			default :
			}
		filterViewer.getFilterList().filterChanged(filter);
	}
}
