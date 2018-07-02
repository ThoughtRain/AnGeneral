package com.prarui.utils.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.prarui.utils.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2018/6/29.
 */

public class NotificationUtils {
    /**
     * 显示
     */
    public void showNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //自定义UI 如果不设置smallIcon不会提示
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true); //自定义通知栏使用
         RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.layout_notification);
         //设置TextView的文本
//        remoteViews.setTextViewText(R.id.left_tv,"这是左边的文字"); //设置某个资源,某个资源所使用的方法,该方法的参数
//         remoteViews.setInt(R.id.left_tv,"setTextColor",context.getResources().getColor(R.color.colorAccent));
//         remoteViews.setTextViewText(R.id.right_tv,"这是右边的文字");
         //设置某个ImageView的图片
        remoteViews.setImageViewResource(R.id.image,R.mipmap.ic_launcher);
      builder.setContent(remoteViews); //默认情况下通知高度为64dp
     builder.setCustomBigContentView(remoteViews); //注意bigContentView 的最大高度是250dp
      NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
      /** *
       * builder.setLights(intledARGB ,intledOnMS ,intledOffMS ); * android支持三色灯提醒，这个方法就是设置不同场景下的不同颜色的灯。 * 其中ledARGB 表示灯光颜色、 ledOnMS 亮持续时间、ledOffMS 暗的时间。 */
      builder.setLights(0xff0000ff, 300, 0);
      /** * 设置震动效果 实现效果：延迟0ms，然后振动300ms，在延迟500ms，接着在振动700ms。 */
      builder.setVibrate(new long[] {0,300,500,700}); /** * 设置声音 builder.setSound(Uri sound); * 获取自定义铃声 * builder.setSound(Uri.parse("file:///sdcard/xx/xx.mp3")); * 获取Android多媒体库内的铃声 数字字符串表示 声音在数据库中的位置 */
      builder.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"));
      /** * setOngoing(boolean ongoing) * * 设置该属性true 通知不可滑动删除 * * 功能：设置为ture，表示它为一个正在进行的通知。 * 他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接) */
      builder.setOngoing(false);
      /** * setProgress(int max, int progress,boolean indeterminate)
       * * 属性： * max:进度条最大数值 * progress:当前进度、 * indeterminate:表示进度是否不确定，true为不确定，false为确定 * 功能：设置带进度条的通知，可以在下载中使用 */ // 利用通知管理器去发布通知
        Notification build = builder.build(); /** * 设置标记 */
        build.flags=Notification.FLAG_AUTO_CANCEL|Notification.DEFAULT_SOUND;
        notificationManager.notify(0,builder.build());


    }
    public void refreshNotification(){

    }

}
