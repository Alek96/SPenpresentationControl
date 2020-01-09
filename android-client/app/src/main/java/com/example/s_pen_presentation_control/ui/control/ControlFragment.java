package com.example.s_pen_presentation_control.ui.control;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.ui.connect.ComputerRemoteViewModel;
import com.example.s_pen_presentation_control.R;
import com.example.s_pen_presentation_control.Tags;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ControlFragment extends Fragment {

    private ControlViewModel mViewModel;
    private ComputerRemoteViewModel mComputerRemoteViewModel;

    private Unbinder unbinder;

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.control_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ControlViewModel.class);
        mComputerRemoteViewModel = ViewModelProviders.of(getActivity()).get(ComputerRemoteViewModel.class);
    }

    @Override
    public void onDestroyView() {
        Log.d(Tags.APP_TAG, "onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.control_button_click)
    public void onButtonClick(View view) {
        mComputerRemoteViewModel.onSPenClick();
    }

    @OnClick(R.id.control_button_double_click)
    public void onButtonDoubleClick(View view) {
        mComputerRemoteViewModel.onSPenDoubleClick();
    }

    @OnClick(R.id.control_button_swipe_right)
    public void onButtonSwipeRight(View view) {
        mComputerRemoteViewModel.onSPenSwipeRight();
    }

    @OnClick(R.id.control_button_swipe_left)
    public void onButtonSwipeLeft(View view) {
        mComputerRemoteViewModel.onSPenSwipeLeft();
    }

    @OnClick(R.id.control_button_swipe_up)
    public void onButtonSwipeUp(View view) {
        mComputerRemoteViewModel.onSPenSwipeUp();
    }

    @OnClick(R.id.control_button_swipe_down)
    public void onButtonSwipeDown(View view) {
        mComputerRemoteViewModel.onSPenSwipeDown();
    }
}
