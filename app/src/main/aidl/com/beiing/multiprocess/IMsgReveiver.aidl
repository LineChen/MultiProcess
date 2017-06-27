// IMsgReveiver.aidl
package com.beiing.multiprocess;
import com.beiing.multiprocess.bean.MsgBean;

// Declare any non-default types here with import statements

interface IMsgReveiver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void onMsgReceive(in MsgBean msg);
}
