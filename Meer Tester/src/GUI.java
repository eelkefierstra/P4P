import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.FlowLayout;

import org.opencv.highgui.VideoCapture;
import org.opencv.video.Video;

import javax.swing.*;
import javax.imageio.ImageIO;


public class GUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public JPanel panel = new JPanel();
	public JLabel label;
	public JButton button;
	
	public GUI()
	{
	    panel.setLayout(new );
		JLabel picLabel = new JLabel("");
		//VideoCapture video = new VideoCapture("C:/Users/Dudecake/Videos/Anime/Sword of the Stranger [BluRay,720p,x264,DTS] vXv/Sword of the Stranger [BluRay,720p,x264,DTS] vXv");
		
		try
		{
			image = ImageIO.read(getClass().getResource("/images/Konachan.com - 199548 atha braids brown_eyes brown_hair hat long_hair original ponytail.png"));
			picLabel = new JLabel(new ImageIcon(image.getScaledInstance(-1, 360, 0)));
	    }
		catch (IOException ex)
		{
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
		label = new JLabel("This is a label!");
	    button = new JButton();
	    button.setText("Press me");
	    panel.add(picLabel);
	    panel.add(label);
	    panel.add(button);
	    this.add(panel);
	    this.setSize(1280, 720);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	    SomeAction action = new SomeAction();
	    button.setAction(action);
	    button.setText("Press me");

	}
	
	class SomeAction extends AbstractAction
	{
		int i = 0;
		/**
		 * 
		 */
		private static final long serialVersionUID = -5706724813693343281L;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			label.setText("" + i);
			i++;
		}
		
	}
}
