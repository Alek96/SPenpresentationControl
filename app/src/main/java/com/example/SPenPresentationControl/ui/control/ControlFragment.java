package com.example.SPenPresentationControl.ui.control;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.SPenPresentationControl.R;
import com.example.SPenPresentationControl.Tags;
import com.example.SPenPresentationControl.ui.SPenRemoteViewModel;
import com.example.SPenPresentationControl.ui.tutorial.TutorialFragment;

public class ControlFragment extends Fragment {

    private ControlViewModel mViewModel;

    public static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateView");
        return inflater.inflate(R.layout.control_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ControlViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager manager = getFragmentManager();
        TutorialFragment tutorialFragment = new TutorialFragment();
        tutorialFragment.show(manager, "fragment_edit_name");
    }
}
