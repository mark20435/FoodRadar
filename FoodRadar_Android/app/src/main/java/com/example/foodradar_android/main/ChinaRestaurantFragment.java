package com.example.foodradar_android.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;

public class ChinaRestaurantFragment extends Fragment {
    private Activity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_china_restaurant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        // 設定畫面到首頁一律不顯示返回鍵
        new Common().setBackArrow(false,activity);
        // 設定首頁AppBar(ActionBar)的Title(抬頭)
        activity.setTitle(R.string.chinacategory);

    }
}