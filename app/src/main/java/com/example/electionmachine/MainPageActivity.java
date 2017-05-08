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
        Integer k = Integer.valueOf(editText.getText().toString());

        list = new ArrayList<HashMap<String, String>>(k);
        if (chain.isEmpty()){
            chain.push(new Block("Genesis block"));
            k--;
            createAdapter(chain.peek());
        }
        for (int i = 1; i < k; i++){
            chain.push(new Block("Это блок номер "+i, chain.peek()));
            createAdapter(chain.peek());
        }
        Intent i = new Intent(this,BlocksActivity.class);
        i.putExtra("ArrayForAdapter", list);
        startActivity(i);



    }

    public void createAdapter(Block block){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("Блок номер ", String.valueOf(block.getIndex()));
        map.put("Данные в блоке: ", block.getData());
        map.put("Hash code блока: ", String.valueOf(block.getHash()));
        map.put("Hash code предыдущего блока: ", String.valueOf(block.getPreviousHash()));
        map.put("Дата и время создания блока: ", block.getTimestamp().toString());
        list.add(0,map);
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        super.onStop();
    }
}
