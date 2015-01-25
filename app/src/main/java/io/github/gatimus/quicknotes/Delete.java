package io.github.gatimus.quicknotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class Delete extends DialogFragment implements DialogInterface.OnClickListener{

    private static final String TAG = "Delete";
    private AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.v(TAG, "Create");
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_delete);
        builder.setIcon(android.R.drawable.ic_menu_delete);
        builder.setMessage(R.string.msg_delete);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
        return builder.create();
    } //onCreateDialog

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case DialogInterface.BUTTON_POSITIVE :
                ((Main)getActivity()).deleteNote();
                break;
            case DialogInterface.BUTTON_NEGATIVE :
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_NEUTRAL :
                dialog.dismiss();
                break;
            default:
                dialog.dismiss();
                break;
        }
    }

} //class
