import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DFrame extends JFrame {

	Pen curPen = new Pen(12, new Color(0));
	Graphics2D doilieGraphicsArea;
	
	public void init() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLayout(new BorderLayout());
		this.setTitle("Digital Doilies");
		this.setMinimumSize(new Dimension(450,450));

		//Drawing panel setup
		JPanel drawingPanel = new JPanel();
		drawingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		doilieGraphicsArea = (Graphics2D) drawingPanel.getGraphics();
		
		//ToolBar set up
		JToolBar toolbar = new JToolBar();	
		toolbar.setRollover(true);
		toolbar.setLayout(new BorderLayout());
		
		//Pen selection
		{
			JPanel penSelectionPanel = new JPanel();
			
//			JComboBox<Pen> penSelector = new JComboBox<Pen>();
//			penSelector.setToolTipText("Select a pen or make a new one");
//			penSelectionPanel.add(penSelector);
//			penSelector.addItem(curPen);
			
			JButton colorSelector = new JButton("Sample");
			colorSelector.setContentAreaFilled(false);
			colorSelector.setOpaque(true);
			colorSelector.setToolTipText("Set the colour of the current pen");
			colorSelector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new colourChooserDialog(colorSelector);
				}
				
			});
			
			penSelectionPanel.add(colorSelector);
			
			JTextField sizeSelector = new JTextField(2);
			sizeSelector.setToolTipText("Set the size of the current pen");
			penSelectionPanel.add(sizeSelector);
			
			JCheckBox eraserSelection = new JCheckBox();
			eraserSelection.setToolTipText("Toggle eraser");
			penSelectionPanel.add(eraserSelection);
			
			toolbar.add(penSelectionPanel, BorderLayout.WEST);
		}
		
		//Doilie control panel
		{
			JPanel doilieControlPanel = new JPanel();
			
			JButton clearDisplay = new JButton("Clear");
			clearDisplay.setToolTipText("Clear the current doilie");
			doilieControlPanel.add(clearDisplay);
			
			JTextField numOfSectors = new JTextField(2);
			numOfSectors.setToolTipText("Change the number of sectors");
			doilieControlPanel.add(numOfSectors);
			
			JCheckBox showSectorLines = new JCheckBox();
			showSectorLines.setToolTipText("Toggle sector lines");
			doilieControlPanel.add(showSectorLines);
			
			JCheckBox reflectDraw = new JCheckBox();
			reflectDraw.setToolTipText("Reflect drawn points");
			doilieControlPanel.add(reflectDraw);
			
			JButton undoButton = new JButton("↩");
			undoButton.setToolTipText("Undo draw action");
			doilieControlPanel.add(undoButton);
			
			JButton redoButton = new JButton("↪");
			redoButton.setToolTipText("Redo draw action");
			doilieControlPanel.add(redoButton);
			
			toolbar.add(doilieControlPanel, BorderLayout.EAST	);
		}
		
		this.add(toolbar, BorderLayout.NORTH);
		
		this.add(drawingPanel, BorderLayout.CENTER);
		
        this.setSize(750,750);
       	this.setVisible(true);
	}

	private class colourChooserDialog extends JDialog {
		
		public colourChooserDialog(JButton colorButton) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			this.setTitle("Colour Chooser");
			this.setSize(new Dimension(700,400));
			
			JColorChooser colourChooser = new JColorChooser();
			
			colourChooser.getSelectionModel().addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					colorButton.setBackground(colourChooser.getColor());
					colorButton.setForeground(colourChooser.getColor());
				}
			
			});
			
			this.add(colourChooser);
			
			this.setVisible(true);
		}
		
	}
	
}
