
public class FPSCounter extends Thread
{
	private long lastTime;
	private double fps;
	
	@Override
	public void run()
	{
		for(;;)
		{
			lastTime = System.nanoTime();
			try
			{
				Thread.sleep(5000);
			}
			catch (Exception e)
			{ }
			fps = 1000000000.0 / (System.nanoTime() - lastTime);
			lastTime = System.nanoTime();
		}
	}
	
	public double GetFPS()
	{
		return fps;
	}
}
