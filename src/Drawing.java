import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Drawing {

	//An array of strokes
	private ArrayList<DrawingStroke> strokeArray = new ArrayList<DrawingStroke>();
	//The number of sectors currently being used
	private int numSectors = 8;
	
	//Adds a stroke to the strokeArray
	public void addStroke(DrawingStroke dStroke) {
		strokeArray.add(dStroke);
	}
	
	//Renders the strokes in accordance with the current number of sectors
	public void render(Graphics2D g2D) {
		
		//The angle between each sector
		double increment = 2*Math.PI/numSectors;
		
		//For each stroke
		for(DrawingStroke curDStroke : strokeArray) {
			
			//For each sector
			for(int curSector = 0; curSector < numSectors; curSector++) {
				//rotate the graphics object by the increment
				g2D.rotate(increment);
				Color curColor = curDStroke.getPenColor();
				int penSize = curDStroke.getPenSize();
				boolean isReflected = curDStroke.isReflected();
				
				//Draw each point
				for(Point curPoint : curDStroke.getPointList()) {
						
					drawCircle(curPoint, penSize, curColor, g2D);
				
					//If it is reflected reflect in x = y
					if(isReflected) {
						drawCircle(new Point((int) curPoint.getY(),(int) curPoint.getX()), penSize, curColor, g2D);
					}
				}
			}
		}
	}
	
	//Draws a circle
	private void drawCircle(Point point, int diameter, Color color, Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) (point.getX()-diameter/2), (int) (point.getY()-diameter/2), diameter, diameter);
	}
	
	
	//Sets the number of sectors
	public void setNumSectors(int numSectors) {
		this.numSectors = numSectors;
	}
	
	//Sets the stroke array (for undoing and redoing)
	public void setPointArray(ArrayList<DrawingStroke> strokeArray) {
		this.strokeArray = strokeArray;
	}
	
	
	//returns the number of sectors
	public int getNumSectors() {
		return numSectors;
	}
	
	//returns the drawing stroke array
	public ArrayList<DrawingStroke> getDSPointArray() {
		return strokeArray;
	}
}
