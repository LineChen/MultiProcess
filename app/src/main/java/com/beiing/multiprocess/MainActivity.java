package com.beiing.multiprocess;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.beiing.multiprocess.bean.MsgBean;
import com.beiing.multiprocess.service.MsgService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ISendMsg msgSender;

    EditText editText;

    /**
     * bind service
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            msgSender = ISendMsg.Stub.asInterface(service);
            try {
                msgSender.registerMsgReceiver(msgReveiver);
                msgSender.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
            try {
                if(msgSender.asBinder().isBinderAlive())
                    msgSender.unregisterMsgReceiver(msgReveiver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * receive msg from service
     */
    IMsgReveiver msgReveiver = new IMsgReveiver.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onMsgReceive(MsgBean msg) throws RemoteException {
            Log.e(TAG, "[client received]" + msg.toString());
        }
    };

    /**
     * observe the binder death
     */
    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (msgSender != null) {
                    msgSender.asBinder().unlinkToDeath(this, 0);
            }

            setUpService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_send);

        setUpService();
    }

    private void setUpService() {
        Intent intent = new Intent(this, MsgService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }


    public void sendMsg(String content){
        MsgBean msg = new MsgBean();
        msg.setFrom("user-client-00");
        msg.setTo("user" + (int)(Math.random() * 100));
        msg.setContent(content);
        msg.setTime(System.currentTimeMillis());

        if (msgSender != null) {
            try {
                msgSender.sendMsg(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(View view) {
        String content = editText.getText().toString().trim();
        if(!TextUtils.isEmpty(content)){
            sendMsg(content);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

}
