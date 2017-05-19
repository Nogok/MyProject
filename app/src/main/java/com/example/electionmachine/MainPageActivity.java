package com.example.electionmachine;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;


public class MainPageActivity extends AppCompatActivity {
    private static final String baseUrl = "https://secure-beyond-82089.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }


    public void openElectionActivity(View view) {
        Intent i = new Intent(this,ElectionActivity.class);
        startActivity(i);
    }
}
