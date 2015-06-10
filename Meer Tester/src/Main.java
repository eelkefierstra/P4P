import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main
{
	private GUI gui = new GUI();
	
    public static void main(String[] args)
    {
		Main p = new Main();
		ShutdownHook shutdown = new ShutdownHook();
	    shutdown.attachShutDownHook();
	    String[] files = null;
	    FPSCounter counter = new FPSCounter();
	    /*
		try
		{
			java.io.File file = new java.io.File(p.getClass().getResource("/audio/").toURI());
	    	files = file.list();
		}
		catch (Exception ex)
		{
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}

	    ExecutorService executor = Executors.newFixedThreadPool(3);
	    ArrayList<Future<String>> futurelist = new ArrayList<Future<String>>();
	    for (String file : files)
	    {
		    futurelist.add(executor.submit(new SomeCallableTask(file)));
		}
		try
		{
			int i = 0;
		  	for (Future<String> future : futurelist)
		  	{
		    	files[i] = future.get();
		    	i++;
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
		*/
		ServoController servos = new ServoController();
	    //AudioStuff audio = new AudioStuff(files);
	    //audio.SetClip(0);
	    int x = 0;
		try
		{
			//audio.PLayClip();
			float i = 0.1875f;
			PWMPin pin = new PWMPin((byte)25);
		    counter.start();
			for(;;)
			{
				p.gui.setTitle(p.getLocationRelativeTo().toString());
				servos.Update(p.getLocationRelativeTo());
				
				for(; i <= 0.26f; i += 0.0125f)
				{
					pin.SetPWM(i);
					System.out.println("PWM = " + i);
					p.gui.setTitle("X = " + (p.getXRelativeTo() + " Y = " + p.getYRelativeTo()));
					//p.gui.label.setText("PWM = " + i);
					Thread.sleep(500);
				}
				i = 0.26f;
				for(; i >= 0.1875f; i -= 0.0125f)
				{
					pin.SetPWM(i);
					System.out.println("PWM = " + i);
					//p.gui.label.setText("PWM = " + i);
					Thread.sleep(500);
				}
				i = 0.025f;
				System.out.println("Loop" + x);
				x++;
				//audio.SetClip(x);
				//audio.PLayClip();
				
				counter.interrupt();
				p.gui.label.setText(counter.GetFPS()+"FPS");
				Thread.sleep(100);
			}
		}
		catch (InterruptedException e)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
        
    public Point getLocationRelativeTo()
    {
        int x = (gui.getX() - MouseInfo.getPointerInfo().getLocation().x) - (gui.getWidth() / 2) * -1;
        int y = (gui.getY() - MouseInfo.getPointerInfo().getLocation().y) + (gui.getHeight() / 2);
        return new Point(x, y);
    }
    
    public int getXRelativeTo()
    {
        int x = gui.getX() - MouseInfo.getPointerInfo().getLocation().x;
        return (x + (gui.getWidth() / 2)) * -1;
    }
    
    public int getYRelativeTo()
    {
        int y = gui.getY() - MouseInfo.getPointerInfo().getLocation().y;
        return y + gui.getHeight() / 2;
    }
}
