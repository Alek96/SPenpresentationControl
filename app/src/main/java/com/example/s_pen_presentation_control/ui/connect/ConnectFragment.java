package com.example.s_pen_presentation_control.ui.connect;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.R;
import com.example.s_pen_presentation_control.SPenRemoteViewModel;
import com.example.s_pen_presentation_control.Tags;

public class ConnectFragment extends Fragment {

    private ConnectViewModel mViewModel;
    private SPenRemoteViewModel sPenRemoteViewModel;
    private TextView mSpenConnectTextView;
    private TextView mComputerConnectTextView;
    private OnConnectionSucceededListener listener;
    private final Handler handler = new Handler();

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
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
                    + " must implement " + ConnectFragment.class.getSimpleName()
                    + "." + OnConnectionSucceededListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        Log.d(Tags.APP_TAG, "onDetach");
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        mSpenConnectTextView = (TextView) view.findViewById(R.id.s_pen_connect);
        mComputerConnectTextView = (TextView) view.findViewById(R.id.computer_connect);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConnectViewModel.class);
        sPenRemoteViewModel = ViewModelProviders.of(getActivity()).get(SPenRemoteViewModel.class);
        sPenRemoteViewModel.checkSdkInfo();
        updateSPenConnectionTextView();
        updateComputerConnectionTextView();
    }

    private void updateSPenConnectionTextView() {
        Log.d(Tags.APP_TAG, "updateSPenConnectionTextView");
        if (sPenRemoteViewModel.getVersionName() != null) {
            mSpenConnectTextView.setText(getString(R.string.spen_connected));
        }
    }

    private void updateComputerConnectionTextView() {
        Log.d(Tags.APP_TAG, "updateComputerConnectionTextView");
        if (sPenRemoteViewModel.getVersionName() != null) {
            mComputerConnectTextView.setText(getString(R.string.computer_connecting));
        }
    }

    @Override
    public void onStart() {
        Log.d(Tags.APP_TAG, "onStart");
        super.onStart();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connectionSucceeded();
            }
        }, 100);
    }

    private void connectionSucceeded() {
        Log.d(Tags.APP_TAG, "connectionSucceeded");
        Toast.makeText(getContext(), "connection succeeded", Toast.LENGTH_SHORT).show();
        listener.onConnectionSucceeded();
    }
}
