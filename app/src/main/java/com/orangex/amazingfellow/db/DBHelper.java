package com.orangex.amazingfellow.db;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Commons;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.DaoMaster;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.DaoSession;

/**
 * Created by orangex on 2017/11/12.
 */

public class DBHelper {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    
  
    private static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(AFApplication.getAppContext(), Commons.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    
    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
