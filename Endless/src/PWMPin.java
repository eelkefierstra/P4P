

public class PWMPin
{
	private byte pin;
	private short position;
	private short previousError;
	private short proportionalGain;
	private short derivativeGain;
	private short middlePosition = 90;
	private short maxPosition;
	private short minPosition;
	private boolean first;
	
	public PWMPin(byte pin, short startPosition, short proportionalGain, short derivativeGain, byte range)
	{
		this.pin = pin;
		this.position = startPosition;
		this.proportionalGain = proportionalGain;
		this.derivativeGain = derivativeGain;
		this.maxPosition = (short) MapPosition(middlePosition + range, 0, 180, 0, 1000);
		this.minPosition = (short) MapPosition(middlePosition - range, 0, 180, 0, 1000);
		this.previousError = 0;
		this.first = true;
	}
	
	public short GetLocation()
	{
		return (short) MapPosition(position, 0, 1000, 0, 180);
	}
	
	private void Actuate()
	{
		//System.out.println(MapPWM(position, 0, 1000, 0.074f, 0.301f));
		PWMController.WritePWM(pin, MapPWM(position, 0, 1000, 0.1f, 0.27f));
	}
	
	public void Update(short error)
	{
		if (!first)
		{
			short errorDelta = (short)(error - previousError);
			short PGain = proportionalGain;
			short DGain = derivativeGain;
			int velocity = (error * PGain + errorDelta * DGain) / 1024;
			position += velocity;
			if (position > maxPosition)
			{
				position = maxPosition;
			}
			else if (position < minPosition)
			{
				position = minPosition;
			}
		}
		else
		{
			first = false;
		}
		previousError = error;
		Actuate();
	}
	
	private float MapPWM(int x, int in_min, int in_max, float out_min, float out_max)
	{
		if (x >= in_max) System.out.println("input te hoog, input " + x + ", max " + in_max);
		float out = ((float)x - (float)in_min) * (out_max - out_min) / ((float)in_max - (float)in_min) + out_min;
		if (out >= out_max) System.out.println("to hoog, " + out);
		return out;
	}

	private int MapPosition(int x, int in_min, int in_max, int out_min, int out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public void Release()
	{
		PWMController.ReleasePin(pin);
	}
}
