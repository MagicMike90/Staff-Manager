package com.sanitation.app.Main.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.sanitation.app.R;



public class SignActionFragment extends Fragment implements FullScreenDialogContent {

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String RESULT_FULL_NAME = "RESULT_FULL_NAME";

    private FullScreenDialogController dialogController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_action, container, false);
    }

    @Override
    public void onDialogCreated(final FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        TextView name = (TextView) getView().findViewById(R.id.name);
//        name.setText(getString(R.string.hi_name, getArguments().getString(EXTRA_NAME)));
//
//        surname = (EditText) getView().findViewById(R.id.surnameField);


//        dialogController.setConfirmButtonEnabled(!surname.getText().toString().isEmpty());
//        surname.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dialogController.setConfirmButtonEnabled(!s.toString().trim().isEmpty());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialog) {
        Bundle result = new Bundle();
//        result.putString(RESULT_FULL_NAME, getArguments().getString(EXTRA_NAME) + " " + surname.getText().toString());
        dialog.confirm(result);
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialog) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.discard_confirmation_title)
                .setMessage(R.string.discard_confirmation_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogController.discard();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to do
                    }
                }).show();

        return true;
    }
}