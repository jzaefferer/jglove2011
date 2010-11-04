package jGlove.filter;

import java.io.Serializable;

/**
 * An <code>OptionList</code> contains an array of <code>Option</code>s
 * and provides a toString() implementation that is useful when used
 * in a Viewer.
 */
public class OptionList implements Serializable {

    private static final long serialVersionUID = 1209252037613630969L;
    
    private Option[] options;
    
    public Option[] getOptions() {
        return options;
    }
    public void setOptions(Option[] options) {
        this.options = options;
    }
    
    public OptionList(Option[] options) {
        this.options = options;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<options.length; i++) {
            sb.append(options[i]);
            sb.append("; "); //$NON-NLS-1$
        }
        sb.deleteCharAt(sb.length()-2);
        return sb.toString();
    }
}
