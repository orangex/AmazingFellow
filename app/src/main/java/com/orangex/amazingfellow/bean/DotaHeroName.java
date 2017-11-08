package com.orangex.amazingfellow.bean;

/**
 * Created by orangex on 2017/11/8.
 */

public class DotaHeroName {
    private int id;
    private String name;
    private String locName;
    
    public DotaHeroName(int id, String name, String locName) {
        this.id = id;
        this.name = name;
        this.locName = locName;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLocName() {
        return locName;
    }
    
    public int getId() {
        return id;
    }
}
