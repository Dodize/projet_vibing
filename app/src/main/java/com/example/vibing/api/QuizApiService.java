package com.example.vibing.api;

import com.example.vibing.models.QuizResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface QuizApiService {
    @GET("api.php")
    Call<QuizResponse> getQuestions(
        @Query("amount") int amount,
        @Query("category") int category,
        @Query("difficulty") String difficulty,
        @Query("type") String type,
        @Query("lang") String lang
    );

    @GET("api.php")
    Call<QuizResponse> getRandomQuestions(
        @Query("amount") int amount,
        @Query("lang") String lang
    );
}