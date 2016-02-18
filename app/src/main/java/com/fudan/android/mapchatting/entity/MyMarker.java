package com.fudan.android.mapchatting.entity;

import com.amap.api.mapcore.ad;
import com.amap.api.maps.model.Marker;

/**
 * Created by Administrator on 2016/2/16 0016.
 */
public class MyMarker {
    private Marker marker;
    private User user;

    //<editor-fold desc="Constructors">
    MyMarker(){

    }

    MyMarker(Marker marker){
        this.marker = marker;
    }

    MyMarker(User user){
        this.user = user;
    }

    MyMarker(Marker marker, User user){
        this.marker = marker;
        this.user = user;
    }
    //</editor-folder>

    //<editor-fold desc="getter and setter">
    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //</editor-fold>
}
