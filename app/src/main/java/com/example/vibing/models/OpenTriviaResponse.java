package com.example.vibing.models;

import java.util.List;

public class OpenTriviaResponse {
    private int response_code;
    private List<OpenTriviaQuestion> results;

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<OpenTriviaQuestion> getResults() {
        return results;
    }

    public void setResults(List<OpenTriviaQuestion> results) {
        this.results = results;
    }

    public static class OpenTriviaQuestion {
        private String type;
        private String difficulty;
        private String category;
        private String question;
        private String correct_answer;
        private List<String> incorrect_answers;

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getCorrect_answer() { return correct_answer; }
        public void setCorrect_answer(String correct_answer) { this.correct_answer = correct_answer; }

        public List<String> getIncorrect_answers() { return incorrect_answers; }
        public void setIncorrect_answers(List<String> incorrect_answers) { this.incorrect_answers = incorrect_answers; }
    }
}