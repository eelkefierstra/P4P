import java.util.logging.Level;
import java.util.logging.Logger;

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
	// TODO Make MeerStuff a List
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
		// throws some random exception, wut?
		// TODO Auto-generated constructor stub
		//System.out.println("Foo");
		/*
        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);
        */
	}

	@Override
	protected void startApp() throws MIDletStateChangeException
	{
		// TODO Auto-generated method stub
		t = new MeerStuff();
		new Thread(t).start();

		try
		{
			for(int i = 0; i < Integer.MAX_VALUE; i++)
			{
				t.setPWM(i);
				Thread.sleep(50);
			}
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			Logger.getLogger(Stuff.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@Override
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException 
	{
		// TODO Auto-generated method stub
		t.dinges();
	}

}
