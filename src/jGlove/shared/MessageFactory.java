package jGlove.shared;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * A messagefactory can be used to read objects from an XML file
 * by extending and implementing create() and setValue().
 * @see jGlove.midi.MidiMessageFactory
 */
public abstract class MessageFactory {

    private String filename;
    private File file;
    private InputStream in;

    protected List messages;

    private SAXReader reader = new SAXReader();

    public MessageFactory(File file) {
        this.file = file;
    }
    
    public MessageFactory(String filename) {
        this.filename = filename;
    }
    
    public MessageFactory(InputStream in) {
        this.in = in;
    }
    
    private Document parse() throws DocumentException {
        if(file != null) {
        	return reader.read(file);
        }
    	if(filename != null) {
        	return reader.read(filename);
        } 
    	if(in != null) {
        	return reader.read(in);
        }
    	return null;
    }

    /**
     * Is called when an element with mixed content
     * is found.
     * @param element the element with mixed content
     */
    protected abstract void create(Element element);

    /**
     * Is called when an element with only text content
     * is found.
     * @param key the name of the element
     * @param value the text value of the element
     */
    protected abstract void setValue(String key, String value);
    
    private void parseTree(Element element) {
		if (element == null) {
			return;
		}

		Iterator childs = element.elementIterator();
		while (childs.hasNext()) {
			Element child = (Element) childs.next();
			if (child.hasMixedContent()) {
				create(child);
			} else {
				setValue(child.getName(), child
						.getTextTrim());
			}

			parseTree(child);
		}
	}

    /**
	 * 
	 * @return null on error
	 */
    public List getMessages() {
        if (messages == null) {
            try {
                messages = new ArrayList(3);
                
                Document doc = parse();
                Element root = doc.getRootElement();
                
                parseTree(root);
            } catch (DocumentException e) {
            	e.printStackTrace();
            }
        }
        return messages;
    }

}
