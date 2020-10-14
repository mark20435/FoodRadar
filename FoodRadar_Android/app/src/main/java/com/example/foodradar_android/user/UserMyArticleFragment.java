package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class UserMyArticleFragment extends Fragment {

    private Activity activity;
    private NavController navController;
    private List<MyArticle> myArticleList = new ArrayList<>();
    private List<UserImageTask> imageTasks= new ArrayList<>();
    private RecyclerView rcvMyArticle;
    //    private Integer userId;
    private String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";
    private String MYARTICLE_SERVLET = URL_SERVER + "MyArticleServlet";
    private Integer imageSize = 500;
    final private String TAG = "UserMyArticleFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    // 顯示右上角的OptionMenu選單
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.appbar_menu,menu);  // 從res取用選項的清單“R.menu.my_menu“
//        super.onCreateOptionsMenu(menu, inflater);
    }
    // 顯示右上角的OptionMenu選單
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
//            case R.id.Finish:
//                navController.navigate(R.id.action_userAreaFragment_to_userSysSetupFragment);
//                break;
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
        activity.setTitle(R.string.title_of_myarticle);
        return inflater.inflate(R.layout.fragment_user_my_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvMyArticle = view.findViewById(R.id.id_rcvMyArticle);
        rcvMyArticle.setLayoutManager(new LinearLayoutManager(activity));
//        userId = 3; // 取得user登入的 帳號/userId
        myArticleList = getMyRes();
//        Log.d("TAG","myResList: " + myResList);
        showMyRes(myResList);
    }

    private int getUserId(){
        return Common.USER_ID;
    }


    // 自訂的內部類別(MyArticleAdapter) 繼承 RecyclerView.Adapter
    // 並同時自己建立 MyViewHolder 繼承 RecyclerView.ViewHolder 用來放要呈現在 ItemView 裡物件的資料
    private class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater; // 放外面傳進來的 Context / Activity
        private List<MyArticle> myArticleListAdp; // 放外面傳進來的 物件List
        private Integer imageSize;

        // 自訂的內部類別(MyArticleAdapter)的建構子
        MyArticleAdapter (Context context, List<MyArticle> myArticleListAdp) {
            layoutInflater = LayoutInflater.from(context);
            this.myArticleListAdp = myArticleListAdp;
            this.imageSize = getResources().getDisplayMetrics().widthPixels / 4; // 取得螢幕寬度當圖片尺寸的基準
        }

        // 自訂的內部類別(MyArticleAdapter)的方法(setMyArticleListAdp)，接外面傳進來的 物件List
        public void setMyArticleListAdp (List<MyArticle> myArticleListAdp) {
            this.myArticleListAdp = myArticleListAdp;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }


        @NonNull
        @Override
        public MyArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyArticleAdapter.MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }




}