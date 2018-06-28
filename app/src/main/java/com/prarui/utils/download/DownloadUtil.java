package com.prarui.utils.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.IOException;


/**
 * Created by prarui on 2017/9/29.
 * app升级代码
 */

public class DownloadUtil {
    //直接切换回主线程；
    private boolean isFinish = false;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (null != listener) {
                        listener.onDownloadFailed();
                    }
                    break;
                case 1:
                    if (null != listener) {
                        listener.onDownloading(message.arg1);
                    }
                    break;
                case 2:
                    if (null != listener) {
                        listener.onDownloadSuccess();
                    }

                    break;

            }
            return false;
        }
    });
    private static DownloadUtil downloadUtil;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }


    public void setOnDownloadListener(OnDownloadListener listener) {
        this.listener = listener;

    }
    public void starDownload(){

    }

    private OnDownloadListener listener;

    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */


    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();

        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
