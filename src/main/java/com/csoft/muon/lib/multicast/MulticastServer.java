package com.csoft.muon.lib.multicast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class MulticastServer extends Thread {

    private static final int SEC = 1000;
    private static final String LABEL = "name";

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;

    private int listeningPort;
    private String multicastGroupAddr;

    public MulticastServer(int listeningPort, String multicastGroup) throws IOException {
        super(LABEL);
        this.listeningPort = listeningPort;
        this.multicastGroupAddr = multicastGroup;
        socket = new DatagramSocket(listeningPort);

        try {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }
    }

    public void run() {
        DatagramPacket packet;
        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];
                String dString = null;
                if (in == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();
                buf = dString.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = InetAddress.getByName(multicastGroupAddr);
                packet = new DatagramPacket(buf, buf.length, address, listeningPort);
                socket.send(packet);

                try {
                    Thread.sleep((long) Math.random() * SEC);
                } catch (InterruptedException e) {
                }

            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }

    protected String getNextQuote() {
        String returnValue = null;
        try {
            if ((returnValue = in.readLine()) == null) {
                in.close();
                moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
}
