package com.acorp.overdew;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.acorp.overdew.utils.Signal;
import com.acorp.overdew.utils.Utils;
import com.acorp.overdew.utils.devices.Outlet;
import com.github.mob41.blapi.BLDevice;
import com.github.mob41.blapi.RM2Device;
import com.github.mob41.blapi.mac.Mac;

public class OverDoer {
	
	public static BLDevice dev;
	public static RM2Device rm2;
	
	public static void main(String[] args) throws Exception {
		try {
			System.out.println("Connecting to (10.0.0.169 | 34:EA:34:CC:E5:6A)");
			dev = new RM2Device("10.0.0.169", new Mac("34:EA:34:CC:E5:6A"));
			boolean success = dev.auth();
			System.out.println("Auth status: " + (success ? "Success!" : "FAILED!"));
			if (dev instanceof RM2Device){
				rm2 = (RM2Device) dev;
				if (args.length > 0) {
					// send+lavalamp+on
					if (args[0].contains("send")) {
						String[] cmd = args[0].split("\\+");
						String id = cmd[1];
						if (cmd[2].equalsIgnoreCase("on")) {
							Signal on = new Signal(getByte("/home/overhome/rf-data/" + id + "/on.txt"));
							on.sendData(rm2);
							System.out.println("Sent packet to turn on to " + id);
						}
						else {
							Signal off = new Signal(getByte("/home/overhome/rf-data/" + id + "/off.txt"));
							off.sendData(rm2);
							System.out.println("Sent packet to turn off to " + id);
						}
					}
					else if (args[0].contains("forcecode")) {
						String[] cmd = args[0].split("\\+");
						Signal s = new Signal(args[1]);
						s.sendData(rm2);
						System.out.println("Sent: " + s.toString());
					}
					else if (args[0].contains("learn")) {
						String[] cmd = args[0].split("\\+");
						learn(cmd[1]);
					}
				}
			} else {
				System.out.println("The \"dev\" is not a RM2Device instance.");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void learn(String id) throws Exception {
		System.out.println("Push your ON button for your " + id + " remote! (you have 10 seconds)");
		Utils.sendSweepRequest(rm2);
		Signal on = new Signal(Utils.obtainRFCodeFromSession(rm2));
		System.out.println("Signal Received!");
		System.out.println(on.toString());
		Thread.sleep(2000);
		System.out.println("Push your OFF button for your " + id + " remote! (you have 10 seconds)");
		Utils.sendSweepRequest(rm2);
		Signal off = new Signal(Utils.obtainRFCodeFromSession(rm2));
		System.out.println("Signal Received!");
		System.out.println(off.toString());
		Thread.sleep(2000);
		
		Outlet outlet = new Outlet(id, on, off);
		outlet.saveToFile();
		System.out.println("Saved rf codes to /home/overhome/rf-data/" + id);
	}
	
	public static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static byte[] getByte(String path) {
		File file = new File(path);
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	 
	public static void writeByteToFile(String path, byte[] bytes) {
		File file = new File(path);
		try {
			FileUtils.writeByteArrayToFile(file, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
