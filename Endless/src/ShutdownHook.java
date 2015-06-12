import java.util.concurrent.ScheduledExecutorService;

public class ShutdownHook 
{
	public void attachShutDownHook(final ScheduledExecutorService executor)
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				//ServoController.ResetPWM();
				executor.shutdown();
				System.out.println("exit");
			}
		});
	}
}