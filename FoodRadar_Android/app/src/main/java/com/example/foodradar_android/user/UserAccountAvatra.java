package com.example.foodradar_android.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

// 儲存資料(檔)到Internal Storage時，序列化頭像物件用的類別
public class UserAccountAvatra implements Serializable {
    private final String TAG = "TAG_SerialObject";

    public transient Bitmap bitmapObject;
    private byte[] bitmapByte;

    public String stringObject;


    public UserAccountAvatra(){
        try {
            this.bitmapObject = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }
    }


    public UserAccountAvatra(Bitmap bitmapObject){
        this.bitmapObject = bitmapObject;
        this.bitmapByte = bitmapToByte(bitmapObject);
    }
    public void setBitmapObject(Bitmap bitmapObject) {
        this.bitmapObject = bitmapObject;
        this.bitmapByte = bitmapToByte(bitmapObject);
    }
    public Bitmap getBitmapObject() {
        try {
            return BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
            return null;
        }
    }
    private byte[] bitmapToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // quality設100代表不壓縮，範圍值0~100
            return stream.toByteArray();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
            return null;
        }
    }


    public UserAccountAvatra(String stringObject){this.stringObject = stringObject;}
    public String getStringObject() {return stringObject;}
    public void setStringObject(String stringObject) {this.stringObject = stringObject;}

}
