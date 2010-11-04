package jGlove.mouse;

/**
 * An event to inform listeners about a changed value of 
 * a certain type (eg. X-Axis now at 56).
 */
public class MouseSensorEvent {

    public static final int
    	EVENT_X = 0,
        EVENT_Y = 1,
    	EVENT_BUTTON1 = 2,
        EVENT_BUTTON2 = 3,
        EVENT_BUTTON3 = 4,
        EVENT_BUTTON4 = 5,
        EVENT_BUTTON5 = 6;
    
    public static final int[] EVENTS = {
    	EVENT_X,
    	EVENT_Y,
    	EVENT_BUTTON1,
    	EVENT_BUTTON2,
    	EVENT_BUTTON3,
        EVENT_BUTTON4,
        EVENT_BUTTON5,
    };
    
    private int type;
    private int value;
    
    public MouseSensorEvent(int type, int value) {
        this.type = type;
        this.value = value;
    }
    
    public int getType() {
        return type;
    }
    public int getValue() {
        return value;
    }
    
}
