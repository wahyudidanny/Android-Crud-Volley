package com.example.loginapps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login,register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = (EditText)  findViewById(R.id.edit_usernameLogin);
        password = (EditText)  findViewById(R.id.edit_passwordLogin);
        login = (Button)  findViewById(R.id.btn_loginLogin);
        register = (Button)  findViewById(R.id.btn_registerLogin);
        progressDialog = new ProgressDialog( Login.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this, MainActivity.class);
                startActivity(registerIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUsername = username.getText().toString();
                String sPassword = password.getText().toString();

                CheckLogin(sUsername,sPassword);
            }
        });

    }


    public void CheckLogin(final String username,final String password){
        if  (checkNetworkingConnection()){
            progressDialog.show();
            StringRequest stringRequest =  new StringRequest(Request.Method.POST, DbContract.SEVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                    JSONObject jsonObject= new JSONObject(response);
                                    JSONArray serverResponse = jsonObject.getJSONArray("server_response");


                                for(int i=0; i < serverResponse.length(); i++){

                                    JSONObject jsonData = serverResponse.getJSONObject(i);

                                    if (jsonData.has("status")){
                                        String resp = jsonObject.getString("server_response" );
                                        Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
                                    }else{

                                        Users users = new Users(
                                                jsonData.getInt("id"),
                                                jsonData.getString("fullname"),
                                                jsonData.getString("username"),
                                                jsonData.getString("email")
                                        );

                                       SessionManagement sessionManagement = new SessionManagement(Login.this);
                                       sessionManagement.saveSession(users);

                                       Intent dashboardIntent = new Intent(Login.this ,GetDataActivity.class);
                                       startActivity(dashboardIntent);

                                    }

                                }


                                   // if (!serverResponse.has("status")){ // error
                                    //    Toast.makeText(getApplicationContext(),jsonObject.getString("server_response"), Toast.LENGTH_SHORT).show();
                                  ///  }else{
//
//                                        Users users = new Users(
//                                                jsonObject.getString("id"),
//                                                jsonObject.getString("fullname"),
//                                                jsonObject.getString("username"),
//                                                jsonObject.getString("email")
//
//                                        );


                                  //  }

                           // while(countData< serverResponse.length()){
                              //  JSONObject  = serverResponse.getJSONObject(countData);
//                                    id = jsonObject.getString("id");
//                                    item_code= jsonObject.getString("item_code");
//                                    item_name= jsonObject.getString("item_name");
//                                    item_price= Double.parseDouble(jsonObject.getString("price"));
//                                    item_stock= Integer.parseInt(jsonObject.getString("stock"));
//                                    arrayList.add(new Data(id,item_code,item_name,item_price,item_stock));
//                                    countData++;
//
                          //   }

                                   // String resp = jsonObject.getString("server_response" );
//
//                                if (resp.equals("[{\"status\":\"OK\"}]")){
//                                    Toast.makeText(getApplicationContext(),"Login berhasil", Toast.LENGTH_SHORT).show();
//                                 //  Intent dashboardIntent = new Intent(Login.this ,Dashboard.class);
//
//                                 //begin session
//                                    Users user;
//                                    //Users user = new Users("1","0", username.toString(),"" , );
//                                    //SessionManagement sessionManagement = new SessionManagement(Login.this);
//                                   // sessionManagement.saveSession(user);
//
//                                   Intent dashboardIntent = new Intent(Login.this ,GetDataActivity.class);
//                                    startActivity(dashboardIntent);
//
//
//                                }else{
//                                    Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
//                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",username);
                    params.put("password",password);
                    return params;
                }
            };

            VolleyConnection.getInstance(Login.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            },2000);

        }else{
            Toast.makeText(getApplicationContext(),"no connection internet", Toast.LENGTH_SHORT).show();

        }

    }


    public boolean checkNetworkingConnection(){
        ConnectivityManager connectivityManager  =  (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}