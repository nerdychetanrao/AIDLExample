package com.chetan.commonapp.balanceServiceCommon;



import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugobuy on 4/25/18.
 */

public class DailyCash implements Parcelable {
    int mDay;
    int mMonth;
    int mYear;
    int mCash;
    String mDayOfWeek;

    public DailyCash(int mDay, int mMonth, int mYear, int mCash, String mDayOfWeek) {
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.mCash = mCash;
        this.mDayOfWeek = mDayOfWeek;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmCash() {
        return mCash;
    }

    public void setmCash(int mCash) {
        this.mCash = mCash;
    }

    public String getmDayOfWeek() {
        return mDayOfWeek;
    }

    public void setmDayOfWeek(String mDayOfWeek) {
        this.mDayOfWeek = mDayOfWeek;
    }

    public DailyCash() {

    }

    public DailyCash(Parcel in) {
        mDay = in.readInt() ;
        mMonth = in.readInt() ;
        mYear = in.readInt() ;
        mCash = in.readInt() ;
        mDayOfWeek = in.readString() ;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mDay);
        out.writeInt(mMonth) ;
        out.writeInt(mYear) ;
        out.writeInt(mCash) ;
        out.writeString(mDayOfWeek) ;
    }

    public static final Parcelable.Creator<DailyCash> CREATOR
            = new Parcelable.Creator<DailyCash>() {

        public DailyCash createFromParcel(Parcel in) {
            return new DailyCash(in) ;
        }

        public DailyCash[] newArray(int size) {
            return new DailyCash[size];
        }
    };

    public int describeContents()  {
        return 0 ;
    }

}

