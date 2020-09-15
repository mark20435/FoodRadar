package com.example.foodradar_android.article;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class RankArticleFragment extends Fragment {
    private static final String TAG = "TAG_ArticleFragment";
    private RecyclerView rvArticleRank;
    private List<Article> articles;
    private Activity activity;
    private List<ImageTask> imageTasks;
    private SwipeRefreshLayout swipeRefreshLayoutRank;
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
        return inflater.inflate(R.layout.fragment_rank_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvArticleRank = view.findViewById(R.id.rvArticleRank);
        swipeRefreshLayoutRank = view.findViewById(R.id.swipeRefreshLayoutRank);

        rvArticleRank.setLayoutManager(new LinearLayoutManager(activity));
        articles = getArticle();
        showArticle(articles);

        //swipeRefreshLayout
        swipeRefreshLayoutRank.setOnRefreshListener(() -> {
            swipeRefreshLayoutRank.setRefreshing(true);
            showArticle(articles);
            swipeRefreshLayoutRank.setRefreshing(false);
        });
    }

    //向server端取得Article資料
    private List<Article> getArticle() {
        List<Article> articles = null;
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
                articles = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            //暫定Toast，須修改錯誤時執行的動作
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return articles;
    }

    private void showArticle(List<Article> articleList) {
        if (articleList == null || articleList.isEmpty()) {
            //暫定Toast，須修改錯誤時執行的動作
            Common.showToast(activity, R.string.textNoArticleFound);
            Log.e(TAG, "article:" + articleList);
        }
       ArticleAdapter articleAdapter = (ArticleAdapter) rvArticleRank.getAdapter();
        if (articleAdapter == null) {
            rvArticleRank.setAdapter(new ArticleAdapter(activity, articleList));
        }else {
                articleAdapter.setArticleList(articleList);
                articleAdapter.notifyDataSetChanged();
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
            imageSize = getResources().getDisplayMetrics().heightPixels;
        }

        public List<Article> getArticleList() {
            return ArticleList;
        }

        public void setArticleList(List<Article> articleList) {
            ArticleList = articleList;
        }

        @Override
        public int getItemCount() {
            return articles == null ? 0 : articles.size();
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
                ivFavoriteIcon = itemView.findViewById(R.id.ivFavoriteIcon);

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
            final Article article = ArticleList.get(position);
//            final ArticleGood articleGood = ArticleGoodList.get(position);
            //onBindViewHolder才會向後端發出請求取得圖片
            //取得大圖
            String url = Common.URL_SERVER + "ImgServlet";
            int articleId = article.getArticleId();
            ImageTask imageTask = new ImageTask(url, articleId, imageSize, myViewHolder.imgView);
            imageTask.execute();
            imageTasks.add(imageTask);

            //取得使用者小圖
            String urlIcon = Common.URL_SERVER + "UserAccountServlet";
            int userId = article.getUserId();
            ImageTask imageTaskIcon = new ImageTask(urlIcon, userId, imageSize, myViewHolder.userIcon);
            imageTaskIcon.execute();
            imageTasks.add(imageTaskIcon);

            String goodCount = article.getGoodCount() + "";
            String commentCount = article.getCommentCount() + "";
            String favoriteCount = article.getFavoriteCount() + "";

            myViewHolder.userName.setText(article.getUserName());
            myViewHolder.resCategoryInfo.setText(article.getResCategoryInfo());
            myViewHolder.articleTitle.setText(article.getArticleTitle());
            myViewHolder.resName.setText(article.getResName());
            myViewHolder.tvArticleTime.setText(article.getArticleTime());
            myViewHolder.tvGoodCount.setText(goodCount);
            myViewHolder.tvCommentCount.setText(commentCount);
            myViewHolder.tvFavoriteArticle.setText(favoriteCount);
            myViewHolder.ivGoodIcon.setImageResource(R.drawable.ic_baseline_thumb_up_24);
            myViewHolder.ivArticleCommentIcon.setImageResource(R.drawable.ic_baseline_chat_bubble_24);
            myViewHolder.ivFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);

            //設定點讚功能，1.會員登入判斷還沒寫，要候補    2.判斷是否已點讚
//            int articleGoodStatus = articleGood.getArticleGoodStatus();

            myViewHolder.ivGoodIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView goodIcon = v.findViewById(R.id.ivGoodIcon);
                    goodIcon.setColorFilter(Color.parseColor("#4599A6"));

                }
            });

            //設定收藏功能，1.會員登入判斷還沒寫，要候補    2.判斷是否已收藏
            myViewHolder.ivFavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView favoriteIcon = v.findViewById(R.id.ivFavoriteIcon);
                    favoriteIcon.setColorFilter(Color.parseColor("#EADDAB"));
                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (articleGetAllTask != null) {
            articleGetAllTask.cancel(true);
            articleGetAllTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0) {
            for (ImageTask imageTask : imageTasks) {
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }

        if (articleGetAllTask != null) {
            articleGetAllTask.cancel(true);
            articleGetAllTask = null;
        }
    }

}