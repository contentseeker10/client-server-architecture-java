package dev.contentseeker10;

import dev.contentseeker10.packet.Decoder;
import dev.contentseeker10.packet.Encoder;
import dev.contentseeker10.packet.Message;
import dev.contentseeker10.packet.Packet;

public class Main {
    static void main() {
        System.out.println("=== Starting Packet Encoding/Decoding Test ===\n");

        int cmdType = 255;
        int userId = 1024;
        String payloadData = "Hello, this is a secure test payload!";
        Message originalMessage = new Message(cmdType, userId, payloadData);

        byte magic = Encoder.MAGIC;
        byte source = 0x01;
        long packetId = 9876543210L;
        Packet originalPacket = new Packet(magic, source, packetId, originalMessage);

        System.out.println("1. Original Packet:");
        System.out.println(originalPacket);
        System.out.println();

        byte[] encodedData = Encoder.encode(originalPacket);
        System.out.println("2. Encoded Byte Array:");
        System.out.println("Total Length: " + encodedData.length + " bytes");
        System.out.println("Hex Dump: " + bytesToHex(encodedData));
        System.out.println();

        try {
            Packet decodedPacket = Decoder.decode(encodedData);

            System.out.println("3. Decoded Packet:");
            System.out.println(decodedPacket);
            System.out.println();

            System.out.println("4. Verification Results:");
            boolean isValidId = originalPacket.getPacketId() == decodedPacket.getPacketId();
            boolean isValidPayload = originalPacket.getMessage().getPayload().equals(decodedPacket.getMessage().getPayload());
            boolean isValidCmd = originalPacket.getMessage().getCmdType() == decodedPacket.getMessage().getCmdType();

            System.out.println("Packet ID Matches: " + isValidId);
            System.out.println("Command Type Matches: " + isValidCmd);
            System.out.println("Payload Matches: " + isValidPayload);

            if (isValidId && isValidPayload && isValidCmd) {
                System.out.println("\nSUCCESS: Packet was successfully encoded, encrypted, decrypted, and decoded!");
            } else {
                System.err.println("\nFAILURE: Decoded data does not match the original.");
            }

        } catch (Exception e) {
            System.err.println("Decoding failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
