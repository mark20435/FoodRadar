package com.example.foodradar_android;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class MainFragment extends Fragment {
    private static final String TAG = "TAG_ResListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvRes;
    private Activity activity;
    private CommonTask resGetAllTask;
    private CommonTask resDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Res> resList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.home);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 首頁沒有返回鍵，所以在onResume把返回鍵設定為不顯示
        new Common().setBackArrow(false, activity);
    }
}