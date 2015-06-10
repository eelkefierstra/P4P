import java.util.concurrent.Callable;


public class SomeCallableTask implements Callable<String>
{
	String file;
	
	public SomeCallableTask(String file)
	{
		this.file = file;
	}
	
	public String call() throws Exception
	{
		return "/audio/" + file;
	}
}
