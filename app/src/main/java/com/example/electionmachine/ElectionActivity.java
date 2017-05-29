package com.example.electionmachine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionActivity extends AppCompatActivity {

    private static final String baseUrl = "https://secure-beyond-82089.herokuapp.com";
    ElectionService service;
    RadioGroup radioGroup;
    TextView textView;
    Response<List<Initiative>> response;
    Response<Vote> voteResponse;
    int VoteFromUser;
    List<Initiative> list;
    DigitalSign ds;
    KeyPair kp;
    Initiative initiative;
    Vote vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);
        textView = (TextView) findViewById(R.id.tv);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ElectionActivity.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ElectionService.class);
        try {
             ds = new DigitalSign();
             kp = DigitalSign.DSA.generateKeyPair((long) 17);
            Toast.makeText(this, "Pair Created", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(this, "Pair is not created!", Toast.LENGTH_SHORT).show();
            Log.e("KEY_ERROR", e.toString());
        }

    }

    public void request(View view) {
        new MyAsyncTask().execute("");

    }

    public void createElection(View view) {
        list = response.body();
        initiative = list.get(list.size()-1);
        for(int i = 0; i < initiative.variants.length; i++) {
            RadioButton r = new RadioButton(this);
            r.setText(initiative.variants[i]);
            radioGroup.addView(r);
        }

    }

    public void Votation(View view) {
        int n = radioGroup.getCheckedRadioButtonId();
        RadioButton r = (RadioButton)findViewById(n);
        String variant = r.getText().toString();
        for(int i = 0; i < initiative.variants.length; i++){
            if (variant.equals(initiative.variants[i])){
                VoteFromUser = i+1;
            }
        }
        try {
            Toast.makeText(this, "Try Block created", Toast.LENGTH_SHORT).show();
            byte[] buff = kp.getPublic().getEncoded();
            Log.d("Election", "key :"+Arrays.toString(buff));
            Toast.makeText(this, "Buff created", Toast.LENGTH_SHORT).show();
            vote = new Vote(initiative,VoteFromUser, Base64.encodeToString(buff,Base64.DEFAULT));
            Gson gson = new Gson();
            String s = gson.toJson(vote);
            buff = DigitalSign.DSA.signData(s.getBytes(), kp.getPrivate());
            Log.d("Election", "sign:"+Arrays.toString(buff));
            Toast.makeText(this, "Before Base64", Toast.LENGTH_SHORT).show();
            vote.dsaSign = Base64.encodeToString(buff,Base64.DEFAULT).toString();
            Toast.makeText(this, "BASE64 is working!", Toast.LENGTH_SHORT).show();
            Call<Void> call = service.createVote(vote);
            Log.d("Election","vote: "+gson.toJson(vote));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {

                    Toast.makeText(ElectionActivity.this,"DONE!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(ElectionActivity.this,"FAIL!", Toast.LENGTH_SHORT).show();
                }});
            Toast.makeText(this, "Try block ended", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(this, "Exception cought!", Toast.LENGTH_SHORT).show();
        }
    }



     class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Call<List<Initiative>> call = service.getAllInitives();
                response = call.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText("Данные получены!");
        }
    }
}