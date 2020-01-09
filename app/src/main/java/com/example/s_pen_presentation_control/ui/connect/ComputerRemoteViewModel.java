package com.example.s_pen_presentation_control.ui.connect;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.s_pen_presentation_control.Tags;

import java.io.IOException;

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
        sendMessage("onSPenSwipeLeft");
    }

    public void onSPenSwipeUp() {
        Log.d(Tags.APP_TAG, "onSPenSwipeUp");
        sendMessage("onSPenSwipeUp");
    }

    public void onSPenSwipeDown() {
        Log.d(Tags.APP_TAG, "onSPenSwipeDown");
        sendMessage("onSPenSwipeDown");
    }

    public void connect(String address, int port, OnConnect onConnect) {
        if (client != null) {
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
            if (Boolean.TRUE.equals(result)) {
                new receivedTask().execute();
            }
            onConnect.onConnect(result);
        }
    }

    private Client.OnClientRemove onClientRemove = this::removeClient;

    private synchronized void removeClient() {
        Log.i(Tags.APP_TAG, "Remove client");
        try {
            client.close();
        } catch (IOException e) {
            Log.e(Tags.APP_TAG, "Error closing Client", e);
        }
        client = null;
    }


    private class receivedTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (client == null) {
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
            Log.d(Tags.APP_TAG, "incomingMessage");
            if (input.equals("exit")) {
                removeClient();
            }
        }
    }

    private synchronized void sendMessage(String message) {
        if (client != null) {
            new SendTask(message).execute();
        }
    }

    private class SendTask extends AsyncTask<Void, Void, Void> {
        private String message;

        private SendTask(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... values) {
            if (client != null) {
                client.send(message);
            }
            return null;
        }
    }
}
