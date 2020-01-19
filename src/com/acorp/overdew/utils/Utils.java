package com.acorp.overdew.utils;

import java.io.IOException;
import java.net.DatagramPacket;

import com.acorp.overdew.cmdpackets.SweepFreqCmdPayload;
import com.github.mob41.blapi.RM2Device;

public class Utils {
	public static void sendSweepRequest(RM2Device dev) throws IOException {
		dev.enterLearning();
		SweepFreqCmdPayload cmdPayload = new SweepFreqCmdPayload();
		DatagramPacket packet = dev.sendCmdPkt(10000, cmdPayload);
	}
	
	public static byte[] obtainRFCodeFromSession(RM2Device dev) throws Exception {
		byte[] dat = null;
		boolean run = true;
		long startTime = System.currentTimeMillis();
		while(run) {
			dat = dev.checkData();
			if (dat != null) {
				System.out.println("[!!!] SEARCH SUCCESS");
				run = false;
			}
			if ((System.currentTimeMillis() - startTime) / 1000.0 >= 10.0) {
				System.out.println("[!!!] SEARCH FAILED (Time out is set to 10 seconds...)");
				run = false;
			}
		}
		return dat;
	}
}
