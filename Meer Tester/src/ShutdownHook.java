import java.io.IOException;
import java.net.ServerSocket;

public class ShutdownHook 
{
	public void attachShutDownHook(ServerSocket server)
	{
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				//ServoController.ResetPWM();
				try
				{
					System.out.println("Closing server");
					server.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				System.out.println("exit");
			}
		});
	}
}