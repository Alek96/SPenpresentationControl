package com.wpam.spenpresentationcontrol.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.wpam.spenpresentationcontrol.MyKeyEvent;

@Entity
public class SPenEvent {
    public enum SPenEventId {
        CLICK("SPenClick", MyKeyEvent.VK_RIGHT),
        DOUBLE_CLICK("SPenDoubleClick", MyKeyEvent.VK_LEFT),
        SWIPE_RIGHT("SPenSwipeRight", MyKeyEvent.VK_RIGHT),
        SWIPE_LEFT("SPenSwipeLeft", MyKeyEvent.VK_LEFT),
        SWIPE_UP("SPenSwipeUp", MyKeyEvent.VK_UP),
        SWIPE_DOWN("SPenSwipeDown", MyKeyEvent.VK_DOWN);

        private String id;
        private int defaultKeyEvent;

        SPenEventId(String id, int defaultKeyEvent) {
            this.id = id;
            this.defaultKeyEvent = defaultKeyEvent;
        }

        @NonNull
        public String getId() {
            return id;
        }

        @NonNull
        public int getDefaultKeyEvent() {
            return defaultKeyEvent;
        }
    }

    @PrimaryKey
    @NonNull
    public String id;
    public int keyEvent;

    public SPenEvent(SPenEventId id) {
        this.id = id.getId();
        this.keyEvent = id.getDefaultKeyEvent();
    }

    public SPenEvent(String id, int keyEvent) {
        this.id = id;
        this.keyEvent = keyEvent;
    }
}
