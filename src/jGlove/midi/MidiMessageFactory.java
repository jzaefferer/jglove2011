package jGlove.midi;

import jGlove.shared.MessageFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.dom4j.Element;

/**
 * An implementaion of the abstract MessageFactory, 
 * creates several <code>MidiMessageDescription</code>s 
 * from an XML file.
 */
public class MidiMessageFactory extends MessageFactory {

    private MidiMessageDescription tempMessage;
    private Descriptor tempDescriptor;
    
    public MidiMessageFactory(File file) {
        super(file);
    }

    public MidiMessageFactory(InputStream in) {
        super(in);
    }

    public MidiMessageFactory(String filename) {
        super(filename);
    }

    protected void create(Element element) {
        if (element.getName().equals("midiMessage")) { //$NON-NLS-1$
            tempMessage = new MidiMessageDescription();
            tempDescriptor = new Descriptor();
            tempMessage.setDescriptor(tempDescriptor);
            messages.add(tempMessage);
        } else if (element.getName().equals("option")) { //$NON-NLS-1$
        	ArrayList options = tempMessage.getOptions();
        	if(options == null) {
        		options = new ArrayList();
                tempMessage.setOptions(options);
        	}
        	tempDescriptor = new Descriptor();
        	options.add(tempDescriptor);
        	
        	tempDescriptor.setType(element.attributeValue("type")); //$NON-NLS-1$
        	String id = element.attributeValue("id"); //$NON-NLS-1$
        	if(id != null) {
        		tempDescriptor.setId(Integer.parseInt(id));
        	}
        }
    }

    protected void setValue(String key, String value) {
		if (key.equals("label")) { //$NON-NLS-1$
			tempDescriptor.setLabel(value);
		} else if (key.equals("description")) { //$NON-NLS-1$
			tempDescriptor.setDescription(value);
		} else if (key.equals("help")) { //$NON-NLS-1$
			tempDescriptor.setHelp(value);
		} else if (key.equals("value")) { //$NON-NLS-1$
			tempDescriptor.setValue(Integer.parseInt(value));
		}
	}
    
}
