import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	private BufferedImage curImage;
	
	private Color penColour = new Color(255,255,255);
	private int penSize = 12;
	private boolean isEraser = false;
	
	private int numSectors = 12;
	private boolean drawSectors = true;
	private boolean reflectDrawnPoints = false;
	
	private final Stack<BufferedImage> undoStack = new LimitedSizedStack<BufferedImage>(8);
	private final Stack<BufferedImage> redoStack = new LimitedSizedStack<BufferedImage>(8);
	private final Stack<Integer> drawPointStack = new Stack<Integer>(); 
	
	
	public void init() {
		clearScreen();
		DrawListener drawListener = new DrawListener();
		this.addMouseMotionListener(drawListener);
		this.addMouseListener(drawListener);
	}
	
	
    public void paint(Graphics g) {
    	super.paint(g); 
    	
    	Graphics2D g2D = (Graphics2D) g;
    	
		if(drawPointStack != null && !drawPointStack.isEmpty()) {
			Graphics2D gImg = (Graphics2D) curImage.getGraphics();
			
			if(isEraser) {
				gImg.setColor(new Color(0,0,0));
			} else {
				gImg.setColor(penColour);
			}
			
			while(!drawPointStack.isEmpty()) {
				try {

					int y = drawPointStack.pop();
					int x = drawPointStack.pop();
					
					drawCircle(x, y, penSize, gImg);
				} catch (EmptyStackException e) {
					System.out.println(e.getMessage());
				}
			}
			gImg.dispose();
		}
		
		
		if(curImage != null) {
			g.drawImage(curImage, 0, 0, null);
			
			if(drawSectors) {
				double increment = 2*Math.PI/numSectors;
				g2D.setStroke(new BasicStroke(1));
				g2D.setColor(new Color(255,255,255));
				int width = curImage.getWidth();
				int height = curImage.getHeight();
				
				for(double angle = 0; angle <= 2*Math.PI; angle+=increment) {
					g2D.drawLine(width/2, height/2,(int) (width/2 + Math.cos(angle)*width),(int) (height/2 + Math.sin(angle)*height));
				}
			}
		}
    }
	
	private void drawCircle(int x, int y, int diameter, Graphics2D g) {
		g.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
	}
    
	public void clearScreen() {
		curImage = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		undoStack.clear();
		redoStack.clear();
		repaint();
	}
	
	
	public void undo() {
		try {
			BufferedImage img = undoStack.pop();
			redoStack.push(copyBufferedImage(curImage));
			curImage = img;
			repaint();
		} catch (EmptyStackException e) {
			
		}
	}
	
	public void redo() {
		try {
			BufferedImage img = redoStack.pop();
			undoStack.push(copyBufferedImage(curImage));
			curImage = img;
			repaint();
		} catch (EmptyStackException e) {
			
		}
	}
	
	private BufferedImage copyBufferedImage(BufferedImage img) {
		BufferedImage returnImage = new BufferedImage(img.getWidth(),img.getHeight(), img.getType());
		Graphics g = returnImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		return returnImage;
	}
	
	
	public void setColor(Color colour) {
		penColour = colour;
	}
	
	public void setSize(int size) {
		penSize = size;
	}
	
	public void setIsEraser(boolean isEraser) {
		this.isEraser = isEraser;
	}
	
	
	public void setSectors(int sectors) {
		numSectors = sectors;
		repaint();
	}
	
	public void setDrawSectors(boolean drawSectors) {
		this.drawSectors = drawSectors;
		repaint();
	}
	
	public void setReflectPoints(boolean reflectDrawnPoints) {
		this.reflectDrawnPoints = reflectDrawnPoints;
	}
	
	
	private class DrawListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			undoStack.push(copyBufferedImage(curImage));
			drawPointStack.push(e.getX());
			drawPointStack.push(e.getY());
			redoStack.clear();
			repaint();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			drawPointStack.push(e.getX());
			drawPointStack.push(e.getY());
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
