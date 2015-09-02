package net.gocourse.net;

import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gocourse.util.UIUtil;

import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-21
 * Time: 11:34
 * FIXME
 */
public class MyStringRequest {

    public static StringRequest getStringRequest(int method,String url,
                 final Map<String, String> params,final Map<String, String> header,
                 Response.Listener<String> listener,
                 Response.ErrorListener errorListener){
        StringRequest request=new StringRequest(method,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params==null)
                    return super.getParams();
                else
                    return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(header==null)
                    return super.getHeaders();
                else
                    return header;
            }
        };
        return request;
    }
    public static StringRequest getStringRequest(int method,String url,final View view,
                                                 final Map<String, String> params,final Map<String, String> header,
                                                 Response.Listener<String> listener
                                                 ){
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                UIUtil.showSnackBar(view,"无法提交请求，请检查您的网络状态。",true);
            }
        };
        StringRequest request=new StringRequest(method,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params==null)
                    return super.getParams();
                else
                    return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(header==null)
                    return super.getHeaders();
                else
                    return header;
            }
        };
        return request;
    }
}
