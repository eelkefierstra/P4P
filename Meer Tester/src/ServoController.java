//import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.opencv.core.Point;
/*
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
*/
public class ServoController// implements Runnable
{
	private static final File servos = new File("/dev/pi-blaster");
	private static final byte[] pins = { 23 };
	
	public static void WritePWM(int pin, float percentage)
	{
		if (Arrays.asList(pins).contains(pin))
		{
			throw new IllegalArgumentException("Pin is outside range");
		}
		try
		{
			OutputStream out = new FileOutputStream(servos);
			OutputStreamWriter writer = new OutputStreamWriter(out);
			writer.write(pin+"="+percentage+"\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public static void ResetPWM()
	{
		try
		{
			for(byte i = 0; i < pins.length; i++)
			{
				OutputStream out = new FileOutputStream(servos);
				OutputStreamWriter writer = new OutputStreamWriter(out);
				writer.write("release"+ pins[i] +"\n");
				writer.flush();
				writer.close();
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	/*
	//private boolean running;
	private int[] servos = { 0, 0, 0, 0 };
	
	// create gpio controller
    private final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    private final GpioPinPwmOutput[] pins = { gpio.provisionPwmOutputPin(RaspiPin.GPIO_01) };
	
	
	public ServoController()
	{
		//running = true;
        // set shutdown state for this pin
		/*
		for(int i = 0; i < pins.length; i++)
		{
			pins[i].setShutdownOptions(true, PinState.LOW);
		}
		*/
	//}
	/*
	public void setPWM(int[] x)
	{
		System.out.println(x[0]);
		for(int i = 0; i < x.length; i++)
		{
			pins[i].setPwm(x[i]);
		}
	}
	/*
	public void run()
	{
		try
		{
			for(;;)
			{
				//System.out.println("Foo " + servos[0]);
				pins[0].setPwm(servos[0]);
				Thread.sleep(1000);
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
	/*
	private void setServos()
	{
		/*
		for(byte i = 0; i < pins.length; i++)
		{
			pins[i].setPwm(servos[i]);
		}
		*/
	/*}
	
	public void dinges()
	{
		//running = false;
        gpio.shutdown();
	}
	*/
}