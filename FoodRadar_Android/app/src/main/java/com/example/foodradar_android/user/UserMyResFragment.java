package com.example.foodradar_android.user;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;

public class UserMyResFragment extends Fragment {
    private Activity activity;
    private NavController navController;
    private RecyclerView rcvMyRes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        setHasOptionsMenu(true);
        // 顯示返回的箭頭
//        new Common().setBackArrow(true, activity);
        // 設定MainActivity導覽圖的Controller，導覽圖的id是“fragment”
        navController = Navigation.findNavController(activity, R.id.fragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 這是 android 預設的返回鍵id
            case android.R.id.home:
                navController.popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.title_of_myres);
        return inflater.inflate(R.layout.fragment_user_my_res, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvMyRes = view.findViewById(R.id.rcvMyRes);
        rcvMyRes.setLayoutManager(new LinearLayoutManager(activity));

    }



}