package com.example.s_pen_presentation_control.ui.tutorial;

import androidx.lifecycle.ViewModel;

import com.example.s_pen_presentation_control.R;

public class TutorialViewModel extends ViewModel {
    public enum Stage {
        START(R.string.tutorial_description_empty),
        SEND_COMMAND(R.string.tutorial_description_send_command),
        EDIT_COMMAND(R.string.tutorial_description_edit_command),
        FINISHED(R.string.tutorial_description_empty);

        private static Stage[] mValues = values();
        private int mTextId;

        Stage(int textId) {
            mTextId = textId;
        }

        public int getTextId() {
            return mTextId;
        }

        public int getProgress() {
            return 100 / (mValues.length - 3) * (this.ordinal() - 1);
        }

        public Stage getNext() {
            return mValues[(this.ordinal() + 1) % mValues.length];
        }

        public Stage getPrevious() {
            return mValues[(this.ordinal() - 1 + mValues.length) % mValues.length];
        }
    }

    private Stage mStage;

    public TutorialViewModel() {
        this.mStage = Stage.START.getNext();
    }

    public Stage getStage() {
        return mStage;
    }

    public void nextStage() {
        if (!isLastStage()) {
            mStage = mStage.getNext();
        }
    }

    public void previousStage() {
        if (!isFirstStage()) {
            mStage = mStage.getPrevious();
        }
    }

    public boolean isFirstStage() {
        return mStage == Stage.START.getNext();
    }

    public boolean isLastStage() {
        return mStage == Stage.FINISHED.getPrevious();
    }
}
