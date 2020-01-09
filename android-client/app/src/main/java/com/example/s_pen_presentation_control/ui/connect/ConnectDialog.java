package com.example.s_pen_presentation_control.ui.connect;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.R;
import com.example.s_pen_presentation_control.Tags;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConnectDialog extends DialogFragment {

    private ComputerRemoteViewModel mComputerRemoteViewModel;
    private OnConnectionSucceededListener listener;

    private Unbinder unbinder;

    @BindView(R.id.connect_address)
    EditText mAddressEditTextVew;
    @BindView(R.id.connect_port)
    EditText mPortEditTextVew;
    @BindView(R.id.connect_error)
    TextView mErrorTextVew;
    @BindView(R.id.connect_button)
    Button mConnectButton;


    public static ConnectDialog newInstance() {
        return new ConnectDialog();
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

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        Log.d(Tags.APP_TAG, "onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mComputerRemoteViewModel = ViewModelProviders.of(getActivity()).get(ComputerRemoteViewModel.class);
    }

    @OnClick(R.id.connect_button)
    public void onConnectButtonClick(View view) {
        Log.d(Tags.APP_TAG, "onConnectButtonClick");
        String address = mAddressEditTextVew.getText().toString();
        String sPort = mPortEditTextVew.getText().toString();
        if ("".matches(address)) {
            mErrorTextVew.setText("Address is required");
            return;
        } else if ("".matches(sPort)) {
            mErrorTextVew.setText("Port is required");
            return;
        }
        mErrorTextVew.setText("Connecting...");
        int port = Integer.valueOf(sPort);
        Log.d(Tags.APP_TAG, "address: " + address + ", port: " + port);
        mComputerRemoteViewModel.connect(address, port, result -> {
            if (result) {
                listener.onConnectionSucceeded();
            } else {
                mErrorTextVew.setText("Connection Failed");
            }
        });
    }
}
