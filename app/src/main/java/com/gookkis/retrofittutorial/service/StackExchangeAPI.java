package com.gookkis.retrofittutorial.service;

import com.gookkis.retrofittutorial.model.QuestionModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by herikiswanto on 3/30/16.
 */
public interface StackExchangeAPI {

    //variable base URL
    public static String baseURL = "https://api.stackexchange.com/2.2/";

    //membuat Instance Retrofit
    Retrofit client = new Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //melakukan get dan query pada url
    @GET("/search?")
    public Call<QuestionModel> getQuestion(@Query("order") String order,
                                           @Query("sort") String sort,
                                           @Query("tagged") String tagged,
                                           @Query("site") String site);

}
