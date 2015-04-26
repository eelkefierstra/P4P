import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
	// TODO Make MeerStuff a List
	private ServoController t;
	//private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		Main p = new Main();
		// TODO Auto-generated constructor stub
		//System.out.println("Foo");
		p.t = new ServoController();
		new Thread(p.t).start();
		
		try
		{
			for(int i = 0; i < Integer.MAX_VALUE; i++)
			{
				p.t.setPWM(i);
				Thread.sleep(50);
			}
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
		p.t.dinges();
	}
}
