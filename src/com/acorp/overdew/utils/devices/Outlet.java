package com.acorp.overdew.utils.devices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.acorp.overdew.OverDoer;
import com.acorp.overdew.utils.Signal;
import com.github.mob41.blapi.RM2Device;

public class Outlet {
	private String id;
	private Signal onSignal;
	private Signal offSignal;
	public Outlet(String id, Signal onSignal, Signal offSignal) {
		this.id = id;
		this.onSignal = onSignal;
		this.offSignal = offSignal;
	}
	
	public Outlet(String getById) throws IOException {
		this.id = getById;
		BufferedReader Buff = new BufferedReader(new FileReader("/home/overhome/rf-data/" + getById +"/on.txt"));
        this.onSignal = new Signal(Buff.readLine().getBytes());
        System.out.println(this.onSignal.toString());
        Buff.close();
        Buff = new BufferedReader(new FileReader("/home/overhome/rf-data/" + getById +"/off.txt"));
        this.offSignal = new Signal(Buff.readLine().getBytes());
        System.out.println(this.offSignal.toString());
        Buff.close();
	}
	
	public void on(RM2Device dev) throws IOException {
		this.onSignal.sendData(dev);
	}
	
	public void off(RM2Device dev) throws IOException {
		this.offSignal.sendData(dev);
	}
	
	public String getId() {
		return this.id;
	}
	
	public Signal getOnSignal() {
		return this.onSignal;
	}
	
	public Signal getOffSignal() {
		return this.offSignal;
	}
	
	public void saveToFile() throws IOException {
		String PATH = "/home/overhome/rf-data/" + id + "/";
	    String directoryName = PATH;

	    File directory = new File(directoryName);
	    if (!directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }
	    OverDoer.writeByteToFile(directoryName + "on.txt", this.onSignal.getData());
	    OverDoer.writeByteToFile(directoryName + "off.txt", this.offSignal.getData());

	}
}




/*
 * 
 * 
 * 
 * 
 * String PATH = "/home/overhome/rf-data/" + id + "/";
	    String directoryName = PATH;
	    String fileName = "on.txt";

	    File directory = new File(directoryName);
	    if (!directory.exists()){
	        directory.mkdir();
	        // If you require it to make the entire directory path including parents,
	        // use directory.mkdirs(); here instead.
	    }

	    File file = new File(directoryName + fileName);
	    try{
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(this.onSignal.toString());
	        bw.close();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.exit(-1);
	    }
	    
*/
