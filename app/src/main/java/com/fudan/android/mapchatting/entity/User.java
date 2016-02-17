package com.fudan.android.mapchatting.entity;

import java.util.ArrayList;

/**
 * Created by Ethan on 16/2/17.
 */
public class User {
    private String userName;
    private String password;
    private String userInfo;
    private ArrayList<Discussion> attendedDiscussion;
    // TODO: 16/2/17 Portrait


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<Discussion> getAttendedDiscussion() {
        return attendedDiscussion;
    }

    public void setAttendedDiscussion(ArrayList<Discussion> attendedDiscussion) {
        this.attendedDiscussion = attendedDiscussion;
    }
}
