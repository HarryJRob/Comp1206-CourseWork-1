import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

//A new stroke is indicated by a mouse press.
public class DrawingStroke {

	//Stores the points in this current stroke
	private ArrayList<Point> pointList = new ArrayList<Point>();
	//Stores the variables used to make this stroke
	private int penSize;
	private Color penColor;
	private boolean isReflected;
	
	//Can only be passed on object construction.
	public DrawingStroke(int penSize, Color penColor, boolean isReflected) {
		this.penSize = penSize;
		this.penColor = penColor;
		this.isReflected = isReflected;
	}
	
	//Adds a point to the pointlist
	public void addPoint(Point pointToAdd) {
		pointList.add(pointToAdd);
	}
	
	//Returns the point list
	public ArrayList<Point> getPointList() {
		return pointList;
	}
	
	//returns the pens size for this stroke
	public int getPenSize() {
		return penSize;
	}
	
	//return the pens colour for this stroke
	public Color getPenColor() {
		return penColor;
	}
	
	//return if this stroke is reflected
	public boolean isReflected() {
		return isReflected;
	}
}
