import java.awt.Point;

public class DroneTracker
{
	native void Setup();
	
	native void Track();
	
	native byte[] GetFeed();
	
	native byte[] GetThresh();
	
	native byte[] GetHSV();
	
	public DroneTracker()
	{
		Setup();
	}
}
