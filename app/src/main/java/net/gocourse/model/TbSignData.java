package net.gocourse.model;/**
 * Created by xc on 2015/7/27.
 * GoCourse_Teacher_Android
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-27
 * Time: 18:52
 * FIXME
 */
public class TbSignData implements Serializable {
    private static final long serialVersionUID = 12L;
    private TbSignBasicData basicData;

    public TbSignBasicData getBasicData() {
        return basicData;
    }

    public List<DataAppendInfo> getAppendList() {
        return appendList;
    }

    private List<DataAppendInfo> appendList;



    public TbSignData(JSONObject obj){
        try{
            JSONArray arr= obj.getJSONArray("append");
            if(arr!=null){
                appendList=new ArrayList<DataAppendInfo>();
                for (int i=0;i<arr.size();i++){
                    appendList.add(DataAppendInfo.getInfo(arr.getJSONObject(i)));
                }
            }
            obj.remove("append");
            basicData=new TbSignBasicData(obj);
        }catch (Exception  e){
            e.printStackTrace();
        }


    }
}
