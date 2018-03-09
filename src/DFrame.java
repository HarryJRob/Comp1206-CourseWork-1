import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
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
		JToolBar toolbar = new JToolBar();	
		toolbar.setRollover(true);
		toolbar.setLayout(new BorderLayout());
		
		//Pen selection
		{
			JPanel penSelectionPanel = new JPanel();
			
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
			
			penSelectionPanel.add(colorSelector);
			
			JTextField sizeSelector = new JTextField(2);
			
			sizeSelector.setToolTipText("Set the size of the current pen");
			penSelectionPanel.add(sizeSelector);
			
			JCheckBox eraserSelection = new JCheckBox();
			eraserSelection.setToolTipText("Toggle eraser");
			eraserSelection.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setIsEraser(eraserSelection.isSelected());
				}
			
			} );
			penSelectionPanel.add(eraserSelection);
			
			toolbar.add(penSelectionPanel, BorderLayout.WEST);
		}
		
		//Doilie control panel
		{
			JPanel doilieControlPanel = new JPanel();
			
			JButton clearDisplay = new JButton("Clear");
			clearDisplay.setToolTipText("Clear the current doilie");
			clearDisplay.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clearScreen();
				}
			
			} );
			doilieControlPanel.add(clearDisplay);
			
			JTextField numOfSectors = new JTextField(2);
			numOfSectors.setToolTipText("Change the number of sectors");
			doilieControlPanel.add(numOfSectors);
			
			JCheckBox showSectorLines = new JCheckBox();
			showSectorLines.setSelected(true);
			showSectorLines.setToolTipText("Toggle sector lines");
			showSectorLines.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setDrawSectors(showSectorLines.isSelected());
				}
			
			} );
			doilieControlPanel.add(showSectorLines);
			
			JCheckBox reflectDraw = new JCheckBox();
			reflectDraw.setToolTipText("Reflect drawn points");
			reflectDraw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
				}
			
			} );
			doilieControlPanel.add(reflectDraw);
			
			JButton undoButton = new JButton("↩");
			undoButton.setToolTipText("Undo draw action");
			undoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.undo();
				}
			
			} );
			doilieControlPanel.add(undoButton);
			
			JButton redoButton = new JButton("↪");
			redoButton.setToolTipText("Redo draw action");
			redoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.redo();
				}
			
			} );
			doilieControlPanel.add(redoButton);
			
			toolbar.add(doilieControlPanel, BorderLayout.EAST	);
		}
		
		this.add(toolbar, BorderLayout.NORTH);
		
		this.add(drawingPanel, BorderLayout.CENTER);
		
        this.setSize(750,750);
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
