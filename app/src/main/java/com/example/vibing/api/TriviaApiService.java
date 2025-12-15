package com.example.vibing.api;

import com.example.vibing.models.TriviaResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaApiService {
    @GET("v2/questions")
    Call<TriviaResponse> getQuestions(
        @Query("limit") int limit,
        @Query("categories") String categories,
        @Query("difficulty") String difficulty
    );

    @GET("v2/questions")
    Call<TriviaResponse> getRandomQuestions(
        @Query("limit") int limit
    );
}