package com.orangex.amazingfellow.features.homepage.recent.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.features.homepage.HomeActivity;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.DotaMatchModel;
import com.orangex.amazingfellow.utils.DotaUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by orangex on 2017/11/11.
 */

public class NotificationsManager {
    private static final String TAG = NotificationsManager.class.getSimpleName();
    private static final int ID_DOTA = 1;
    private static final int ID_OW = 2;
    
    public static void updateNotification(List<MatchModel> delta) {
//        if (APPUtil.isAppForeground()) {
//            return;
//        }
        Log.i(TAG, "updateNotification: ");
        NotificationManager mNotificationManager = (NotificationManager) AFApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
    
        Collections.sort(delta, new Comparator<MatchModel>() {
            @Override
            public int compare(MatchModel matchModel, MatchModel t1) {
                return Long.valueOf(t1.getId()).compareTo(Long.valueOf(matchModel.getId()));
            }
        });
        
        
        for (int i = 0; i < delta.size(); i ++) {
            if (delta.get(i) instanceof DotaMatchModel) {
                DotaMatchModel model = (DotaMatchModel) delta.get(i);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(ID_DOTA, getDotaNotification(model));
                    break;
                }
            }
        }
    }
    
    private static Notification getDotaNotification(DotaMatchModel model) {
        Notification.Builder builder = new Notification.Builder(AFApplication.getAppContext());
        //设置小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置大图标
        //        mBuilder.setLargeIcon(bitmap);
        //设置标题
        builder.setContentTitle("震惊!");
        //设置通知正文
        if (model.getMvpType() == DotaMatchModel.MVP_TYPE_MVP) {
            builder.setContentText(String.format("%s刚刚使用%s拿下了MVP,为他打Call", model.getPlayerName(), DotaUtil.getHeroLocNameById(model.getHero())));
        } else if (model.getMvpType() == DotaMatchModel.MVP_TYPE_GLORIOUS){
            builder.setContentText(String.format("%s刚刚使用%s完成了一场虽败犹荣的比赛,", model.getPlayerName(), DotaUtil.getHeroLocNameById(model.getHero())));
        }
        
        //设置摘要
//        builder.setSubText("这是摘要");
        //设置是否点击消息后自动clean
        builder.setAutoCancel(true);
        //显示指定文本
        builder.setContentInfo("Info");
        //与setContentInfo类似，但如果设置了setContentInfo则无效果
        //用于当显示了多个相同ID的Notification时，显示消息总数
//        builder.setNumber(2);
        //通知在状态栏显示时的文本
        builder.setTicker("在状态栏上显示的文本");
        //设置优先级
        builder.setPriority(Notification.PRIORITY_HIGH);
        //自定义消息时间，以毫秒为单位，当前设置为比系统时间少一小时
        builder.setWhen(System.currentTimeMillis());
        //设置为一个正在进行的通知，此时用户无法清除通知
//        builder.setOngoing(true);
        //设置消息的提醒方式，震动提醒：DEFAULT_VIBRATE     声音提醒：NotificationCompat.DEFAULT_SOUND
        //三色灯提醒NotificationCompat.DEFAULT_LIGHTS     以上三种方式一起：DEFAULT_ALL
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        //设置震动方式，延迟零秒，震动一秒，延迟一秒、震动一秒
//        builder.setVibrate(new long[]{0, 1000, 1000, 1000});
    
        Intent intent = new Intent(AFApplication.getAppContext(), HomeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(AFApplication.getAppContext(), 0, intent, 0);
        builder.setContentIntent(pIntent);
        return builder.build();
    }
}
