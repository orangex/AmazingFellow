package com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota;

import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;

import java.util.List;

/**
 * Created by orangex on 2017/11/3.
 */

public class DotaMatchModel extends MatchModel {

    public static final Integer GLORY_KILL = 1;
    public static final Integer GLORY_DESTROY = 2;
    public static final Integer GLORY_GOLD = 3;
    public static final Integer GLORY_HEALTH = 4;
    public static final Integer GLORY_ASSIST = 5;
    
    public static final int MVP_TYPE_MVP = 0x10;
    public static final int MVP_TYPE_GLORIOUS = 0x11;
    
    public static final int GAME_LEVEL_NORMAL = 1;
    public static final int GAME_LEVEL_HIGH = 2;
    public static final int GAME_LEVEL_VERYHIGN = 3;
    
    private String steamID64;
    private String playerName;
    private int hero;
    private int kills;
    private int deaths;
    private int assits;
    private int gold;
    private int gpm;
    private int epm;
    private int scaledDamage;
    private List<Integer> glorys;
    

    
    private int mvpType;
    
    
    /**
     * 1 = Normal ,2 = High ,3 =Very High
     */
    private int gameLevel;
    private String fightRate;
    private String damageRate;
    private List<Integer> equipmentList;
    /**
     * in minute
     */
    private int lastTime;
    private String efficiency;
    private int dhb;
    private String timeOffsetDesc;
    private int pageInDotaMore;
    
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
    
    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }
    public int getLastTime() {
        return lastTime;
    }
    public String getEfficiency() {
        return efficiency;
    }
    
    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }
    
    public int getDhb() {
        return dhb;
    }
    
    public void setDhb(int dhb) {
        this.dhb = dhb;
    }
    
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public String getTimeOffsetDesc() {
        return timeOffsetDesc;
    }
    
    public void setTimeOffsetDesc(String timeOffsetDesc) {
        this.timeOffsetDesc = timeOffsetDesc;
    }
    
    @Override
    public String toString() {
        return "DotaMatchModel{" +
                "steamID64='" + steamID64 + '\'' +
                ", playerName='" + playerName + '\'' +
                ", hero=" + hero +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", assits=" + assits +
                ", gold=" + gold +
                ", gpm=" + gpm +
                ", epm=" + epm +
                ", scaledDamage=" + scaledDamage +
                ", glorys=" + glorys +
                ", gameLevel=" + gameLevel +
                ", fightRate='" + fightRate + '\'' +
                ", damageRate='" + damageRate + '\'' +
                ", equipmentList=" + equipmentList +
                ", lastTime=" + lastTime +
                ", efficiency='" + efficiency + '\'' +
                ", dhb=" + dhb +
                ", timeOffsetDesc='" + timeOffsetDesc + '\'' +
                '}';
    }
    
    public int getPageInDotaMore() {
        return pageInDotaMore;
    }
    
    public void setPageInDotaMore(int pageInDotaMore) {
        this.pageInDotaMore = pageInDotaMore;
    }
    
    public int getMvpType() {
        return mvpType;
    }
    
    public void setMvpType(int mvpType) {
        this.mvpType = mvpType;
    }
    
    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }
    public int getGameLevel() {
        return gameLevel;
    }
    
}
