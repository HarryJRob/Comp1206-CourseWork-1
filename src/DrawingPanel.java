import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	
	private int numSectors = 8;
	private boolean drawSectors = true;
	private boolean reflectDrawnPoints = false;
	
	private final Stack<BufferedImage> undoStack = new LimitedSizedStack<BufferedImage>(8);
	private final Stack<BufferedImage> redoStack = new LimitedSizedStack<BufferedImage>(8);
	private final Stack<Integer> drawPointStack = new Stack<Integer>(); 
	
	
	public void init() {
		undoStack.clear();
		redoStack.clear();
		
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				int width = e.getComponent().getWidth();
				int height = e.getComponent().getHeight();
				if(width < height) {
					e.getComponent().setSize(new Dimension(e.getComponent().getWidth(),e.getComponent().getWidth()));
				} else if(height < width) {
					e.getComponent().setSize(new Dimension(e.getComponent().getHeight(),e.getComponent().getHeight()));
				}
				
				curImage = scaleImage(curImage, e.getComponent().getWidth(), e.getComponent().getHeight());
			}

			private BufferedImage scaleImage(BufferedImage imgToScale, int newWidth, int newHeight) {
				BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, imgToScale.getType());
				
				Graphics2D gScaled = (Graphics2D) scaledImage.getGraphics();
				gScaled.drawImage(imgToScale, 0, 0, newWidth, newHeight, 0, 0, imgToScale.getWidth(), imgToScale.getHeight(), null);
				gScaled.dispose();
				return scaledImage;
			}
			
			@Override
			public void componentShown(ComponentEvent arg0) { }

			@Override
			public void componentHidden(ComponentEvent e) { }

			@Override
			public void componentMoved(ComponentEvent e) { }
			
		});
		this.setSize(new Dimension(450,450));
		curImage = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		DrawListener drawListener = new DrawListener();
		this.addMouseMotionListener(drawListener);
		this.addMouseListener(drawListener);
	}
	
	
    public void paint(Graphics g) {
    	super.paint(g); 
    	
    	Graphics2D g2D = (Graphics2D) g;
    	if(curImage != null) {
			int centerX = curImage.getWidth()/2;
			int centerY = curImage.getHeight()/2;
	    	
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
	
						
						double increment = 2*Math.PI/numSectors;
						
						for(double angle = 0; angle < 2*Math.PI; angle += increment) {
							
							drawCircle((int) rotatePoint(x, y, centerX, centerY, angle, false),(int) rotatePoint(x, y, centerX, centerY, angle, true), penSize, gImg);
							
							if(reflectDrawnPoints) {
								drawCircle((int) rotatePoint(x, y, centerX, centerY, angle, true),(int) rotatePoint(x, y, centerX, centerY, angle, false), penSize, gImg);
							}
							
						}
						
					} catch (EmptyStackException e) {
						System.out.println(e.getMessage());
					}
				}
				gImg.dispose();
			}
	
			g2D.drawImage(curImage, 0, 0, null);
			
			if(drawSectors) {
				double increment = 2*Math.PI/numSectors;
				g2D.setStroke(new BasicStroke(1));
				g2D.setColor(new Color(255,255,255));
				if(numSectors != 1)
					for(double angle = 0; angle <= 2*Math.PI; angle+=increment) {
						g2D.drawLine(centerX, centerY,(int) rotatePoint(centerX, 0, centerX, centerY, angle, false),(int) rotatePoint(centerX, 0, centerX, centerY, angle, true));
					}
			}
    	}
    }
	
	private void drawCircle(int x, int y, int diameter, Graphics2D g) {
		g.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
	}
	
	private double rotatePoint(double pointX, double pointY, double centerX, double centerY, double angle, boolean XorY) {
		if(XorY) {
			return (centerY + Math.sin(angle)*(pointX-centerX) + Math.cos(angle)*(pointY-centerY));
		} else {
			return (centerX + Math.cos(angle)*(pointX-centerX) - Math.sin(angle)*(pointY-centerY));
		}
	}
	
	public void clearScreen() {
		undoStack.add(curImage);
		curImage = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
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
	
	
	public BufferedImage getCurImage() {
		return copyBufferedImage(curImage);
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
