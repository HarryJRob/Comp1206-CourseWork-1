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
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	private Drawing curDrawing;
	
	private Color penColour = new Color(255,255,255);
	private int penSize = 12;
	private boolean isEraser = false;
	private boolean isReflected;
	
	private boolean drawSectors = true;
	
	private int centerX;
	private int centerY;
	
	private final Stack<Drawing> undoStack = new LimitedSizedStack<Drawing>(16);
	private final Stack<Drawing> redoStack = new LimitedSizedStack<Drawing>(16);
	
	
	public void init() {
		undoStack.clear();
		redoStack.clear();
		
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
		
		curDrawing = new Drawing();
		DrawListener drawListener = new DrawListener();
		this.addMouseMotionListener(drawListener);
		this.addMouseListener(drawListener);
	}
	
	
    public void paint(Graphics g) {
    	super.paint(g); 
    	
    	Graphics2D g2D = (Graphics2D) g;
    	
    	if(curDrawing != null) {
    		curDrawing.render(centerX, centerY, g2D);
    		
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
	
	public void clearScreen() {
		undoStack.add(curDrawing);
		curDrawing = new Drawing();
		redoStack.clear();
		repaint();
	}
	
	
	public void undo() {
		try {
			Drawing drawing = undoStack.pop();
			redoStack.push(cloneDrawing(curDrawing));
			curDrawing = drawing;
			repaint();
			
		} catch (EmptyStackException e) {
			
		}
	}
	
	public void redo() {
		try {
			Drawing drawing = redoStack.pop();
			undoStack.push(cloneDrawing(curDrawing));
			curDrawing = drawing;
			repaint();
			
		} catch (EmptyStackException e) {
			
		}
	}
	
	public Drawing cloneDrawing(Drawing drawingToClone) {
		Drawing returnDrawing = new Drawing();
		
		returnDrawing.setNumSectors(drawingToClone.getNumSectors());
		returnDrawing.setPointArray((ArrayList<DrawingStroke>) drawingToClone.getDSPointArray().clone());
		
		return returnDrawing;
	}
	
	
	public Drawing getCurDrawing() {
		return cloneDrawing(curDrawing);
	}
	
	public void setDrawing(Drawing drawing) {
		curDrawing = cloneDrawing(drawing);
		repaint();
	}
	
	
	public void setColor(Color colour) {
		penColour = colour;
	}
	
	public void setPenSize(int size) {
		penSize = size;
	}
	
	public void setIsEraser(boolean isEraser) {
		this.isEraser = isEraser;
	}
	
	
	public void setSectors(int sectors) {
		curDrawing.setNumSectors(sectors);
		repaint();
	}
	
	public void setDrawSectors(boolean drawSectors) {
		this.drawSectors = drawSectors;
		repaint();
	}
	
	public void setReflectPoints(boolean reflectDrawnPoints) {
		isReflected = reflectDrawnPoints;
		repaint();
	}
	
	
	private class DrawListener extends MouseAdapter {

		DrawingStroke curStroke;
		
		@Override
		public void mousePressed(MouseEvent e) {
			undoStack.push(cloneDrawing(curDrawing));
			
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
	
	
	private class LimitedSizedStack<T> extends Stack<T> {
		private int maximumStackSize;
		
		public LimitedSizedStack(int maxSize) {
			super();
			this.maximumStackSize = maxSize;
		}
		
		@Override
		public T push(T item) {
			while(this.size() >= maximumStackSize) {
				this.remove(0);
			}
			return super.push(item);
		}
	}
}
