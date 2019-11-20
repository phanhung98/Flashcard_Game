package com.ccvn.flashcard_game.models;

public class Flashcard {

    private String word;
    private String upload_path;
    private String[] answer_options;


    public String[] getAnswer_options() {
        return answer_options;
    }

    public void setAnswer_options(String[] answer_options) {
        this.answer_options = answer_options;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUpload_path() {
        return upload_path;
    }

    public void setUpload_path(String upload_path) {
        this.upload_path = upload_path;
    }
}
