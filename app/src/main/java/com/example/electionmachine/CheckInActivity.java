package com.example.electionmachine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CheckInActivity extends AppCompatActivity {

    /**
     * Стандартная реализация регистрации с использованием SharedPreferences
     * TODO Переделать под БД
     * */
    String login, password, ppasword;
    EditText Login, Password, PPasword;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setTitle("Регистрация");
        Login = (EditText)findViewById(R.id.login);
        Password = (EditText)findViewById(R.id.password);
        PPasword = (EditText)findViewById(R.id.ppassword);
    }
    public void comeBack(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void Check(View view){
        login = Login.getText().toString();
        password = Password.getText().toString();
        ppasword = PPasword.getText().toString();
        sp = getSharedPreferences("SP",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (password.equals(ppasword)) {
            editor.putString(login, password);
            editor.commit();
            Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Введённые пароли не совпадают", Toast.LENGTH_SHORT).show();
            Password.setText("");
            PPasword.setText("");
        }
    }
}
