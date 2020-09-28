package com.example.foodradar_android.article;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ArticleFragment extends Fragment {
    private Activity activity;
    private NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common().setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

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
        FloatingActionButton fbArticleInsert = view.findViewById(R.id.fbArticleInsert);
        fbArticleInsert.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_articleFragment_to_articleInsertFragment));
    }
}