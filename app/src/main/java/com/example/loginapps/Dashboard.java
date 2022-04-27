package com.example.loginapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapps.databinding.ActivityDashboardBinding;

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

public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Data> arrayList = new ArrayList<>();
    String DATA_JSON_STRING, data_json_string;
    ProgressDialog progressDialog;
    int countData = 0;


   private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = (RecyclerView) findViewById(R.id.list_data);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
       // adapter= new RecyclerAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(Dashboard.this);

        //read data
        getJSON();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                readDatafromServer();
            }
        },1000);

      BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



    }


    public void readDatafromServer(){
        if(checknetworkConnection()){
                arrayList.clear();
                try {
                    JSONObject object = new JSONObject(data_json_string);
                    JSONArray serverResponse = object.getJSONArray("server_response");
                    String id,item_code,item_name;
                    double item_price;
                    int item_stock;

                    while(countData< serverResponse.length()){
                        JSONObject jsonObject = serverResponse.getJSONObject(countData);
                        id = jsonObject.getString("id");
                        item_code= jsonObject.getString("item_code");
                        item_name= jsonObject.getString("item_name");
                        item_price= Double.parseDouble(jsonObject.getString("item_price"));
                        item_stock= Integer.parseInt(jsonObject.getString("item_stock"));

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

    public boolean checknetworkConnection(){
        ConnectivityManager connectivityManager =  (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());

    }

    public void getJSON(){

        new BackgroundTask().execute();

    }

    class BackgroundTask extends AsyncTask<Void,Void,String>{

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



}