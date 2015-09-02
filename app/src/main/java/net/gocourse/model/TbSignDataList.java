package net.gocourse.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-27
 * Time: 18:51
 * FIXME 教师端签到列表
 */
public class TbSignDataList implements Serializable {
    private static final long serialVersionUID = 132L;
    public List<TbSignData> getList() {
        return list;
    }

    private List<TbSignData> list;

    public TbSignDataList(){}
    public TbSignDataList(JSONObject obj){
        list=new ArrayList<TbSignData>();
        try{
            JSONArray array= obj.getJSONArray("list");
            for(int i = 0;i<array.size();i++){
                TbSignData tb=new TbSignData(array.getJSONObject(i));
                list.add(tb);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
