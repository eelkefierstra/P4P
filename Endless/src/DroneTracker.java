import java.awt.Point;

public class DroneTracker
{
	private Point loc;
	
	native void Setup();
	
	native void Track();
	
	native byte[] GetFeed();
	
	native byte[] GetThresh();
	
	native byte[] GetHSV();
	
	private native int GetX();
	
	private native int GetY();
	
	public DroneTracker()
	{
		loc = new Point();
		Setup();
	}
	
	public Point GetLoc()
	{
		loc.x = GetX();
		loc.y = GetY();
		return loc;
	}
}
