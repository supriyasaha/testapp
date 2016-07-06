package com.example.coupondunia.goodbox;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable{

    String number;
    String message;
    long timestamp;

    public MessageModel(){}
    protected MessageModel(Parcel in) {
        number = in.readString();
        message = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(message);
        dest.writeLong(timestamp);
    }
}
