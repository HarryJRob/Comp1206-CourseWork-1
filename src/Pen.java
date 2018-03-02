import java.awt.Color;

public class Pen {
	
	private int penSize;
	private Color penColor;
	private boolean isEraser;
	
	public Pen(int size, Color color) {
		penSize = size;
		penColor = color;
		isEraser = false;
	}
	
	public Pen(int size, Color color, boolean isEraser) {
		penSize = size;
		penColor = color;
		this.isEraser = isEraser;
	}
	
	public Color getColor() {
		return penColor;
	}
	
	public void setColor(Color newColor) {
		penColor = newColor;
	}
	
	public int getSize() {
		return penSize;
	}
	
	public void setSize(int newSize) {
		penSize = newSize;
	}
	
	public boolean isEraser() {
		return isEraser;
	}
	
	public void toggleEraser() {
		isEraser = !isEraser;
	}
}
