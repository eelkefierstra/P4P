//import java.awt.MouseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

public class Main
{
	private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		@SuppressWarnings("unused")
		Main p = new Main();
		ShutdownHook shutdown = new ShutdownHook();
	    shutdown.attachShutDownHook();
		int x = 1;
		try
		{
			float i = 0.025f;
			for(;;)
			{
				for(; i <= 0.125f; i += 0.000125f)
				{
					ServoController.WritePWM(23, i);
					System.out.println("PWM = " + i);
					p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(50);
				}
				for(; i >= 0.01f; i -= 0.000125f)
				{
					ServoController.WritePWM(23, i);
					System.out.println("PWM = " + i);
					p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(50);
				}
				System.out.println("Loop" + x);
				x++;
			}
		}
		catch (InterruptedException e)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
