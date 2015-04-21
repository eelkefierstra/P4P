import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
/*
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
*/

public class Stuff extends MIDlet
{
	private MeerStuff t;
	/*
	// create gpio controller
    final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
    final GpioPinAnalogOutput pin1 = gpio.provisionAnalogOutputPin(RaspiPin.GPIO_00, "OtherLED");
	*/
	public Stuff() 
	{
		// TODO Auto-generated constructor stub
		System.out.println("Foo");
		t = new MeerStuff();
		new Thread(t).start();
		/*
        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);
        */
		for(byte i = 0; i < 250; i += 50)
		{
			t.setPWM(i);
		}
	}

	@Override
	protected void startApp() throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException 
	{
		// TODO Auto-generated method stub
		t.dinges();
	}

}
