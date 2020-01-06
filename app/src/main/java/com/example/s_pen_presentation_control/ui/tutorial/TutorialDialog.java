package com.example.s_pen_presentation_control.ui.tutorial;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.s_pen_presentation_control.R;
import com.example.s_pen_presentation_control.Tags;

public class TutorialDialog extends DialogFragment {

    private TutorialViewModel mTutorialViewModel;
    private View mView;
    private Button mButtonNext;
    private Button mButtonBack;
    private TextView mDescriptionTextVew;
    private ProgressBar mProgressBar;
    private TutorialDialogListener listener;


    public interface TutorialDialogListener {
        public void onTutorialDialogFinished(DialogFragment dialog);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(Tags.APP_TAG, "onAttach");
        super.onAttach(context);
        try {
            listener = (TutorialDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement " + TutorialDialog.class.getSimpleName()
                    + "." + TutorialDialogListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tutorial);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.tutorial_fragment, null);
        builder.setView(mView);

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mTutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);

        mButtonBack = mView.findViewById(R.id.tutorial_button_back);
        mButtonNext = mView.findViewById(R.id.tutorial_button_next);
        mDescriptionTextVew = mView.findViewById(R.id.tutorial_text_view);
        mProgressBar = mView.findViewById(R.id.tutorial_progress_bar);

        mButtonBack.setOnClickListener(onButtonBackClickListener);
        mButtonNext.setOnClickListener(onButtonNextClickListener);
        updateViews();
    }

    private View.OnClickListener onButtonBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTutorialViewModel.isFirstStage()) {
                Log.e(Tags.APP_TAG, "cannot set previous Stage, because it is first one");
            } else {
                mTutorialViewModel.previousStage();
                updateViews();
            }
        }
    };

    private View.OnClickListener onButtonNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTutorialViewModel.isLastStage()) {
                listener.onTutorialDialogFinished(TutorialDialog.this);
            } else {
                mTutorialViewModel.nextStage();
                updateViews();
            }
        }
    };

    private void updateViews() {
        mDescriptionTextVew.setText(mTutorialViewModel.getStage().getTextId());
        mButtonBack.setEnabled(!mTutorialViewModel.isFirstStage());
        mButtonNext.setText(mTutorialViewModel.isLastStage() ? R.string.close : R.string.next);
        mProgressBar.setProgress(mTutorialViewModel.getStage().getPrograss());
    }
}
