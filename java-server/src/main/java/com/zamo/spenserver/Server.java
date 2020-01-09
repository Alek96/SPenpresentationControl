package com.zamo.spenserver;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class.getSimpleName());

    private boolean running = true;
    private ServerSocket serverSocket = null;
    private CommunicationThread client = null;

    public Server(int port) {
        try {
            String address = getAddress();
            serverSocket = new ServerSocket(port);
            log.info("Server started on address {}. port {}", address, serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Can not bind to port", e);
        }
    }

    private String getAddress() {
        Enumeration<NetworkInterface> nets = null;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("Can not get Network Interfaces", e);
            return "error";
        }
        for (NetworkInterface netInt : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netInt.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress.getHostAddress().contains("192.168.1.")) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return "";
    }

    static void displayInterfaceInformation(NetworkInterface netint) {
        log.debug("Display name: {}", netint.getDisplayName());
        log.debug("Name: {}", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            log.debug("InetAddress: {}", inetAddress);
        }
        log.debug("\n");
    }

    @Override
    public void run() {
        while (running) {
            try {
                log.info("Waiting for client...");
                acceptClient(serverSocket.accept());
            } catch (IOException e) {
                log.error("Server accept error", e);
                stop();
            }
        }
    }

    private void acceptClient(Socket socket) {
        if (client == null) {
            client = new CommunicationThread(this, socket);
            client.start();
        } else {
            log.info("Client refused : maximum 1 reached.");
        }
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while closing serverSocket", e);
        }
    }

    public synchronized void removeClient() {
        log.info("Remove client");
        try {
            client.close();
        } catch (IOException e) {
            log.error("Error closing thread", e);
        }
        client = null;
    }

    public synchronized void incomingMessage(String input) {
        log.debug("incomingMessage: {}", input);
        if (input.equals("exit")) {
            client.send("exit");
            removeClient();
        }
    }
}
