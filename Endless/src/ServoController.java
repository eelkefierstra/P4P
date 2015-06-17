import java.awt.Point;


public class ServoController
{
	private PWMPin[] pins = { new PWMPin((byte)18, (short)500, (short)200, (short)300, (byte) 90), 
			new PWMPin((byte) 23, (short)500, (short)200, (short)300, (byte) 90), 
			new PWMPin((byte) 24, (short)500, (short)200, (short)300, (byte) 25), 
			new PWMPin((byte) 25, (short)500, (short)200, (short)300, (byte) 90) };
	
	public ServoController()
	{
		
	}
	
	public void Update(Point location)
	{
		if (Math.abs(location.x) > 54)
		{
			pins[2].Update((short) location.x);
		}
		if (Math.abs(location.y) > 37)
		{
			pins[3].Update((short) location.y);
		}
		if (pins[2].GetLocation() < 70)
		{
			pins[0].Update((short) -50);
		}
		else if (pins[2].GetLocation() > 110)
		{
			pins[0].Update((short) 50);
		}
		if (pins[3].GetLocation() < 35)
		{
			pins[1].Update((short) -50);
		}
		else if (pins[3].GetLocation() > 145)
		{
			pins[1].Update((short) 50);
		}
	}
	
	public int GetLocation(int i)
	{
		return pins[i].GetLocation();
	}
}
