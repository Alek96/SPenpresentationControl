package com.example.SPenPresentationControl;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SPenPresentationControl.ui.control.ControlFragment;
import com.example.SPenPresentationControl.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnConnectionSucceededListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onConnectionSucceeded() {
        Log.d(Tags.APP_TAG, "onConnectionSucceeded");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ControlFragment.newInstance())
                .commit();
    }
}
