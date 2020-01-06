package com.example.s_pen_presentation_control.ui.control;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.R;
import com.example.s_pen_presentation_control.Tags;
import com.example.s_pen_presentation_control.ui.tutorial.TutorialDialog;

public class ControlFragment extends Fragment {

    private ControlViewModel mViewModel;
    private View mView;
    private Button mButtonClick;
    private Button mButtonDoubleClick;
    private Button mButtonSwipeRight;
    private Button mButtonSwipeLeft;
    private Button mButtonSwipeUp;
    private Button mButtonSwipeDown;


    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateView");
        mView = inflater.inflate(R.layout.control_fragment, container, false);
        mButtonClick = mView.findViewById(R.id.control_button_click);
        mButtonDoubleClick = mView.findViewById(R.id.control_button_double_click);
        mButtonSwipeRight = mView.findViewById(R.id.control_button_swipe_right);
        mButtonSwipeLeft = mView.findViewById(R.id.control_button_swipe_left);
        mButtonSwipeUp = mView.findViewById(R.id.control_button_swipe_up);
        mButtonSwipeDown = mView.findViewById(R.id.control_button_swipe_down);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ControlViewModel.class);
    }
}
