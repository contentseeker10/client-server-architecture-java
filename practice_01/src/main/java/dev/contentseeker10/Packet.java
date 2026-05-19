package dev.contentseeker10;

import java.nio.ByteBuffer;

public class Packet {

    private final static byte MAGIC = 0x13;

    private byte source;
    private long packetId;
    private int length;
    private short headerCrc16;

    ByteBuffer message;

    private short messageCrc16;



}
