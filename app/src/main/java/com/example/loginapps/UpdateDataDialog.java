package com.example.loginapps;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateDataDialog extends AppCompatDialogFragment{

    private ArrayList<Data> newArrayList = new ArrayList<>();
    private EditText updateItemID,updateItemCode,updateItemName,updateItemPrice,updateItemStock;
    //private UpdateDataDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle data = getArguments();
        if (data != null){
            newArrayList.addAll(data.getParcelableArrayList("Data"));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_data_dialog, null);
        builder.setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                       // overridePendingTransition(0, 0);
                      //  Intent intent  = new Intent(this,GetDataActivity.class);
                       // startActivity(getIntent());
                       // overridePendingTransition(0, 0);
                       // refreshLayout.setRefreshing(false);
//                        Intent myIntent = new Intent(getContext(), GetDataActivity.class);
//                        startActivity(myIntent);
                    }
                })
                .setTitle("Modify Data")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String id = updateItemID.getText().toString();
                        deleteDataToServer(id);
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    String id = updateItemID.getText().toString();
                    String item_code = updateItemCode.getText().toString();
                    String item_name = updateItemName.getText().toString();
                    double itemPrice = Double.parseDouble(String.valueOf(updateItemPrice.getText()));
                    int itemStock = Integer.parseInt(String.valueOf(updateItemStock.getText()));
                    updateDataToServer(id,item_code,item_name,itemPrice,itemStock,dialogInterface);

                    updateItemName.setText(item_name);
                    dialogInterface.dismiss();

            }
        });

        updateItemID = view.findViewById(R.id.edt_updateId);
        updateItemID.setText(newArrayList.get(0).getId());
        updateItemID.setEnabled(false);

        updateItemCode = view.findViewById(R.id.edt_updateItemCode);
        updateItemCode.setText(newArrayList.get(0).getItem_code());

        updateItemName = view.findViewById(R.id.edt_updateItemName);
        updateItemName.setText(newArrayList.get(0).getItem_name());

        updateItemPrice = view.findViewById(R.id.edt_updateItemPrice);
        updateItemPrice.setText(String.valueOf((double) newArrayList.get(0).getItemPrice()));

        updateItemStock = view.findViewById(R.id.edt_updateItemStock);
        updateItemStock.setText(String.valueOf((int) newArrayList.get(0).getStock()));


        return builder.create();

    }


        public void deleteDataToServer(final String id) {

            StringRequest stringRequest =  new StringRequest(Request.Method.POST,
                    DbContract.SEVER_DELETE_DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject= new JSONObject(response);
                                String resp = jsonObject.getString("server_response" );

                                if (resp.equals("[{\"status\":\"OK\"}]")){

                                }else{

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Override
                protected Map<String,String>getParams() throws AuthFailureError {

                    Map<String,String>  param = new HashMap<>();
                    param.put("id",id);

                    return param;
                }
            };

            VolleyConnection.getInstance(null).addToRequestQue(stringRequest);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    dialogInterface.cancel();
//                }
//            },2000);


        }

        public void updateDataToServer(final String id,final String item_code,final String item_name,final double item_price,final int item_stock,DialogInterface dialogInterface) {

            StringRequest stringRequest =  new StringRequest(Request.Method.POST,
                    DbContract.SEVER_UPDATE_DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject= new JSONObject(response);
                                String resp = jsonObject.getString("server_response" );

                                if (resp.equals("[{\"status\":\"OK\"}]")){

                                }else{
                                   // Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Override
                protected Map<String,String>getParams() throws AuthFailureError {

                    Map<String,String>  param = new HashMap<>();
                    param.put("id",id);
                    param.put("item_code",item_code);
                    param.put("item_name",item_name);
                    param.put("price", String.valueOf(item_price));
                    param.put("stock", String.valueOf(item_stock));

                    return param;
                }
            };

        VolleyConnection.getInstance(null).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogInterface.cancel();
                }
            },2000);

    }






//
//    public interface UpdateDataDialogListener{
//       //progressDialog.setMessage("Please Wait");
//        //progressDialog.setCancelable(false);
//       // createDataToServer(itemCode,itemName,itemPrice,itemStock);
//
//        void  update(String id,String item_code,String item_name,double item_price, int item_stock);
//    }


//    public void update(String id,String item_code,String item_name,double item_price, int item_stock) {
//        progressDialog.setMessage("Please Wait");
//        progressDialog.setCancelable(false);
//        updateDataToServer(itemCode,itemName,itemPrice,itemStock);
//    }


}
