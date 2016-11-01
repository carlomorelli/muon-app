package com.csoft.muon.lib.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient {
    
    private static final String MULTICAST_GROUP = "230.0.0.1";
    private static final int LISTENING_PORT = 8000;

    public static void main(String[] args) throws IOException, InterruptedException {
 
        
        MulticastServer server = new MulticastServer(LISTENING_PORT, MULTICAST_GROUP);
        server.start();
 
            // get a datagram socket
        MulticastSocket socket = new MulticastSocket(LISTENING_PORT);
        InetAddress address = InetAddress.getByName(MULTICAST_GROUP);
        socket.joinGroup(address);
        DatagramPacket packet;
        
        for (int i = 0; i < 10; i++) {
            
            byte[] buf = new byte[256];
            
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Quote of the Moment: " + received);
            
//            Thread.sleep(1000);
        }
        
        socket.leaveGroup(address);
        socket.close();
    }
}