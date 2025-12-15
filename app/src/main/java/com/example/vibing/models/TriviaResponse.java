package com.example.vibing.models;

import java.util.List;

public class TriviaResponse {
    private List<TriviaQuestion> results;

    public List<TriviaQuestion> getResults() {
        return results;
    }

    public void setResults(List<TriviaQuestion> results) {
        this.results = results;
    }

    public static class TriviaQuestion {
        private String category;
        private String id;
        private String correctAnswer;
        private List<String> incorrectAnswers;
        private QuestionText question;
        private List<String> tags;
        private String type;
        private String difficulty;

        // Getters and setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

        public List<String> getIncorrectAnswers() { return incorrectAnswers; }
        public void setIncorrectAnswers(List<String> incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }

        public QuestionText getQuestion() { return question; }
        public void setQuestion(QuestionText question) { this.question = question; }

        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

        public static class QuestionText {
            private String text;

            public String getText() { return text; }
            public void setText(String text) { this.text = text; }
        }
    }
}