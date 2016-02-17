package com.fudan.android.mapchatting.entity;

/**
 * Created by Ethan on 16/1/6.
 */
public class Content {
    private String sentence;
    private String mUsername;

    public Content(String sentence, String phoneNum) {
        this.sentence = sentence;
        this.mUsername = phoneNum;
    }

    public String getSentence() {
        return sentence;
    }

    public String getUsername() {
        return mUsername;
    }
}
