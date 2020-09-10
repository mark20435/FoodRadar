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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
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
        return inflater.inflate(R.layout.fragment_new_article, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView articleSearchView = view.findViewById(R.id.articleSearchView);
        rvArticle = view.findViewById(R.id.rvArticle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        rvArticle.setLayoutManager(new LinearLayoutManager(activity));
        articleList = getArticle();
        showArticle(articleList);

        //swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showArticle(articleList);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        //searchView
//        articleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String nextText) {
//                // 如果searchView為空字串，就顯示全部資料；否則就顯示搜尋後結果
//                if (nextText.isEmpty()) {
//                    showArticle(articleList);
//                } else {
//                    List<Article> searchArticle = new ArrayList<>();
//                    for (Article article : articleList) {
//                        if ((article.getArticleTitle().toUpperCase().contains(nextText.toUpperCase())) ||
//                                (article.getResCategoryInfo().toUpperCase().contains(nextText.toUpperCase()))) {
//                            searchArticle.add(article);
//                        }
//                    }
//                    showArticle(searchArticle);
//                }
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }


    //向server端取得Article資料
    private List<Article> getArticle() {
        List<Article> articleList = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ArticleServlet";
            JsonObject jsonObject = new JsonObject();
            //？？
            jsonObject.addProperty("action", "getAllById");
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
            if (articleAdapter == null) {
                rvArticle.setAdapter(new ArticleAdapter(activity, articleList));
            }
        }
    }


    private class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Article> ArticleList;
        private int imageSize;

        //取得圖片並設定顯示圖片尺寸設定，ArticleAdapter建構方法
        ArticleAdapter(Context context, List<Article> articleList) {
            layoutInflater = LayoutInflater.from(context);
            this.ArticleList = articleList;

            //螢幕寬度當作將圖的尺寸
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        //List<Article> 建構方法
        public List<Article> getArticleList() {
            return ArticleList;
        }

        public void setArticleList(List<Article> articleList) {
            ArticleList = articleList;
        }

        @Override
        public int getItemCount() {
            return articleList == null ? 0 : articleList.size();
//            return 10;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView userIcon, ivArticleCommentIcon, imgView;
            ImageView ivGoodIcon, ivFavoriteIcon;
            TextView userName, resCategoryInfo, articleTitle, resName, tvArticleTime;
            TextView tvGoodCount, tvCommentCount, tvFavoriteArticle;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                userIcon = itemView.findViewById(R.id.userIcon);
                ivArticleCommentIcon = itemView.findViewById(R.id.ivArticleCommentIcon);
                imgView = itemView.findViewById(R.id.imgView);
                userName = itemView.findViewById(R.id.userName);
                resCategoryInfo = itemView.findViewById(R.id.resCategoryInfo);
                articleTitle = itemView.findViewById(R.id.articleTitle);
                resName = itemView.findViewById(R.id.resName);
                tvArticleTime = itemView.findViewById(R.id.tvArticleTime);
                tvGoodCount = itemView.findViewById(R.id.tvCommentCount);
                tvCommentCount = itemView.findViewById(R.id.tvgoodCount);
                tvFavoriteArticle = itemView.findViewById(R.id.tvFavoriteArticle);
                ivGoodIcon = itemView.findViewById(R.id.ivGoodIcon);
                ivFavoriteIcon = itemView.findViewById(R.id.tvFavoriteArticle);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.article_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleAdapter.MyViewHolder myViewHolder, int position) {
            //article物件 > 包裝要呈現在畫面的資料
            final Article article = articleList.get(position);
            //onBindViewHolder才會向後端發出請求取得圖片
            //取得大圖
            String url = Common.URL_SERVER + "ImgServlet";
            int id = article.getArticleId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imgView);
            imageTask.execute();
            imageTasks.add(imageTask);

            //取得使用者小圖
            String urlIcon = Common.URL_SERVER + "UserServlet";
            int userId = article.getArticleId();
            ImageTask imageTaskIcon = new ImageTask(urlIcon, userId, imageSize, myViewHolder.userIcon);
            imageTaskIcon.execute();
            imageTasks.add(imageTaskIcon);

            myViewHolder.userName.setText(article.getUserName());
            myViewHolder.resCategoryInfo.setText(article.getResCategoryInfo());
            myViewHolder.articleTitle.setText(article.getArticleTitle());
            myViewHolder.resName.setText(article.getResName());
            myViewHolder.tvArticleTime.setText(article.getArticleTime());
            myViewHolder.tvGoodCount.setText(article.getGoodCount());
            myViewHolder.tvCommentCount.setText(article.getCommentCount());
            myViewHolder.tvFavoriteArticle.setText(article.getFavoriteCount());
            myViewHolder.ivGoodIcon.setImageResource(R.drawable.ic_baseline_thumb_up_24);
            myViewHolder.ivArticleCommentIcon.setImageResource(R.drawable.ic_baseline_chat_bubble_24);
            myViewHolder.ivFavoriteIcon.setImageResource(R.drawable.ic_baseline_turned_in_24);


        }
    }
}