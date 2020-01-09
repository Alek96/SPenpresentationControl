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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TutorialDialog extends DialogFragment {

    private TutorialViewModel mTutorialViewModel;
    private TutorialDialogListener listener;

    private Unbinder unbinder;

    @BindView(R.id.tutorial_button_next)
    Button mButtonNext;
    @BindView(R.id.tutorial_button_back)
    Button mButtonBack;
    @BindView(R.id.tutorial_text_view)
    TextView mDescriptionTextVew;
    @BindView(R.id.tutorial_progress_bar)
    ProgressBar mProgressBar;


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

    @Override
    public void onDetach() {
        Log.d(Tags.APP_TAG, "onDetach");
        super.onDetach();
        listener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tutorial);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.tutorial_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        Log.d(Tags.APP_TAG, "onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Tags.APP_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mTutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel.class);
        updateViews();
    }

    @OnClick(R.id.tutorial_button_back)
    public void onButtonBackClick(View view) {
        if (mTutorialViewModel.isFirstStage()) {
            Log.e(Tags.APP_TAG, "cannot set previous Stage, because it is first one");
        } else {
            mTutorialViewModel.previousStage();
            updateViews();
        }
    }

    @OnClick(R.id.tutorial_button_next)
    public void onButtonNextClick(View view) {
        if (mTutorialViewModel.isLastStage()) {
            listener.onTutorialDialogFinished(TutorialDialog.this);
        } else {
            mTutorialViewModel.nextStage();
            updateViews();
        }
    }

    private void updateViews() {
        mDescriptionTextVew.setText(mTutorialViewModel.getStage().getTextId());
        mButtonBack.setEnabled(!mTutorialViewModel.isFirstStage());
        mButtonNext.setText(mTutorialViewModel.isLastStage() ? R.string.close : R.string.next);
        mProgressBar.setProgress(mTutorialViewModel.getStage().getProgress());
    }
}
