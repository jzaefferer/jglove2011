package jGlove.filter;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.shared.ISource;

import java.util.Arrays;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A <code>FilterViewer</code> is a <code>Composite</code> that is capable
 * of managing an <code>ISource</code>' filters (adding, removing, modifying).
 */
public class FilterViewer extends Composite {

    private final String ACTIVE_COLUMN  = "active"; //$NON-NLS-1$
    private final String FILTER_COLUMN  = "filter"; //$NON-NLS-1$
    private final String OPTIONS_COLUMN = "options"; //$NON-NLS-1$

    // Set column names
    private String[] columnNames = new String[] { 
        ACTIVE_COLUMN,
        FILTER_COLUMN,
        OPTIONS_COLUMN,
    };
    
    private Composite parent;
    
    private Button addButton;
    private Button deleteButton;
    
    private Table filterTable;
    private TableViewer filterTableViewer;
    
    private FilterList filterList;
    
    public FilterViewer(Composite parent, ISource source) {
        this(parent, SWT.NULL, source);
    }
    
    public FilterViewer(Composite parent, int style, ISource source) {
        super(parent, style);
        filterList = source.getFilterList();
        
        this.parent = this;
        GridLayout layout = new GridLayout();
        layout.marginHeight = layout.marginWidth = 0;
        layout.numColumns = 2;
        setLayout(layout);
        
        createControl();
    }
    
	private void createControl() {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
		SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		filterTable = new Table(parent, style);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.heightHint = 50;
		gd.verticalSpan = 2;
		filterTable.setLayoutData(gd);
		filterTable.setLinesVisible(true);
		filterTable.setHeaderVisible(false);
        
		
		// 1st column with active button
		TableColumn column = new TableColumn(filterTable, SWT.NULL, 0);		
		column.setText(Messages.getString("FilterViewercolumnname_active")); //$NON-NLS-1$
		column.setWidth(20);
		
		// 2nd column with filter name
		column = new TableColumn(filterTable, SWT.LEFT, 1);
		column.setText(Messages.getString("FilterViewercolumnname_filter")); //$NON-NLS-1$
		column.setWidth(100);

		// 3rd column with filter options
		column = new TableColumn(filterTable, SWT.LEFT, 2);
		column.setText(Messages.getString("FilterViewercolumnname_options")); //$NON-NLS-1$
		column.setWidth(150);
		
		filterTableViewer = new TableViewer(filterTable);
		filterTableViewer.setUseHashlookup(true);
		filterTableViewer.setColumnProperties(columnNames);
		
//		 Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];

		editors[0] = new CheckboxCellEditor(filterTable);
		editors[1] = new TextCellEditor(filterTable, SWT.NULL);
		editors[2] = new DialogCellEditor(filterTable) {
			protected Object openDialogBox(Control cellEditorWindow) {
				OptionList optionList = (OptionList)getValue();
				FilterOptionsDialog dialog = new FilterOptionsDialog(cellEditorWindow.getShell(), optionList);
				dialog.open();
				return null;
			}
		};
		

		// Assign the cell editors to the viewer 
		filterTableViewer.setCellEditors(editors);
		
		// Set the cell modifier for the viewer
		filterTableViewer.setCellModifier(new FilterCellModifier(this));

		filterTableViewer.setContentProvider(new FilterContentProvider());
		filterTableViewer.setLabelProvider(new FilterLabelProvider());
        
		// The input for the table viewer is the instance of ExampleTaskList
		filterTableViewer.setInput(filterList);
		
		addButton = new Button(parent, SWT.PUSH);
		addButton.setText(Messages.getString("FilterViewerbutton_add")); //$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 50;
		gd.heightHint = 25;
		addButton.setLayoutData(gd);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                ChooseFilterDialog dialog = new ChooseFilterDialog(parent.getShell());
                if(dialog.open() == Window.OK) {
                    int selection = dialog.getSelection();
                    filterList.addFilter(FilterList.createFilter(selection));
                }
            }
        });
        
		
		deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setText(Messages.getString("FilterViewerbutton_remove")); //$NON-NLS-1$
		deleteButton.setEnabled(false);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 50;
		gd.heightHint = 25;
		deleteButton.setLayoutData(gd);
        deleteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                IFilter filter = (IFilter) ((IStructuredSelection) 
                        filterTableViewer.getSelection()).getFirstElement();
                if (filter != null) {
                    filterList.removeFilter(filter);
                }               
            }
        });
        filterTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
            public void selectionChanged(SelectionChangedEvent event) {
                IFilter filter = (IFilter) ((IStructuredSelection) 
                        filterTableViewer.getSelection()).getFirstElement();
                if (filter != null) {
                    deleteButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                }
            }
        });
	}
	
	/**
	 * Return the column names in a collection
	 * 
	 * @return List  containing column names
	 */
	public java.util.List getColumnNamesAsList() {
		return Arrays.asList(columnNames);
	}

	/**
	 * @return currently selected item
	 */
	public ISelection getSelection() {
		return filterTableViewer.getSelection();
	}

	/**
	 * Return the ExampleTaskList
	 */
	public FilterList getFilterList() {
		return filterList;	
	}
	
    /**
	 * InnerClass that acts as a proxy for the FilterList providing content
	 * for the Table. It implements the IFilterListViewer interface since it must
	 * register changeListeners with the ExampleTaskList
	 */
	private class FilterContentProvider implements IStructuredContentProvider,
			IFilterListViewer {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null)
				((FilterList) newInput).addChangeListener(this);
			if (oldInput != null)
				((FilterList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			filterList.removeChangeListener(this);
		}

		public Object[] getElements(Object parent) {
			return filterList.getFilters().toArray();
		}

		public void addFilter(IFilter filter) {
			filterTableViewer.add(filter);
		}

		public void removeFilter(IFilter filter) {
			filterTableViewer.remove(filter);
		}

		public void updateFilter(IFilter filter) {
			filterTableViewer.update(filter, null);
		}
	}

    private class FilterLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		/**
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 *      int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			IFilter filter = (IFilter) element;
			switch (columnIndex) {
			case 0: // COMPLETED_COLUMN
				break;
			case 1:
				result = filter.getName();
				break;
			case 2:
                OptionList options = filter.getOptions();
				if(options != null) {
				    result = options.toString();
                } else {
                    result = Messages.getString("FilterViewererror_nooptions"); //$NON-NLS-1$
                }
				break;
			default:
				break;
			}
			return result;
		}

		/**
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
		 *      int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return (columnIndex == 0) ?   // COMPLETED_COLUMN?
		            getImage(((IFilter) element).getActive()) : 
		                null;
		}
		
		private Image getImage(boolean active) {
		    return JGlovePlugin.getImage((active) ? IJGloveConstants.IMG_CHECKED : IJGloveConstants.IMG_UNCHECKED); 
		}
	}
	
}
