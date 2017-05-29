package com.example.electionmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class BlocksActivity extends AppCompatActivity {
    //СКОРЕЕ ВСЕГО НЕ ПРИГОДИТСЯ, НО ВОЗМОЖНО И НУЖНО БУДЕТ. ПОКА ОСТАВИТЬ!
    ArrayList<HashMap<String,String>> list;
    String[] from;
    int[] to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocks);
        setTitle("Info");
        list = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("ArrayForAdapter");
        from = new String[5];
        to = new int[]{R.id.hash, R.id.date, R.id.prevHash, R.id.blocknum, R.id.data};
        int i = 0;
        for (Map.Entry<String,String> entry: list.get(0).entrySet()){
            String s = entry.getKey();
            from[i] = s;
            i++;
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.adapter_item,from,to);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
