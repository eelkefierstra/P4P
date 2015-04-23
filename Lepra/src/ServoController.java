import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.opencv.core.Point;



import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.pwm.*;
/*
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
*/
public class ServoController implements Runnable
{
	private GPIOPin pin; 
	private boolean running;
	private int i;
	private int[] servos = { 0, 0, 0, 0 };
	
	// throws some random exception, wut?
	// create gpio controller
    //private final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    //final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
    //private final GpioPinPwmOutput[] pins = { gpio.provisionPwmOutputPin(RaspiPin.GPIO_01) };
	
	
	public ServoController()
	{
		running = true;
		i = 0;
		try
		{
			pin = DeviceManager.open(7);
		}
		catch(Exception e)
		{
			Logger.getLogger(ServoController.class.getName()).log(Level.SEVERE, null, e);
		}
        // set shutdown state for this pin
        //pin.setShutdownOptions(true, PinState.LOW);
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
				pin.setValue(true);
				Thread.sleep(500);
				pin.setValue(false);
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
        try
        {
        	//gpio.shutdown();
            pin.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServoController.class.getName()).log(Level.SEVERE, null, ex);
        }

	}
}
