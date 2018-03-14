import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class DoilieMainFrame extends JFrame {
	
	public void init() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLayout(new BorderLayout());
		this.setTitle("Digital Doilies");
		this.setMinimumSize(new Dimension(1350,450));

		//Drawing panel setup
		DrawingPanel drawingPanel = new DrawingPanel();
		drawingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawingPanel.setBackground(new Color(0));
		
		//ToolBar set up
		JPanel toolbar = new JPanel();	

		toolbar.setLayout(new GridLayout(9,1));
		
		//Pen selection
		{
			
			JButton colorSelector = new JButton("Colour Selector");
			colorSelector.setContentAreaFilled(false);
			colorSelector.setOpaque(true);
			colorSelector.setToolTipText("Set the colour of the current pen");
			toolbar.add(colorSelector);
			
			JSpinner sizeSelector = new JSpinner();
			sizeSelector.setModel(new SpinnerNumberModel(12, 2, Integer.MAX_VALUE, 1));
			sizeSelector.setToolTipText("Set the size of the current pen");
			toolbar.add(sizeSelector);
			
			JCheckBox eraserSelection = new JCheckBox("Toggle Eraser");
			eraserSelection.setToolTipText("Toggle eraser");
			toolbar.add(eraserSelection);
			
			colorSelector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new colourChooserDialog(colorSelector, drawingPanel);
				}
				
			});
			
			sizeSelector.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					drawingPanel.setPenSize((Integer)sizeSelector.getValue());
				}

			} );
			
			eraserSelection.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setIsEraser(eraserSelection.isSelected());
				}
			
			} );
		}
		
		//Doilie control panel
		{
			JButton clearDisplay = new JButton("Clear");
			clearDisplay.setToolTipText("Clear the current doilie");
			toolbar.add(clearDisplay);
			
			JSpinner numOfSectors = new JSpinner();
			numOfSectors.setModel(new SpinnerNumberModel(8, 1, Integer.MAX_VALUE, 1));

			numOfSectors.setToolTipText("Change the number of sectors");
			toolbar.add(numOfSectors);
			
			JCheckBox showSectorLines = new JCheckBox("Toggle Sector Lines");
			showSectorLines.setSelected(true);
			showSectorLines.setToolTipText("Toggle sector lines");
			toolbar.add(showSectorLines);
			
			JCheckBox reflectDraw = new JCheckBox("Reflect drawn points");
			reflectDraw.setToolTipText("Reflect drawn points");
			toolbar.add(reflectDraw);
			
			JButton undoButton = new JButton("↩");
			undoButton.setToolTipText("Undo draw action");
			toolbar.add(undoButton);
			
			JButton redoButton = new JButton("↪");
			redoButton.setToolTipText("Redo draw action");
			toolbar.add(redoButton);
			
			clearDisplay.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.clearScreen();
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
					drawingPanel.setSectors((int) numOfSectors.getValue());
				}
			
			} );
			
			numOfSectors.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					drawingPanel.setSectors((int) numOfSectors.getValue());
				}

			} );
			
			showSectorLines.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setDrawSectors(showSectorLines.isSelected());
				}
			
			} );
			
			reflectDraw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.setReflectPoints(reflectDraw.isSelected());
				}
			
			} );
			
			undoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.undo();
				}
			
			} );
			
			redoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawingPanel.redo();
				}
			
			} );
		}
		
		DoilieGallaryPanel gallary = new DoilieGallaryPanel(drawingPanel);
		gallary.init();
		
		this.add(toolbar, BorderLayout.WEST);
		
		this.add(drawingPanel, BorderLayout.CENTER);
		
		this.add(gallary, BorderLayout.SOUTH);
		
        this.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width*0.5),(int) (Toolkit.getDefaultToolkit().getScreenSize().height*0.7));
       	this.setVisible(true);
       	drawingPanel.init();
       	
       	drawingPanel.repaint();
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
