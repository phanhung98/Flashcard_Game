package com.ccvn.flashcard_game.models;

import java.util.List;

public class Game {

    private int id;
    private String name;
    private double score;
    private String upload_path;
    private int flashcard_total;
    private List<Integer> flashcard_id = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUpload_path() {
        return upload_path;
    }

    public void setUpload_path(String upload_path) {
        this.upload_path = upload_path;
    }

    public int getFlashcard_total() {
        return flashcard_total;
    }

    public void setFlashcard_total(int flashcard_total) {
        this.flashcard_total = flashcard_total;
    }

    public List<Integer> getFlashcard_id() {
        return flashcard_id;
    }

    public void setFlashcard_id(List<Integer> flashcard_id) {
        this.flashcard_id = flashcard_id;
    }
}
