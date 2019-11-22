package com.ccvn.flashcard_game.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListGame {
    @SerializedName("Game")
    @Expose
    private List<Game> games = null;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
