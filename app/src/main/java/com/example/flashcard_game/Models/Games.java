package com.example.flashcard_game.Models;

public class Games {

    private String game_name;
    private String thumbnail;
    private int flashcard_count;


    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFlashcard_count() {
        return flashcard_count;
    }

    public void setFlashcard_count(int flashcard_count) {
        this.flashcard_count = flashcard_count;
    }

}
