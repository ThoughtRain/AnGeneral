package com.prarui.utils.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;


import com.prarui.utils.common.TagLog;
import com.prarui.utils.json.GsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 在这个类中提供了网络去求的访问方式；
 * <p>post和get两种；
 * <p>
 * <p>
 * Created by Prarui on 2017/5/25.
 */

public class OkHttpManager {
    private static final String ERROR = "error";
    private static final String SUCCEED = "succeed";
    private static OkHttpClient mOkHttpClient = null;
    private static HashMap<String, Call> map = new HashMap<>();
    private static final int REQUEST_CODE_ERROR=0;
    private static final int REQUEST_CODE_SUCCEED=1;
    private static final int REQUEST_CODE_LOADING=2;
    private static final int REQUEST_CODE_COMPLETE=3;
    private static OkHttpConfig.Builder config=null;
    private static void build() {
        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new OKHttpInterceptor())
                    .build();
        }
    }

    /**
     * 键值对和token的key
     * @param pram
     * @param tokenName
     */
    public static void build(Map<String,String> pram,String tokenName){
         build();
        OkHttpManager.config=new OkHttpConfig.Builder().addHeader(pram).setToken(tokenName);
    }
    public static void build(Map<String,String> pram){
        build();
        OkHttpManager.config=new OkHttpConfig.Builder().addHeader(pram).setToken("token");
    }
    /**
     * post请求方式
     **/

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private static OkHttpManager manager;

    public static OkHttpManager with() {
        if (null == manager) {
            manager = new OkHttpManager();
        }
        return manager;
    }

    /**
     * get请求方式
     **/
    public void setGetRequest(String tag, String url, String token, HashMap<String, String> params, OnOkHttpResultCallbackListener listener) {
        //API 14以下的处理
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String get_url = url;
        if (params == null) {
            params = new HashMap<>();
        } else {
            //拼接url
            if (params != null && params.size() > 0) {
                int i = 0;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (i++ == 0) {
                        get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                    } else {
                        get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                    }
                }
            }
        }

        //请求体
        Request request;
        Request.Builder getbuilder;
        if (TextUtils.isEmpty(token)) {
            getbuilder = new Request.Builder()
                    .url(get_url)
                    .get();
            if(config!=null){
            getbuilder.headers(config.build());
            }
            request=getbuilder.build();
        } else {
            getbuilder = new Request.Builder().url(get_url)
                    .get()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded");
                    if(config!=null){
                        config.addHeader(config.getToken(),token);
                        getbuilder.headers(config.build());
                      }
                      request=getbuilder.build();
        }

        if (NetWorkUtils.isOpenNetwork()) {
            getNetWorkData(tag, request, listener);
        } else {
            listener.onError("网络错误，请检查你的网络");
        }
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void setPostRequest(String tag, String url, String token, HashMap<String, String> params, OnOkHttpResultCallbackListener listener) {
        //API 14以下的处理
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (params == null) {
            params = new HashMap<>();
        }
        //  RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey().toString();
            String value = null;
            //判断值是否是空的
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }

            builder.add(key, value);
        }
        String json = GsonUtils.toJson(params);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request;
        Request.Builder postbuilder;
        if (token == null) {
            postbuilder = new Request.Builder()
                    .url(url)
                    .post(requestBody);
               if(config!=null){
                postbuilder.headers(config.build());
                          }
                   request=postbuilder.build();
        } else {
            postbuilder = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(requestBody);
                    if(config!=null){
                        config.addHeader(config.getToken(),token);
                        postbuilder.headers(config.build());
                     }
                 request= postbuilder.build();
        }

        if (NetWorkUtils.isOpenNetwork()) {
            getNetWorkData(tag, request, listener);
        } else {
            listener.onError("网络错误，请检查你的网络");
        }
    }

    public void setPutRequest(String tag, String url, String token, HashMap<String, String> params, OnOkHttpResultCallbackListener listener) {
        //API 14以下的处理
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (params == null) {
            params = new HashMap<>();
        }
        //   RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey().toString();
            String value = null;

//     判断值是否是空的
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }

//     把key和value添加到formbody中
            builder.add(key, value);
        }
        //   requestBody = builder.build();
        String json = GsonUtils.toJson(params);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request;
        Request.Builder putbuilder;
        if (token == null) {
            putbuilder = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .put(requestBody);
                   if(config!=null){
                    putbuilder.headers(config.build());
                    }
            request=putbuilder.build();
        } else {
            putbuilder = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .put(requestBody);
                    if(config!=null){
                    config.addHeader(config.getToken(),token);
                    putbuilder.headers(config.build());
                    }
                    request=putbuilder.build();
        }

        Log.e("url", url);
        if (NetWorkUtils.isOpenNetwork()) {
            getNetWorkData(tag, request, listener);
        } else {
            listener.onError("网络错误，请检查你的网络");
        }
    }

    //返回结果
    private void getNetWorkData(String tag, final Request request, final OnOkHttpResultCallbackListener listener) {
        final Handler handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case 0:
                        Bundle data = message.getData();
                        String error = data.getString(ERROR);
                        if (null != listener) {
                            listener.onError(error);
                        }

                        break;
                    case 1:
                        Bundle bundle = message.getData();
                        String json = bundle.getString(SUCCEED);
                        if (null != listener) {
                            listener.onResponseSucceed(json);
                        }
                        break;
                    case 2:
                          if(null!=listener){
                              listener.onLoading();
                             }
                        break;
                        case 3:
                            if(null!=listener){
                                listener.onComplete();
                            }
                            break;

                }

                return false;
            }
        });

        handler.sendEmptyMessage(2);
        final Call call = mOkHttpClient.newCall(request);
        map.put(tag, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("error", "请求数据失败");
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                handler.sendEmptyMessage(3);
                final String json = response.body().string();
                TagLog.d(json);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString(SUCCEED, json);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

    }

    public void cancelAll() {
        mOkHttpClient.dispatcher().cancelAll();
    }

    public void cancelNetWorkByTag(String tag) {
        for (Map.Entry<String, Call> entry : map.entrySet()) {
            if (tag.equals(entry.getKey())) {
                Call call = entry.getValue();
                call.cancel();
                map.remove(entry.getKey());
            }
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }
    }


    //返回接口
    public interface OnOkHttpResultCallbackListener {
        /**
         * 请求中；
         */
        void onLoading();

        /**
         * 请求完成
         */

        void onComplete();

        /**
         * 返回的错误
         *
         * @param e
         */
        void onError(String e);

        /**
         * 返回请求成功的例子
         *
         * @param json
         */
        void onResponseSucceed(String json);

    }

}
