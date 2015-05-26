

public class PWMPin
{
	byte pin;
	short position;
	short error;
	short previousError;
	short proportionalGain;
	short derivativeGain;
	short maxPosition;
	short minPosition;
	boolean first;
	
	public PWMPin(byte pin)
	{
		this.pin = pin;
		first = true;
	}
	
	public PWMPin(byte pin, short startPosition, short proportionalGain, short derivativeGain)
	{
		this.pin = pin;
		this.position = startPosition;
		this.proportionalGain = proportionalGain;
		this.derivativeGain = derivativeGain;
		this.maxPosition = 1000;
		this.minPosition = 0;
		this.previousError = 0;
		this.first = true;
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
