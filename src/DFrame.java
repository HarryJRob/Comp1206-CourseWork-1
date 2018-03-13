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
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DFrame extends JFrame {
	
	public void init() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLayout(new BorderLayout());
		this.setTitle("Digital Doilies");
		this.setMinimumSize(new Dimension(450,450));

		//Drawing panel setup
		DrawingPanel drawingPanel = new DrawingPanel();
		drawingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawingPanel.setBackground(new Color(0));
		
		//ToolBar set up
		JPanel toolbar = new JPanel();	

		toolbar.setLayout(new GridLayout(9,1));
		
		//Pen selection
		{
			
			JButton colorSelector = new JButton("Sample");
			colorSelector.setContentAreaFilled(false);
			colorSelector.setOpaque(true);
			colorSelector.setToolTipText("Set the colour of the current pen");
			colorSelector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new colourChooserDialog(colorSelector, drawingPanel);
				}
				
			});
			
			toolbar.add(colorSelector);
			
			JSpinner sizeSelector = new JSpinner();
			sizeSelector.setValue(12);
			sizeSelector.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					drawingPanel.setPenSize((Integer)sizeSelector.getValue());
				}

			} );
			sizeSelector.setToolTipText("Set the size of the current pen");
			toolbar.add(sizeSelector);
			
			JCheckBox eraserSelection = new JCheckBox("Toggle Eraser");
			eraserSelection.setToolTipText("Toggle eraser");
			eraserSelection.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setIsEraser(eraserSelection.isSelected());
				}
			
			} );
			toolbar.add(eraserSelection);
		}
		
		//Doilie control panel
		{
			
			JButton clearDisplay = new JButton("Clear");
			clearDisplay.setToolTipText("Clear the current doilie");
			clearDisplay.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clearScreen();
				}
			
			} );
			toolbar.add(clearDisplay);
			
			JSpinner numOfSectors = new JSpinner();
			numOfSectors.setValue(8);
			numOfSectors.setToolTipText("Change the number of sectors");
			numOfSectors.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					drawingPanel.setSectors((Integer)numOfSectors.getValue());
				}

			} );
			toolbar.add(numOfSectors);
			
			JCheckBox showSectorLines = new JCheckBox("Toggle Sector Lines");
			showSectorLines.setSelected(true);
			showSectorLines.setToolTipText("Toggle sector lines");
			showSectorLines.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setDrawSectors(showSectorLines.isSelected());
				}
			
			} );
			toolbar.add(showSectorLines);
			
			JCheckBox reflectDraw = new JCheckBox("Reflect drawn points");
			reflectDraw.setToolTipText("Reflect drawn points");
			reflectDraw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
				}
			
			} );
			toolbar.add(reflectDraw);
			
			JButton undoButton = new JButton("↩");
			undoButton.setToolTipText("Undo draw action");
			undoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.undo();
				}
			
			} );
			toolbar.add(undoButton);
			
			JButton redoButton = new JButton("↪");
			redoButton.setToolTipText("Redo draw action");
			redoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.redo();
				}
			
			} );
			toolbar.add(redoButton);
		}
		
		this.add(toolbar, BorderLayout.WEST);
		
		this.add(drawingPanel, BorderLayout.CENTER);
		
        this.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width*0.5),(int) (Toolkit.getDefaultToolkit().getScreenSize().height*0.7));
       	this.setVisible(true);
       	drawingPanel.init();
	}

	@SuppressWarnings("serial")
	private class colourChooserDialog extends JDialog {
		
		public colourChooserDialog(JButton colorButton, DrawingPanel drawingPanel) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			this.setTitle("Colour Chooser");
			this.setSize(new Dimension(700,400));
			
			JColorChooser colourChooser = new JColorChooser();
			
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
