import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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
public class DoilieGallaryPanel extends JPanel {

	private static int DEFAULT_ICON_SIDE_LENGTH = 100;
	
	private JLabel[] labels = new JLabel[12];
	private Drawing[] drawings = new Drawing[12];
	private int selection = -1;
	
	private DrawingPanel drawingPanel;
	
	BufferedImage defaultIcon;
	
	public DoilieGallaryPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		
		defaultIcon = new BufferedImage(DEFAULT_ICON_SIDE_LENGTH, DEFAULT_ICON_SIDE_LENGTH, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2D = (Graphics2D) defaultIcon.getGraphics();
		g2D.setColor(new Color(0));
		g2D.fillRect(0, 0, defaultIcon.getWidth(), defaultIcon.getHeight());
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		
		//Buttons
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(3,1));
			
			JButton saveButton = new JButton("Save");
			saveButton.setToolTipText("Save the current image to the gallary");
			buttonPanel.add(saveButton);
			
			JButton deleteButton = new JButton("Delete");
			deleteButton.setToolTipText("Delete the selected image");
			buttonPanel.add(deleteButton);
			
			JButton loadButton = new JButton("Load");
			loadButton.setToolTipText("Load the current image");
			buttonPanel.add(loadButton);
			
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(selection != -1 && drawingPanel != null) {
						
						drawings[selection] = drawingPanel.getCurDrawing();
						BufferedImage newIcon = new BufferedImage(DEFAULT_ICON_SIDE_LENGTH, DEFAULT_ICON_SIDE_LENGTH, BufferedImage.TYPE_4BYTE_ABGR);
						Graphics2D g2D = (Graphics2D) newIcon.getGraphics();
						g2D.setColor(new Color(0));
						g2D.fillRect(0, 0, newIcon.getWidth(), newIcon.getHeight());
						
						drawings[selection].render(DEFAULT_ICON_SIDE_LENGTH/2, DEFAULT_ICON_SIDE_LENGTH/2, (Graphics2D) newIcon.getGraphics(),  ((double) DEFAULT_ICON_SIDE_LENGTH/ (double) drawingPanel.getHeight()));
						labels[selection].setIcon(new ImageIcon(newIcon));
						labels[selection].repaint();
					}
				}
			});
			
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(selection != -1 && drawingPanel != null && drawings[selection] != null) {
						drawings[selection] = null;
						labels[selection].setIcon(new ImageIcon(defaultIcon));
					}
				}
			});
			
			loadButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(selection != -1 && drawingPanel != null && drawings[selection] != null) {
						drawingPanel.setDrawing(drawings[selection]);
					}
				}
			});
			
			this.add(buttonPanel, BorderLayout.WEST);
		}
		
		
		//Labels
		{
			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new FlowLayout());
			
			for(int i = 0; i < labels.length; i++) {
				
				labels[i] = new JLabel();
				labels[i].addMouseListener(new labelMouseListener(i));
				labels[i].setBackground(new Color(0));
				labels[i].setIcon(new ImageIcon(defaultIcon));
				iconPanel.add(labels[i]);
				
			}
			
			this.add(iconPanel, BorderLayout.CENTER);
		}
	}
	
	
	private class labelMouseListener extends MouseAdapter {

		private int index;
		
		public labelMouseListener(int index) {
			this.index = index;
		}
		
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
