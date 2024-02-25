package com.dk7aditya.firebaseimagerecognitionthroughml;
import com.dk7aditya.firebaseimagerecognitionthroughml.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("country") String country, @Query("q") String query, @Query("apiKey") String apiKey);
}
