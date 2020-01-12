package com.wpam.spenpresentationcontrol.ui.connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wpam.spenpresentationcontrol.R;
import com.wpam.spenpresentationcontrol.Tags;
import com.wpam.spenpresentationcontrol.model.AppDatabase;
import com.wpam.spenpresentationcontrol.model.SocketAddress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConnectDialog extends DialogFragment {

    private ComputerRemoteViewModel mComputerRemoteViewModel;
    private OnConnectionSucceededListener listener;
    private AppDatabase mDatabase;
    private SocketAddress socketAddress;

    private Unbinder unbinder;

    @BindView(R.id.connect_address)
    EditText mAddressEditTextVew;
    @BindView(R.id.connect_port)
    EditText mPortEditTextVew;
    @BindView(R.id.connect_error)
    TextView mErrorTextVew;
    @BindView(R.id.connect_button)
    Button mConnectButton;


    public ConnectDialog(ComputerRemoteViewModel computerRemoteViewModel, AppDatabase database) {
        this.mComputerRemoteViewModel = computerRemoteViewModel;
        mDatabase = database;
    }

    public interface OnConnectionSucceededListener {
        void onConnectionSucceeded();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(Tags.APP_TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof OnConnectionSucceededListener) {
            listener = (OnConnectionSucceededListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement " + ConnectDialog.class.getSimpleName()
                    + "." + OnConnectionSucceededListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        Log.d(Tags.APP_TAG, "onDetach");
        super.onDetach();
        listener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.connect_to_pc);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.connect_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        builder.setView(view);

        pullSocketAddress();

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);
        }
        return dialog;
    }

    private void pullSocketAddress() {
        new Thread(() -> {
            Log.d(Tags.APP_TAG, "Pull SocketAddress");
            socketAddress = mDatabase.socketAddressDao().getFirst();
            if (socketAddress != null) {
                updateViewBySocketAddress();
            }
        });
    }

    private void updateViewBySocketAddress() {
        Activity activity = getActivity();
        if (activity != null) {
            getActivity().runOnUiThread(() -> {
                Log.d(Tags.APP_TAG, "updateViewBySocketAddress");
                mAddressEditTextVew.setText(socketAddress.address);
                mPortEditTextVew.setText(socketAddress.port);
            });
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(Tags.APP_TAG, "onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.connect_button)
    void onConnectButtonClick(View view) {
        Log.d(Tags.APP_TAG, "onConnectButtonClick");
        if (mComputerRemoteViewModel.isConnected()) {
            mConnectButton.setText(R.string.connect);
            mComputerRemoteViewModel.removeClient();
            return;
        }
        if (validateAddress() && validatePort()) {
            startConnecting();
        }
    }

    private void startConnecting() {
        mErrorTextVew.setText(R.string.connecting);
        mConnectButton.setText(R.string.stop_connecting);

        String address = mAddressEditTextVew.getText().toString();
        int port = Integer.valueOf(mPortEditTextVew.getText().toString());
        saveSocketAddress(address, port);

        mComputerRemoteViewModel.connect(address, port, result -> {
            if (result) {
                listener.onConnectionSucceeded();
            } else {
                mErrorTextVew.setText(R.string.connection_failed);
                mConnectButton.setText(R.string.connect);
            }
        });
    }

    private boolean validateAddress() {
        String address = mAddressEditTextVew.getText().toString();
        if ("".matches(address)) {
            mErrorTextVew.setText(R.string.address_is_required);
            return false;
        }
        return true;
    }

    private boolean validatePort() {
        String sPort = mPortEditTextVew.getText().toString();
        if ("".matches(sPort)) {
            mErrorTextVew.setText(R.string.port_is_required);
            return false;
        }
        return true;
    }

    private void saveSocketAddress(String address, int port) {
        Log.d(Tags.APP_TAG, "saveSocketAddress, address: " + address + ", port: " + port);
        if (socketAddress == null) {
            insertSocketAddress(new SocketAddress(address, port));
        } else {
            updateSocketAddress(address, port);
        }
    }

    private void insertSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        new Thread(() -> {
            Log.d(Tags.APP_TAG, "Insert SocketAddress");
            mDatabase.socketAddressDao().insertAll(socketAddress);
        });
    }

    private void updateSocketAddress(String address, int port) {
        socketAddress.address = address;
        socketAddress.port = port;
        new Thread(() -> {
            Log.d(Tags.APP_TAG, "Update SocketAddress");
            mDatabase.socketAddressDao().updateAll(socketAddress);
        });
    }
}
