import java.awt.Point;


public class ServoController
{
	private PWMPin[] pins = { new PWMPin((byte)18, (short)500, (short)400, (short)300), new PWMPin((byte) 23), new PWMPin((byte) 24), new PWMPin((byte) 25) };
	public short PIXY_MIN_X             =    0;
	public short PIXY_MAX_X             =  319;
	public short PIXY_MIN_Y             =    0;
	public short PIXY_MAX_Y             =  199;

	public short PIXY_X_CENTER          =  (short) ((PIXY_MAX_X-PIXY_MIN_X) / 2);
	public short PIXY_Y_CENTER          =  (short) ((PIXY_MAX_Y-PIXY_MIN_Y) / 2);
	public short PIXY_RCS_MIN_POS       =    0;
	public short PIXY_RCS_MAX_POS       =  180;
	public short PIXY_RCS_CENTER_POS    =  (short) ((PIXY_RCS_MAX_POS-PIXY_RCS_MIN_POS) / 2);
	private boolean odd;
	
	public ServoController()
	{
		odd = true;
	}
	
	public void Update(Point location)
	{
		if (odd)
		{
			short xError = (short) (PIXY_X_CENTER - location.x);
			short yError = (short) (location.y - PIXY_Y_CENTER);
			
			if (Math.abs(xError) > 250)
			{
				pins[0].Update(xError);
			}
			if (Math.abs(yError) < 125)
			{
				pins[1].Update(yError);
			}
		}
		odd = !odd;
	}
}
