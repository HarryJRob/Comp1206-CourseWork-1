import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * This Frame is the container for the entire program.
 */
@SuppressWarnings("serial")
public class DoilieMainFrame extends JFrame {
	
	//Initialises the frame
	public void init() {
		
		//Frame setup
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLayout(new BorderLayout());
		this.setTitle("Digital Doilies");
		this.setMinimumSize(new Dimension(1350,450));

		//Creates a drawingPanel to handle all drawing logic
		DrawingPanel drawingPanel = new DrawingPanel();
		drawingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawingPanel.setBackground(new Color(0));
		
		//ToolBar set up
		JPanel toolbar = new JPanel();	

		toolbar.setLayout(new GridLayout(13,1));
		
		//Pen options
		{
			//Used to select the colour of the pen
			JButton colorSelector = new JButton("Colour Selector");
			colorSelector.setContentAreaFilled(false);
			colorSelector.setOpaque(true);
			colorSelector.setToolTipText("Set the colour of the current pen");
			toolbar.add(colorSelector);

			//Displays a label to adviser user on what a component is user for
			JLabel sizeLabel = new JLabel("Pen Size");
			sizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
			sizeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
			toolbar.add(sizeLabel);
			
			//Allows for the user to change the size of the pen
			JSpinner sizeSelector = new JSpinner();
			sizeSelector.setModel(new SpinnerNumberModel(12, 2, Integer.MAX_VALUE, 1));
			sizeSelector.setToolTipText("Set the size of the current pen");
			toolbar.add(sizeSelector);
			
			//Allows the user to toggle the eraser function
			JCheckBox eraserSelection = new JCheckBox("Toggle Eraser");
			eraserSelection.setToolTipText("Toggle eraser");
			toolbar.add(eraserSelection);
			
			//colorSelector event listener
			colorSelector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//Creates on a colour chooser dialogue box
					new colourChooserDialog(colorSelector, drawingPanel);
				}
				
			});
			
			//sizeSelector event listener
			sizeSelector.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					//Changes the pen size 
					drawingPanel.setPenSize((Integer)sizeSelector.getValue());
				}

			} );
			
			//eraserSelection event listener
			eraserSelection.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//Changes if the eraser is enabled 
					drawingPanel.setIsEraser(eraserSelection.isSelected());
				}
			
			} );
		}
		
		//Doilie control panel
		{
			//Enables the user to clear the display
			JButton clearDisplay = new JButton("Clear");
			clearDisplay.setToolTipText("Clear the current doilie");
			toolbar.add(clearDisplay);

			//Displays a label to describe what the spinner does
			JLabel sectorLabel = new JLabel("Number of Sectors");
			sectorLabel.setHorizontalAlignment(SwingConstants.LEFT);
			sectorLabel.setVerticalAlignment(SwingConstants.BOTTOM);
			toolbar.add(sectorLabel);
			
			//Enables the user to change the number of sectors
			JSpinner numOfSectors = new JSpinner();
			numOfSectors.setModel(new SpinnerNumberModel(8, 1, Integer.MAX_VALUE, 1));
			numOfSectors.setToolTipText("Change the number of sectors");
			toolbar.add(numOfSectors);
			
			//A check box used to denote if sector lines should be shown
			JCheckBox showSectorLines = new JCheckBox("Toggle Sector Lines");
			showSectorLines.setSelected(true);
			showSectorLines.setToolTipText("Toggle sector lines");
			toolbar.add(showSectorLines);
			
			//A check box used to denote if the users points should be mirrored
			JCheckBox reflectDraw = new JCheckBox("Reflect drawn points");
			reflectDraw.setToolTipText("Reflect drawn points");
			toolbar.add(reflectDraw);
			
			//Displays a label to describe what the undo button does
			JLabel undoLabel = new JLabel("Undo");
			undoLabel.setHorizontalAlignment(SwingConstants.LEFT);
			undoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
			toolbar.add(undoLabel);
			
			//Undoes the previous action
			JButton undoButton = new JButton("↩");
			undoButton.setToolTipText("Undo draw action");
			toolbar.add(undoButton);
			
			//Displays a label to describe what the redo button does
			JLabel redoLabel = new JLabel("Redo");
			redoLabel.setHorizontalAlignment(SwingConstants.LEFT);
			redoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
			toolbar.add(redoLabel);
			
			//Redoes the previously undone action
			JButton redoButton = new JButton("↪");
			redoButton.setToolTipText("Redo draw action");
			toolbar.add(redoButton);
			
			//The event listener to clear the display
			clearDisplay.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clearScreen();
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
					drawingPanel.setSectors((int) numOfSectors.getValue());
				}
			
			} );
			
			//The event listener to change the number of sectors
			numOfSectors.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					drawingPanel.setSectors((int) numOfSectors.getValue());
				}

			} );
			
			//The event listener to allow the toggling of showing sector lines
			showSectorLines.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setDrawSectors(showSectorLines.isSelected());
				}
			
			} );
			
			//The event listener to allow for the toggling of reflecting a users drawn points
			reflectDraw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
				}
			
			} );
			
			//The event listener to allow for the undoing of the last action.
			undoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.undo();
				}
			
			} );
			
			//The event listener to allow for redoing the last undone action
			redoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.redo();
				}
			
			} );
		}
		
		//Creates a GalleryPanel to handle all gallery logic.
		DoilieGalleryPanel gallery = new DoilieGalleryPanel(drawingPanel);
		gallery.init();
		
		//Adds the panels to the frame
		this.add(toolbar, BorderLayout.WEST);
		this.add(drawingPanel, BorderLayout.CENTER);
		this.add(gallery, BorderLayout.SOUTH);
		
		//Set the size based on the screen resolution
        this.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width*0.5),(int) (Toolkit.getDefaultToolkit().getScreenSize().height*0.7));
       	this.setVisible(true);
       	drawingPanel.init();
	}

	//Defines the logic for the colour chooser dialogue box
	private class colourChooserDialog extends JDialog {
		
		public colourChooserDialog(JButton colorButton, DrawingPanel drawingPanel) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			this.setTitle("Colour Chooser");
			this.setSize(new Dimension(700,400));
			
			//In built colour chooser in swing
			JColorChooser colourChooser = new JColorChooser();
			
			//Event handler for when a new colour is selected
			colourChooser.getSelectionModel().addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Color c = colourChooser.getColor();
					colorButton.setBackground(c);
					colorButton.setForeground(c);
					drawingPanel.setColor(c);
				}
			
			});
			
			this.add(colourChooser);
			
			this.setVisible(true);
		}
		
	}

	
}
