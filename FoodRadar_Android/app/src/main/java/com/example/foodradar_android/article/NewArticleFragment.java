package com.example.foodradar_android.article;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewArticleFragment extends Fragment {
    private static final String TAG = "TAG_ArticleFragment";
    private RecyclerView rvArticle;
    private List<Article> articleList;
    private Activity activity;
    private List<ImageTask> imageTasks;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonTask articleGetAllTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageTasks = new ArrayList<>();
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.newArticle);
        return inflater.inflate(R.layout.fragment_new_article, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvArticle = view.findViewById(R.id.rvArticle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        rvArticle.setLayoutManager(new LinearLayoutManager(activity));
        articleList = getArticle();
        showArticle(articleList);
    }


    //向server端取得Article資料
    private List<Article> getArticle() {
        List<Article> articleList = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ArticleServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            articleGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = articleGetAllTask.execute().get();
                Type listType = new TypeToken<List<Article>>() {
                }.getType();
                articleList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            //暫定Toast，須修改錯誤時執行的動作
            Common.showToast(activity, R.string.textNoNetwork);
        }

        return articleList;
    }


    private void showArticle(List<Article> articleList) {
        if (articleList == null || articleList.isEmpty()) {
            //暫定Toast，須修改錯誤時執行的動作
            Common.showToast(activity, R.string.textNoArticleFound);
            Log.e(TAG, "article:" + articleList);
        } else {
            ArticleAdapter articleAdapter = (ArticleAdapter) rvArticle.getAdapter();
            if (articleAdapter == null){
                rvArticle.setAdapter(new ArticleAdapter(activity, articleList));
            }
        }
    }


    private class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Article> ArticleList;
        private int imageSize;

        //取得圖片並設定顯示圖片尺寸設定，ArticleAdapter建構方法
        ArticleAdapter(Context context, List<Article> articleList){
            layoutInflater = LayoutInflater.from(context);
            this.ArticleList = articleList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }
        //List<Article> 建構方法
        public List<Article> getArticleList() {
            return ArticleList;
        }

        public void setArticleList(List<Article> articleList) {
            ArticleList = articleList;
        }

        @NonNull
        @Override
        public ArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.article_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return articleList == null ? 0 : articleList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView userIcon, ivArticleGoodIcon, ivArticleCommentIcon, ivArticleFavoriteIcon, imgView ;
            TextView userName, resCategoryInfo, articleTitle, resName, tvArticleTime;
            TextView tvGoodCount, tvCommentCount, tvFavoriteArticle;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                userIcon = itemView.findViewById(R.id.userIcon);
                ivArticleGoodIcon = itemView.findViewById(R.id.ivArticleGoodIcon);
                ivArticleCommentIcon = itemView.findViewById(R.id.ivArticleCommentIcon);
                ivArticleFavoriteIcon = itemView.findViewById(R.id.ivArticleFavoriteIcon);
                imgView = itemView.findViewById(R.id.imgView);
                userName = itemView.findViewById(R.id.userName);
                resCategoryInfo = itemView.findViewById(R.id.resCategoryInfo);
                articleTitle = itemView.findViewById(R.id.articleTitle);
                resName = itemView.findViewById(R.id.resName);
                tvArticleTime = itemView.findViewById(R.id.tvArticleTime);
                tvGoodCount = itemView.findViewById(R.id.tvCommentCount);
                tvCommentCount = itemView.findViewById(R.id.tvCommentCount);

            }
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleAdapter.MyViewHolder holder, int position) {

        }
    }
}