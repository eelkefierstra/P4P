//import java.awt.MouseInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

public class Main
{
	private ServoController t;
	//private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		Main p = new Main();
		// TODO Auto-generated constructor stub
		//System.out.println("Foo");
		p.t = new ServoController();
		new Thread(p.t).start();
		
		//try
		//{
			/*
			int i = 1000;
			for(;;)
			{
				for(; i <= 2000; i += 100)
				{
					p.t.setPWM(i);
					//p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(1000);
				}
				for(; i >= 1000; i -= 100)
				{
					p.t.setPWM(i);
					//p.gui.label.setText("PWM = " + i);
					//p.gui.button.setText(p.gui.);
					Thread.sleep(1000);
				}
				System.out.println("dinges");
			}
			*/
			String i = System.console().readLine();
			while(i != "exit")
			{
				p.t.setPWM(Integer.parseInt(i));
				i = System.console().readLine();
			}
		//}
		/*
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
		*/
		p.t.dinges();
	}
}
