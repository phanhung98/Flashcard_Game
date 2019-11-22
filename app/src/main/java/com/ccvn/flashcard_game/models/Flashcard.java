package com.ccvn.flashcard_game.models;

public class Flashcard {

    private String word;
    private String uploadpath;
    private String[] answeroptions;


    public String[] getAnsweroptions() {
        return answeroptions;
    }

    public void setAnsweroptions(String[] answeroptions) {
        this.answeroptions = answeroptions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUploadpath() {
        return uploadpath;
    }

    public void setUploadpath(String uploadpath) {
        this.uploadpath = uploadpath;
    }
}
