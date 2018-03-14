import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JPanel;


//A panel which handles all of the logic in it require for drawing the doilie
@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	//The current drawing on screen
	private Drawing curDrawing;
	
	private Color penColour = new Color(255,255,255);
	private int penSize = 12;
	private boolean isEraser = false;
	private boolean isReflected;
	
	private boolean drawSectors = true;
	
	//The central point of the screen
	private int centerX;
	private int centerY;
	
	//Stacks to store drawings.
	private final Stack<Drawing> undoStack = new LimitedSizedStack<Drawing>(16);
	private final Stack<Drawing> redoStack = new LimitedSizedStack<Drawing>(16);
	
	//Initiates this panel.
	public void init() {
		undoStack.clear();
		redoStack.clear();
		
		//Allows for dynamic resizing of the panel.
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				centerX = e.getComponent().getWidth()/2;
				centerY = e.getComponent().getHeight()/2;
			}
			
			@Override
			public void componentShown(ComponentEvent arg0) { }

			@Override
			public void componentHidden(ComponentEvent e) { }

			@Override
			public void componentMoved(ComponentEvent e) { }
			
		});
		this.setSize(new Dimension(450,450));
		
		//Add a mouse listener to the panel
		curDrawing = new Drawing();
		DrawListener drawListener = new DrawListener();
		this.addMouseMotionListener(drawListener);
		this.addMouseListener(drawListener);
	}
	
	//Used to paint onto the panel
    public void paint(Graphics g) {
    	super.paint(g); 
    	
    	if(curDrawing != null) {
        	Graphics2D g2D = (Graphics2D) g;
    		

        	//Note: this sets the center of the screen to be the 0, 0 co ordinate
    		g2D.translate(centerX, centerY);
        	//Use the current drawing infomation to render onto the panel
    		curDrawing.render(g2D);
    		
    		//If the draw sectors flag is true draw lines from 0, 0 (the center of the screen) to the top of the screen and then rotate 
    		if(drawSectors) {
    			double increment = 2*Math.PI/curDrawing.getNumSectors();
    			g2D.setStroke(new BasicStroke(1));
    			g2D.setColor(new Color(255,255,255));
    			
    			if(curDrawing.getNumSectors() != 1) {
    				for(int curSector = 0; curSector < curDrawing.getNumSectors(); curSector++) {
    					g2D.rotate(increment);
    					g2D.drawLine(0, 0, 0, centerY);
    				}
    			}
    			
    		}
    	}

   }	
	
    //Clear the screen by creating a new Drawing. 
	public void clearScreen() {
		undoStack.add(curDrawing);
		curDrawing = new Drawing();
		redoStack.clear();
		repaint();
	}
	
	//Undoes the last action
	public void undo() {
		if(!undoStack.isEmpty()) {
			Drawing drawing = undoStack.pop();
			redoStack.push(cloneDrawing(curDrawing));
			curDrawing = drawing;
			repaint();
			
		} 
	}
	
	//Redoes the last undone action
	public void redo() {
		if(!redoStack.isEmpty()) {
			Drawing drawing = redoStack.pop();
			undoStack.push(cloneDrawing(curDrawing));
			curDrawing = drawing;
			repaint();
			
		} 
	}
	
	//Does a deep copy of the drawing
	@SuppressWarnings("unchecked")
	public Drawing cloneDrawing(Drawing drawingToClone) {
		Drawing returnDrawing = new Drawing();
		
		returnDrawing.setNumSectors(drawingToClone.getNumSectors());
		returnDrawing.setPointArray((ArrayList<DrawingStroke>) drawingToClone.getDSPointArray().clone());
		
		return returnDrawing;
	}
	
	//Returns the current drawing
	public Drawing getCurDrawing() {
		return cloneDrawing(curDrawing);
	}
	
	//Sets the current drawing
	public void setDrawing(Drawing drawing) {
		curDrawing = cloneDrawing(drawing);
		undoStack.clear();
		redoStack.clear();
		repaint();
	}
	
	//Sets the pen colour
	public void setColor(Color colour) {
		penColour = colour;
	}
	
	//Sets the pen size
	public void setPenSize(int size) {
		penSize = size;
	}
	
	//Sets if the pen is currently a eraser
	public void setIsEraser(boolean isEraser) {
		this.isEraser = isEraser;
	}
	
	
	//Sets the number of sectors
	public void setSectors(int sectors) {
		curDrawing.setNumSectors(sectors);
		repaint();
	}
	
	//Sets if the sectors should be drawn
	public void setDrawSectors(boolean drawSectors) {
		this.drawSectors = drawSectors;
		repaint();
	}
	
	//Sets if the points drawn should be reflected
	public void setReflectPoints(boolean reflectDrawnPoints) {
		isReflected = reflectDrawnPoints;
		repaint();
	}
	
	//The event listener used to draw with the mouse
	private class DrawListener extends MouseAdapter {

		DrawingStroke curStroke;
		
		//When the mouse is pressed start a new stroke and start adding points to it
		@Override
		public void mousePressed(MouseEvent e) {
			undoStack.push(cloneDrawing(curDrawing));
			
			//If the pen is currently an eraser then draw in black
			//Note: this is not how we were told to implement this but doing circle intersection logic between two strokes took way too much time
			if(isEraser) {
				curStroke = new DrawingStroke(penSize, new Color( 0, 0, 0), isReflected);
			} else {
				curStroke = new DrawingStroke(penSize, penColour, isReflected);
			}
			curStroke.addPoint(new Point(e.getX() - centerX, e.getY() - centerY));
			curDrawing.addStroke(curStroke);
			
			redoStack.clear();
			repaint();
		}
		
		//Add points to the current stroke
		@Override
		public void mouseDragged(MouseEvent e) {
			if(isEraser) {
				curStroke.addPoint(new Point(e.getX() - centerX, e.getY() - centerY));
			} else {
				curStroke.addPoint(new Point(e.getX() - centerX, e.getY() - centerY));
			}
			repaint();
		}
		
	}
	
	//A limited size stack used for undo and redo so that it does not use up too much memory
	private class LimitedSizedStack<T> extends Stack<T> {
		private int maximumStackSize;
		
		public LimitedSizedStack(int maxSize) {
			super();
			this.maximumStackSize = maxSize;
		}
		
		@Override
		public T push(T item) {
			//If the stack is overside remove the last elements until it is suitably sized
			while(this.size() >= maximumStackSize) {
				this.remove(0);
			}
			return super.push(item);
		}
	}
}
