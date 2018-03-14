import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DoilieGalleryPanel extends JPanel {

	//Defines the size of the icon used to display a thumbnail of the drawing
	private static int DEFAULT_ICON_SIDE_LENGTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().height*0.09);
	
	//Stores 12 labels used to display the thumbnails of the drawings 
	private JLabel[] labels = new JLabel[12];
	//Stores 12 drawings
	private Drawing[] drawings = new Drawing[12];
	//Stores which drawing is currently selected
	private int selection = -1;
	
	//Stores a reference to the drawing panel for loading
	private DrawingPanel drawingPanel;
	
	//A image used for the blank thumbnail
	BufferedImage defaultIcon;
	
	//Constructor
	public DoilieGalleryPanel(DrawingPanel drawingPanel) {
		//Stores a reference to the drawingPanel
		this.drawingPanel = drawingPanel;
		
		//Creates the default thumbnail
		defaultIcon = new BufferedImage(DEFAULT_ICON_SIDE_LENGTH, DEFAULT_ICON_SIDE_LENGTH, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2D = (Graphics2D) defaultIcon.getGraphics();
		g2D.setColor(new Color(0));
		g2D.fillRect(0, 0, defaultIcon.getWidth(), defaultIcon.getHeight());
	}
	
	//Initiate the gallery
	public void init() {
		this.setLayout(new BorderLayout());
		
		//Control buttons
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(3,1));
		
			//Used to save the current image on screen into the selected gallery image
			JButton saveButton = new JButton("Save");
			saveButton.setToolTipText("Save the current image to the gallery");
			buttonPanel.add(saveButton);
			
			//Used to delete the currently selected gallery image
			JButton deleteButton = new JButton("Delete");
			deleteButton.setToolTipText("Delete the selected image");
			buttonPanel.add(deleteButton);
			
			//Used to load the currently selected gallery image
			JButton loadButton = new JButton("Load");
			loadButton.setToolTipText("Load the current image");
			buttonPanel.add(loadButton);
			
			//action listener for the save button
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//If the selection is valid to save into and there is a panel to get a image to save from
					if(selection != -1 && drawingPanel != null) {
						drawings[selection] = drawingPanel.getCurDrawing();
						
						//Construct a thumbnail out of the current drawing by scaling it down to size
						BufferedImage newIcon = new BufferedImage(DEFAULT_ICON_SIDE_LENGTH, DEFAULT_ICON_SIDE_LENGTH, BufferedImage.TYPE_4BYTE_ABGR);
						Graphics2D g2D = (Graphics2D) newIcon.getGraphics();
						g2D.setColor(new Color(0));
						g2D.fillRect(0, 0, newIcon.getWidth(), newIcon.getHeight());
						
						g2D.translate(DEFAULT_ICON_SIDE_LENGTH/2, DEFAULT_ICON_SIDE_LENGTH/2);
						double scale = ((double) DEFAULT_ICON_SIDE_LENGTH/ (double) drawingPanel.getHeight());
						g2D.scale(scale, scale);
						
						drawings[selection].render(g2D);
						
						//Sets the icon
						labels[selection].setIcon(new ImageIcon(newIcon));
						labels[selection].repaint();
					}
				}
			});
			
			//action listener for the delete button
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Delete the selected drawing and sets the thumbnail to the defaultIcon
					if(selection != -1 && drawingPanel != null && drawings[selection] != null) {
						drawings[selection] = null;
						labels[selection].setIcon(new ImageIcon(defaultIcon));
					}
				}
			});
			
			//action listener for the load button
			loadButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//If the selection is valid the drawing panel exists and a drawing is stored in that selection then load it to the drawing panel
					if(selection != -1 && drawingPanel != null && drawings[selection] != null) {
						drawingPanel.setDrawing(drawings[selection]);
					}
				}
			});
			
			this.add(buttonPanel, BorderLayout.WEST);
		}
		
		
		//Logic for selecting the labels
		{
			//Creates a panel to house the labels
			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new FlowLayout());
			
			//Construct each label
			for(int i = 0; i < labels.length; i++) {
				
				labels[i] = new JLabel();
				//Add the label mouse listener
				labels[i].addMouseListener(new labelMouseListener(i));
				//set the icon to the default image
				labels[i].setIcon(new ImageIcon(defaultIcon));
				iconPanel.add(labels[i]);
				
			}
			
			this.add(iconPanel, BorderLayout.CENTER);
		}
	}
	
	//Label mouse listener
	private class labelMouseListener extends MouseAdapter {

		private int index;
		
		public labelMouseListener(int index) {
			this.index = index;
		}
		
		//When the label is clicked on highlight it with a border and change the selection number to this labels index in the labels array
		@Override
		public void mousePressed(MouseEvent e) {
			if(selection != -1) {
				labels[selection].setBorder(BorderFactory.createEmptyBorder());
			}
			selection = index;
			labels[selection].setBorder(BorderFactory.createLineBorder(new Color(0xbe0000)));
		}
	}
}
