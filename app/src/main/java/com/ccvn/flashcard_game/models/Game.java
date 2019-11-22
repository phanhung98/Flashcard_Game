package com.ccvn.flashcard_game.models;

public class Game {

    private int id;
    private String name;
    private int highest_score;
    private String thumbnail;
    private int flashcard_total;

    public int getHighest_score() {
        return highest_score;
    }

    public void setHighest_score(int highest_score) {
        this.highest_score = highest_score;
    }

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFlashcard_total() {
        return flashcard_total;
    }

    public void setFlashcard_total(int flashcard_total) {
        this.flashcard_total = flashcard_total;
    }
}
