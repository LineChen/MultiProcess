package com.beiing.multiprocess.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.beiing.multiprocess.IMsgReveiver;
import com.beiing.multiprocess.ISendMsg;
import com.beiing.multiprocess.NewProcessActivity;
import com.beiing.multiprocess.R;
import com.beiing.multiprocess.bean.MsgBean;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by linechen on 2017/6/26.<br/>
 * 描述：
 * </br>
 */

public class MsgService extends Service {
    private static final String TAG ="MsgService";

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    RemoteCallbackList<IMsgReveiver> msgReveiverCallbackList = new RemoteCallbackList<>();

    IBinder msgSender = new ISendMsg.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void sendMsg(MsgBean msg) throws RemoteException {
            Log.e(TAG, "[client send success]" + msg.toString());
        }

        @Override
        public void registerMsgReceiver(IMsgReveiver msgReceiver) throws RemoteException {
            msgReveiverCallbackList.register(msgReceiver);
        }

        @Override
        public void unregisterMsgReceiver(IMsgReveiver msgReceiver) throws RemoteException {
            msgReveiverCallbackList.unregister(msgReceiver);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("New msg")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setWhen(System.currentTimeMillis());
        Intent intent1 = new Intent(this, NewProcessActivity.class);
        intent1.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intent);

        new Thread(new Session()).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return msgSender;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    private class Session implements Runnable{

        @Override
        public void run() {

            try {

                while (true){
                    MsgBean msg = new MsgBean();
                    int from = (int)(Math.random() * 100 + 100);
                    msg.setFrom("user" + from);
                    msg.setTo("user-client-00");
                    String content = "msg" + Math.random() * 1000;
                    msg.setContent(content);
                    msg.setTime(System.currentTimeMillis());
                    int count = msgReveiverCallbackList.beginBroadcast();
                    for (int i = 0; i < count; i++) {
                        IMsgReveiver msgReceiver = msgReveiverCallbackList.getBroadcastItem(i);
                        if (msgReceiver != null) {
                            msgReceiver.onMsgReceive(msg);
                        }
                    }
                    builder.setContentText(content);
                    notificationManager.notify(from, builder.build());
                    msgReveiverCallbackList.finishBroadcast();
                    Thread.sleep(10000);
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

}
