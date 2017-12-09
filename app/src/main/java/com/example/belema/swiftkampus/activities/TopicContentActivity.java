package com.example.belema.swiftkampus.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.belema.swiftkampus.R;
import com.example.belema.swiftkampus.ServiceGenerator;
import com.example.belema.swiftkampus.apiMethods.ApiMethods;
import com.example.belema.swiftkampus.gson.TopicContent;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicContentActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);
        Intent intent = getIntent();
        String name = intent.getStringExtra("topicName");
        setTitle(name);

        //final ProgressBar progressBar = findViewById(R.id.contentLoader);
         pdfView = findViewById(R.id.pdfView);

        int topicId = intent.getIntExtra("topicId", 0);

        ApiMethods content = ServiceGenerator.INSTANCE.createService(ApiMethods.class);
        Call<ArrayList<TopicContent>> getContent
                = content.getContent(String.valueOf(topicId));

        getContent.enqueue(new Callback<ArrayList<TopicContent>>() {
            @Override
            public void onResponse(Call<ArrayList<TopicContent>> call, Response<ArrayList<TopicContent>> response) {
                //progressBar.setVisibility(View.GONE);
                System.out.println(response.message());
                if (response.isSuccessful()){
                    ArrayList<TopicContent> result = response.body();
                    try {
                        String fileLocation = result.get(0).getFileLocation();
                        System.err.println(fileLocation);
                        new RetrievePdfStream().execute("https://unibenportal.azurewebsites.net/MaterialUpload/" + fileLocation);
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopicContent>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                TopicContentActivity.super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

     class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}
