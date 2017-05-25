package com.example.electionmachine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionActivity extends AppCompatActivity {
    private static final String baseUrl = "https://secure-beyond-82089.herokuapp.com";
    ElectionService service;
    RadioGroup radioGroup;
    TextView textView;
    Response<Election> response;
    String VoteFromUser;

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



    }

    public void request(View view) {
        new MyAsyncTask().execute("");

    }

    public void createElection(View view) {
        Election el = response.body();
        for(int i = 0; i < el.candidates.size(); i++) {
            RadioButton r = new RadioButton(this);
            r.setText(el.candidates.get(i).name);
            radioGroup.addView(r);
        }

    }

    public void Votation(View view) {
        int n = radioGroup.getCheckedRadioButtonId();
        RadioButton r = (RadioButton)findViewById(n);
        VoteFromUser = r.getText().toString();
        new AsyncTaskSendVote().execute("");

    }

    class AsyncTaskSendVote extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            MyDSA dsa = new MyDSA(VoteFromUser);
            Call<MyDSA> c = service.sendDSA(dsa.y,dsa.getS(),dsa.getR(),dsa.getQ(),dsa.getP(),dsa.getG());
            Call<Vote> call = service.createVote(VoteFromUser);

            try {
                Response<Vote> r = call.execute();
                Response<MyDSA> re = c.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


     class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Call<Election> call = service.getElection();
            try {
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