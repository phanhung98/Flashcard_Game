package com.ccvn.flashcard_game.models;

import java.util.List;

public class Flashcard {

    private int id;
    private String word;
    private int typeId;
    private String uploadPath;
    private String rightAnswer;
    private List<String> answerOption = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public List<String> getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(List<String> answerOption) {
        this.answerOption = answerOption;
    }
}
