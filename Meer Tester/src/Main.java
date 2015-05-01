//import java.awt.MouseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

public class Main
{
	//private ServoController t;
	//private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		//Main p = new Main();
		ShutdownHook shutdown = new ShutdownHook();
	    shutdown.attachShutDownHook();
		//System.out.println("Foo");
		//p.t = new ServoController();
		//new Thread(p.t).start();
		
		try
		{
			float i = 0.025f;
			//int[] x = { 0 };
			for(;;)
			{
				for(; i <= 0.125f; i += 0.005f)
				{
					//ServoController.WritePWM(23, i);
					//x[0] = i;
					//p.t.setPWM(x);
					//p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(1000);
				}
				for(; i >= 0.025f; i -= 0.005f)
				{
					//ServoController.WritePWM(23, i);
					//x[0] = i;
					//p.t.setPWM(x);
					//p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(1000);
				}
				System.out.println("dinges");
			}
			
		/*
			String i = System.console().readLine();
			while(i != "exit")
			{
				int[] x = { Integer.parseInt(i) };
				p.t.setPWM(x);
				i = System.console().readLine();
			}
			*/
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
		
		//p.t.dinges();
	}
}
