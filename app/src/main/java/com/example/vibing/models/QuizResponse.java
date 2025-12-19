package com.example.vibing.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuizResponse {
    @SerializedName("data")
    private List<QuizQuestion> data;

    public List<QuizQuestion> getData() {
        return data;
    }

    public void setData(List<QuizQuestion> data) {
        this.data = data;
    }
}