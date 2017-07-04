package com.beiing.ndktest;

/**
 * Created by linechen on 2017/6/29.<br/>
 * 描述：
 * </br>
 */

public class NativeFunction {
    static {
        // 加载C代码库, 库的名称, 必须是CMakeLists.txt中指定的名称
        System.loadLibrary("native-lib");
    }

    //获取C中隐藏的AppKey
    public static native String getAppKey();

    //在C中打印log
//    public static native void printLog(String str);

}
