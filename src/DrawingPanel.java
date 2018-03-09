import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {

	private BufferedImage curImage;
	private final Stack<BufferedImage> undoStack = new LimitedSizedStack<BufferedImage>(8);
	private final Stack<BufferedImage> redoStack = new LimitedSizedStack<BufferedImage>(8);
	
	//graphics context
	//othershit
	
	public void init() {
		clearScreen();
		DrawListener drawListener = new DrawListener();
		this.addMouseMotionListener(drawListener);
		this.addMouseListener(drawListener);
	}
	
	@Override
	public void repaint() {
		super.repaint();
	}

	public void clearScreen() {
		curImage = new BufferedImage(this.getWidth(),this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	public BufferedImage getCurrentDoilie() {
		return curImage;
	}
	
	private class DrawListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("Click : \t\t" + e.getX() + " , " + e.getY());
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("Drag : \t\t" + e.getX() + " , " + e.getY());
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("Released : \t" + e.getX() + " , " + e.getY());
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
