package com.example.loginapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteDataDialog extends AppCompatDialogFragment {

    private EditText deleteId;
    private DeleteDataDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_data_dialog, null);
        builder.setView(view)
                .setTitle("Delete Data")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // String id_delete = deleteId.getText().toString();
                //listener.post(itemCode);
            }
        });

        deleteId = view.findViewById(R.id.edt_deleteId);
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDataDialogListener) context;
        }catch (ClassCastException e){


        }
    }

    public interface DeleteDataDialogListener{
        void Delete(String id);
    }
}
