package com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota;

import com.orangex.amazingfellow.db.converters.ListIntegerConverter;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;


/**
 * Created by orangex on 2017/11/3.
 */
@Entity(active = true, generateConstructors = false)
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
    @Id
    private Long matchID;
    private String steamID64;
    private String playerName;
    private Long endAt;
    private int hero;
    private int kills;
    private int deaths;
    private int assits;
    private int gold;
    private int gpm;
    private int epm;
    private int scaledDamage;
    private boolean gloryKill = false;
    private boolean gloryDestroy = false;
    private boolean gloryGold = false;
    private boolean gloryHealth = false;
    private boolean gloryAssist = false;
    
    
    private int mvpType;
    
    
    /**
     * 1 = Normal ,2 = High ,3 =Very High
     */
    private int gameLevel;
    private String fightRate;
    private String damageRate;
    @Convert(columnType = String.class, converter = ListIntegerConverter.class)
    private List<Integer> equipmentList;
    /**
     * in minute
     */
    private int lastTime;
    private String efficiency;
    private int dhb;
    private String timeOffsetDesc;
    private int pageInDotaMore;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1003763973)
    private transient DotaMatchModelDao myDao;
    
    public DotaMatchModel() {
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
    
    
    public boolean isGloryKill() {
        return gloryKill;
    }
    
    public void setGloryKill(boolean gloryKill) {
        this.gloryKill = gloryKill;
    }
    
    public boolean isGloryDestroy() {
        return gloryDestroy;
    }
    
    public void setGloryDestroy(boolean gloryDestroy) {
        this.gloryDestroy = gloryDestroy;
    }
    
    public boolean isGloryGold() {
        return gloryGold;
    }
    
    public void setGloryGold(boolean gloryGold) {
        this.gloryGold = gloryGold;
    }
    
    public boolean isGloryHealth() {
        return gloryHealth;
    }
    
    public void setGloryHealth(boolean gloryHealth) {
        this.gloryHealth = gloryHealth;
    }
    
    public boolean isGloryAssist() {
        return gloryAssist;
    }
    
    public void setGloryAssist(boolean gloryAssist) {
        this.gloryAssist = gloryAssist;
    }
    
    
    public boolean getGloryKill() {
        return this.gloryKill;
    }
    
    
    public boolean getGloryDestroy() {
        return this.gloryDestroy;
    }
    
    
    public boolean getGloryGold() {
        return this.gloryGold;
    }
    
    
    public boolean getGloryHealth() {
        return this.gloryHealth;
    }
    
    
    public boolean getGloryAssist() {
        return this.gloryAssist;
    }
    
    
    public String getFightRate() {
        return this.fightRate;
    }
    
    
    public void setFightRate(String fightRate) {
        this.fightRate = fightRate;
    }
    
    
    public String getDamageRate() {
        return this.damageRate;
    }
    
    
    public void setDamageRate(String damageRate) {
        this.damageRate = damageRate;
    }
    
    
    public List<Integer> getEquipmentList() {
        return this.equipmentList;
    }
    
    
    public void setEquipmentList(List<Integer> equipmentList) {
        this.equipmentList = equipmentList;
    }
    

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getMatchID() {
        return matchID;
    }
    
    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }
    
    public Long getEndAt() {
        return endAt;
    }
    
    public void setEndAt(Long endAt) {
        super.setEndAt(endAt);
        this.endAt = endAt;
    }

    @Override
    public String toString() {
        return "DotaMatchModel{" +
                "matchID=" + matchID +
                ", steamID64='" + steamID64 + '\'' +
                ", playerName='" + playerName + '\'' +
                ", endAt=" + endAt +
                ", hero=" + hero +
                ", timeOffsetDesc='" + timeOffsetDesc + '\'' +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 443733362)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDotaMatchModelDao() : null;
    }
}
