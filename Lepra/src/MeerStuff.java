import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class MeerStuff implements Runnable
{
	
	private boolean running;
	public byte i;
	// create gpio controller
    final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    //final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
    final GpioPinPwmOutput pin1 = gpio.provisionPwmOutputPin(RaspiPin.GPIO_01);

	
	public MeerStuff()
	{
		running = true;
		i = 0;
        // set shutdown state for this pin
        //pin.setShutdownOptions(true, PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);

	}
	
	public void setPWM(byte i)
	{
		this.i = i;
	}
	
	public void run()
	{
		//while(running)
		//{
			System.out.println("Foo " + i);
			pin1.setPwm(i);
		//}
	}
	
	public void dinges()
	{
		running = false;
        try
        {
        	gpio.shutdown();
            //pin.close();
        }
        finally
        {
        	
        }
        /*
        catch (IOException ex)
        {
            Logger.getLogger(Tester.class.getName()).
                log(Level.SEVERE, null, ex);
        }*/

	}
}
