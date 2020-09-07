package com.example.foodradar_android;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodradar_android.res.Res;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;

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
    // 首頁不顯示返回箭頭
    @Override
    public void onResume() {
        super.onResume();
        new Common().setBackArrow(false, activity);
    }
}