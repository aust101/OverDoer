package com.acorp.overdew.cmdpackets;

import com.github.mob41.blapi.pkt.CmdPayload;
import com.github.mob41.blapi.pkt.Payload;

public class SweepFreqCmdPayload implements CmdPayload {

    private final Payload payload;

    private final byte[] payloadBytes;

    public SweepFreqCmdPayload(){
        payloadBytes = new byte[16];
        payloadBytes[0] = 0x19;

        payload = new Payload(){

            @Override
            public byte[] getData() {
                return payloadBytes;
            }

        };
    }

    @Override
    public byte getCommand() {
        return 0x6a;
    }

    @Override
    public Payload getPayload() {
        return payload;
    }

}