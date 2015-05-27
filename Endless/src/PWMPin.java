

public class PWMPin
{
	private byte pin;
	private short position;
	private short error;
	private short previousError;
	private short proportionalGain;
	private short derivativeGain;
	private short middlePosition = 90;
	private short maxPosition;
	private short minPosition;
	private boolean first;
	
	public PWMPin(byte pin)
	{
		this.pin = pin;
		first = true;
	}
	
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
	
	public void SetPWM(float value)
	{
		PWMController.WritePWM(pin, value);
	}
	
	public void Actuate()
	{
		System.out.println(MapPWM(position, 0, 1000, 0.025f, 0.125f));
		//PWMController.WritePWM(pin, MapPWM(position, 0, 180, 0.025f, 0.125f));
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
			this.error = error;
			first = false;
		}
		previousError = error;
		Actuate();
	}
	
	private float MapPWM(int x, int in_min, int in_max, float out_min, float out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
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
