import java.awt.Point;

public class allus
{
	ServoController servo = new ServoController();
	
	if(location.x != servo.PIXY_X_CENTER && location.y == servo.PIXY_Y_CENTER)
	{
		if(location.x < servo.PIXY_X_CENTER && location.y == servo.PIXY_Y_CENTER)
		{
			
		}
		if(location.x > servo.PIXY_X_CENTER && location.y == servo.PIXY_Y_CENTER)
		{
			
		}
	}
	if(location.y != servo.PIXY_Y_CENTER && location.y == servo.PIXY_X_CENTER)
	{
		if(location.y < servo.PIXY_Y_CENTER && location.y == servo.PIXY_X_CENTER)
		{
			
		}
		if(location.y > servo.PIXY_Y_CENTER && location.y == servo.PIXY_X_CENTER)
		{
			
		}
	}
	else if(location.x != servo.PIXY_X_CENTER && location.y != servo.PIXY_Y_CENTER)
	{
		if(location.x > PIXY_X_CENTER && location.y > PIXY_Y_CENTER)
		{
			
		}
		if(location.x < PIXY_X_CENTER && location.y > PIXY_Y_CENTER)
		{
			
		}
		if(location.x < PIXY_X_CENTER && location.y < PIXY_Y_CENTER)
		{
			
		}
		if(location.x > PIXY_X_CENTER && location.y < PIXY_Y_CENTER)
		{
			
		}
	}
	
}