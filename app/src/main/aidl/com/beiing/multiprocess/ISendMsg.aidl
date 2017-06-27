// ISendMsg.aidl
package com.beiing.multiprocess;
import com.beiing.multiprocess.IMsgReveiver;
import com.beiing.multiprocess.bean.MsgBean;

// Declare any non-default types here with import statements

interface ISendMsg {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void sendMsg(in MsgBean msg);

    void registerMsgReceiver(IMsgReveiver msgReceiver);

    void unregisterMsgReceiver(IMsgReveiver msgReceiver);

}
