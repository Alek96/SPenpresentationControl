package com.zamo.spenserver;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

public class Server implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class.getSimpleName());

    private boolean started = true;
    private ServerSocket serverSocket = null;
    private CommunicationThread client = null;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server started on port {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Can not bind to port", e);
        }
    }

    @Override
    public void run() {
        while (started) {
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
        started = false;
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
