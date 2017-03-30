package com.victor.project.listr;

/**
 * Created by Seth on 3/29/2017.
 */

public class List {
    private String id;
    private String name;
    public boolean editable;

    public List(String id, String name, boolean editable){
        this.id = id;
        this.name = name;
        this.editable = editable;
    }
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
}
