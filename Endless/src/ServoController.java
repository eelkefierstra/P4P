import java.awt.Point;


public class ServoController
{
	private PWMPin[] pins = { new PWMPin((byte)18, (short)500, (short)300, (short)400), new PWMPin((byte) 23), new PWMPin((byte) 24), new PWMPin((byte) 25) };
	
	public ServoController()
	{
	}
	
	public void Update(Point location)
	{
		if (Math.abs(location.x) > 250)
		{
			pins[0].Update((short) location.x);
		}
		if (Math.abs(location.y) > 125)
		{
			pins[1].Update((short) location.y);
		}
	}
}
