package com.orangex.amazingfellow.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.bean.DotaHeroName;
import com.orangex.amazingfellow.bean.DotaHeroNameJsonBean;
import com.orangex.amazingfellow.bean.DotaHeroNameJsonBean.ResultBean.HeroesBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by orangex on 2017/11/8.
 */

public class DotaUtil {// TODO: 2017/11/11 更新 json
    private static ArrayList<DotaHeroName> sDotaHeroNames = new ArrayList<>();// TODO: 2017/11/8 sparseArray
    private static HashMap<String, Integer> sIDMap = new HashMap<>();
   
    public static void initNameMap() {// TODO: 2017/11/8 retry when e occurs
        String resultString = null;
        try {
            InputStream inputStream = AFApplication.getAppContext().getAssets().open("heroName.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultString == null) {
            return;
        }
    
        DotaHeroNameJsonBean heroName = JSON.parseObject(resultString, DotaHeroNameJsonBean.class);
        int i = -1;
        for (HeroesBean heroname : heroName.getResult().getHeroes()
                ) {
            i++;
            String name = heroname.getName().replace("npc_dota_hero_", "");
            DotaHeroName dotaHeroName = new DotaHeroName(i + 1, name, heroname.getLocalized_name());
            sDotaHeroNames.add(dotaHeroName);
            sIDMap.put(heroname.getLocalized_name(), i + 1);
        }
    }
    
    public static Object getHeroPicById(int hero) {
        if (hero < 1) {
            return R.drawable.drag_frame;
        }
        String name = sDotaHeroNames.get(hero - 1).getName();
        if (name == null) {
            return R.drawable.drag_frame;
        } else {
            return  String.format("http://cdn.max-c.com/app/dota2/%s@3x.png", name);
        }
    }
    
    public static String getHeroLocNameById(int hero) {
        if (hero < 1) {
            return AFApplication.getAppContext().getString(R.string.heroname_card_default);
        }
        String name = sDotaHeroNames.get(hero - 1).getLocName();
        if (TextUtils.isEmpty(name)) {
            return AFApplication.getAppContext().getString(R.string.heroname_card_default);
        }
        return name;
    }
    
    public static int getHeroIdByLocname(String name) {
        Integer id = sIDMap.get(name);
        if (id == null) {
            return -1;
        } else {
            return id;
        }
    }
}
