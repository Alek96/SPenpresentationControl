package com.wpam.spenpresentationcontrol.ui.connect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.wpam.spenpresentationcontrol.MyKeyEvent;
import com.wpam.spenpresentationcontrol.Tags;
import com.wpam.spenpresentationcontrol.model.AppDatabase;
import com.wpam.spenpresentationcontrol.model.SPenEvent;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.HashMap;

public class ComputerRemoteViewModel extends ViewModel {

    private Client client = null;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private AppDatabase database;
    private HashMap<String, SPenEvent> sPenEventHashMap;

    public ComputerRemoteViewModel() {
        sPenEventHashMap = new HashMap<>();
    }

    public void setupDatabas(AppDatabase database) {
        this.database = database;
        new Thread(pullSPenEvents).start();
    }

    private Runnable pullSPenEvents = () -> {
        Log.d(Tags.APP_TAG, "Pull SPenEvents");
        for (SPenEvent.SPenEventId sPenEventId : SPenEvent.SPenEventId.values()) {
            SPenEvent sPenEvent = database.sPenEventDao().get(sPenEventId.getId());
            if (sPenEvent == null) {
                Log.d(Tags.APP_TAG, "SPenEvent with is " + sPenEventId.getId() + " not found in database. Use default value");
                sPenEventHashMap.put(sPenEventId.getId(), new SPenEvent(sPenEventId));
            } else {
                sPenEventHashMap.put(sPenEvent.id, sPenEvent);
            }

        }
    };

    public void onSPenClick() {
        Log.d(Tags.APP_TAG, "onSPenClick");
        onSPenEvent(SPenEvent.SPenEventId.CLICK);
    }

    public void onSPenDoubleClick() {
        Log.d(Tags.APP_TAG, "onSPenDoubleClick");
        onSPenEvent(SPenEvent.SPenEventId.DOUBLE_CLICK);
    }

    public void onSPenSwipeRight() {
        Log.d(Tags.APP_TAG, "onSPenSwipeRight");
        onSPenEvent(SPenEvent.SPenEventId.SWIPE_RIGHT);
    }

    public void onSPenSwipeLeft() {
        Log.d(Tags.APP_TAG, "onSPenSwipeLeft");
        onSPenEvent(SPenEvent.SPenEventId.SWIPE_LEFT);
    }

    public void onSPenSwipeUp() {
        Log.d(Tags.APP_TAG, "onSPenSwipeUp");
        onSPenEvent(SPenEvent.SPenEventId.SWIPE_UP);
    }

    public void onSPenSwipeDown() {
        Log.d(Tags.APP_TAG, "onSPenSwipeDown");
        onSPenEvent(SPenEvent.SPenEventId.SWIPE_DOWN);
    }

    private void onSPenEvent(SPenEvent.SPenEventId sPenEventId) {
        Log.d(Tags.APP_TAG, "onSPenEventId: " + sPenEventId.getId());
        SPenEvent sPenEvent = sPenEventHashMap.get(sPenEventId.getId());
        if (sPenEvent == null) {
            Log.d(Tags.APP_TAG, "onSPenEventId " + sPenEventId.getId() + " not configured");
            return;
        }
        Integer[] myKeyEvents = new Integer[]{sPenEvent.keyEvent};
        sendMessage(Arrays.toString(myKeyEvents));
    }

    public boolean isConnected() {
        return client != null;
    }

    void connect(String address, int port, OnConnect onConnect) {
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
                new Thread(new receivedTask()).start();
            }
            onConnect.onConnect(result);
        }
    }

    private Client.OnClientRemove onClientRemove = this::removeClient;

    synchronized void removeClient() {
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

    private class receivedTask implements Runnable {

        @Override
        public void run() {
            Log.d(Tags.APP_TAG, "doInBackground, start receivedTask");
            if (client == null) {
                Log.d(Tags.APP_TAG, "client is null");
                return;
            }
            client.run(onMessageReceived);
        }

        private Client.OnMessageReceived onMessageReceived = message -> {
            Log.d(Tags.APP_TAG, "incomingMessage, input: " + message);
            if (message.equals("exit")) {
                Log.d(Tags.APP_TAG, "received exit commend");
                removeClient();
            }
            mainHandler.post(() -> {
                //Your code to run in GUI thread here
            });
        };
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
