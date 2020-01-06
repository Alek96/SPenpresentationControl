package com.example.s_pen_presentation_control;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.s_pen_presentation_control.ui.control.ControlFragment;
import com.example.s_pen_presentation_control.ui.connect.ConnectFragment;
import com.example.s_pen_presentation_control.ui.tutorial.TutorialDialog;

public class MainActivity extends AppCompatActivity implements ConnectFragment.OnConnectionSucceededListener, TutorialDialog.TutorialDialogListener {

    private TutorialDialog mTutorialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ConnectFragment.newInstance())
                    .commitNow();
        }
        mTutorialDialog = new TutorialDialog();
    }

    @Override
    public void onConnectionSucceeded() {
        Log.d(Tags.APP_TAG, "onConnectionSucceeded");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ControlFragment.newInstance())
                .commit();

        mTutorialDialog.show(getSupportFragmentManager(), "dialog_tutorial");
    }

    @Override
    public void onTutorialDialogFinished(DialogFragment dialog) {
        Log.d(Tags.APP_TAG, "onTutorialDialogFinished");
        mTutorialDialog.dismiss();
    }
}
