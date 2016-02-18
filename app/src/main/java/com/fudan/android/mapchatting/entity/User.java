package com.fudan.android.mapchatting.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Administrator on 2016/2/16 0016.
 */
public class User implements Serializable {
    private UUID userId;
    private String name;


    public User(){
        userId = UUID.randomUUID();
    }

    public User(String name){
        userId = UUID.randomUUID();
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
