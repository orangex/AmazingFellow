package com.orangex.amazingfellow.features.homepage.recent.pulling.data;

/**
 * Created by orangex on 2017/11/3.
 */
public class MatchModel {
    public static final int TYPE_DOTA = 0x01;
    public static final int TYPE_OW = 0x02;
    public static final int TYPE_PUBG = 0x03;
    public static final int TYPE_CSGO = 0x04;
    private Long endAt;
    
    
    public Long getEndAt() {
        return endAt;
    }
    
    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }
}
