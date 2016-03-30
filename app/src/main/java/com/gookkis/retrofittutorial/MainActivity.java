package com.gookkis.retrofittutorial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gookkis.retrofittutorial.adapter.AdapterQuestion;
import com.gookkis.retrofittutorial.model.Item;
import com.gookkis.retrofittutorial.model.QuestionModel;
import com.gookkis.retrofittutorial.service.StackExchangeAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private Button btnProses;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisialisasi view
        btnProses = (Button) findViewById(R.id.btn_proses);
        recyclerView = (RecyclerView) findViewById(R.id.rv_question);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //menghilangkan progress bar
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //method button on click
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menampilkan progress bar
                progressBar.setVisibility(View.VISIBLE);

                //membuat string untuk query dapat juga membuat edittext dan mengambil value nya
                String order = "desc";
                String sort = "activity";
                String tag = "android";
                String site = "stackoverflow";

                //membuat service
                StackExchangeAPI apiService =
                        StackExchangeAPI.client.create(StackExchangeAPI.class);

                //melakukan call dengan method getQuestion.
                Call<QuestionModel> call = apiService.getQuestion(order, sort, tag, site);

                //proses call
                call.enqueue(new Callback<QuestionModel>() {
                    @Override
                    public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {

                        //merubah respon body menjadi model
                        QuestionModel QuestionModel = response.body();

                        //instance adapter dengan object Item (QuestionModel.getItems)
                        AdapterQuestion adapter = new AdapterQuestion(getApplicationContext(), QuestionModel.getItems(),
                                new AdapterQuestion.OnItemClickListener() {
                                    @Override
                                    public void onClick(final Item item) {
                                        //method on click list membuka url
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(item.getLink()));
                                        startActivity(i);
                                    }
                                });
                        //menghilangkan progres bar
                        progressBar.setVisibility(View.INVISIBLE);

                        //men-set adapter pada recycler view
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<QuestionModel> call, Throwable t) {
                        // Log error
                        Log.e(TAG, "onFailure: ", t.fillInStackTrace());

                        //menghilangkan progres bar
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }


}
