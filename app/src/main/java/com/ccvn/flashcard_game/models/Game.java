package com.ccvn.flashcard_game.models;

public class Game {

    private int id;
    private String name;
    private int highestscore;
    private String thumbnail;
    private int flashcardtotal;

    public int getHighestscore() {
        return highestscore;
    }

    public void setHighestscore(int highestscore) {
        this.highestscore = highestscore;
    }

    public int getFlashcardtotal() {
        return flashcardtotal;
    }

    public void setFlashcardtotal(int flashcardtotal) {
        this.flashcardtotal = flashcardtotal;
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
