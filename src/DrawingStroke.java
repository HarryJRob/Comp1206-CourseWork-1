import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class DrawingStroke {

	private ArrayList<Point> pointList = new ArrayList<Point>();
	private int penSize;
	private Color penColor;
	private boolean isReflected;
	
	public DrawingStroke(int penSize, Color penColor, boolean isReflected) {
		this.penSize = penSize;
		this.penColor = penColor;
		this.isReflected = isReflected;
	}
	
	public void addPoint(Point pointToAdd) {
		pointList.add(pointToAdd);
	}
	
	public ArrayList<Point> getPointList() {
		return pointList;
	}
	
	public int getPenSize() {
		return penSize;
	}
	
	public Color getPenColor() {
		return penColor;
	}
	
	public boolean isReflected() {
		return isReflected;
	}
}
