package com.mad41.tripreminder.trip_ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class loadingDailogFragment  extends DialogFragment {
    private  final String message;

    private loadingDailogFragment(String message) {
        this.message = message;
    }

    public static loadingDailogFragment newInstance(String message) {
        loadingDailogFragment fragment = new loadingDailogFragment(message);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;
    }

}
