package com.example.vibing.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class QuizQuestion {
    private String id;
    private String type;
    private Title title;
    private String description;
    private int difficulty;
    private Theme theme;
    private List<Answer> answers;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Title getTitle() { return title; }
    public void setTitle(Title title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public Theme getTheme() { return theme; }
    public void setTheme(Theme theme) { this.theme = theme; }

    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    // Helper method to get French question text
    public String getQuestion() { 
        return title != null && title.getFr() != null ? title.getFr() : ""; 
    }

    // Helper class for title
    public static class Title {
        private String fr;
        private String en;

        public String getFr() { return fr; }
        public void setFr(String fr) { this.fr = fr; }

        public String getEn() { return en; }
        public void setEn(String en) { this.en = en; }
    }

    // Helper class for theme
    public static class Theme {
        private String title;
        private String code;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    // Helper class for answers
    public static class Answer {
        private String id;
        private Title title;
        @SerializedName("isValid")
        private boolean isValid;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public Title getTitle() { return title; }
        public void setTitle(Title title) { this.title = title; }

        public boolean isValid() { return isValid; }
        public void setValid(boolean valid) { isValid = valid; }

        // Helper method to get French answer text
        public String getAnswerText() { 
            return title != null && title.getFr() != null ? title.getFr() : ""; 
        }
    }
}