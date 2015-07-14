import java.awt.Point;


public class ServoController
{
	private PWMPin[] pins = { 
			new PWMPin((byte) 18, (short)500, (short)200, (short)300, (byte) 90), 
			new PWMPin((byte) 23, (short)500, (short)200, (short)300, (byte) 25), 
			new PWMPin((byte) 24, (short)500, (short)200, (short)300, (byte) 90), 
			new PWMPin((byte) 25, (short)500, (short)200, (short)300, (byte) 90) 
			};
	
	public ServoController()
	{
		
	}
	
	public void Update(Point location)
	{
		// Checks if the location is outside the deadzone
		if (Math.abs(location.x) > 54)
		{
			// Updates the servo in the head
			pins[2].Update((short) location.x);
		}
		// Checks if the location is outside the deadzone
		if (Math.abs(location.y) > 37)
		{
			// Updates the servo in the head
			pins[3].Update((short) location.y);
		}
		// Checks if the third servo is almost on the left edge of its reach 
		if (pins[2].GetLocation() < 70)
		{
			// Moves the arm so the third servo doesn't reach the end of it's reach
			pins[0].Update((short) -50);
		}
		// Checks if the third servo is almost on the right edge of its reach 
		else if (pins[2].GetLocation() > 110)
		{
			// Moves the arm so the third servo doesn't reach the end of it's reach
			pins[0].Update((short) 50);
		}
		// Checks if the fourth servo is almost on the down edge of its reach 
		if (pins[3].GetLocation() < 35)
		{
			// Moves the arm so the fourth servo doesn't reach the end of it's reach
			pins[1].Update((short) -50);
		}
		// Checks if the fourth servo is almost on the upper edge of its reach 
		else if (pins[3].GetLocation() > 145)
		{
			// Moves the arm so the fourth servo doesn't reach the end of it's reach
			pins[1].Update((short) 50);
		}
	}
	
	// Returns the current location of the given servo
	public int GetLocation(int i)
	{
		return pins[i].GetLocation();
	}
}
