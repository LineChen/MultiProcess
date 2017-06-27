package com.beiing.multiprocess.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by linechen on 2017/6/26.<br/>
 * 描述：
 * </br>
 */

public class MsgBean implements Parcelable{
    private String from;
    private String to;
    private String content;
    private long time;

    public MsgBean() {
    }

    protected MsgBean(Parcel in) {
        from = in.readString();
        to = in.readString();
        content = in.readString();
        time = in.readLong();
    }

    public static final Creator<MsgBean> CREATOR = new Creator<MsgBean>() {
        @Override
        public MsgBean createFromParcel(Parcel in) {
            return new MsgBean(in);
        }

        @Override
        public MsgBean[] newArray(int size) {
            return new MsgBean[size];
        }
    };

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(content);
        dest.writeLong(time);
    }

    @Override
    public String toString() {
        return "MsgBean{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
