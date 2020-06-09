package stitch;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class CrossStitch extends JFrame {

	private JPanel contentPane;
	private static CrossStitch frame;
	File file;
	JFileChooser fc;
	private JTextField widthTextField;
	private JTextField colorTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new CrossStitch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CrossStitch() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		ImageIcon image = new ImageIcon("src/stitch/images/spool.jpg");
		this.setIconImage(image.getImage());
		
		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBounds(0, 11, 434, 228);
		optionPanel.setLayout(null);
		contentPane.add(optionPanel);
		
		JButton chooseFileButton = new JButton("Choose File");
		chooseFileButton.setFocusPainted(false);
		chooseFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 //Set up the file chooser.
		        if (fc == null) {
		            fc = new JFileChooser();
		 
		        //Add a custom file filter and disable the default
		        //(Accept All) file filter.
		            fc.addChoosableFileFilter(new ImageFilter());
		            fc.setAcceptAllFileFilterUsed(false);
		 
		        //Add custom icons for file types.
		            fc.setFileView(new ImageFileView());
		 
		        //Add the preview pane.
		            fc.setAccessory(new ImagePreview(fc));
		        }
		 
		        //Show it.
		        int returnVal = fc.showDialog(CrossStitch.this,
		                                      "Attach");
		 
		        //Process the results.
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            file = fc.getSelectedFile();
		            paintImage(file);
		            //log.append("Attaching file: " + file.getName()
		                     //  + "." + newline);
		        } else {
		            //log.append("Attachment cancelled by user." + newline);
		        }
		        //log.setCaretPosition(log.getDocument().getLength());
		 
		        //Reset the file chooser for the next time it's shown.
		        fc.setSelectedFile(null);
			}
		});
		chooseFileButton.setBounds(153, 194, 113, 23);
		optionPanel.add(chooseFileButton);
		//TODO refactor
		widthTextField = new JTextField();
		widthTextField.setBounds(113, 62, 86, 20);
		optionPanel.add(widthTextField);
		widthTextField.setColumns(10);
		
		JButton createPatternButton = new JButton("Create Pattern");
		createPatternButton.setBounds(141, 122, 137, 23);
		optionPanel.add(createPatternButton);
		
		JLabel widthLabel = new JLabel("Width of Pattern");
		widthLabel.setBounds(113, 37, 113, 14);
		optionPanel.add(widthLabel);
		
		colorTextField = new JTextField();
		colorTextField.setBounds(227, 62, 86, 20);
		optionPanel.add(colorTextField);
		colorTextField.setColumns(10);
		
		JLabel colorLabel = new JLabel("Number of Colors");
		colorLabel.setBounds(227, 37, 113, 14);
		optionPanel.add(colorLabel);
		//TODO refactor
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setIcon(new ImageIcon(CrossStitch.class.getResource("/stitch/images/save.png")));
		fileMenu.add(saveMenuItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		fileMenu.add(exitMenuItem);
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		JMenuItem tipContentMenuItem = new JMenuItem("Tips");
		helpMenu.add(tipContentMenuItem);
		
		JMenuItem crossStitchMenuItem = new JMenuItem("Cross Stitch Help");
		helpMenu.add(crossStitchMenuItem);
		
		JMenuItem howMenuItem = new JMenuItem("Instructions");
		helpMenu.add(howMenuItem);
		return menuBar;
	}
	
	private void paintImage(File f) {
		if (file != null) {
			BufferedImage image;
			try {
				image = ImageIO.read(f);
				int desiredWidth = getDesiredWidth();
				if (desiredWidth <= 0) {
					desiredWidth = 100;
				}
				int[][] newImage = convertImage(resizeImage(image, desiredWidth)); //TODO replace 100 with user choice
				Color[][] colorImage = convertColor(newImage);
				JFrame newFrame = new JFrame("Image");
				newFrame.getContentPane().setLayout(new BorderLayout());
				newFrame.setSize(colorImage[0].length*6, colorImage.length*7);
				
				newFrame.getContentPane().add(new FrameForCanvas(colorImage), BorderLayout.CENTER);
				newFrame.setVisible(true);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
	}
	
	private static int[][] convertImage(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	      int[][] result = new int[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else {
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }

	      return result;
	   }
	
	private BufferedImage resizeImage(BufferedImage image, int newWidth) {
		int newHeight = (int)Math.round(((double)newWidth)*image.getHeight()/image.getWidth());
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, newWidth, newHeight, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		return resizedImage;
	}
	
	private Color[][] convertColor(int[][] image) {
		Color[][] colorImage = new Color[image.length][image[0].length];
		for (int i=0; i<image.length; i++) {
			for (int j=0; j<image[0].length; j++) {
				colorImage[i][j] = new Color(image[i][j]);
			}
		}
		return colorImage;
	}
	
	private int getDesiredWidth() {
		String widthString = widthTextField.getText();
		try {
			int width = Integer.parseInt(widthString);
			return width;
		}
		catch (Exception e) {
			return 0;
		}
	}
}
