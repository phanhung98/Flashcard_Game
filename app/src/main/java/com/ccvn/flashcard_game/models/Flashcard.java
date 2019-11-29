package com.ccvn.flashcard_game.models;

import java.util.List;

public class Flashcard {

    private int id;
    private String word;
    private int type_id;
    private String upload_path;
    private String right_answer;
    private List<String> value = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getUpload_path() {
        return upload_path;
    }

    public void setUpload_path(String upload_path) {
        this.upload_path = upload_path;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
