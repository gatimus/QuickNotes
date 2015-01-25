package io.github.gatimus.quicknotes;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class NewDialog extends DialogFragment implements DialogInterface.OnClickListener{

    private static final String TAG = "NewDialog";
    private AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.v(TAG, "Create");
        super.onCreateDialog(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_new);
        builder.setIcon(android.R.drawable.ic_menu_add);
        builder.setMessage(R.string.msg_new);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNeutralButton(R.string.cancel, this);
        builder.setNegativeButton(R.string.no, this);
        return builder.create();
    } //onCreateDialog

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case DialogInterface.BUTTON_POSITIVE :
                ((Main)getActivity()).saveNote();
                ((Main)getActivity()).newNote();
                break;
            case DialogInterface.BUTTON_NEGATIVE :
                ((Main)getActivity()).newNote();
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