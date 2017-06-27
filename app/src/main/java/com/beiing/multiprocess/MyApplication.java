package com.beiing.multiprocess;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by linechen on 2017/6/26.<br/>
 * 描述：
 * </br>
 */

public class MyApplication extends Application {
    private static String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        if(!getAppProcessName(this).equals(getPackageName())) return;

        //init
        init();
    }

    private void init() {
        Log.e(TAG, "main process init.");
    }

    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

}
