package net.gocourse.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;

/**
 * User: xucan
 * Date: 2015-07-13
 * Time: 11:20
 * SharedPreferences与Model对象间的转换
 * @deprecated 无法很好处理集合等复杂类型
 */
public class ParseSpModelUtil {
    private Context mContext;
    private String spName;
    private Object model;
    private SharedPreferences sp;

    public ParseSpModelUtil(Context mContext,String spName,Object model){
        this.mContext=mContext;
        this.model=model;
        this.spName=spName;
        sp =mContext.getSharedPreferences(spName, mContext.MODE_PRIVATE);

    }
    //从本地sp获取对象数据
    public Object getModelFromSp(Context mContext,String spName,Object model){
        //所有方法
        Method[] methods = model.getClass().getDeclaredMethods();
        //提取setXXX方法
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    //判断参数类型
                    if(method.getParameterTypes()[0].getName().equals("int")){
                        method.invoke(model,Integer.valueOf(sp.getString(field,"0")));
                    }
                    else if(method.getParameterTypes()[0].getName().equals(String.class.getName())){
                        method.invoke(model,sp.getString(field,""));
                    }
                    else if(method.getParameterTypes()[0].getName().equals(Boolean.class.getName())){
                        method.invoke(model,Boolean.parseBoolean(sp.getString(field, "false")));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;


    }
    //把对象数据存储在本地sp
    public boolean saveModelToSp(){
        SharedPreferences.Editor editor= sp.edit();
        //获取所有方法
        Method[] methods = model.getClass().getDeclaredMethods();
        //提取getXXX的方法
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    //提取字段名
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    //存储字段值
                    Object value = method.invoke(model, (Object[]) null);
                    //根据返回值类型
                    if(method.getParameterTypes()[0].getName().equals("int")){
                        editor.putInt(field, null == value ? 0 : Integer.parseInt((String) value));
                    }
                    else if(method.getParameterTypes()[0].getName().equals(String.class.getName())){
                        editor.putString(field, null == value ? "" : (String) value);
                    }
                    else if(method.getParameterTypes()[0].getName().equals(Boolean.class.getName())){
                        editor.putBoolean(field, null == value ? false : Boolean.parseBoolean((String) value));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        editor.commit();
        return true;
    }

}
