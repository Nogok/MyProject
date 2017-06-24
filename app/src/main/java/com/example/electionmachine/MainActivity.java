package com.example.electionmachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /**
     *Самая начальная активность
     * Позволяет залогиниться или зарегестрироваться.
     */


    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button)findViewById(R.id.SignIn);


    }

    public void LogIn(View view){
        Intent i = new Intent(this,MainPageActivity.class);
        startActivity(i);
    }


}