import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Polizeilicht {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("<--Pi4J--> Polizeilicht");

        // GPIO Controller
        final GpioController gpio = GpioFactory.getInstance();

        // PIN 1 is red, Pin 2 blue, both initially off
        final GpioPinDigitalOutput pinRed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Red", PinState.LOW);
        final GpioPinDigitalOutput pinBlue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Blue", PinState.LOW);

        // Button to stop, wired to ground, so we need pull-up
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);

        do {
            // flicker blue
            pinBlue.high();
            Thread.sleep(50);
            pinBlue.low();
            Thread.sleep(50);
            pinBlue.high();
            Thread.sleep(50);;
            pinBlue.low();
            Thread.sleep(100);
            // flicker red
            pinRed.high();
            Thread.sleep(50);
            pinRed.low();
            Thread.sleep(50);
            pinRed.high();
            Thread.sleep(50);;
            pinRed.low();
            Thread.sleep(100);
        } while (myButton.isHigh());
        gpio.shutdown();
    }
}