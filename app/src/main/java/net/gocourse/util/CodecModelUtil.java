package net.gocourse.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: xucan
 * Date: 2015-07-13
 * Time: 14:49
 * 将对象Base64编码，以字符串保存在sp
 */
public class CodecModelUtil {
    private Object model;
    private SharedPreferences sp;
    private String saveName;

    public CodecModelUtil(Context mContext,String spName,Object model,String saveName){
        this.model=model;
        this.saveName=saveName;
        sp =mContext.getSharedPreferences(spName, mContext.MODE_PRIVATE);
    }
    //保存到本地sp
    public void saveToSp() {
        // 创建字节输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            // 将对象写入字节流
            oos.writeObject(model);
            // 将字节流编码成base64的字符串
            String modelStr = new String(Base64.encodeBase64(bos
                    .toByteArray()));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(saveName, modelStr);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从本地取出
    public Object readFromSp() {
        Object obj = null;
        String productBase64 = sp.getString(saveName, "");

        //读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bis = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream ois = new ObjectInputStream(bis);
            //读取对象
            obj = ois.readObject();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 清楚该字段的本地存储
     */
    public void clearSp(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(saveName,"");
        editor.commit();
    }
}
