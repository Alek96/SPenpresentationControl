package com.example.s_pen_presentation_control;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.ui.connect.ConnectFragment;
import com.example.s_pen_presentation_control.ui.control.ControlFragment;
import com.example.s_pen_presentation_control.ui.tutorial.TutorialDialog;

public class MainActivity extends AppCompatActivity implements ConnectFragment.OnConnectionSucceededListener, TutorialDialog.TutorialDialogListener {

    private ComputerRemoteViewModel mComputerRemoteViewModel;
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
        mComputerRemoteViewModel = ViewModelProviders.of(this).get(ComputerRemoteViewModel.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getMetaState() & KeyEvent.META_CTRL_ON) != 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    mComputerRemoteViewModel.onSPenClick();
                    break;
                case KeyEvent.KEYCODE_B:
                    mComputerRemoteViewModel.onSPenDoubleClick();
                    break;
                case KeyEvent.KEYCODE_C:
                    mComputerRemoteViewModel.onSPenSwipeRight();
                    break;
                case KeyEvent.KEYCODE_D:
                    mComputerRemoteViewModel.onSPenSwipeLeft();
                    break;
                case KeyEvent.KEYCODE_E:
                    mComputerRemoteViewModel.onSPenSwipeUp();
                    break;
                case KeyEvent.KEYCODE_F:
                    mComputerRemoteViewModel.onSPenSwipeDown();
                    break;
                default:
                    break;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
