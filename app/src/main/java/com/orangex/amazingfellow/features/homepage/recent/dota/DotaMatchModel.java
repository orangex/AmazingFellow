package com.orangex.amazingfellow.features.homepage.recent.dota;

import com.orangex.amazingfellow.features.homepage.recent.MatchModel;

import java.util.List;

/**
 * Created by orangex on 2017/11/3.
 */

public class DotaMatchModel extends MatchModel {
    private static final int GLORY_KILL = 1;
    private static final int GLORY_ASSIS = 2;
    private static final int GLORY_ = 3;

    private String steamID64;
    private int hero;
    private int kills;
    private int deaths;
    private int assits;
    private int gold;
    private int gpm;
    private int epm;
    private int scaledDamage;
    private List<Integer> glorys;
    /**
     * 1 = Normal ,2 = High ,3 =Very High
     */
    private int level;
    private String fightRate;
    private String damageRate;
    private List<Integer> equipmentList;
    /**
     * in minute
     */
    private int lastTime;
    
    public DotaMatchModel(int type) {
        super(type);
    }
    
    
    public int getHero() {
        return hero;
    }
    
    public void setHero(int hero) {
        this.hero = hero;
    }
    
    public int getKills() {
        return kills;
    }
    
    public void setKills(int kills) {
        this.kills = kills;
    }
    
    public int getDeaths() {
        return deaths;
    }
    
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    
    public int getAssits() {
        return assits;
    }
    
    public void setAssits(int assits) {
        this.assits = assits;
    }
    
    public int getGold() {
        return gold;
    }
    
    public void setGold(int gold) {
        this.gold = gold;
    }
    
    public int getGpm() {
        return gpm;
    }
    
    public void setGpm(int gpm) {
        this.gpm = gpm;
    }
    
    public int getEpm() {
        return epm;
    }
    
    public void setEpm(int epm) {
        this.epm = epm;
    }
    
    public int getScaledDamage() {
        return scaledDamage;
    }
    
    public void setScaledDamage(int scaledDamage) {
        this.scaledDamage = scaledDamage;
    }

    public List<Integer> getGlorys() {
        return glorys;
    }

    public void setGlorys(List<Integer> glorys) {
        this.glorys = glorys;
    }

    public String getSteamID64() {
        return steamID64;
    }

    public void setSteamID64(String steamID64) {
        this.steamID64 = steamID64;
    }
    
    @Override
    public String toString() {
        return "DotaMatchModel{" +
                "steamID64='" + steamID64 + '\'' +
                ", hero=" + hero +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", assits=" + assits +
                ", gold=" + gold +
                ", gpm=" + gpm +
                ", epm=" + epm +
                ", scaledDamage=" + scaledDamage +
                ", glorys=" + glorys +
                ", level=" + level +
                ", fightRate='" + fightRate + '\'' +
                ", damageRate='" + damageRate + '\'' +
                ", equipmentList=" + equipmentList +
                ", lastTime=" + lastTime +
                '}';
    }
}
