import java.util.concurrent.ScheduledExecutorService;

public class ShutdownHook 
{
	ScheduledExecutorService executor;
	
	public void attachShutDownHook(ScheduledExecutorService executor)
	{
		this.executor = executor;
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