import java.awt.Point;
import java.awt.image.BufferedImage;


public class DroneTracker
{
	native void Setup();
	
	native Point Track();
	
	native BufferedImage GetFeed();
	
	native BufferedImage GetThresh();
	
	native BufferedImage GetHSV();
	
	public DroneTracker()
	{
		Setup();
	}
}
