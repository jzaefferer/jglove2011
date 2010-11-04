package jGlove.midi;

import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.JGlovePluginImages;
import jGlove.help.HelpView;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that allows configuration of a <code>MidiMessage</code>.
 */
public class MidiConfigView extends ViewPart {

	public static final String ID = "jGlove.app.views.midiConfigView"; //$NON-NLS-1$
	
	private FormToolkit toolkit;

	private ScrolledForm form;
	private Composite body;
	
	private Section typeSection;
	private Button[] typeButtons;
	
	private Section generalSection;
	private Section optionsSection;
	
	private Button invertButton;
	private Spinner channelSpinner;
	private Spinner low;
	private Spinner high;
	
	private int optionsCount;
	
	private static MidiMessageDescription[] messages;
	
	/**
	 * save wheather this MidiConfigView was already initialised
	 */
	private boolean init;
	
	private MidiMessage midiMessage;
	
	private ExpansionAdapter formReflower = new ExpansionAdapter(){
		public void expansionStateChanged(ExpansionEvent e) {
			form.reflow(true);
		}
	};
	
	private SelectionListener helpListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			HelpView hv = (HelpView)getSite().getPage().findView(HelpView.ID);
			if(hv != null) {
				Text helpText = hv.getHelpText();
				String helpString = (String)e.widget.getData("help"); //$NON-NLS-1$
				if(!helpString.equals(helpText.getText())) {
					helpText.setText(helpString);
				}
			}
		}
	};
	
	public void setFocus() {
		body.setFocus();
	}
	
	public void dispose() {
		toolkit.dispose();
	}
	
	public void createPartControl(Composite parent) {
		if(messages == null) {
			MidiMessageFactory factory = new MidiMessageFactory(JGlovePlugin
					.getFile("resource/midiMessage.xml")); //$NON-NLS-1$
			List messageList = factory.getMessages();
            messages = (MidiMessageDescription[])messageList.toArray(new MidiMessageDescription[messageList.size()]);
		}
		
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		body = form.getBody();
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		body.setLayout(layout);
		
		Action getMidiMessageAction = new Action("Debug MIDI Message", Action.AS_PUSH_BUTTON){
			public void run() {
				String message;
				if(midiMessage == null) {
					message = "MIDI Message not configured";
				} else {
					message = midiMessage.toString();
				}
				MessageDialog.openInformation(body.getShell(), "MIDI Message", message);
			}
		};
        getMidiMessageAction.setImageDescriptor(JGlovePluginImages.getImageDescriptor(IJGloveConstants.IMG_MIDI));
		getViewSite().getActionBars().getMenuManager().add(getMidiMessageAction);
		
		Action showHelpAction = new Action(Messages.MidiConfigView_showhelp_text, Action.AS_PUSH_BUTTON) {
			public void run() {
				IWorkbenchPage page = getSite().getPage();
				try {
					page.showView(HelpView.ID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		showHelpAction.setImageDescriptor(JGlovePlugin.getImageDescriptor(IJGloveConstants.IMG_HELP));
		showHelpAction.setToolTipText(Messages.MidiConfigView_showhelp_tooltip);
		
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		toolbar.add(showHelpAction);
		
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		menu.add(showHelpAction);
		
	}
	
	public void secondaryInit(String title, MidiMessage midiMessage) {
		if(init) return;
		init = true;
		this.midiMessage = midiMessage;
		setPartName(title+Messages.MidiConfigView_generic_viewtitlte);
		createControl();
		createGeneralOptions();
		if(midiMessage.getType() != -1) {
			try {
				createOptionalOptions(midiMessage.getType());
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
		body.layout();
		body.redraw();
	}
	
	/**
	 * 
	 * @return configured MidiMessage
	 */
	public MidiMessage getMidiMessage() {
//		if(selectedType == -1) {
//			return null;
//		}
		return midiMessage;
	}
	
	private void createControl() {
		typeSection = createSection(body, Messages.MidiConfigView_typesection_text, Messages.MidiConfigView_typesection_description);
		
		Composite client = toolkit.createComposite(typeSection);
		client.setLayout(new GridLayout());
		
		SelectionAdapter typeListener = new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.getSource();
				if(b.getSelection()) {
					int index = ((Integer)b.getData()).intValue();
					if(midiMessage.getType() != index) {
						midiMessage.setType(index);
						midiMessage.setOptions(null);
						if(optionsSection != null) {
							optionsSection.dispose();
						}
						if(index != -1) {
							Descriptor messageDescriptor = messages[index].getDescriptor();
							typeSection.setText(messageDescriptor.getLabel());
							typeSection.setDescription(messageDescriptor.getDescription());
							try {
								createOptionalOptions(index);
							} catch(Exception exception) {
								exception.printStackTrace();
							}
						}
						form.reflow(true);
					}
					
				}
			}
		};
		
		noneButton = toolkit.createButton(client, Messages.MidiConfigView_type_none, SWT.RADIO);
		noneButton.setData(new Integer(-1));
		noneButton.addSelectionListener(typeListener);
		
		typeButtons = new Button[messages.length];
		for(int i=0; i<messages.length; i++) {
			typeButtons[i] = toolkit.createButton(client, messages[i].getDescriptor().getLabel(), SWT.RADIO);
			typeButtons[i].addSelectionListener(typeListener);
			typeButtons[i].setToolTipText(messages[i].getDescriptor().getDescription());
			typeButtons[i].setData(new Integer(i));
			typeButtons[i].setData("help", messages[i].getDescriptor().getHelp()); //$NON-NLS-1$
			typeButtons[i].addSelectionListener(helpListener);
		}	
		typeSection.setClient(client);
		
		int type = midiMessage.getType();
		if(type == -1) {
			noneButton.setSelection(true);
		} else {
			typeButtons[type].setSelection(true);
			Descriptor messageDescriptor = messages[type].getDescriptor();
			typeSection.setText(messageDescriptor.getLabel());
			typeSection.setDescription(messageDescriptor.getDescription());
		}
		
		toolkit.paintBordersFor(body);
		
	}
	
	private Button noneButton;
	
	private void createGeneralOptions() {
		generalSection = createSection(body, Messages.MidiConfigView_generalsection_text, Messages.MidiConfigView_generalsection_description);
		
		Composite client = toolkit.createComposite(generalSection);
		client.setLayout(new GridLayout(2, false));
		generalSection.setClient(client);
		
		toolkit.createLabel(client, ""); //$NON-NLS-1$
		invertButton = toolkit.createButton(client, Messages.MidiConfigView_invertbutton, SWT.CHECK);
		invertButton.setSelection(midiMessage.getInvert());
		invertButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				midiMessage.setInvert(invertButton.getSelection());
			}
		});
		invertButton.setData("help", Messages.MidiConfigView_invert_help); //$NON-NLS-1$
		invertButton.addSelectionListener(helpListener);
		
		channelSpinner = createSpinner(client, Messages.MidiConfigView_channel, Messages.MidiConfigView_channel_tooltip, true, 1, 0, 15);
		channelSpinner.setSelection(midiMessage.getChannel());
		channelSpinner.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				midiMessage.setChannel(channelSpinner.getSelection());
			}
		});
		channelSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				midiMessage.setChannel(channelSpinner.getSelection());
			}
		});
		channelSpinner.setData("help", Messages.MidiConfigView_channel_help1 + //$NON-NLS-1$
				Messages.MidiConfigView_channel_help2);
		channelSpinner.addSelectionListener(helpListener);
	}
	
	private void checkSelection(Widget source, int id, String type) {
		if(type.equals("range")) { //$NON-NLS-1$
			Spinner spinner = (Spinner)source;
			int selection = spinner.getSelection();
			switch(id) {
			case -1:
				midiMessage.setLow(selection);
				if(high != null && !high.isDisposed()) {
					high.setMinimum(selection+1);
				}
				break;
			case -2:
				midiMessage.setHigh(selection);
				if(low != null && !low.isDisposed()) {
					low.setMaximum(selection-1);
				}
				break;
			default:
				if(id < 255) {
					getOptions()[id] = selection;
				}
			}
		} else if(type.equals("toggle") || type.equals("radio")) { //$NON-NLS-1$ //$NON-NLS-2$
			Button button = (Button)source;
			getOptions()[id] = (button.getSelection())? 1 : 0;
		} else if(type.equals("note")) { //$NON-NLS-1$
			NoteButton button = (NoteButton)source;
			getOptions()[id] = button.getSelection();
		}
	}
	
	private void createOptionalOptions(int index) {
		optionsSection = createSection(body, messages[index].getDescriptor().getLabel() + Messages.MidiConfigView_optionssection_text, Messages.MidiConfigView_optionssection_description);

		Composite client = toolkit.createComposite(optionsSection);
		client.setLayout(new GridLayout(2, false));
		optionsSection.setClient(client);
		
		SelectionListener listener = new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Widget source = (Widget)e.getSource();
				int id = ((Integer)source.getData("id")).intValue(); //$NON-NLS-1$
				String type = (String)source.getData("type"); //$NON-NLS-1$
				checkSelection(source, id, type);
			}
		};
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Widget source = (Widget)e.getSource();
				int id = ((Integer)source.getData("id")).intValue(); //$NON-NLS-1$
				String type = (String)source.getData("type"); //$NON-NLS-1$
				checkSelection(source, id, type);
			}
		};
		
		optionsCount = 0;
		Iterator options = messages[index].getOptions().iterator();
		while(options.hasNext()) {
			Descriptor option = (Descriptor)options.next();
			if(option.getId() >= 0 && option.getId() < 255) {
				optionsCount++;
			}
		}
		options = messages[index].getOptions().iterator();
		while(options.hasNext()) {
			Descriptor option = (Descriptor)options.next();
			int id = option.getId();
			Widget widget = null;
			
			if(option.getType().equals("range")) { //$NON-NLS-1$
				int value = -1;
				switch(id) {
				case -1:
					value = midiMessage.getLow();
					break;
				case -2:
					value = midiMessage.getHigh();
					break;
				default:
					if(id < 255) {
						int[] midiOptions = midiMessage.getOptions();
						if(midiOptions != null && midiOptions[id] != -1) {
							value = midiOptions[id];
						}
					}
				}
				if(value == -1) {
					value = option.getValue();
				}
				Spinner spinner = createSpinner(client, option.getLabel(), option.getDescription(), true, value, 0, 127);
				spinner.addSelectionListener(listener);
				spinner.addModifyListener(modifyListener);
				if(id == -1) low = spinner;
				if(id == -2) high = spinner;
				spinner.addSelectionListener(helpListener);
				widget = spinner;
			} else if(option.getType().equals("toggle") || option.getType().equals("radio")) { //$NON-NLS-1$ //$NON-NLS-2$
				toolkit.createLabel(client, ""); //$NON-NLS-1$
				Button button = toolkit.createButton(client, option.getLabel(), option.getType().equals("toggle") ? SWT.CHECK : SWT.RADIO); //$NON-NLS-1$
				int[] midiOptions = midiMessage.getOptions();
				if(midiOptions != null && midiOptions[id] != -1) {
					button.setSelection((midiOptions[id] == 1)? true : false);
				} else {
					button.setSelection(option.getValue() == 1);
				}
				button.addSelectionListener(listener);
				button.addSelectionListener(helpListener);
				widget = button;				

			} else if(option.getType().equals("note")) { //$NON-NLS-1$
				toolkit.createLabel(client, Messages.MidiConfigView_octave);
				NoteButton button = new NoteButton(client, SWT.NULL, toolkit);
				toolkit.createLabel(client, Messages.MidiConfigView_note);
				int[] midiOptions = midiMessage.getOptions();
				if(midiOptions != null && midiOptions[id] != -1) {
					button.setSelection(midiOptions[id]);
				} else {
					button.setSelection(option.getValue());
				}
				button.addSelectionListener(listener);
				button.addSelectionListener(helpListener);
				widget = button;
			}
			
			widget.setData("help", option.getHelp()); //$NON-NLS-1$
			
			widget.setData("id", new Integer(id)); //$NON-NLS-1$
			widget.setData("type", option.getType()); //$NON-NLS-1$
			checkSelection(widget, id, option.getType());
		}
		toolkit.paintBordersFor(client);
	}
	
	private int[] getOptions() {
		int[] options = midiMessage.getOptions();
		if(options == null) {
			options = new int[optionsCount];
			for(int i=0; i<options.length; i++) {
				options[i] = -1;
			}
			midiMessage.setOptions(options);
		}
		return options;
	}
	
	/**
	 * 
	 * @param parent
	 * @param title
	 * @param description
	 * @return the configured section
	 */
	private Section createSection(Composite parent, String title, String description) {
		Section s = toolkit.createSection(parent, Section.TWISTIE | Section.DESCRIPTION | Section.EXPANDED);
		TableWrapData td = new TableWrapData();
		td.grabHorizontal = true;
		s.setLayoutData(td);
		s.setText(title);
		toolkit.createCompositeSeparator(s);
		s.setDescription(description);
		s.addExpansionListener(formReflower);
		return s;
	}
	
	/**
	 * create a new spinner widget
	 * @param parent the composite to which the widgets are added
	 * @param text if not null, this text will be put in fron of the spinner
	 * @param tooltip
	 * @param border wheather to paint a (FormToolkit) border around the spinner
	 * @param selection the current selection
	 * @param minimum
	 * @param maximum
	 * @return the created widget for further customization
	 */
	private Spinner createSpinner(Composite parent, String text, String tooltip, boolean border, int selection, int minimum, int maximum) {
		if(text != null) {
			Label l = toolkit.createLabel(parent, text);
			l.setToolTipText(tooltip);
		}
		Spinner spinner = new Spinner(parent, SWT.NULL);
		if(border) spinner.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		toolkit.paintBordersFor(parent);
		spinner.setToolTipText(tooltip);
		spinner.setMaximum(maximum);
		spinner.setMinimum(minimum);
		spinner.setSelection(selection);
		return spinner;
	}

}
