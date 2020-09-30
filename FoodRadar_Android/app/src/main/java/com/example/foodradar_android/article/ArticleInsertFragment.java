package com.example.foodradar_android.article;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

public class ArticleInsertFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    public Activity activity;
    private EditText etConNum, etArticleTitle, etArticleText, etConAmount;
    private TextView tvResName;
    private RecyclerView rvInsertImage;
    private ImageView ivPlaceIcon;
//    public int userId ;
    public BottomNavigationView articleNavigation;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etConNum = view.findViewById(R.id.etConNum);    //輸入消費人數
        etConAmount = view.findViewById(R.id.etConAmount);  //輸入消費金額
        etArticleTitle = view.findViewById(R.id.etArticleTitle);    //輸入標題
        etArticleText = view.findViewById(R.id.etArticleText);  //輸入內文


//        articleNavigation = view.findViewById(R.id.articleNavigation);
//        articleNavigation.setVisibility(View.INVISIBLE);

        //跳轉至，選擇餐廳頁面(外)
        ivPlaceIcon = view.findViewById(R.id.ivPlaceIcon);
        tvResName = view.findViewById(R.id.tvResName);
        ivPlaceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_articleInsertFragment_to_resAddressFragment);
            }
        });




        //橫向顯示圖片
        rvInsertImage = view.findViewById(R.id.rvInsertImage);
        rvInsertImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true));


    }

    //取得UserID方法
    private Integer getUserID() { return Common.USER_ID;}

    //右上角，送出按鈕
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_insert_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Integer userId = getUserID();
        String resName = tvResName.toString();
        String conNumStr = etConNum.getText().toString().trim();   //輸入人數轉為字串
        int conNum = Integer.parseInt(conNumStr);  //輸入人數，int型態
        if (conNumStr.length() <= 0 || conNum <= 0) {
            etConNum.setError("請輸入正確消費人數");
            return false;
        }
        String conAmountStr = etConAmount.getText().toString().trim();   //輸入消費轉為字串
        int conAmount = Integer.parseInt(conAmountStr);   //輸入消費金額，int型態
        if(conAmountStr.length() <= 0 || conAmount <= 0) {
            etConAmount.setError("請輸入正確消費金額");
            return false;
        }
        String articleTitle = etArticleTitle.getText().toString().trim();   //輸入文章主題
        if(articleTitle.length() <= 0) {
            etArticleTitle.setError("請輸入文章主題");
            return false;
        }
        String articleText = etArticleText.getText().toString().trim();     //輸入文章內文
        if(articleText.length() <= 0) {
            etArticleTitle.setError("請輸入文章內文");
            return false;
        }

        //送出文章資料(不含圖片)
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ArticleServlet";
//            Article article = new Article(0, articleTitle, articleText, conNum, conAmount,resId , userId, true);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "articleInsert");

        }

        return true;
//      return super.onOptionsItemSelected(item);
    }
}