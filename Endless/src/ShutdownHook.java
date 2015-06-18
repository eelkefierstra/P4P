
public class ShutdownHook 
{
	public void attachShutDownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				ServoController.ResetPWM();
				System.out.println("exit");
			}
		});
	}
}