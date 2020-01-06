package com.example.s_pen_presentation_control;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class ComputerRemoteViewModel extends ViewModel {
    public void onSPenClick() {
        Log.d(Tags.APP_TAG, "onSPenClick");
    }

    public void onSPenDoubleClick() {
        Log.d(Tags.APP_TAG, "onSPenDoubleClick");
    }

    public void onSPenSwipeRight() {
        Log.d(Tags.APP_TAG, "onSPenSwipeRight");
    }

    public void onSPenSwipeLeft() {
        Log.d(Tags.APP_TAG, "onSPenSwipeLeft");
    }

    public void onSPenSwipeUp() {
        Log.d(Tags.APP_TAG, "onSPenSwipeUp");
    }

    public void onSPenSwipeDown() {
        Log.d(Tags.APP_TAG, "onSPenSwipeDown");
    }
}
