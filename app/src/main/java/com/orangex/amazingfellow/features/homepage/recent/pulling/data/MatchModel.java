package com.orangex.amazingfellow.features.homepage.recent.pulling.data;

/**
 * Created by orangex on 2017/11/3.
 */

public class MatchModel {
    public static final int TYPE_DOTA = 0x01;
    public static final int TYPE_OW = 0x02;
    public static final int TYPE_PUBG = 0x03;
    public static final int TYPE_CSGO = 0x04;
    

    
    private int type;
    private long startAt;
    private long duration;
    private String id;


    public MatchModel(int type, String id) {
        this.type = type;
        this.id = id;
    }
    public MatchModel(int type){
        this.type = type;
    }

    public MatchModel(int type, long startAt) {
        this.type = type;
        this.startAt = startAt;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public long getStartAt() {
        return startAt;
    }
    
    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    @Override
    public String toString() {
        return "MatchModel{" +
                "type=" + type +
                ", startAt=" + startAt +
                ", duration=" + duration +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    

}
