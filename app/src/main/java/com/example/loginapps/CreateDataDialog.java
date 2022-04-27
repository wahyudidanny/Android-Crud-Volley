package com.example.loginapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateDataDialog extends AppCompatDialogFragment {

    private EditText addItemCode,addItemName,addItemPrice,addItemStock;
    private CreateDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_data_dialog, null);
        builder.setView(view)
                .setTitle("Create Data")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String itemCode = addItemCode.getText().toString();
                String itemName = addItemName.getText().toString();
                double itemPrice = Double.parseDouble(String.valueOf(addItemPrice.getText()));
                int itemStock = Integer.parseInt(String.valueOf(addItemStock.getText()));;
                listener.post(itemCode,itemName,itemPrice,itemStock);
            }
        });

        addItemCode = view.findViewById(R.id.edt_addItemCode);
        addItemName = view.findViewById(R.id.edt_addItemName);
        addItemPrice = view.findViewById(R.id.edt_addItemPrice);
        addItemStock = view.findViewById(R.id.edt_addItemStock);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CreateDialogListener) context;
        }catch (ClassCastException e){


        }
    }

    public interface CreateDialogListener{
        void post(String itemCode,String itemName,double itemPrice,int itemStock);
    }

}
