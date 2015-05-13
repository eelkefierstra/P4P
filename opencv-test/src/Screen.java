import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5826058727264357858L;
	public JLabel screen = new JLabel("hvuibvisbvcisvbisvbh");
	
	public Screen()
	{
		this.add(screen);
		this.setSize(1280, 720);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
