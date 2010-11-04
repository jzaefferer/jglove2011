package jGlove.monitor;

import jGlove.JGlovePlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A <code>Graph</code> extends <code>Canvas</code> and can be used
 * inside any <code>Composite</code> to show a diagram
 * of one or more signals.
 * <p>
 * By retrieving the size of the parent Composite,
 * the Diagram is independent in size and will scale
 * to every possible width and height.
 * <p>
 * Performance issues increase exponential with size,
 * therefore it may be a good idea to check for more
 * optimations.
 */
public class Graph extends Canvas {

	private Display display;

	private Composite parent;
	
	// colors for the graph-lines
	private Color rgbUltraMarineColor = new Color(display, 90, 0, 255);
	private Color rgbRedColor = new Color(display, 255, 21, 0);
	private Color rgbFieryOrangeColor = new Color(display, 238, 119, 13);
	private Color rgbGreenColor = new Color(display, 0, 255, 100);
	private Color rgbWineredColor = new Color(display, 222, 151, 151);
	private Color rgbGreenyellowColor = new Color(display, 129, 194, 44);
	private Color rgbOrangeColor = new Color(display, 255, 168, 0);
	private Color rgbImperialPurpleColor = new Color(display, 127, 7, 166);
	private Color rgbYellowColor = new Color(display, 255, 255, 0);
	private Color rgbMintColor = new Color(display, 0, 246, 197);
	private Color rgbDarkGreenColor = new Color(display, 44, 122, 6);
	private Color rgbCitrusColor = new Color(display, 210, 255, 0);

	private Color[] allColors = {
		rgbUltraMarineColor,
		rgbRedColor,
		rgbFieryOrangeColor,
		rgbGreenColor,
		rgbWineredColor,
		rgbGreenyellowColor,
		rgbOrangeColor,
		rgbImperialPurpleColor,
		rgbYellowColor,
		rgbMintColor,
		rgbDarkGreenColor,
		rgbCitrusColor
	};
	
	// colors for grid and dimension indicator
	private Color grayColor = new Color(display,173, 167, 167);
	private Color blackColor = new Color(display,0,0,0);
	private Color darkGrayColor = new Color(display,157,136,136);

	// default-text settings if no device exists
	private String[] text = {
			Messages.Graph_error_nothingtomonitor
	};
	
	private int antialias = 1;
	
//	private int alpha = 255;
		
	private int border = 10;

	private int clientWidth = 10;

	private int clientHeight = 100;
	
	/**
	 * clientWidth / length
	 */
	private int spacer;

	/**
	 * max. displayed graphs and values per graph
	 */
	private int[][] shownvalues;

	/**
	 * length of the text-bounding rectangle
	 */
	private int textBoxLength = 100;

	/**
	 * strength of the graph-lines
	 */
	private int lineWidth = 1;

	private Image image;

	/**
	 * the grid displayed in the background
	 */
	private ImageData gridData;
	
	/**
	 * number of values to be displayed (increase: slower...)
	 */
	private int length;
	
	/**
	 * helping variable for avoiding nullpointerexceptions by using the color-array
	 */
	private int colorCount;

	private int width;
	private int height;
	
	/**
	 * Graph-Constructor, if a source-variable is given.
	 * Loads "Default"-Constructor for setting parent and length
	 * @param parent
	 * @param length
	 */
	public Graph(Composite parent, int length, String[] text) {
		this(parent,length);
//		if( source.length != 0) {	
//			
//		}
		setText(text);
	}
	
	public void setText(String[] text) {
		this.text = text;
		gridData = getGridImageData();
	}
	
	/**
	 * "Default"-Constructor, if there is no connected device
	 * (e.g. testing with any given SourceComponent[])
	 * @param parent
	 * @param length
	 */
	public Graph(Composite parent, int length) {
		super(parent, SWT.NO_BACKGROUND);
		display = parent.getDisplay();
		this.length = length;
		this.parent = parent;

		createListeners();
		
	}
	
	private void createListeners() {
		addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				if(width<=0 || height <=0) {
					return;
				}
				if(image != null && !image.isDisposed()) {
					try {
						e.gc.drawImage(image, 0,0);
					} catch(NullPointerException ex) {
						// TODO when could that occur? should check for null instead
						ex.printStackTrace();
						JGlovePlugin.log(ex);
					}
				}	
			}
		});
		parent.addControlListener(new ControlAdapter(){
			public void controlResized(ControlEvent e) {
				setAreaSize();
				calculateSpacer();
				if (width > 0 && height > 0) {
					gridData = getGridImageData();
				}
			}
		});
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				grayColor.dispose();
				darkGrayColor.dispose();
				blackColor.dispose();
				for(int i=0; i<allColors.length; i++) {
					allColors[i].dispose();
				}
			}

		});
	}
	
	/**
	 * Returns the numbers of values to be displayed
	 * @return int as number of values to be displayed
	 */
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
		calculateSpacer();
	}
	
	/**
	 * scales the thickness for each line
	 */
	public void setLineWidth() {
		// TODO review/rewrite
		lineWidth = 1;
		
		if (height < 280 && width < 250)
			lineWidth = 1;
		else {
			if (height < 500) {
				if ( height < 220||(width < 220 && height < 350)|| text.length > 10) 
					lineWidth = 1;
				else
					lineWidth = 2;
			}
			else
				if ( width < 400 )
					lineWidth = 2;
				else {
					if (width < 660 || text.length>10)
						lineWidth = 3;
					else
						lineWidth = 4;
				}
		}
		
		// was:
		/*
		if(getLength()<40) {
			if ((height < 400 || width < 400)&&(height > Math.round(width/2) || width > Math.round(height/2))) { 
				lineWidth = 1;
		    }else if((((height > 400 && height < 600)||(width > 400 && width < 600)))&&(height > Math.round(width/2) || width > Math.round(height/2))) {
				lineWidth = 2;
			} else if(((height > 600 && height < 800)||(width > 600 && width < 800))&&(height > Math.round(width/2) || width > Math.round(height/2))) {
				lineWidth = 3;
			} else if((height > 800 || width > 800)&&(height > Math.round(width/2) || width > Math.round(height/2))) {
				lineWidth = 4;
			}
		} 
		*/
	}
	
	
	/**
	 * sets the sizes of the visible and drawable field
	 */
	public void setAreaSize() {
		Rectangle parentSize = parent.getClientArea();
		width = parentSize.width;
		height = parentSize.height;
		setSize(parent.getSize());

		clientHeight = height - 2*border;
		clientWidth = width - 2*border;
		
		setLineWidth();
	}
	
	public void updatePaint(boolean flush) {
		if(flush) {
			gridData = getGridImageData();
		}
		updatePaint();
	}
	
	public void updatePaint() {
		if(gridData == null) {
			gridData = getGridImageData();
		}
		if(image != null && !image.isDisposed()) {
			image.dispose();
			image = null;
		}
		if(gridData == null) {
			return;
		}
		image = new Image(display, gridData);
		GC gc = new GC(image);
		try {		
			gc.setAntialias(antialias);
		} catch(SWTException swte) {
			/*
			 * TODO Handle SWTException
             * on Windows 2000, no GDI+ is avaible, resulting
             * in a SWTException when trying to setAnitlias()
             * 
             * by catching the exception and doing nothing, 
             * it is possible to use antialias on systems 
             * that support it
             * 
             * a better approach would be to test if antialias
             * is avaible, cache the result and never try again
             * if it failed...
             * 
             * another possible approach: report that "bug" :-)
			 */
		}
		
		colorCount = 0; // sets the counter for the available colors
		for (int piece = 0; piece < shownvalues.length; piece++) {
			
			Color color = getColor();
			
			int xpos1 = border;
			int ypos1 = calculateHeight(shownvalues[piece][0]) + border;
			
			for (int i = 1; i <= shownvalues[piece].length-1; i++) {
				gc.setForeground(color);
				int xpos2 = xpos1 + spacer;
				int ypos2 = calculateHeight(shownvalues[piece][i]) + border ;
				
				
				gc.setLineWidth(lineWidth);
				
				if ( (clientWidth+border) >= (xpos2) ) {
					gc.drawLine(xpos1, ypos1, xpos2, ypos2);
				} else {

					double heightDifference = ypos2 - ypos1; // Height-difference between ypos1 and ypos2

					/*
					 * Gap between the last visible vertical grid-line and the boundary of the drawable field.
					 * This value is divided by the value of the space between two seperate vertical lines.
					 */
					double visibleField = Math.abs((xpos1-(clientWidth+border))/(double)spacer);
					
					/*
					 * With the coefficient "a6" the height-difference "a4" will be multiplied for
					 * calculating the effectively still visible height of the line.
					 */
					double visibleHeight = heightDifference*visibleField;

					/* 
					 * Drawing of the "last" line with value-revised height.
					 */
					gc.drawLine(xpos1, ypos1, clientWidth+border, (int)Math.round(ypos1+visibleHeight));
					
					break;
				}
				ypos1 = ypos2;
				xpos1 = xpos2;
			}
		}
		// was:
		/*
		try {
			for (int piece = 0; piece < shownvalues.length; piece++) {

				Color color = getColor();

				int xpos1 = border;
				int ypos1 = calculateHeight(shownvalues[piece][0]) + border;

				for (int i = 1; i <= shownvalues[piece].length - 1; i++) {
					gc.setForeground(color);
					int xpos2 = xpos1 + spacer;
					int ypos2 = calculateHeight(shownvalues[piece][i]) + border;

					gc.setLineWidth(lineWidth);

					if ((clientWidth + border) >= (xpos2)) {
						gc.drawLine(xpos1, ypos1, xpos2, ypos2);
					} else {

						double heightDifference = ypos2 - ypos1; // Height-difference between ypos1 and ypos2

						// Gap between the last visible vertical grid-line and the boundary of the drawable field.
						// This value is divided by the value of the space between two seperate vertical lines.
						double visibleField = Math
								.abs((xpos1 - (clientWidth + border))
										/ (double) spacer);

						// With the coefficient "a6" the height-difference "a4" will be multiplied for
						// calculating the effectively still visible height of the line.
						double visibleHeight = heightDifference * visibleField;

						// Drawing of the "last" line with value-revised height.
						gc.drawLine(xpos1, ypos1, clientWidth + border,
								(int) Math.round(ypos1 + visibleHeight));

						break;
					}
					ypos1 = ypos2;
					xpos1 = xpos2;
				}
			}
		} catch (NullPointerException ex) {
//			ex.printStackTrace();
			JGlovePlugin.log(ex);
		}
		*/
		gc.dispose();
	}
	
	/**
	 * sets the given values to be displayed
	 * @param shownvalues
	 */
	public void update(int[][] shownvalues) {
		this.shownvalues = shownvalues;
		
		if (display != null && !display.isDisposed()) {
			try {
				updatePaint();
			} catch(IllegalArgumentException e) {
				// TODO catch that earlier?
				JGlovePlugin.log(e);
			} catch(NullPointerException e) {
				// TODO when does that get thrown? must null check somewhere instead
				JGlovePlugin.log(e);
			} catch(SWTException e) {
				// TODO where is that coming from?
				JGlovePlugin.log(e);
			}
			if (display != null && !display.isDisposed()) {
				try {
                    display.syncExec(new Runnable() {
                        public void run() {
                            if (!isDisposed()) {
                                redraw();
                            }
                        }

                    });
                } catch (NullPointerException e) {
                    /*
                     * TODO: handle exception
                     * see above, display is null when the
                     * program is exited, resulting in a
                     * NullPointerException
                     */ 
                }
			}
		}
	}

	/**
	 * @param ypos
	 * @return current height-position
	 */
	
	/**
	 * calculates the displayable worth by referencing the current visible height and
	 * checks if the height-value is not higher than the max. displayable value
	 * 
	 * @param value
	 * @return the calculated position (approx.)
	 */
	private int calculateHeight(int value) {
		value = Math.min(127, value);
		value = Math.max(0, value);
		double x = value*clientHeight/127.0;
		return clientHeight-(int)(Math.round(x));
	}
	
	/**
	 * calculates a spacer (space between two displayed grid-lines)
	 */
	private void calculateSpacer() {
		spacer = 1+( (int)Math.floor(clientWidth/(double)(length-1)));
	}
	
	/**
	 * prevends array-overflow if there�ll be more objects than colors
	 * @return current Color object
	 */
	private Color getColor() {
		if(colorCount == allColors.length) {
			colorCount = 0;
		}
		return allColors[colorCount++];
	}
	
	/**
	 * checks the text-array for the longest String
	 * @param gc
	 * @return represents the longest String
	 */
	private int calculateTextSpace(GC gc) {
		Point point1 = gc.stringExtent(text[0]);
		for (int piece = 0; piece < text.length - 1; piece++) {
			Point point2 = gc.stringExtent(text[piece + 1]);
			if (point2.x > point1.x) {
				point1.x = point2.x;
			}
		}
		return point1.x;
	}
	
	/**
	 * draws a fine`n�smart shadow-effect for the legend/caption
	 * @param gc
	 */
	private void paintFXShadows(GC gc) {

		gc.setBackground(darkGrayColor);
		
		// Box-shadowing
		try {
			gc.setAlpha(130);
		} catch(SWTException swte) {
			/**
			 *	TODO Handle SWTException
			 */
		}
		gc.fillRectangle(clientWidth-textBoxLength-13,border+6,textBoxLength+11,text.length*14);
		try {	
			gc.setTextAntialias(antialias);
		} catch(SWTException swte) {
			/**
			 * TODO Handle SWTException
			 */
		}
		
		gc.setForeground(darkGrayColor);
		for(int piece = 0; piece < text.length; piece++) {
			// Line-shadowing
			gc.drawLine(clientWidth-textBoxLength-13, (border+4)*(piece+1)+gc.stringExtent(text[piece]).y+2, clientWidth-3, (border+4)*(piece+1)+gc.stringExtent(text[piece]).y+2);
			
			// Text-shadowing
			int nubsi = textBoxLength - Math.round(gc.stringExtent(text[piece]).x);
			gc.drawText(text[piece],clientWidth-textBoxLength+(nubsi/2)-8,(int)((border+4)*(piece+1))+2,true);
		}
		
		// Box-shadowing
		gc.drawRectangle(clientWidth-textBoxLength-13,border+6,textBoxLength+10, (border+4)*(text.length-1)+gc.stringExtent(text[0]).y);
	
	}

	private void paintLegend(GC gc) {
		textBoxLength = calculateTextSpace(gc);
		
		try {
			gc.setAntialias(antialias);
			gc.setTextAntialias(antialias);
		} catch(SWTException swte) {
			/**
			 * TODO Handle SWTException
			 */
		}
		
		paintFXShadows(gc);
		
		colorCount = 0;
		
		// text width matching line-color as background to be displayed (for all given signals)
		for (int piece = 0; piece < text.length; piece++) {
			
			Color color = getColor();

			try {
				gc.setAlpha(130);
			} catch(SWTException swte) {
				/**
				 * TODO Handle SWTException
				 */
			}
			
			// Text-illustration
			int nubsi = textBoxLength - Math.round(gc.stringExtent(text[piece]).x);
			if(text[piece]==Messages.Graph_error_nothingtomonitor) {
				gc.setBackground(grayColor);
				gc.fillRectangle(clientWidth-textBoxLength-15,(border+4)*(piece+1),textBoxLength+10,gc.stringExtent(text[piece]).y);
				try {
					gc.setAlpha(255);
				} catch(SWTException swte) {
					/**
					 * TODO Handle SWTException
					 */
				}
				gc.setForeground(rgbRedColor);

				gc.drawText(text[piece],clientWidth-textBoxLength+(nubsi/2)-10,(int)((border+4)*(piece+1)),true);
				gc.setForeground(blackColor);
			} else {
				gc.setBackground(color);
				gc.fillRectangle(clientWidth-textBoxLength-15,(border+4)*(piece+1),textBoxLength+10,gc.stringExtent(text[piece]).y);
				try {
					gc.setAlpha(255);
				} catch(SWTException swte) {
					/**
					 * TODO Handle SWTException
					 */
				}
				gc.setForeground(blackColor);
				
				gc.drawText(text[piece],clientWidth-textBoxLength+(nubsi/2)-10,(int)((border+4)*(piece+1)),true);
			}
			try {
				gc.setAlpha(255);
			} catch(SWTException swte) {
				/**
				 * TODO Handle SWTException
				 */
			}
			gc.drawLine(clientWidth-textBoxLength-15, (border+4)*(piece+1)+gc.stringExtent(text[piece]).y, clientWidth-5, (border+4)*(piece+1)+gc.stringExtent(text[piece]).y);
			

		}

		gc.setForeground(blackColor);
		
		// Drawing a box
		gc.drawRectangle(clientWidth-textBoxLength-15,border+4,textBoxLength+10, (border+4)*(text.length-1)+gc.stringExtent(text[0]).y);
	}
	
	/**
	 * draws the grid and returns the image data
	 * <p>
	 * use that data to create a new image and draw the graph on it
	 * @return represents the drawn grid
	 */
	public ImageData getGridImageData() {
		if(width <= 0 || height <= 0) {
			return null;
		}
		Image image = new Image(display, width, height);
		GC gc = new GC(image);
		
		gc.setForeground(grayColor);
		
		// vertical lines
		/*
		 * draws the vertical grid-lines for the worths to be displayed.
		 * 
		 * Numbers of worths to be displayed: length
		 * Max. displayable height: clienHeight
		 * Border-gap: border
		 */
		for (int i = 1; i < length; i++) {
			if ( (clientWidth+border) >= (border + i * spacer) )
			gc.drawLine(border + i * spacer,border,border + i * spacer,clientHeight + border);
		}
		
		// horizontal lines	
		/*
		 * draws the horizontal grid-lines by referencing the numbers of all possible spacer drawable
		 * within the current displayable height.
		 * 
		 * Current height: clientHeight
		 * Gap between two lines, or rather wild-card: spacer
		 * Border-gab: border
		 */
		for (int i = 1; i < (int)((clientHeight)/(spacer))+1; i++) {
			// Nur zeichnen, wenn der Darstellungsbereich nicht unterschritten wird
			if(clientHeight-i*(spacer)>=0) {
			gc.drawLine(border, clientHeight + border - i * (spacer),clientWidth + border,clientHeight + border - i * (spacer));
			}
//			gc.drawLine(border, clientHeight - i * spacer,clientWidth+border,clientHeight - i * spacer);
		}
		
		gc.setForeground(blackColor);
		
		// dimension-indicator mounting
				
		// horizontal dimension line
		gc.drawLine(border, clientHeight+border,clientWidth + border,clientHeight+border);
		// delimiter for the horizontal dimension line
		gc.drawLine(clientWidth+border,border+clientHeight,clientWidth+border,clientHeight - border/2+border);
		
		
		// vertical dimension line
		gc.drawLine(border, border, border,clientHeight+border);
		// delimiter for the vertical dimension line
		gc.drawLine(border, border,border + border/2, border);
		
		if(text.length > 0) {
			paintLegend(gc);
		}
		gc.dispose();
		
		return image.getImageData();
	}

}
