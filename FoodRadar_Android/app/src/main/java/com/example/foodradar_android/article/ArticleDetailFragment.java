package com.example.foodradar_android.article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "TAG_Detail";
    private RecyclerView rvArticleImage, rvComment;
    private Article article;
    private List<Img> imgs;
    private List<Comment> comments;
    private NavController navController;
    private Activity activity;
    private TextView tvDetailArticleTitle, tvDetailUserName, tvDtdailResCategoryInfo, tvDetailArticleTime, tvDetailResName,
            tvDetailAvgCon, tvArticleText, tvDetailGoodCount, tvDetailCommentCount, tvDetailFavoriteArticle;
    private ImageView ivDetailUserIcon, ivDetailSetting, user, ivDetailGoodIcon, ivDetailArticleCommentIcon, ivDetailFavoriteIcon;
    private EditText etComment;   //留言
    private Button btCommentSend;   //送出留言
    private CommonTask articleGetAllTask;
    private CommonTask commentGetAllTask;
    private CommonTask commentDeleteTask;
    private CommonTask articleDeleteTask;
    private List<ImageTask> imageTasks;
    private Integer articleIdBox = Article.ARTICLE_ID;
    private Integer userIdBox = Article.USER_ID;
    private int imageSize;
    private ConstraintLayout articleConstraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        imageTasks = new ArrayList<>();
        // 顯示左上角的返回箭頭
//        new Common().setBackArrow(true, activity);
//        setHasOptionsMenu(true);
//        navController = Navigation.findNavController(activity, R.id.mainFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
     Common.faButtonControl(activity, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDetailArticleTitle = view.findViewById(R.id.tvDetailArticleTitle);
        tvDetailUserName = view.findViewById(R.id.tvDetailUserName);
        tvDetailArticleTime = view.findViewById(R.id.tvDetailArticleTime);
        tvDtdailResCategoryInfo = view.findViewById(R.id.tvDtdailResCategoryInfo);
        tvDetailResName = view.findViewById(R.id.tvDetailResName);
        tvDetailAvgCon = view.findViewById(R.id.tvDetailAvgCon);
        tvArticleText = view.findViewById(R.id.tvArticleText);
        ivDetailUserIcon = view.findViewById(R.id.ivDetailUserIcon);
        ivDetailGoodIcon = view.findViewById(R.id.ivDetailGoodIcon);    //讚Icon
        tvDetailGoodCount = view.findViewById(R.id.tvDetailGoodCount);  //讚數
        ivDetailArticleCommentIcon = view.findViewById(R.id.ivDetailArticleCommentIcon);    //留言Icon
        tvDetailCommentCount = view.findViewById(R.id.tvDetailCommentCount);    //留言數
        ivDetailFavoriteIcon = view.findViewById(R.id.ivDetailFavoriteIcon);    //收藏Icon
        tvDetailFavoriteArticle = view.findViewById(R.id.tvDetailFavoriteArticle);  //收藏數

        user = view.findViewById(R.id.user); //留言Bar使用者Icon

        //留言功能
        etComment = view.findViewById(R.id.etComment);  //留言Ed
        btCommentSend = view.findViewById(R.id.btCommentSend);  //送出留言
        btCommentSend.setOnClickListener(v -> {
            String comment = etComment.getText().toString().trim();
            if (comment.length() <= 0) {
                Common.showToast(activity, R.string.commentNoInsert);
                etComment.setError("請輸入留言");
                return;
            }
            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "CommentServlet";
                Comment commentInsert = new Comment(0, articleIdBox, userIdBox, true, comment);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "commentInsert");
                jsonObject.addProperty("comment", new Gson().toJson(commentInsert));
                int count = 0;
                try {
                    String result = new CommonTask(url, jsonObject.toString()).execute().get();
                    count = Integer.parseInt(result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (count == 0) {
                    Common.showToast(activity, R.string.commentInsertError);
                } else {
                    Common.showToast(activity, R.string.commentInsertSuccess);

                    //先將加入過的留言加入List內，再呼叫show方法顯示出來
                    comments.add(commentInsert);
                    showComments(comments);
                }
            } else {
                Common.showToast(activity, "連線失敗");
            }
            //收起鍵盤
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                // 如果鍵盤是開啟的 > 關閉
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            //清空edText
            etComment.setText("");
        });


        //向server端取得文章資料
        String url = Common.URL_SERVER + "ArticleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("articleId", articleIdBox); //articleIdBox > 前頁取得的文章ID
        String jsonOut = jsonObject.toString();
        articleGetAllTask = new CommonTask(url, jsonOut);
        try {
            String jsonIn = articleGetAllTask.execute().get();
            Type listType = new TypeToken<Article>() {
            }.getType();
            article = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //宣告物件接文章內容，顯示在畫面
        String title = article.getArticleTitle();   //標題
        tvDetailArticleTitle.setText(title);
        String userName = article.getUserName();    //使用者名稱
        tvDetailUserName.setText(userName);
        String resCategory = article.getResCategoryInfo();  //餐廳種類
        tvDtdailResCategoryInfo.setText(resCategory);
        String resName = article.getResName();  //餐聽名稱
        tvDetailResName.setText("店名：" + resName);
        String DetailArticleTime = article.getArticleTime();  //文章發表時間(修改後時間顯示後補)
        tvDetailArticleTime.setText("發表時間：" + DetailArticleTime);
        String articleText = article.getArticleText();  //文章內文
        tvArticleText.setText(articleText);
        String DetailAvgCon = article.avgCon(); //平均消費
        tvDetailAvgCon.setText(DetailAvgCon);
        String DetailCommentCount = article.getCommentCount() + "";     //取得留言數
        tvDetailCommentCount.setText(DetailCommentCount);

        //取得使用者小圖 > 文章，留言Bar
        String urlIcon = Common.URL_SERVER + "UserAccountServlet";
        int userId = article.getUserId();
        ImageTask imageTaskIcon = new ImageTask(urlIcon, userId, imageSize, ivDetailUserIcon);
        imageTaskIcon.execute();
        imageTasks.add(imageTaskIcon);
        ImageTask imageTaskCommentIcon = new ImageTask(urlIcon, userId, imageSize, user);
        imageTaskCommentIcon.execute();
        imageTasks.add(imageTaskCommentIcon);

        //取得文章圖片
        //橫向recyclerView
        rvArticleImage = view.findViewById(R.id.rvArticleImage);    //圖片recyclerView
        rvArticleImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        imgs = getImgs();
        showImgs(imgs);

        //顯示留言
        rvComment = view.findViewById(R.id.rvComment);  //留言recyclerView
        rvComment.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true));
        comments = getComments();
        showComments(comments);


        //設定點讚功能，1.會員登入判斷還沒寫，要候補
        //2.先判斷使用者是否已點讚
        final boolean articleGoodStatus = article.isArticleGoodStatus();
        ImageView goodIcon = ivDetailGoodIcon;
        if (articleGoodStatus) {
            goodIcon.setColorFilter(Color.parseColor("#1877F2"));
        } else {
            goodIcon.setColorFilter(Color.parseColor("#424242"));
        }
        ivDetailGoodIcon.setImageResource(R.drawable.ic_baseline_thumb_up_24);
        tvDetailGoodCount.setText((article.getArticleGoodCount() + ""));
        ivDetailGoodIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!article.isArticleGoodStatus()) {
                    if (Common.networkConnected(activity)) {
                        String insertGoodUrl = Common.URL_SERVER + "ArticleServlet";
                        int insertUserId = article.getUserId();
                        int insertArticleId = article.getArticleId();
                        Article articleGood = new Article(insertUserId, insertArticleId);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "articleGoodInsert");
                        jsonObject.addProperty("articleGood", new Gson().toJson(articleGood));
                        int count = 0;
                        try {
                            String result = new CommonTask(insertGoodUrl, jsonObject.toString()).execute().get();
                            count = Integer.parseInt(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) {
                            Common.showToast(activity, "點讚失敗");
                        } else {
                            article.setArticleGoodCount(article.getArticleGoodCount() + 1);
                            tvDetailGoodCount.setText((article.getArticleGoodCount() + ""));
                            goodIcon.setColorFilter(Color.parseColor("#1877F2"));
                            article.setArticleGoodStatus(true);
                        }
                    } else {
                        Common.showToast(activity, "取得連線失敗");
                    }
                } else {
                    if (Common.networkConnected(activity)) {
                        String deleteGoodUrl = Common.URL_SERVER + "ArticleServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "articleGoodDelete");
                        jsonObject.addProperty("articleId", article.getArticleId());
                        jsonObject.addProperty("userId", article.getUserId());
                        int count = 0;
                        try {
                            articleDeleteTask = new CommonTask(deleteGoodUrl, jsonObject.toString());
                            String result = articleDeleteTask.execute().get();
                            count = Integer.parseInt(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) { //如果選擇的資料已經沒東西
                            Common.showToast(activity, "取消失敗");
                        } else {
                            article.setArticleGoodCount(article.getArticleGoodCount() - 1);
                            tvDetailGoodCount.setText(((article.getArticleGoodCount()) + ""));
                            goodIcon.setColorFilter(Color.parseColor("#424242"));
                            article.setArticleGoodStatus(false);
                        }
                    } else {
                        Common.showToast(activity, "取消讚連線失敗");
                    }
                }
            }
        });

        //設定收藏功能，1.會員登入判斷還沒寫，要候補
        //2.先判斷使用者是否已收藏
        final boolean articleFavoriteStatus = article.isArticleFavoriteStatus();
        ImageView favoriteIcon = ivDetailFavoriteIcon;
        if (articleFavoriteStatus) {
            favoriteIcon.setColorFilter(Color.parseColor("#EADDAB"));
        } else {
            favoriteIcon.setColorFilter(Color.parseColor("#424242"));
        }
        ivDetailFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
        tvDetailFavoriteArticle.setText((article.getFavoriteCount() + ""));
        ivDetailFavoriteIcon.setOnClickListener(v -> {
            if (!article.isArticleFavoriteStatus()) {
                if (Common.networkConnected(activity)) {
                    String insertFavoriteUrl = Common.URL_SERVER + "ArticleServlet";
                    int favoriteUserId = article.getUserId();
                    int favoriteArticleId = article.getArticleId();
                    Article articleFavorite = new Article(favoriteUserId, favoriteArticleId);
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("action", "articleFavoriteInsert");
                    jsonObject1.addProperty("articleFavorite", new Gson().toJson(articleFavorite));
                    int count = 0;
                    try {
                        String result = new CommonTask(insertFavoriteUrl, jsonObject1.toString()).execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(activity, "收藏失敗");
                    } else {
                        article.setFavoriteCount((article.getFavoriteCount() + 1));
                        tvDetailFavoriteArticle.setText(((article.getFavoriteCount()) + ""));
                        ivDetailFavoriteIcon.setColorFilter(Color.parseColor("#EADDAB"));
                        article.setArticleFavoriteStatus(true);
                    }
                } else {
                    Common.showToast(activity, "取得連線失敗");
                }
            } else {
                if (Common.networkConnected(activity)) {
                    String deleteFavoriteUrl = Common.URL_SERVER + "ArticleServlet";
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("action", "articleFavoriteDelete");
                    jsonObject1.addProperty("userId", article.getUserId());
                    jsonObject1.addProperty("articleId", article.getArticleId());
                    int count = 0;
                    try {
                        articleDeleteTask = new CommonTask(deleteFavoriteUrl, jsonObject1.toString());
                        String result = articleDeleteTask.execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) { //如果選擇的資料已經沒東西
                        Common.showToast(activity, "取消失敗");
                    } else {
                        article.setFavoriteCount((article.getFavoriteCount() - 1));
                        tvDetailFavoriteArticle.setText((article.getFavoriteCount() + ""));
                        favoriteIcon.setColorFilter(Color.parseColor("#424242"));
                        article.setArticleFavoriteStatus(false);
                    }
                } else {
                    Common.showToast(activity, "取得連線失敗");
                }
            }
        });
    }

    //圖片 > 先向後端取得圖片資訊(imgId,articleId)，但先不取得圖片
    private List<Img> getImgs() {
        List<Img> imgs = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ImgServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllById");
            jsonObject.addProperty("articleId", article.getArticleId());
            String jsonOut = jsonObject.toString();
            articleGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = articleGetAllTask.execute().get();
                Type listType = new TypeToken<List<Img>>() {
                }.getType();
                imgs = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return imgs;
    }

    //文章圖片 > 顯示imgs
    private void showImgs(List<Img> imgs) {
        if (imgs == null || imgs.isEmpty()) {
            Common.showToast(activity, R.string.textNoImgFound);
        }
        ImgAdpter imgAdpter = (ImgAdpter) rvArticleImage.getAdapter();
        if (imgAdpter == null) {
            rvArticleImage.setAdapter(new ImgAdpter(activity, imgs));
        } else {
            imgAdpter.setImgs(imgs);
            imgAdpter.notifyDataSetChanged();
        }
    }

    private class ImgAdpter extends RecyclerView.Adapter<ImgAdpter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Img> imgs;
        private int imageSize;

        ImgAdpter(Context context, List<Img> imgs) {
            layoutInflater = LayoutInflater.from(context);
            this.imgs = imgs;
            /* 螢幕寬度除以2當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }

        @Override
        public int getItemCount() {
            return imgs == null ? 0 : imgs.size();
        }

        void setImgs(List<Img> imgs) {
            this.imgs = imgs;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivArticleImage;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivArticleImage = itemView.findViewById(R.id.ivArticleImage);
            }
        }

        @NonNull
        @Override
        public ImgAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.article_image_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ImgAdpter.MyViewHolder myViewHolder, int position) {
            final Img img = imgs.get(position);
            String url = Common.URL_SERVER + "ImgServlet";
            int imgId = img.getImgId();
            int articleId = img.getArticleId();
            ImageTask imageTask = new ImageTask(url, imgId, imageSize, myViewHolder.ivArticleImage, articleId);
//            ImageTask imageTask = new ImageTask(url, imgId, imageSize, myViewHolder.ivArticleImage);
            imageTask.execute();
            imageTasks.add(imageTask);

            myViewHolder.ivArticleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setView(R.layout.show_image_item)
                                .setCancelable(true) // 必須點擊按鈕方能關閉，預設為true
                                .show();
                }
            });
        }

    }

    //取得comment連線 > 先不取得comment使用者圖示
    public List<Comment> getComments() {
        List<Comment> comments = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "CommentServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findCommentById");
            jsonObject.addProperty("articleId", articleIdBox);
            String jsonOut = jsonObject.toString();
            commentGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = commentGetAllTask.execute().get();
                Type listType = new TypeToken<List<Comment>>() {
                }.getType();
//                comments = new Gson().fromJson(jsonIn, listType);
                comments = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return comments;
    }

    public void showComments(List<Comment> commentList) {
        if (commentList == null || commentList.isEmpty()) {
            Common.showToast(activity, R.string.textNoCommentFound);
        }
        CommentAdapter commentAdapter = (CommentAdapter) rvComment.getAdapter();
        if (commentAdapter == null) {
            rvComment.setAdapter(new CommentAdapter(activity, comments));
        } else {
            commentAdapter.setComments(commentList);   //將comments的資料放入commentsAdapter內
            commentAdapter.notifyDataSetChanged();
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Comment> comments;
        private int imageSize;

        CommentAdapter(Context context, List<Comment> comments) {
            layoutInflater = LayoutInflater.from(context);
            this.comments = comments;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels * 4;
        }

        //comments > setter，將List<Comment> 放入CommentAdapter需要用到
        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        public CommentAdapter(List<Comment> comments) {
            this.comments = comments;
        }

        @Override
        public int getItemCount() {
            return comments == null ? 0 : comments.size();
        }

        //抓取要呈現的資料 > comment
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCommentUserAvatar, ivCommentSetting, ivCommentGoodIcon;
            TextView tvCommentUserName, tvCommentText, tvCommentTime, tvCommentGood;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCommentUserAvatar = itemView.findViewById(R.id.ivCommentUserAvatar);
                ivCommentSetting = itemView.findViewById(R.id.ivCommentSetting);
                ivCommentGoodIcon = itemView.findViewById(R.id.ivCommentGoodIcon);
                tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
                tvCommentText = itemView.findViewById(R.id.tvCommentText);
                tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
                tvCommentGood = itemView.findViewById(R.id.tvCommentGood);
            }
        }

        //橋接方法(透過橋接器) > comment
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.article_comment_view, parent, false);
            return new MyViewHolder(itemView);
        }

        //將抓取至MyViewHolder的文章資料呈現到畫面上 > comment
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Comment comment = comments.get(position);
            //取得使用者大頭貼 > 留言
            String urlIcon = Common.URL_SERVER + "UserAccountServlet";
            int userId = article.getUserId();
            ImageTask imageTaskIcon = new ImageTask(urlIcon, userId, imageSize, myViewHolder.ivCommentUserAvatar);
            imageTaskIcon.execute();
            imageTasks.add(imageTaskIcon);

            //留言資訊
            myViewHolder.tvCommentUserName.setText(comment.getUserName());
            myViewHolder.tvCommentText.setText(comment.getCommentText());
            myViewHolder.tvCommentTime.setText(comment.getCommentTime());
            myViewHolder.ivCommentSetting.setImageResource(R.drawable.ic_baseline_more_vert_24);    //設定功能，後要做optionMenu


            //設定留言點讚功能，1.會員登入判斷還沒寫，要候補 > comment
            //2.先判斷使用者是否已點讚 > comment
//            final CommentGood commentGood = commentGoods.get(position);
            boolean commentGoodStatus = comment.isCommentGoodStatus();
            ImageView CommentGoodIcon = myViewHolder.ivCommentGoodIcon;
            if (commentGoodStatus) {
                CommentGoodIcon.setColorFilter(Color.parseColor("#1877F2"));
            } else {
                CommentGoodIcon.setColorFilter(Color.parseColor("#424242"));
            }
            myViewHolder.ivCommentGoodIcon.setImageResource(R.drawable.ic_baseline_thumb_up_24);
            int commentGoodCount = comment.getCommentGoodCount();
            myViewHolder.tvCommentGood.setText((comment.getCommentGoodCount() + ""));
            //3.設定監聽器
            myViewHolder.ivCommentGoodIcon.setOnClickListener(v -> {
                if (!comment.isCommentGoodStatus()) {
                    if (Common.networkConnected(activity)) {
                        String commentGoodUrl = Common.URL_SERVER + "CommentGoodServlet";
                        int insertUserId = comment.getUserId();
                        int insertcommentId = comment.getCommentId();
                        Comment commentGood1 = new Comment(insertUserId, insertcommentId);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "commentGoodInsert");
                        jsonObject.addProperty("commentGood", new Gson().toJson(commentGood1));
                        int count = 0;
                        try {
                            String result = new CommonTask(commentGoodUrl, jsonObject.toString()).execute().get();
                            count = Integer.parseInt(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) {
                            Common.showToast(activity, "點讚失敗");
                        } else {
                            comment.setCommentGoodCount(comment.getCommentGoodCount() + 1);
                            myViewHolder.tvCommentGood.setText((comment.getCommentGoodCount() + ""));
                            CommentGoodIcon.setColorFilter(Color.parseColor("#1877F2"));
                            comment.setCommentGoodStatus(true);
                        }
                    } else {
                        Common.showToast(activity, "取得連線失敗");
                    }
                } else {
                    if (Common.networkConnected(activity)) {
                        String deleteGoodUrl = Common.URL_SERVER + "CommentGoodServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "commentGoodDelete");
                        jsonObject.addProperty("commentId", comment.getCommentId());
                        jsonObject.addProperty("userId", comment.getUserId());
                        int count = 0;
                        try {
                            commentDeleteTask = new CommonTask(deleteGoodUrl, jsonObject.toString());
                            String result = commentDeleteTask.execute().get();
                            count = Integer.parseInt(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) { //如果選擇的資料已經沒東西
                            Common.showToast(activity, "取消失敗");
                        } else {
                            comment.setCommentGoodCount(comment.getCommentGoodCount() - 1);
                            myViewHolder.tvCommentGood.setText((comment.getCommentGoodCount() + ""));
                            CommentGoodIcon.setColorFilter(Color.parseColor("#424242"));
                            comment.setCommentGoodStatus(false);
                        }
                    } else {
                        Common.showToast(activity, "取消讚連線失敗");
                    }
                }
            });
        }
    }


//    // 顯示右上角的OptionMenu選單
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//    }
//
//    // 顯示右上角的OptionMenu選單
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                navController.popBackStack();
////                navController.navigate(R.id.action_articleDetailFragment_to_articleFragment);
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    //生命週期結束，釋放記憶體
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