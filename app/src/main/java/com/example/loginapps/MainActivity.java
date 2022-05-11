package com.example.loginapps;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText name,username,email,password,checkPassword;
    Button login,register;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)  findViewById(R.id.edit_nameRegister);
        username = (EditText)  findViewById(R.id.edit_usernameRegister);
        email = (EditText)  findViewById(R.id.edit_emailRegister);
        password = (EditText)  findViewById(R.id.edit_passwordRegister);
        checkPassword = (EditText)  findViewById(R.id.edit_confirmPasswordRegister);
        login= (Button)  findViewById(R.id.btn_loginRegister);
        register= (Button)  findViewById(R.id.btn_registerRegister);
        progressDialog = new ProgressDialog( MainActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this ,Login.class);
                startActivity(loginIntent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = name.getText().toString();
                String sUsername = username.getText().toString();
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                String sCheckPassword = checkPassword.getText().toString();

                if(sPassword.equals(sCheckPassword) && !sPassword.equals("")){
                        CreateDataToServer(sName,sUsername,sEmail,sPassword);
                        Intent loginIntent = new Intent(MainActivity.this, Login.class);
                        startActivity(loginIntent);
                }else{
                    Toast.makeText(getApplicationContext(),"Passoword Unmatch", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void CreateDataToServer(final String name, final String username,final String email, final String password){
        if  (checkNetworkingConnection()){
                progressDialog.show();
                StringRequest stringRequest =  new StringRequest(Request.Method.POST,
                        DbContract.SEVER_REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject= new JSONObject(response);
                                    String resp = jsonObject.getString("server_response" );
                                    if (resp.equals("[{\"status\":\"OK\"}]")){
                                        Toast.makeText(getApplicationContext(),"registrasi berhasil", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
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
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("name", name);
                        param.put("username", username);
                        param.put("email", email);
                        param.put("password", password);

                        return param;
                    }
                };
            VolleyConnection.getInstance(MainActivity.this).addToRequestQue(stringRequest);

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


    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {

        //SessionManagement sessionManagement = new SessionManagement(Ma.this);
       // int userID = sessionManagement.getSession();

      //  if(userID != -1){
            //user id logged in and move to main activity
        //    moveToMainActivity();
       // }else{
            //do nothing
       // }

    }
}