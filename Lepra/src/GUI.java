import java.awt.FlowLayout;
import javax.swing.*;


public class GUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel = new JPanel();
	
	public GUI()
	{
	    panel.setLayout(new FlowLayout());
	    JLabel label = new JLabel("This is a label!");
	    JButton button = new JButton();
	    button.setText("Press me");
	    panel.add(label);
	    panel.add(button);
	    this.add(panel);
	    this.setSize(300, 300);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
}
