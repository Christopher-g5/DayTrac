package edu.sjsu.android.daytrac;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class MyDialogFragment extends DialogFragment {
    private boolean bContinue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning");
        builder.setMessage("This group paused their activities, are you sure you want to know them?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            // When user selects yes
//            ArrayList<Contact> contacts = MainActivity.getContacts();
//            Intent intent = new Intent(getActivity(), ViewContactActivity.class);
//            intent.putExtra("position", contacts.size() - 1);
//            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            // When user selects no
        });
// Create the AlertDialog object and return it
        return builder.create();
    }
}
