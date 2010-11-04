package jGlove;

import jGlove.shared.IPropertyChangeListener;
import jGlove.shared.ISource;
import jGlove.shared.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * An abstract <code>Composite</code> that can display an
 * <code>ISource</code> object.
 */
public abstract class Form extends Composite implements IPropertyChangeListener {

	protected Button activeButton;
	protected Label nameLabel;
	protected Label typeLabel;
	protected Label valueLabel;
	protected ProgressBar valueProgressBar;
	protected Button calibrateButton;
	
	protected Color activeBackgroundColor;
	protected Color nonactiveBackgroundColor;
	
	protected ISource source;
	
	protected FormToolkit toolkit;
	
	protected GridLayout g;
	
	public Form(Composite parent, ISource source, FormToolkit toolkit) {
		super(parent, SWT.NULL);
		this.toolkit = toolkit;
		this.source = source;
		
        source.getPropertyChangeSupport().addPropertyChangeListener(this);
		
		toolkit.adapt(this);
		setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		
		ColumnLayoutData cd = new ColumnLayoutData();
		cd.horizontalAlignment = ColumnLayoutData.CENTER;
		setLayoutData(cd);
	}
	
	protected void createControl(int rows) {
		g = new GridLayout();
		g.numColumns = rows;
		setLayout(g);
		
		activeButton = toolkit.createButton(this, Messages.Form_activebutton_text, SWT.CHECK);
		activeButton.setToolTipText(Messages.Form_activebutton_tooltip);
		activeButton.setSelection(source.getActive());
		activeButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				boolean active = activeButton.getSelection();
				setActive(active);
				Core.getDefault().getPropertyChangeSupport().firePropertyChange("active", null); //$NON-NLS-1$
			}
		});
		
		typeLabel = toolkit.createLabel(this, ""); //$NON-NLS-1$
		typeLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		typeLabel.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SOURCE_TYPES[source.getType()]));
		
		nameLabel = toolkit.createLabel(this, source.getName(), SWT.NULL);
		nameLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		
		valueLabel = toolkit.createLabel(this, String.valueOf(source.getValue()), SWT.CENTER);
		valueLabel.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		
		valueProgressBar = new ProgressBar(this, SWT.SMOOTH);
		valueProgressBar.setSelection(source.getValue());
		
		calibrateButton = toolkit.createButton(this, Messages.Form_calibratebutton_text, SWT.PUSH);
		calibrateButton.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SETUP));

		activeBackgroundColor = valueProgressBar.getBackground();
		nonactiveBackgroundColor = toolkit.getColors().getBackground();
		
	}
	
	protected void changeColors(boolean active) {
		if(active) {
			setBackground(activeBackgroundColor);
			activeButton.setBackground(activeBackgroundColor);
		} else {
			setBackground(nonactiveBackgroundColor);
			activeButton.setBackground(nonactiveBackgroundColor);
		}
	}
	
	public void setActive(boolean active) {
		changeColors(active);
		activeButton.setSelection(active);
		source.setActive(active);
	}
	
	public boolean getActive() {
		return source.getActive();
	}
	
	public void setName(String name) {
		nameLabel.setText(name);
		source.setName(name);
	}
	
	public void setType(int type) {
		typeLabel.setImage(JGlovePlugin.getImage(IJGloveConstants.IMG_SOURCE_TYPES[source.getType()]));
		source.setType(type);
	}
	
	public String getSourceName() {
		return source.getName();
	}
	
	public int getType() {
		return source.getType();
	}
	
	public void dispose() {
		getSource().getPropertyChangeSupport().removePropertyChangeListener(this);
		super.dispose();
	}
	
	public ISource getSource() {
		return source;
	}
}
