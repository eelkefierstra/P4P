import java.util.logging.Level;
import java.util.logging.Logger;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Stuff extends MIDlet
{
	// TODO Make MeerStuff a List
	private MeerStuff t;
	public Stuff() 
	{
		// TODO Auto-generated constructor stub
		//System.out.println("Foo");
	}

	@Override
	protected void startApp() throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub
		t = new MeerStuff();
		new Thread(t).start();

		try
		{
			for(int i = 0; i < Integer.MAX_VALUE; i++)
			{
				t.setPWM(i);
				Thread.sleep(50);
			}
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			Logger.getLogger(Stuff.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@Override
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException 
	{
		// TODO Auto-generated method stub
		t.dinges();
	}

}
