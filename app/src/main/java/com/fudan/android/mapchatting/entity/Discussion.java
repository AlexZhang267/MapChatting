package com.fudan.android.mapchatting.entity;

/**
 * Created by Ethan on 16/1/6.
 */
public class Discussion {
    private String mDiscussionId;
    private String mDiscussion;
    private String mUsername; // TODO: 16/1/6 Should be user
    private String mDate; // TODO: 16/1/6 Should be Data Type
    private double mXcoordinate;
    private double mYcoordinate;

    public Discussion(String discussionId, String discussion, String phoneNum, String date) {
        this.mDiscussionId = discussionId;
        this.mDiscussion = discussion;
        this.mUsername = phoneNum;
        this.mDate = date;
    }

    public String getDiscussionId() {
        return mDiscussionId;
    }

    public String getDiscussion() {
        return mDiscussion;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getDate() {
        return mDate;
    }
}
