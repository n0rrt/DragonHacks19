package com.controls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import java.util.ArrayList;
public class SerialParse implements SerialPortEventListener{
	SerialPort serialPort;

	private static final String PORT_NAMES[] = {"COM4"};

	private static BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	int i = 0;
	int[] xVals = new int[10];
	int[] yVals = new int[10];
	int[] zVals = new int[10];
	static ArrayList<int[]> prevValues = new ArrayList<int[]>();
	int avgX;
	int avgY;
	int avgZ;
	int totalX = 0;
	int totalY = 0;
	int totalZ = 0;
	static int runCount = 0;
	public void initialize()
	{
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		while(portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName: PORT_NAMES)
			{
				if (currPortId.getName().equals(portName))
				{
					portId = currPortId;

					break;
				}
			}
			System.out.println("Memes");
		}
		if (portId == null)
		{
			System.out.println("Could not find Port");
			return;
		}

		try
		{
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}

	}
	public void close()
	{
		synchronized(serialPort){
			if (serialPort != null)
			{
				serialPort.removeEventListener();
				serialPort.close();
			}
		}
	}
	public void serialEvent(SerialPortEvent oEvent)
	{
		synchronized(oEvent){
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			{

				try
				{

					String inputLine = null;

					if (input.ready())
					{

						inputLine = input.readLine();





						String[] chunks = inputLine.split("\t");
						if (i < 10)
						{
							xVals[i] = Integer.parseInt(chunks[0]);
							yVals[i] = Integer.parseInt(chunks[1]);
							zVals[i] = Integer.parseInt(chunks[2]);
							totalX += xVals[i];
							totalY += yVals[i];
							totalZ += zVals[i];
							i++;
//							System.out.println(totalX);
						}
						else
						{
							avgX = (int)Math.floor(totalX / xVals.length);
							avgY = (int)Math.floor(totalY / yVals.length);
							avgZ = (int)Math.floor(totalZ / zVals.length);
							int[] averages = {avgX, avgY, avgZ};
//							int[] averages = SpellCast.normalize(Integer.parseInt(chunks[0]), Integer.parseInt(chunks[1]), Integer.parseInt(chunks[2]));
							int normX = Integer.parseInt(chunks[0]) - averages[0];
							int normY = Integer.parseInt(chunks[1]) - averages[1];
							int normZ = Integer.parseInt(chunks[2]) - averages[2];
//							System.out.println("Raw");
//							SpellCast.trackSpell(Integer.parseInt(chunks[0]), Integer.parseInt(chunks[1]), Integer.parseInt(chunks[2]), Integer.parseInt(chunks[3]));
//							System.out.println("Averages");
//							SpellCast.trackSpell(averages[0], averages[1], averages[2], Integer.parseInt(chunks[3]));
//							System.out.println("Norm");
							try {
								prevValues.add(SpellCast.getPos(normX, normY, normZ));
							}
							catch(Exception e){
								int[] placeHolder = {0, 0, 0};
								prevValues.add(placeHolder);
							}
//							System.out.println(SpellCast.trackSpell(normX, normY, normZ));
							for (int x = 0; x<3; x++)
							{
								System.out.print(SpellCast.getPos(normX, normY, normZ)[x]);
								System.out.print("\t");
							}
							System.out.println();
							//System.out.println(inputLine);
//							System.out.println(chunks[0] + "\t" + chunks[1] + "\t" + chunks[2] + "\t");

							System.out.println("Run count: " + runCount);
							runCount++;
						}

					}

				}
				catch (Exception e)
				{
					System.err.println(e.toString());

				}
			}

		}
	}

	public static void serialParseInit() throws Exception
	{
		SerialParse m = new SerialParse();
		m.initialize();
		System.out.println("Started");
	}
}
