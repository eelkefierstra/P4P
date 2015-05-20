import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServoController
{
	private static final File servos = new File("/dev/pi-blaster");
	private static final byte[] pins = { 18, 23, 24, 25 };
	private static final byte[] maxActuation = { 90, 90, 90, 25 };
	private static final byte[] minActuation = { -90, -90, -90, -25 };
	
	private ServoController()
	{
		
	}
	
	public static void WritePWM(int pin, float percentage)
	{
		if (!Arrays.asList(pins).contains(pin))
		{
			String i = (pin == 23) + " Positive";
			System.out.println(i);
			//throw new IllegalArgumentException("Pin" + pin + " is outside range");
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
				writer.write("release "+ pins[i] +"\n");
				writer.flush();
				writer.close();
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
