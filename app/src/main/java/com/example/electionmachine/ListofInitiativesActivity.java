package com.example.electionmachine;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListofInitiativesActivity extends AppCompatActivity {

    ElectionService service;
    String[] descriptionsOfinitiatives;
    Response<List<Initiative>> listResponse;
    Call<List<Initiative>> listCall;
    List<Initiative> initiatives = new ArrayList<>();
    Initiative initiativeForDiagram;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = new Intent(this,DiagramActivity.class);
        setContentView(R.layout.activity_listof_initiatives);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InitiativeCreationActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        listCall = service.getAllInitives();
        listCall.enqueue(new Callback<List<Initiative>>() {
            @Override
            public void onResponse(Response<List<Initiative>> response) {
                initiatives = response.body();
                Log.e("INITIATIVES", initiatives.size()+"");
                descriptionsOfinitiatives = new String[initiatives.size()];
                for (int i = 0; i < initiatives.size(); i++){
                    descriptionsOfinitiatives[i] = initiatives.get(i).description;
                }
                ListView listView = (ListView)findViewById(R.id.listOfInitiative);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListofInitiativesActivity.this,android.R.layout.simple_list_item_1,descriptionsOfinitiatives);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String result = ((TextView) view).getText().toString();
                        for (int i = 0; i < initiatives.size();i++){
                            if (initiatives.get(i).description.equals(result)){
                                initiativeForDiagram = initiatives.get(i);
                                break;
                            }
                        }
                        Gson gson = new Gson();
                        String initiativeJSON = gson.toJson(initiativeForDiagram);
                        i.putExtra("Initiative", initiativeJSON);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });



    }

}
