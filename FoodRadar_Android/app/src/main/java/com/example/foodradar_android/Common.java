package com.example.foodradar_android;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Common {

    // 設定Fargement的AppBar是否要顯示返回的箭頭
    public void setBackArrow(Boolean isDisplay, Activity activity){
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(isDisplay);
    }

}
