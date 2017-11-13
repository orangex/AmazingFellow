package com.orangex.amazingfellow.db.converters;

/**
 * Created by orangex on 2017/11/12.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;


public class ListIntegerConverter implements PropertyConverter<List<Integer>,String> {
    
    @Override
    public List<Integer> convertToEntityProperty(String databaseValue) {
        return JSON.parseArray(databaseValue, Integer.class);
    }
    
    @Override
    public String convertToDatabaseValue(List<Integer> entityProperty) {
        return JSONArray.toJSONString(entityProperty);
    }
}