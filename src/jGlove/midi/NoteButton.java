package jGlove.midi;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * NoteButton is used inside the <code>MidiConfigView</code>
 * to select a note and providing the midi value of this 
 * note (60 is C-1).
 */
public class NoteButton extends Composite {

	private Button[] buttons = new Button[12];
	private Spinner octaveSpinner;
	
	private Color white;
	private Color black;
	private Font font;
	
	private final static String[] labels = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
	
	
	public NoteButton(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style);
		if(toolkit == null) {
			throw new IllegalArgumentException("toolkit may not be null"); //$NON-NLS-1$
		}
		toolkit.adapt(this);
		
		white = new Color(parent.getDisplay(), 255, 255, 255);
		black = new Color(parent.getDisplay(), 0, 0, 0);
		font = new Font(parent.getDisplay(), "Verdana", 7, SWT.DEFAULT); //$NON-NLS-1$
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 5;
		layout.marginHeight = layout.marginWidth = 1;
		layout.numColumns = buttons.length;
		setLayout(layout);
		
		GridData gd = new GridData();
		gd.verticalSpan = 2;
		setLayoutData(gd);
		
		parent.addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				white.dispose();
				black.dispose();
			}
		});

		octaveSpinner = new Spinner(this, SWT.NULL);
		octaveSpinner.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		octaveSpinner.setMinimum(1);
		octaveSpinner.setMaximum(9);
		gd = new GridData();
		gd.horizontalSpan = buttons.length;
		octaveSpinner.setLayoutData(gd);
		octaveSpinner.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				notifyListeners(SWT.Selection, new Event());
			}
		});
		
		
		SelectionAdapter buttonListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int value = ((Integer) e.widget.getData()).intValue();
				for (int i=0; i < buttons.length; i++) {
					buttons[i].setSelection(i == value);
				}
				notifyListeners(SWT.Selection, new Event());
			}
		};
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = toolkit.createButton(this, "", SWT.TOGGLE); //$NON-NLS-1$

			buttons[i].setData(new Integer(i));
			buttons[i].addSelectionListener(buttonListener);
			buttons[i].setText(labels[i]);
			buttons[i].setFont(font);
		}
		
		toolkit.paintBordersFor(this);
	}
	
	public void addSelectionListener(SelectionListener listener) {
		if (listener == null) throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		addListener(SWT.Selection, new TypedListener(listener));
	}
	
	public void removeSelectionListener(SelectionListener listener) {
		if (listener == null) throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Selection, new TypedListener(listener));
	}
	
	public void setSelection(int selection) {
		octaveSpinner.setSelection(selection/12);
		buttons[selection%12].setSelection(true);
	}
	
	private int getSelectedNote() {
		for(int i=0; i<buttons.length; i++) {
			if(buttons[i].getSelection()) {
				return i;
			}
		}
		return 0;
	}

	
	public int getSelection() {
		return octaveSpinner.getSelection()*12 + getSelectedNote();
	}

}
