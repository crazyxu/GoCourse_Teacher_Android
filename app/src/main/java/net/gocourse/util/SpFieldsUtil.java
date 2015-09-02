package net.gocourse.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: xucan
 * Date: 2015-07-21
 * Time: 20:29
 * FIXME 用于存储字段到SharedPreferences
 */
public class SpFieldsUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String spName;
    public SpFieldsUtil(Context mContext,String spName){
        sp=mContext.getSharedPreferences(spName,mContext.MODE_PRIVATE);
        editor=sp.edit();
    }

    /**
     * 清空数据
     */
    public void clear(){
        editor.clear();
        editor.commit();
    }

    /**
     * 从SharedPreferences中获取字段
     * @param fieldName 字段名
     * @param fieldType 字段类型
     * @return 字段对象
     */
    public Object getFieldFromSp(String fieldName,String fieldType){
        Object fieldValue=null;
        if(!TextUtils.isEmpty(fieldName)){
            if(fieldType.equals(String.class.getName())){
                fieldValue=sp.getString(fieldName,"");
            }else if(fieldType.equals(Boolean.class.getName())){
                fieldValue=sp.getBoolean(fieldName,false);
            }else if(fieldType.equals(Integer.class.getName())){
                fieldValue=sp.getInt(fieldName, 0);
            }else if(fieldType.equals(Long.class.getName())){
                fieldValue=sp.getLong(fieldName, 0);
            }else if(fieldType.equals(float.class.getName())){
                fieldValue=sp.getFloat(fieldName,0);
            } else if(fieldType.equals(Set.class.getName())){
                fieldValue=sp.getStringSet(fieldName,null);
            }
        }
        return fieldValue;
    }

    /**
     * 将字段值保存到SharedPreferences
     * @param fieldName 字段名
     * @param fieldType 字段类型
     * @param fieldValue 字段值
     */
    public void saveFieldToSp(String fieldName,String fieldType,Object fieldValue){
        if(!TextUtils.isEmpty(fieldName)){
            if(fieldType.equals(String.class.getName())){
                editor.putString(fieldName,(String)fieldValue);
            }else if(fieldType.equals(Boolean.class.getName())){
                editor.putBoolean(fieldName, (Boolean) fieldValue);
            }else if(fieldType.equals(Integer.class.getName())){
                editor.putInt(fieldName, (Integer) fieldValue);
            }else if(fieldType.equals(Long.class.getName())){
                editor.putLong(fieldName, (Long) fieldValue);
            }else if(fieldType.equals(float.class.getName())){
                editor.putFloat(fieldName, (float) fieldValue);
            } else if(fieldType.equals(Set.class.getName())){
                editor.putStringSet(fieldName, (Set) fieldValue);
            }
            editor.commit();
        }
    }

    /**
     * 从SharedPreferences中获取Map
     * @param fieldsMap 原始Map，key，value对应字段名和字段类型
     * @return
     */
    public Map<String,Object> getMapFromSp(Map<String,String> fieldsMap){
        Map<String,Object> map=new HashMap<String,Object>();
        for(String key : fieldsMap.keySet()){
            map.put(key,getFieldFromSp(key,fieldsMap.get(key)));
        }
        return map;
    }

    /**
     * 将Map保存到SharedPreferences
     * @param fieldsMap
     */
    public void saveMapToSp(Map<String,Object> fieldsMap){
        for(String key : fieldsMap.keySet()){
            saveFieldToSp(key,fieldsMap.get(key).getClass().getName(),fieldsMap.get(key));
        }
    }

}
