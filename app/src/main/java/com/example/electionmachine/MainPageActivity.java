package com.example.electionmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MainPageActivity extends AppCompatActivity {
    Stack<Block> chain;
    SharedPreferences sharedPreferences;
    EditText editText;

    ArrayList<HashMap<String,String>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        chain = new Stack<Block>();
        sharedPreferences = getSharedPreferences("Chain",MODE_PRIVATE);
        editText = (EditText)findViewById(R.id.amount);
    }

    public void crb(View view){
    }

    public void createAdapter(Block block){

    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
