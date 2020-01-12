package com.wpam.spenpresentationcontrol;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.wpam.spenpresentationcontrol.model.AppDatabase;
import com.wpam.spenpresentationcontrol.ui.connect.ComputerRemoteViewModel;
import com.wpam.spenpresentationcontrol.ui.connect.ConnectDialog;
import com.wpam.spenpresentationcontrol.ui.tutorial.TutorialDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ConnectDialog.OnConnectionListener, TutorialDialog.TutorialDialogListener {

    private ComputerRemoteViewModel mComputerRemoteViewModel;
    AppDatabase database;
    private TutorialDialog mTutorialDialog;
    private ConnectDialog mConnectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "myapp.db").fallbackToDestructiveMigration().build();
        mComputerRemoteViewModel = ViewModelProviders.of(this).get(ComputerRemoteViewModel.class);
        mComputerRemoteViewModel.setupDatabase(database);
        mConnectDialog = new ConnectDialog(mComputerRemoteViewModel, database);
        mTutorialDialog = new TutorialDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mComputerRemoteViewModel.isConnected()) {
            mConnectDialog.show(getSupportFragmentManager(), "dialog_connect");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onConnectionSucceeded() {
        Log.d(Tags.APP_TAG, "onConnectionSucceeded");
        mConnectDialog.dismiss();
        mTutorialDialog.show(getSupportFragmentManager(), "dialog_tutorial");
    }

    @Override
    public void onConnectionLost() {
        Log.d(Tags.APP_TAG, "onConnectionLost, isConnected: " + mComputerRemoteViewModel.isConnected());
        mConnectDialog.show(getSupportFragmentManager(), "dialog_connect");
    }

    @Override
    public void onTutorialDialogFinished(DialogFragment dialog) {
        Log.d(Tags.APP_TAG, "onTutorialDialogFinished");
        mTutorialDialog.dismiss();
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
