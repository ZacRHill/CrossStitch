package stitch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class FrameForCanvas extends JPanel {
	Color[][] image;

	/**
	 * Create the panel.
	 */
	public FrameForCanvas(Color[][] image) {
		this.image = image;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i=0; i<image.length; i++) {
			for (int j=0; j<image[0].length; j++) {
				g.setColor(image[i][j]);
				g.drawRect(j*5, i*5, 5, 5);
		        g.fillRect(j*5, i*5, 5, 5);
			}		
		}
		/*
		g.setColor(Color.BLACK);
        g.drawRect(10, 10, 10, 10);
        g.fillRect(10, 10, 10, 10);
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Serif", Font.PLAIN, 12);
        
        g.setColor(Color.RED);
        g.drawRect(20, 10, 10, 10);
        g.fillRect(20, 10, 10, 10);
        
        g2d.setFont(font);
        g2d.setColor(new Color(255,255,255));
        g2d.drawString("x", 13, 18);
        g2d.drawString("o", 23, 18);
        */
	}
	

}
