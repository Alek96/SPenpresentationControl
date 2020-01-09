package com.example.s_pen_presentation_control.ui.connect;

import android.util.Log;

import com.example.s_pen_presentation_control.Tags;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private String serverName;
    private int serverPort;
    private Socket socket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private OnClientRemove onClientRemove;

    public Client(String serverName, int serverPort, OnClientRemove clientListener) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.onClientRemove = clientListener;
    }

    public void connect() throws IOException {
        socket = new Socket(serverName, serverPort);
        Log.d(Tags.APP_TAG, "Client started on port: " + socket.getLocalPort());
        Log.d(Tags.APP_TAG, "Connected to server " + socket.getRemoteSocketAddress());

        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void send(String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            Log.d(Tags.APP_TAG, "Client " + socket.getRemoteSocketAddress() + "error sending:" + e.getMessage());
            onClientRemove.onClientRemove();
        }
    }

    public void run(OnMessageReceived onMessageReceived) {
        try {
            while (true) {
                onMessageReceived.onMessageReceived(dis.readUTF());
            }
        } catch (IOException e) {
            onClientRemove.onClientRemove();
        }
    }

    public void close() throws IOException {
        Log.d(Tags.APP_TAG, "Client " + socket.getRemoteSocketAddress() + " disconnect from server");
        socket.close();
        dis.close();
        dos.close();
    }

    public interface OnClientRemove {
        public void onClientRemove();
    }

    public interface OnMessageReceived {
        public void onMessageReceived(String message);
    }
}
