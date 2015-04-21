package master;
/*
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import java.io.IOException;
*/
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.midlet.MIDlet;

public class Tester extends MIDlet
{
	//GPIOPin pin;
	// create gpio controller
    final GpioController gpio = GpioFactory.getInstance();
    
    // provision gpio pin #01 as an output pin and turn on
    final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
    final GpioPinAnalogOutput pin1 = gpio.provisionAnalogOutputPin(RaspiPin.GPIO_00, "OtherLED");

    public void startApp()
    {
        // set shutdown state for this pin
        pin.setShutdownOptions(true, PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);

        System.out.println("--> GPIO state should be: ON");
    	

        try 
        {
            //pin = (GPIOPin)DeviceManager.open(7);
            System.out.println("----------------------------------------");
            Thread.sleep(5000);
 
            for (int i = 0; i < 20; i++) 
            {	 
                 System.out.println("Setting pin to true...");
                 pin.low();
                 Thread.sleep(10000);
                 System.out.println("Setting pin to false...");
                 pin.high();
                 Thread.sleep(5000);
                 System.out.println("----------------------------------------");
            }
         }/*
         catch (IOException ex)
         {
             Logger.getLogger(Tester.class.getName()).
                log(Level.SEVERE, null, ex);
         }*/
         catch (InterruptedException ex) 
         {
            Logger.getLogger(Tester.class.getName()).
                log(Level.SEVERE, null, ex);
         }
    }
 
    public void pauseApp()
    {
    	
    }
 
    public void destroyApp(boolean unconditional)
    {
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
