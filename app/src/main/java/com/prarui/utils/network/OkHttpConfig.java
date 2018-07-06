package com.prarui.utils.network;

import com.prarui.utils.common.TagLog;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Headers;

/**
 * Created by prarui on 2018/7/2.
 * 这个类是一个控制OkHttp请求header的管理类；
 */

public class OkHttpConfig {

public static class Builder{
    private Headers headers=null;
    private String token="token";
    private okhttp3.Headers.Builder headersbuilder;
    public Builder addHeader(Map<String, String> headersParams){
        headersbuilder =new okhttp3.Headers.Builder();
        if(headersParams != null)
        {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                TagLog.d("get http", "get_headers==="+key+"===="+headersParams.get(key));
            }
        }

        return this;
    }
    public void addHeader(String key,String value){
     headersbuilder.add(key, value);
    }
    public Builder setToken(String token){
        if(token==null){
          return this;
        }
        this.token=token;
        return this;
    }

    public Headers build(){
        headers=headersbuilder.build();
        return headers;
    }

    public String getToken() {
        return token;
    }
}


}
