package jGlove.logfile;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that displays a form to configure a <code>LogFileFormat</code>
 * and write an output file specified by that format.
 * <p>
 * Support for start delay and limited runtime is not fully implemented
 * and therefore commented out.
 * 
 */
public class LogView extends ViewPart {

	public static final String ID = "jGlove.app.views.logView"; //$NON-NLS-1$
	
	private Button timestamp;
	private Button timestampEnd;
	private Button sourceName;
	private Button gestures;
	private Text pathText;
	private Text separatorText;
	private Spinner interval;
//	private Spinner delay;
//	private Spinner tsec;
//	private Spinner tmin;
//	private Spinner thours;
	private Button start;
	private Text previewText;
	private Label message;
    
	private FormToolkit toolkit;
	private ScrolledForm form;
	private Composite body;
	
	private LogFileFormat lff;
	private LogFileWriter lfw;
	
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }
    
    public void setFocus() {
    	start.setFocus();
    }
    
	public void createPartControl(final Composite parent) {
		
		lff = new LogFileFormat();

		toolkit = new FormToolkit(parent.getDisplay());
//		toolkit.adapt(parent);
        
		form = toolkit.createScrolledForm(parent);
		body = form.getBody();
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing=7;
		body.setLayout(layout);
		
		GridData gd;

		// timestamp
		
		Label lt = toolkit.createLabel(body, Messages.LogView_timestamp);
		lt.setLayoutData(getLeftLabelGridData());
		timestamp = toolkit.createButton(body, "", SWT.CHECK); //$NON-NLS-1$
		timestamp.setSelection(lff.isTimestamp());
		timestamp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lff.setTimestamp(timestamp.getSelection());
				updatePreview();
			}
		});
		
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		timestamp.setLayoutData(gd);
		
		// timestampEnd
		
		Label lte = toolkit.createLabel(body, Messages.LogView_timestampend);
		lte.setLayoutData(getLeftLabelGridData());
		timestampEnd = toolkit.createButton(body, "", SWT.CHECK); //$NON-NLS-1$
		timestampEnd.setSelection(lff.isTimestampEnd());
		timestampEnd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lff.setTimestampEnd(timestampEnd.getSelection());
				updatePreview();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		timestampEnd.setLayoutData(gd);
	    
		// source name in header
		
		Label lsr = toolkit.createLabel(body, Messages.LogView_sourcenames);
		lsr.setLayoutData(getLeftLabelGridData());
		sourceName = toolkit.createButton(body, "", SWT.CHECK); //$NON-NLS-1$
		sourceName.setSelection(lff.isSourceName());
		sourceName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lff.setSourceName(sourceName.getSelection());
				updatePreview();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		sourceName.setLayoutData(gd);
		
     
		// include gestures
		
		Label lg = toolkit.createLabel(body, Messages.LogView_includegestures);
		lg.setLayoutData(getLeftLabelGridData());
		gestures = toolkit.createButton(body, "", SWT.CHECK); //$NON-NLS-1$
		gestures.setSelection(lff.isGesture());
		gestures.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lff.setGesture(gestures.getSelection());
				updatePreview();
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		gestures.setLayoutData(gd);
		
		// separator
		
		Label ls = toolkit.createLabel(body,Messages.LogView_seperator);
		ls.setLayoutData(getLeftLabelGridData());
		separatorText = toolkit.createText(body, lff.getSeparator());
		separatorText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				lff.setSeparator(separatorText.getText());
				updatePreview();
			}
		});
		gd = new GridData();
		gd.widthHint=40;
		gd.horizontalSpan = 2;
		separatorText.setLayoutData(gd);
		
        // format
		SelectionAdapter formatListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int value = ((Integer)e.widget.getData()).intValue();
				lff.setFormat(value);
				updatePreview();
			}
		};
		
		Label lfo = toolkit.createLabel(body,Messages.LogView_format);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END|GridData.VERTICAL_ALIGN_BEGINNING);
		gd.verticalSpan =3;
		lfo.setLayoutData(gd);
		Button f1 = toolkit.createButton(body, IJGloveConstants.MODE_10BIT, SWT.RADIO);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		f1.setLayoutData(gd);
		f1.setSelection(true);
		f1.setData(new Integer(0));
		f1.addSelectionListener(formatListener);
		
		Button f2 = toolkit.createButton(body, IJGloveConstants.MODE_8BIT, SWT.RADIO);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		f2.setLayoutData(gd);
		f2.setData(new Integer(1));
		f2.addSelectionListener(formatListener);
		
		Button f3 = toolkit.createButton(body, IJGloveConstants.MODE_7BIT, SWT.RADIO);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		f3.setLayoutData(gd);
		f3.setData(new Integer(2));
		f3.addSelectionListener(formatListener);
		
		// interval
		toolkit.createLabel(body, "Interval (ms)").setLayoutData(getLeftLabelGridData());
		interval = createSpinner(body, true, 1, 0, 999);
		interval.setSelection(lff.interval);
		interval.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				lff.interval = interval.getSelection();
			}
		});
		interval.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				lff.interval = interval.getSelection();
			}
		});
		gd = new GridData();
		gd.horizontalSpan = 2;
		interval.setLayoutData(gd);
		
		// start delay
		
//		Label sd = toolkit.createLabel(body, "Start delay(sec)");
//		sd.setLayoutData(getLeftLabelGridData());
//		delay = createSpinner(body, true, 1, 0, 999);
//		delay.setSelection(lff.getStartDelay());
//		delay.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				lff.setStartDelay(delay.getSelection());
//			}
//		});
//		gd = new GridData();
//		gd.horizontalSpan = 2;
//		delay.setLayoutData(gd);
		
        // time
		
        
        
//		Label ti = toolkit.createLabel(body, "Time(hours,min,sec)");
//        ti.setLayoutData(getLeftLabelGridData());
//        
//        Composite timeComposite = new Composite(body, SWT.NULL);
//        layout = new GridLayout(3, false);
//        layout.marginWidth = 1;
//        layout.marginHeight = 1;
//        timeComposite.setLayout(layout);
//        toolkit.adapt(timeComposite);
//        gd = new GridData();
//        gd.horizontalSpan = 2;
//        timeComposite.setLayoutData(gd);
//        
//		thours = createSpinner(timeComposite, true, 1, 0, 999);
//		thours.setSelection(lff.getHours());
//		thours.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				lff.setStartDelay(delay.getSelection());
//			}
//		});
//	
//		tmin = createSpinner(timeComposite, true, 1, 0, 59);
//		tmin.setSelection(lff.getMinutes());
//		tmin.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				lff.setStartDelay(delay.getSelection());
//			}
//		});
//		
//		tsec = createSpinner(timeComposite, true, 1, 0, 59);
//		tsec.setSelection(lff.getSeconds());
//		tsec.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				lff.setStartDelay(delay.getSelection());
//			}
//		});
		
		// filename
	
		Label label = toolkit.createLabel(body, Messages.LogView_preview);
		label.setLayoutData(getLeftLabelGridData());
		previewText = toolkit.createText(body, "", SWT.WRAP); //$NON-NLS-1$
		previewText.setEditable(false);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 50;
		gd.widthHint = 250;
		previewText.setLayoutData(gd);
		
		
		Label lfi = toolkit.createLabel(body,Messages.LogView_filename);
		lfi.setLayoutData(getLeftLabelGridData());
		pathText = toolkit.createText(body, ""); //$NON-NLS-1$
		pathText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				String text = pathText.getText();
				if(text.length() > 0) {
					lff.setPath(text);
					start.setEnabled(true);
                    message.setText(""); //$NON-NLS-1$
				} else {
					start.setEnabled(false);
                    message.setText(Messages.LogView_error_choosefile);
				}
			}
		});
		gd = new GridData();
		gd.widthHint=150;
		pathText.setLayoutData(gd);
		
		Button open = toolkit.createButton(body, Messages.LogView_choosebutton, SWT.PUSH);
		gd = new GridData();
        gd.widthHint = 75;
//        gd.heightHint = 25;
		open.setLayoutData(gd);
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(body.getShell(), SWT.SAVE);
				String path = dialog.open();
				if(path != null && path.length() > 0) {
					pathText.setText(path);
					start.setEnabled(true);
                    message.setText(""); //$NON-NLS-1$
				}
			}
		});
		
        toolkit.createLabel(body, Messages.LogView_startstop).setLayoutData(getLeftLabelGridData());
		start = toolkit.createButton(body, Messages.LogView_startbutton, SWT.TOGGLE);
		gd = new GridData();
		gd.widthHint = 75;
		gd.heightHint = 25;
		gd.horizontalSpan = 2;
		start.setLayoutData(gd);
		start.setEnabled(false);
		start.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_RECORD));
		start.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(lfw == null) {
					lfw = new LogFileWriter(lff);
					lfw.start();
                    message.setText(Messages.LogView_status_logging);
					System.out.println();
				} else {
					lfw.interrupt();
					lfw = null;
                    message.setText(Messages.LogView_status_stopped);
				}
			}
		});
        
        toolkit.createLabel(body, ""); //$NON-NLS-1$
        message = toolkit.createLabel(body, Messages.LogView_error_choosefile_beforestarting, SWT.WRAP);
		gd = new GridData();
        gd.horizontalSpan = 2;
        message.setLayoutData(gd);
        
//        new Label(body, SWT.NULL);
//		gd = new GridData();
//		gd.widthHint=120;
//		gd.horizontalSpan = 2;
//		Button debugButton = toolkit.createButton(body, "SHOW HEADER/LINE", SWT.PUSH);
//		debugButton.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				LogFileWriter lfw = new LogFileWriter(lff);
//				String example = lfw.createHeader();
//				example += "\n"+lfw.createLine();
//				MessageDialog.openInformation(body.getShell(), "SHOW HEADER/LINE", example);
//			}
//		});
//		debugButton.setLayoutData(gd);
		
//        new Label(body, SWT.NULL);
//		gd = new GridData();
//		gd.widthHint=120;
//		gd.horizontalSpan = 2;
//		debugButton = toolkit.createButton(body, "SHOW LOGFORMAT", SWT.PUSH);
//		debugButton.addSelectionListener(new SelectionAdapter(){
//			public void widgetSelected(SelectionEvent e) {
//				MessageDialog.openInformation(body.getShell(), "LOGFORMAT DEBUG", lff.toString());
//			}
//		});
//		debugButton.setLayoutData(gd);
		
		toolkit.paintBordersFor(body);
		updatePreview();
	}

	private void updatePreview() {
		LogFileWriter lfw = new LogFileWriter(lff);
		String example = lfw.createHeader();
		example += "\n"+lfw.createLine(); //$NON-NLS-1$
		previewText.setText(example);
	}
	
    private GridData getLeftLabelGridData() {
        return new GridData(GridData.HORIZONTAL_ALIGN_END);
    }
    
    /**
     * create a new spinner widget
     * @param parent the composite to which the widgets are added
     * @param border wheather to paint a (FormToolkit) border around the spinner
     * @param selection the current selection
     * @param minimum
     * @param maximum
     * @return the created widget for further customization
     */
    private Spinner createSpinner(Composite parent, boolean border, int selection, int minimum, int maximum) {
        Spinner spinner = new Spinner(parent, SWT.NULL);
        if(border) spinner.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
        toolkit.paintBordersFor(parent);
        spinner.setMaximum(maximum);
        spinner.setMinimum(minimum);
        spinner.setSelection(selection);
        return spinner;
    }
}
