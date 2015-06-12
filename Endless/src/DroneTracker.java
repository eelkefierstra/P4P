import java.awt.Point;

public class DroneTracker
{
	private Point loc;
	
	public native void Setup();
	
	public native boolean Track();
	
	public native int SendFeed();
	
	private native int GetX();
	
	private native int GetY();
	
	public DroneTracker()
	{
		loc = new Point();
	}
	
	public Point GetLoc()
	{
		loc.x = GetX();
		loc.y = GetY();
		return loc;
	}
}
