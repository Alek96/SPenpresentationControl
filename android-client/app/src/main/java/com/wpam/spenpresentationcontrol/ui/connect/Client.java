package com.wpam.spenpresentationcontrol.ui.connect;

import android.util.Log;

import com.wpam.spenpresentationcontrol.Tags;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client {
    private String serverName;
    private int serverPort;
    private Socket socket = null;
    private boolean connected = false;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private OnClientRemove onClientRemove;


    Client(String serverName, int serverPort, OnClientRemove clientListener) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.onClientRemove = clientListener;
    }

    void connect() throws IOException {
        Log.d(Tags.APP_TAG, "connect to address: " + serverName + ", port: " + serverPort);
        socket = new Socket(serverName, serverPort);
        Log.d(Tags.APP_TAG, "Client started on port: " + socket.getLocalPort());
        Log.d(Tags.APP_TAG, "Connected to server " + socket.getRemoteSocketAddress());

        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        connected = true;
    }

    void send(String message) {
        Log.d(Tags.APP_TAG, "send, message: " + message);
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            Log.d(Tags.APP_TAG, "Client " + socket.getRemoteSocketAddress() + " error while sending: " + e.getMessage());
            onClientRemove.onClientRemove();
        }
    }

    void run(OnMessageReceived onMessageReceived) {
        int readBytes = 0;
        byte[] buffer = new byte[4092];
        ByteArrayOutputStream byteStream;

        try {
            while (connected) {
                byteStream = new ByteArrayOutputStream();
                int remainingBytes = dis.readInt();
                while (remainingBytes > 0 && (readBytes = dis.read(buffer, 0, (int) Math.min(buffer.length, remainingBytes))) != -1) {
                    byteStream.write(buffer, 0, readBytes);
                    remainingBytes -= readBytes;
                }
                onMessageReceived.onMessageReceived(byteStream.toString());
            }
        } catch (IOException e) {
            Log.d(Tags.APP_TAG, "Client " + socket.getRemoteSocketAddress() + " error sending:" + e.getMessage());
            onClientRemove.onClientRemove();
        }
    }

    boolean isConnected() {
        return connected;
    }

    synchronized void close() throws IOException {
        Log.d(Tags.APP_TAG, "Client " + socket.getRemoteSocketAddress() + " disconnect from server");
        if (!connected) {
            return;
        }
        connected = false;
        socket.close();
        dis.close();
        dos.close();
    }

    interface OnClientRemove {
        public void onClientRemove();
    }

    interface OnMessageReceived {
        public void onMessageReceived(String message);
    }
}
