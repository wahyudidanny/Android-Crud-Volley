package com.example.loginapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetDataActivity extends AppCompatActivity implements CreateDataDialog.CreateDialogListener, RecyclerAdapter.OnNoteListener {

    private static final String TAG = "Some activity";
    Button createData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Data> arrayList = new ArrayList<>();
    String DATA_JSON_STRING, data_json_string;
    SwipeRefreshLayout refreshLayout;
    ProgressDialog progressDialog;
    int countData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        recyclerView = (RecyclerView) findViewById(R.id.list_data);
        createData = (Button) findViewById(R.id.btn_addData);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter= new RecyclerAdapter(arrayList,this );
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(GetDataActivity.this);
        //refreshLayout = findViewById(R.id.refreshLayout);
        //read data
        getJSON();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                readDataFromServer();
            }
        },1000);

        createData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateDialog();
            }
        });

//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//              refreshLayout.setRefreshing(false);
//            }
//        });

    }


    public void openCreateDialog(){

        CreateDataDialog createDataDialog = new CreateDataDialog();
        createDataDialog.show(getSupportFragmentManager(),"Add Item");

       // UpdateDataDialog updateDataDialog = new UpdateDataDialog();
       // updateDataDialog.show(getSupportFragmentManager(),"");
    }

    public void readDataFromServer(){
        if(checkNetworkConnection()){
            arrayList.clear();
            try {
                JSONObject object = new JSONObject(data_json_string);
                JSONArray serverResponse = object.getJSONArray("server_response");
                String id,item_code,item_name;
                double item_price;
                int item_stock;
                Button updateData;

                while(countData< serverResponse.length()){
                    JSONObject jsonObject = serverResponse.getJSONObject(countData);
                    id = jsonObject.getString("id");
                    item_code= jsonObject.getString("item_code");
                    item_name= jsonObject.getString("item_name");
                    item_price= Double.parseDouble(jsonObject.getString("price"));
                    item_stock= Integer.parseInt(jsonObject.getString("stock"));
                    arrayList.add(new Data(id,item_code,item_name,item_price,item_stock));
                    countData++;

                }

                countData = 0;
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager =  (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());

    }

    public void getJSON(){

        new GetDataActivity.BackgroundTask().execute();

    }

    @Override
    public void onNoteClick(int position) {
    /*
        Log.d(TAG, "onNoteClick: " + position);
        Intent intent  = new Intent(this,UpdateDataDialog.class);
        startActivity(intent);*/

        ArrayList<Data> newArrayList = new ArrayList<>();
        newArrayList.add(arrayList.get(position));

        UpdateDataDialog fragment =  new UpdateDataDialog();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        //data.putSerializable("Data",newArrayList);
        data.putParcelableArrayList("Data",newArrayList);
        fragment.setArguments(data);

        //UpdateDataDialog update = new UpdateDataDialog();
        fragment.show(getSupportFragmentManager(),"Add Item");


        //fragmentTransaction.replace(fragment).commit();
        /*ArrayList<Data> newArrayList = new ArrayList<>();
        newArrayList.add(arrayList.get(position));
        Intent intent  = new Intent(this,SomeActivity.class);
        intent.putExtra("data",newArrayList);
        startActivity(intent);*/
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = DbContract.SEVER_GET_DATA_URL;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL(json_url);
                HttpURLConnection httpurlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputstream = httpurlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
                StringBuilder stringBuilder = new StringBuilder();

                while((DATA_JSON_STRING = bufferedReader.readLine()) != null){
                    stringBuilder.append(DATA_JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputstream.close();
                httpurlConnection.disconnect();

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            data_json_string =result;
        }

    }

    public void createDataToServer(final String item_code,final String item_name,final double item_price,final int item_stock) {
        if (checkNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SEVER_POST_DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                getJSON();
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("OK")){
                                    Toast.makeText(getApplicationContext(),"Berhasil Menambah Data",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getJSON();
                    Toast.makeText(getApplicationContext(),"Error, please check your Volley Connection",Toast.LENGTH_SHORT).show();
                }
            }){

                @Override
                protected Map<String,String>getParams() throws AuthFailureError {

                       Map<String,String>  param = new HashMap<>();
                        param.put("item_code",item_code);
                        param.put("item_name",item_name);
                        param.put("price", String.valueOf(item_price));
                        param.put("stock", String.valueOf(item_stock));

                    return param;
                }
            };

            VolleySingleton.getInstance(GetDataActivity.this).addRequestQue(stringRequest);
            getJSON();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readDataFromServer();
                    progressDialog.cancel();
                }
            },2000);
        }else{
            Toast.makeText(getApplicationContext(),"No Connection Internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void post(String itemCode,String itemName,double itemPrice,int itemStock) {
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        createDataToServer(itemCode,itemName,itemPrice,itemStock);
    }

}