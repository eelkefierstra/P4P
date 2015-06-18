
public class ShutdownHook 
{
	public void attachShutDownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				PWMController.ReleasePin();
				System.out.println("exit");
			}
		});
	}
}