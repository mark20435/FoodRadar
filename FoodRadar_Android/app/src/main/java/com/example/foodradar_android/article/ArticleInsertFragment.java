package com.example.foodradar_android.article;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.R;

public class ArticleInsertFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    public Activity activity;
    private EditText etConNum, etArticleTitle, etArticleText, etConAmount;
    private TextView tvResName;
    private RecyclerView rvInsertImage;
    private ImageView ivPlaceIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle("發文");
        return inflater.inflate(R.layout.fragment_article_insert, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_insert_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etConNum = view.findViewById(R.id.etConNum);
        etConAmount = view.findViewById(R.id.etConAmount);
        etArticleTitle = view.findViewById(R.id.etArticleTitle);
        etArticleText = view.findViewById(R.id.etArticleText);

        ivPlaceIcon = view.findViewById(R.id.ivPlaceIcon);

        tvResName = view.findViewById(R.id.tvResName);

        //橫向顯示圖片
        rvInsertImage = view.findViewById(R.id.rvInsertImage);
        rvInsertImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true));

    }
}