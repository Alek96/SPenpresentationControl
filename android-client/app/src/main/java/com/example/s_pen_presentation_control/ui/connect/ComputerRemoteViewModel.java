package com.example.s_pen_presentation_control.ui.connect;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.s_pen_presentation_control.MyKeyEvent;
import com.example.s_pen_presentation_control.Tags;

import java.io.IOException;
import java.util.Arrays;

public class ComputerRemoteViewModel extends ViewModel {

    private Client client = null;

    public void onSPenClick() {
        Log.d(Tags.APP_TAG, "onSPenClick");
        sendMessage("onSPenClick");
    }

    public void onSPenDoubleClick() {
        Log.d(Tags.APP_TAG, "onSPenDoubleClick");
        sendMessage("onSPenDoubleClick");
    }

    public void onSPenSwipeRight() {
        Log.d(Tags.APP_TAG, "onSPenSwipeRight");
        sendMessage("onSPenSwipeRight");
    }

    public void onSPenSwipeLeft() {
        Log.d(Tags.APP_TAG, "onSPenSwipeLeft");
        Integer[] myKeyEvents = new Integer[]{MyKeyEvent.VK_LEFT};
        sendMessage(Arrays.toString(myKeyEvents));
    }

    public void onSPenSwipeUp() {
        Log.d(Tags.APP_TAG, "onSPenSwipeUp");
        sendMessage("onSPenSwipeUp");
    }

    public void onSPenSwipeDown() {
        Log.d(Tags.APP_TAG, "onSPenSwipeDown");
        sendMessage("onSPenSwipeDown");
    }

    public boolean isConnected() {
        return client != null;
    }

    public void connect(String address, int port, OnConnect onConnect) {
        Log.d(Tags.APP_TAG, "connect, address: " + address + ", port: " + port);
        if (client != null) {
            Log.d(Tags.APP_TAG, "client is not null -> remove");
            removeClient();
        }
        new ConnectTask(address, port, onConnect).execute();
    }

    public interface OnConnect {
        public void onConnect(Boolean result);
    }

    private class ConnectTask extends AsyncTask<Void, String, Boolean> {
        private String address;
        private int port;
        OnConnect onConnect;

        private ConnectTask(String address, int port, OnConnect onConnect) {
            this.address = address;
            this.port = port;
            this.onConnect = onConnect;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(Tags.APP_TAG, "doInBackground, start ConnectTask");
            client = new Client(address, port, onClientRemove);
            try {
                client.connect();
            } catch (IOException e) {
                Log.d(Tags.APP_TAG, "connect not succeed: " + e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(Tags.APP_TAG, "onPostExecute, connection result: " + result);
            if (Boolean.TRUE.equals(result)) {
                new receivedTask().execute();
            }
            onConnect.onConnect(result);
        }
    }

    private Client.OnClientRemove onClientRemove = this::removeClient;

    public synchronized void removeClient() {
        Log.i(Tags.APP_TAG, "Remove client");
        try {
            if (client != null && client.isConnected()) {
                client.close();
            }
        } catch (IOException e) {
            Log.e(Tags.APP_TAG, "Error closing Client", e);
        }
        client = null;
    }

    private class receivedTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(Tags.APP_TAG, "doInBackground, start receivedTask");
            if (client == null) {
                Log.d(Tags.APP_TAG, "client is null");
                return null;
            }
            client.run(onMessageReceived);

            return null;
        }

        private Client.OnMessageReceived onMessageReceived = this::publishProgress;

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            incomingMessage(values[0]);
        }

        private synchronized void incomingMessage(String input) {
            Log.d(Tags.APP_TAG, "incomingMessage, input: " + input);
            if (input.equals("exit")) {
                Log.d(Tags.APP_TAG, "received exit commend");
                removeClient();
            }
        }
    }

    private synchronized void sendMessage(String message) {
        Log.d(Tags.APP_TAG, "sendMessage, message size: " + message.length());
        if (client == null) {
            Log.d(Tags.APP_TAG, "client is null");
            return;
        }
        new Thread(new SendTask(message)).start();
    }

    private class SendTask implements Runnable {
        private String message;

        private SendTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            Log.d(Tags.APP_TAG, "start SendTask");
            if (client == null) {
                Log.d(Tags.APP_TAG, "client is null");
                return;
            }
            client.send(message);
        }
    }
}
