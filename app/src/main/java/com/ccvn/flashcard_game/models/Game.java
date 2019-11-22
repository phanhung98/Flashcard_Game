package com.ccvn.flashcard_game.models;

public class Game {

    private int id;
    private String name;
    private int highestScore;
    private String thumbnail;
    private int flashcardTotal;

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getFlashcardTotal() {
        return flashcardTotal;
    }

    public void setFlashcardTotal(int flashcardTotal) {
        this.flashcardTotal = flashcardTotal;
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


}
