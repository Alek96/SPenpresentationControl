package com.example.s_pen_presentation_control.ui;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.s_pen_presentation_control.Tags;
import com.samsung.android.sdk.penremote.SpenRemote;

public class SPenRemoteViewModel extends ViewModel {
    private SpenRemote mSPenRemote;

    private SpenRemote getSPenRemote() {
        if (mSPenRemote == null) {
            mSPenRemote = SpenRemote.getInstance();
        }
        return mSPenRemote;
    }

    public void checkSdkInfo() {
        SpenRemote sPenRemote = getSPenRemote();
        Log.d(Tags.APP_TAG, "VersionCode=" + sPenRemote.getVersionCode());
        Log.d(Tags.APP_TAG, "versionName=" + getVersionName());
        Log.d(Tags.APP_TAG, "Support Button = " + isSupportButton());
        Log.d(Tags.APP_TAG, "Support Air motion = " + isSupportAirMotion());
    }

    public String getVersionName() {
        SpenRemote sPenRemote = getSPenRemote();
        return sPenRemote.getVersionName();
    }

    public boolean isSupportButton() {
        SpenRemote sPenRemote = getSPenRemote();
        return sPenRemote.isFeatureEnabled(SpenRemote.FEATURE_TYPE_BUTTON);
    }

    public boolean isSupportAirMotion() {
        SpenRemote sPenRemote = getSPenRemote();
        return sPenRemote.isFeatureEnabled(SpenRemote.FEATURE_TYPE_AIR_MOTION);
    }
}
