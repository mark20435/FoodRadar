package com.example.foodradar_android.article;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


public class ArticleFragment extends Fragment {
    private Activity activity;
    private NavController navController;
    public Boolean fbisVisible = true ;
    public FloatingActionButton fbArticleInsert;
    public BottomNavigationView articleNavigation;
    public MenuItem articleFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        // 顯示左上角的返回箭頭
//        new Common();
//        Common.setBackArrow(false, activity);
//        setHasOptionsMenu(true);
//        navController =
//                Navigation.findNavController(activity, R.id.mainFragment);
    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity.setTitle(R.string.article);
    return inflater.inflate(R.layout.fragment_article, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.articleNavigation);
        NavController navController = Navigation.findNavController(activity, R.id.articleFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //浮動button > 跳轉至insert
        fbArticleInsert = view.findViewById(R.id.fbArticleInsert);
         Bundle bundle = new Bundle();
         int newArticle = 2;    //狀態判斷碼  > bundle帶到insert文章
         bundle.putInt("newArticle", newArticle);
        fbArticleInsert.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_articleFragment_to_articleInsertFragment, bundle));


    }


    //隱藏BottomNavigationView 方法
    public static void bottomNavSet (Activity activity, int bottomNavigationInt) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.articleNavigation);
        if (bottomNavigationInt == 0) {
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}