import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.opencv.core.Point;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class ServoController implements Runnable
{
	private boolean running;
	private int i;
	private int[] servos = { 0, 0, 0, 0 };
	
	// create gpio controller
    private final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    final GpioPinDigitalOutput[] pins = { gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH) };
    //private final GpioPinPwmOutput[] pins = { gpio.provisionPwmOutputPin(RaspiPin.GPIO_01) };
	
	
	public ServoController()
	{
		running = true;
		i = 0;
        // set shutdown state for this pin
		/*
		for(int i = 0; i < pins.length; i++)
		{
			pins[i].setShutdownOptions(true, PinState.LOW);
		}
		*/
	}
	
	public void setPWM(int i)
	{
		this.i = i;
	}
	
	public void run()
	{
		try
		{
			while(running)
			{
				System.out.println("Foo " + i);
				//pins[0].setPwm(i);
				Thread.sleep(500);
			}
		}
		catch(Exception e)
		{
			Logger.getLogger(ServoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	/*
	private void updateServos(Point[] x)
	{
		for(byte i = 0; i < x.length; i++)
		{
			//TODO Implement stuff that translates the points to a PWM signal
			//representing the motion of the servos 
		}
	}
	*/
	private void setServos()
	{
		/*
		for(byte i = 0; i < pins.length; i++)
		{
			pins[i].setPwm(servos[i]);
		}
		*/
	}
	
	public void dinges()
	{
		running = false;
        gpio.shutdown();
	}
}
