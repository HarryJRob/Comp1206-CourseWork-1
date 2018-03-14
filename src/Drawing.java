import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Drawing {

	//Points relative to the center
	private ArrayList<DrawingStroke> strokeArray = new ArrayList<DrawingStroke>();
	private int numSectors = 8;
	
	public void addStroke(DrawingStroke dStroke) {
		strokeArray.add(dStroke);
	}
	
	public void render(int centerX, int centerY, Graphics2D g2D) {
		
		g2D.translate(centerX, centerY);
		double increment = 2*Math.PI/numSectors;
		
		Color curColor;
		int penSize;
		boolean isReflected;
		
		for(DrawingStroke curDStroke : strokeArray) {
			
			for(int curSector = 0; curSector < numSectors; curSector++) {
				g2D.rotate(increment);
				curColor = curDStroke.getPenColor();
				penSize = curDStroke.getPenSize();
				isReflected = curDStroke.isReflected();
				
				for(Point curPoint : curDStroke.getPointList()) {
						
					drawCircle(curPoint, penSize, curColor, g2D);
				
					if(isReflected) {
						drawCircle(new Point((int) curPoint.getY(),(int) curPoint.getX()), penSize, curColor, g2D);
					}
				}
			}
		}
	}
	
	public void render(int centerX, int centerY, Graphics2D g2D, double scale) {
		
		g2D.translate(centerX, centerY);
		g2D.scale(scale, scale);
		double increment = 2*Math.PI/numSectors;
		
		Color curColor;
		int penSize;
		boolean isReflected;
		
		for(DrawingStroke curDStroke : strokeArray) {
			
			for(int curSector = 0; curSector < numSectors; curSector++) {
				g2D.rotate(increment);
				curColor = curDStroke.getPenColor();
				penSize = curDStroke.getPenSize();
				isReflected = curDStroke.isReflected();
				
				for(Point curPoint : curDStroke.getPointList()) {
						
					drawCircle(curPoint, penSize, curColor, g2D);
				
					if(isReflected) {
						drawCircle(new Point((int) curPoint.getY(),(int) curPoint.getX()), penSize, curColor, g2D);
					}
				}
			}
		}
	}
	
	private void drawCircle(Point point, int diameter, Color color, Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) (point.getX()-diameter/2), (int) (point.getY()-diameter/2), diameter, diameter);
	}
	
	
	public void setNumSectors(int numSectors) {
		this.numSectors = numSectors;
	}
	
	public void setPointArray(ArrayList<DrawingStroke> pointArray) {
		this.strokeArray = pointArray;
	}
	
	
	public int getNumSectors() {
		return numSectors;
	}
	
	public ArrayList<DrawingStroke> getDSPointArray() {
		return strokeArray;
	}
}
