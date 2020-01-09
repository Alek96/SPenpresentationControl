package com.zamo.spenserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class CommunicationThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(CommunicationThread.class.getSimpleName());

    private Server server;
    private Socket socket;
    private boolean running = false;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public CommunicationThread(Server server, Socket socket) {
        super();
        this.server = server;
        this.socket = socket;
    }

    public void send(String message) {
        log.debug("Send, message size {} ", message.length());
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            log.debug("Client {} error sending: {}", socket.getRemoteSocketAddress(), e.getMessage());
            server.removeClient();
        }
    }

    @Override
    public void run() {
        try {
            log.info("Client {} connected to server", socket.getRemoteSocketAddress());

            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            running = true;
            while (running) {
                server.incomingMessage(dis.readUTF());
            }
        } catch (IOException e) {
            //System.out.println("Client " + socket.getRemoteSocketAddress() + " error reading : " + e.getMessage());
            server.removeClient();
        }
    }

    public void close() throws IOException {
        log.info("Client {} disconnect from server", socket.getRemoteSocketAddress());
        if (!running) {
            return;
        }
        running = true;
        socket.close();
        dis.close();
        dos.close();
    }
}
