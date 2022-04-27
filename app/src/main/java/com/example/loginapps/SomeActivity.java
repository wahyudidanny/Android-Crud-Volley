package com.example.loginapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class SomeActivity extends AppCompatActivity {
    private static final String TAG = "SomeActivity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        ArrayList<Data> nameValuePairs = (ArrayList<Data>) myIntent.getSerializableExtra("data");

    }
}