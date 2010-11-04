package jGlove.mouse;

import jGlove.Core;
import jGlove.IJGloveConstants;
import jGlove.JGlovePlugin;
import jGlove.shared.SwitchAction;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * A view that provides the mouse as an input device.
 */
public class MouseView extends ViewPart {

	public final static String ID = "jGlove.app.views.mouseView"; //$NON-NLS-1$
	
    private Composite parent;
    private Canvas container;

    private Color white;
    private Color black;
    
    private Image image;

    private int
        min,
        max,
        border,
        width,
        height,
        x,
        y;
    
    private boolean[] mouseDown = new boolean[5];
    
    private ListenerList mouseSensorListenerList = new ListenerList();
    
    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
        public void mouseMove(MouseEvent e) {
            int x = e.x * (max+border) / width - border/2;
            int y = e.y * (max+border) / height- border/2;
            setX(Math.max(min, Math.min(max, x)));
            setY(Math.max(min, Math.min(max, y)));
        }
    };
	
	private MouseListener mouseListener = new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
			setMouseDown(false, e.button);
		}
	
		public void mouseDown(MouseEvent e) {
			setMouseDown(true, e.button);
		}
	};
	
	private SwitchAction addMouseSensorsAction;
    
    public MouseView() {
        this(0, 127, 10);
    }
    
    public MouseView(int min, int max, int border) {
        this.min = min;
        this.max = max;
        this.border = border;
    }
    
    public void createPartControl(Composite parent) {
        this.parent = parent;
        container = new Canvas(parent, SWT.NO_BACKGROUND);
        container.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_CROSS));
        white = new Color(parent.getDisplay(), 255, 255, 255);
        black = new Color(parent.getDisplay(), 0, 0, 0);
        addListeners();
        addToolbarActions();
    }
    
    private void addToolbarActions() {
		addMouseSensorsAction = new SwitchAction("", Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void switchTooltip() {
				if(state) {
					setToolTipText(Messages.MouseView_remove_tooltip);
				} else {
					setToolTipText(Messages.MouseView_add_tooltip);
				}
			}
			public void run() {
				try {
					if (!state) {
                        MouseSensor[] sensors = Core.getDefault().getMouseSensors();
                        if(sensors == null) {
                            sensors = new MouseSensor[MouseSensorEvent.EVENTS.length];
                            for (int i = 0; i < MouseSensorEvent.EVENTS.length; i++) {
                                sensors[i] = new MouseSensor(
                                        MouseSensorEvent.EVENTS[i]);
                            }
                            Core.getDefault().setMouseSensors(sensors);
                        }
                        for (int i = 0; i < sensors.length; i++) {
                            addMouseSensorListener(sensors[i]);
                        }
						Core.getDefault().addActives(sensors);
					} else {
                        MouseSensor[] sensors = Core.getDefault().getMouseSensors();
						for (int i = 0; i < sensors.length; i++) {
							removeMouseSensorListener(sensors[i]);
						}
						Core.getDefault().removeActives(sensors);
					}
					state = !state;
					switchTooltip();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		addMouseSensorsAction.setImageDescriptor(JGlovePlugin.
				getImageDescriptor(IJGloveConstants.IMG_MOUSE));
		
		IToolBarManager toolbar = getViewSite().getActionBars().
				getToolBarManager();
		toolbar.add(addMouseSensorsAction);
	}

	private void addListeners() {
        container.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (image == null) {
                    paintBackgroundImage();
                }
                e.gc.drawImage(image, 0, 0);
            }
        });
        parent.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                width = Math.max(1,parent.getClientArea().width);
                height = Math.max(1, height = parent.getClientArea().height);
                container.setSize(width, height);
                paintBackgroundImage();
            }
        });
        container.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                white.dispose();
                black.dispose();
                image.dispose();
            }
        });
        container.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {
				if(addMouseSensorsAction.getState())
					removeSensorListeners();
			}
			public void mouseEnter(MouseEvent e) {
				if(addMouseSensorsAction.getState())  
					addSensorListeners();
			}
		});
    }
	
	private void addSensorListeners() {
		container.addMouseMoveListener(mouseMoveListener);
        container.addMouseListener(mouseListener);
	}
	
	private void removeSensorListeners() {
		container.removeMouseMoveListener(mouseMoveListener);
        container.removeMouseListener(mouseListener);
	}
	
	private void paintBackgroundImage() {
        if (image != null) {
            image.dispose();
        }
        image = new Image(parent.getDisplay(), container.getClientArea());
        GC gc = new GC(image);
        
        // fill background with white
        gc.setBackground(white);
        gc.drawRectangle(container.getClientArea());
        gc.setForeground(black);
        gc.drawText(Messages.MouseView_advice, border*2, border*2, SWT.DRAW_DELIMITER);
        gc.setBackground(black);
        
        // draw markers
        gc.drawLine(border, border, border*2, border); // top left horizontal
        gc.drawLine(border, border, border, border*2); // top left vertical
        
        gc.drawLine(width-border, border, width-border*2, border); // top right horizontal
        gc.drawLine(width-border, border, width-border, border*2); // top right vertical
        
        gc.drawLine(border, height-border, border*2, height-border); // bottom left horizontal
        gc.drawLine(border, height-border, border, height-border*2); // bottom left vertical
        
        gc.drawLine(width-border, height-border, width-border*2, height-border); // bottom right horizontal
        gc.drawLine(width-border, height-border, width-border, height-border*2); // bottom right vertical
        
        gc.dispose();
    }

    public void setFocus() {
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    
    private void setX(int x) {
        if(this.x != x) {
            MouseSensorEvent event = new MouseSensorEvent(MouseSensorEvent.EVENT_X, x);
            fireEvent(event);
            this.x = x;
        }
    }
    
    private void setY(int y) {
        if(this.y != y) {
            MouseSensorEvent event = new MouseSensorEvent(MouseSensorEvent.EVENT_Y, y);
            fireEvent(event);
            this.y = y;
        }
    }
    
    private void setMouseDown(boolean mouseDown, int button) {
		if(this.mouseDown[button-1] != mouseDown) {
			this.mouseDown[button-1] = mouseDown;
			int type = 0;
			switch(button) {
			case 1:
				type = MouseSensorEvent.EVENT_BUTTON1;
				break;
			case 2:
				type = MouseSensorEvent.EVENT_BUTTON2;
				break;
			case 3:
				type = MouseSensorEvent.EVENT_BUTTON3;
				break;
			case 4:
			    type = MouseSensorEvent.EVENT_BUTTON4;
			    break;
			case 5:
			    type = MouseSensorEvent.EVENT_BUTTON5;
			    break;
			}
			fireEvent(new MouseSensorEvent(type, (mouseDown) ? 127 : 0));
		}
	}
    
    private void fireEvent(MouseSensorEvent event) {
        Object[] listeners = mouseSensorListenerList.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((MouseSensorListener) listeners[i]).mouseSensorChanged(event);
        }
    }
    
    public void addMouseSensorListener(MouseSensorListener listener) {
        mouseSensorListenerList.add(listener);
    }
    public void removeMouseSensorListener(MouseSensorListener listener) {
        mouseSensorListenerList.remove(listener);
    }
}
