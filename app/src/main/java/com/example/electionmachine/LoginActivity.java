package com.example.electionmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String login, password;
    EditText Login, Password;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Loging In");
        Login = (EditText)findViewById(R.id.login);
        Password = (EditText)findViewById(R.id.password);
    }
    public void comeBack(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void Log_in(View view){
        login = Login.getText().toString();
        password = Password.getText().toString();
        sp = getSharedPreferences("SP",MODE_PRIVATE);
        Map<String,?> m = sp.getAll();
        boolean b = m.containsKey(login);
        if (b){
            boolean bb = m.get(login).equals(password);
            if (bb){
                Intent i = new Intent(this,MainPageActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(this,"Неверный пароль", Toast.LENGTH_SHORT).show();
                Password.setText("");
            }

        }
        else{
            Toast.makeText(this,"Такой логин не найден", Toast.LENGTH_SHORT).show();
        }
    }


}
