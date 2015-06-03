import java.awt.Point;


public class ServoController
{
	private PWMPin[] pins = { new PWMPin((byte)18, (short)500, (short)300, (short)400, (byte) 90), 
			new PWMPin((byte) 23, (short)500, (short)300, (short)400, (byte) 90), 
			new PWMPin((byte) 24, (short)500, (short)300, (short)400, (byte) 25), 
			new PWMPin((byte) 25, (short)500, (short)300, (short)400, (byte) 90) };
	
	public ServoController()
	{
		
	}
	
	public void Update(Point location)
	{
		if (Math.abs(location.x) > 100)
		{
			pins[2].Update((short) location.x);
		}
		if (Math.abs(location.y) > 75)
		{
			pins[3].Update((short) location.y);
		}
		if (pins[2].GetLocation() < 70)
		{
			pins[0].Update((short) -100);
		}
		else if (pins[2].GetLocation() > 110)
		{
			pins[0].Update((short) 100);
		}
		if (pins[3].GetLocation() < 25)
		{
			pins[1].Update((short) -100);
		}
		else if (pins[3].GetLocation() > 155)
		{
			pins[1].Update((short) 100);
		}
	}
	
	public int GetLocation(int i)
	{
		return pins[i].GetLocation();
	}
}
