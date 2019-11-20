package com.ccvn.flashcard_game.models;

public class Game {

    private String name;
    private String thumbnail;
    private int Flashcard_count;


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

    public int getFlashcard_count() {
        return Flashcard_count;
    }

    public void setFlashcard_count(int flashcard_count) {
        Flashcard_count = flashcard_count;
    }
}
