package com.victor.project.listr;

import java.util.ArrayList;

/**
 * Created by Seth on 3/30/2017.
 */

public class List {
    public double latitude;
    public double longitude;
    public String name;
    public boolean is_public = false;
    public List(){}
    public List(String name, double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
