package com.acorp.overdew.utils;

import java.io.IOException;
import java.net.DatagramPacket;

import com.github.mob41.blapi.RM2Device;
import com.github.mob41.blapi.pkt.cmd.rm2.SendDataCmdPayload;

public class Signal {
	
	private byte[] data;
	public Signal(byte[] data) {
		this.data = data;
	}
	
	public Signal(String fromString) {
		data = fromString.getBytes();
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void sendData(RM2Device dev) throws IOException {
		SendDataCmdPayload sendCmd = new SendDataCmdPayload(data);
		DatagramPacket sendPacket = dev.sendCmdPkt(10000, sendCmd);
	}
	
	public String toString() {
		return new String(data);
	}
}
