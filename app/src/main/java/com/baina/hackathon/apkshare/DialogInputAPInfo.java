package com.baina.hackathon.apkshare;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.baina.hackathon.wifiControl.WifiApControl;

import java.util.Map;

/**
 * Created by Rick Sun on 2016/10/24.
 */

public class DialogInputAPInfo extends DialogFragment {
    // Use this instance of the interface to deliver action events
    private NoticeDialogListener mListener;

    private View view;
    private Map<String, Object> cargo;
    private DialogFragment current;
    private String ssid;
    private String password;

    public void passObjects(Map<String, Object> cargo) {
        this.cargo = cargo;
    }

    public Map<String, Object> getObjects() {
        return this.cargo;
    }

    private void setSSID(String ssid) {
        this.ssid = ssid;
    }

    public String getSSID() {
        return this.ssid;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mListener = (NoticeDialogListener) this.cargo.get("Context");
        current = this;

        view =inflater.inflate(R.layout.dialog_input_ap_info, null);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText username = (EditText) view.findViewById(R.id.username);
                        EditText password = (EditText) view.findViewById(R.id.password);

                        setSSID(username.getText().toString());
                        setPassword(password.getText().toString());
                        mListener.onDialogPositiveClick(current);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        EditText username = (EditText) view.findViewById(R.id.username);
        username.setText(android.os.Build.MODEL);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }
}
