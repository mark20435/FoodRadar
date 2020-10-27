package com.example.foodradar_android.article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ArticleUpdateFragment extends Fragment {
    private static final String TAG = "TAG_articleUpadte";
    public Activity activity;
    private EditText etConNumUpdate, etArticleTitleUpdate, etArticleTextUpdate, etConAmountUpdate;
    private TextView tvResName;
    private RecyclerView rvUpdateImage;
    private ImageView ivCamera;
    private CommonTask articleGetAllTask;
    private List<ImageTask> imageTasks;
    private List<Bitmap> imgList;
    private List<byte[]> imgBits;
    private Bitmap bitmap = null;
    private byte[] imgbit;
    private List<Img> imgs;
    private Uri imageUri;
    private static final int REQ_TAKE_PICTURE = 0;  //設定拍照圖片請求狀態碼
    private static final int REQ_PICK_PICTURE = 1;  //設定取得圖片請求狀態碼
    private static final int REQ_CROP_PICTURE = 2;  //設定裁切圖片請求狀態碼
    private static final int TYPE_PICK = 0; //rvImage
    private static final int TYPE_IMAGE = 1;
    private ImageView ivPlaceIcon;
    private Article article;
    private Integer articleIdBox = Article.ARTICLE_ID;
    private Integer userIdBox = Common.USER_ID; //已登入的使用者ID
    private NavController navController; //返回鍵

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        imageTasks = new ArrayList<>();
        imgs = new ArrayList<>();
        imgList = new ArrayList<>();

        /* 顯示ActionBar */
        setHasOptionsMenu(true);
        /* 左上返回鍵 */
        Common.setBackArrow(false, activity);
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(activity, R.id.mainFragment);
    }

    /* 右上角，送出按鈕 */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_insert_menu, menu);
    }
    /* actionBar動作判斷 */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /* 左上返回鍵 */
//            case android.R.id.home:
//                navController.popBackStack();
//                break;
            /* 右上送出修改發文 */
            case R.id.menuSend:
            boolean textError = true;

            break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle("更新文章");
        return inflater.inflate(R.layout.fragment_article_update, container, false);
    }

    //隱藏bottomNav
    @Override
    public void onStart() {
        super.onStart();
        ArticleFragment.bottomNavSet(activity, 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etConNumUpdate = view.findViewById(R.id.etConNumUpdate);    //消費人數edText
        etConAmountUpdate = view.findViewById(R.id.etConAmountUpdate);  //消費金額edText
        etArticleTitleUpdate = view.findViewById(R.id.etArticleTitleUpdate);    //文章標題edText
        etArticleTextUpdate = view.findViewById(R.id.etArticleTextUpdate);  //文章內文edText
        tvResName = view.findViewById(R.id.tvResNameUpdate);    //餐廳名稱textView

        ivPlaceIcon = view.findViewById(R.id.ivPlaceIcon);  //新增圖片Icon

        ivCamera = view.findViewById(R.id.ivCamera);    //開啟更改相簿
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final String[] photo = {"相機", "相簿"};
                builder.setItems(photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            /* 使用相機 */
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //intent物件 > 意圖取得圖片檔
                            File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);   //File取得外部圖檔
                            file = new File(file, "picture.jpg");
                            imageUri = FileProvider.getUriForFile(activity, activity.getOpPackageName() + ".provider", file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            //叫Intent.resolveActivity()檢查有無⽀援拍照的app
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                startActivityForResult(intent, REQ_TAKE_PICTURE);   //找到相機app就發出需求，取得照片
                            } else {
                                Common.showToast(activity, "找不到相機");
                            }
                        } else {
                            /* 讀取相簿 */
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQ_PICK_PICTURE); //找到相簿後intent就發出請求，取得相片
                            // Navigation.findNavController(v).navigate(R.id.action_articleInsertFragment_to_insertImageFragment);
                        }
                    }
                })
                        .setTitle("選擇圖片來源")
                        .setCancelable(true) // 必須點擊按鈕方能關閉，預設為true
                        .show();
            }
        });


        /* 取得Bundle資料，顯示在edText上 */
        Bundle bundle = getArguments();
        if (bundle != null) {
            String conNumUpdate = bundle.getString("conNum");
            etConNumUpdate.setText(conNumUpdate);
            String conAmountUpdate = bundle.getString("conAmount");
            etConAmountUpdate.setText(conAmountUpdate);
            String articleTitleUpdate = bundle.getString("articleTitle");
            etArticleTitleUpdate.setText(articleTitleUpdate);
            String articleTextUpdate = bundle.getString("articleText");
            etArticleTextUpdate.setText(articleTextUpdate);

            /* 取餐廳資訊，顯示在TextView */
            String resName = bundle.getString("resName");
            String resCategory = bundle.getString("resCategory");
            tvResName.setText(resCategory + "\n" + "餐廳：" + resName);

            /* ImageRecyclerView */
            rvUpdateImage = view.findViewById(R.id.rvUpdateImage);
            rvUpdateImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

            /* 取得原本文章的圖片並顯示 */
            imgs = getImgs();
            showImgs(imgs);
        }
    }

    /* 取得圖片 (資訊)，特定articleId的圖片資訊，先不取圖片 */
    private List<Img> getImgs() {
        List<Img> imgs = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ImgServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllById");
            jsonObject.addProperty("articleId", articleIdBox);
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

    /* 顯示圖片方法 */
    private void showImgs(List<Img> imgs) {
        if (imgs == null || imgs.isEmpty()) {
            Common.showToast(activity, R.string.textNoImgFound);
        }
        ImgAdapter imgAdapter = (ImgAdapter) rvUpdateImage.getAdapter();
        if (imgAdapter == null) {
            rvUpdateImage.setAdapter(new ImgAdapter(activity, imgs));
        } else {
            imgAdapter.setImgs(imgs);
            imgAdapter.notifyDataSetChanged();
        }
    }

    private void showImgList(List<Bitmap> imgList) {
        if (imgs == null || imgs.isEmpty()) {
            Common.showToast(activity, R.string.textNoImgFound);
        }
        ImgAdapter imgAdapter = (ImgAdapter) rvUpdateImage.getAdapter();
        if (imgAdapter == null) {
            rvUpdateImage.setAdapter(new ImgAdapter(activity, imgs));
        } else {
            imgAdapter.setImgs(imgs);
            imgAdapter.notifyDataSetChanged();
        }
    }

    /* 宣告ImageAdpter > 繼承RecyclerView.Adapter */
    //泛型需為RecyclerView.ViewHolder > 才能客製化recyclerView項目
    private class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater; //宣告Inflater > 橋接器
        private List<Img> imgs;
        private int imageSize;

        /* 建構方法 > 將 (圖片) 與recyclerView接合的方法 */
        ImgAdapter(Context context, List<Img> imgs) {
            layoutInflater = LayoutInflater.from(context);
            this.imgs = imgs;
            /* 螢幕寬度除以2當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }

        public LayoutInflater getLayoutInflater() {
            return layoutInflater;
        }

        public void setLayoutInflater(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        public List<Img> getImgs() {
            return imgs;
        }

        public void setImgs(List<Img> imgs) {
            this.imgs = imgs;
        }

        public int getImageSize() {
            return imageSize;
        }

        public void setImageSize(int imageSize) {
            this.imageSize = imageSize;
        }

        /* 決定recyclerView的個數 */
        @Override
        public int getItemCount() {
            return imgs == null ? 0 : (imgs.size());
        }

        /* 宣告MyViewHolder 1.顯示出原本文章的image > 繼承RecyclerView.ViewHolder */
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivArticleImageUpdate;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivArticleImageUpdate = itemView.findViewById(R.id.ivArticleImageInsert);
            }
        }

        /* 圖片 > 將ImgAdapter接合到myViewHolder > 判斷顯示圖片還是新增圖片 */
        @NonNull
        @Override
        public ImgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            itemView = layoutInflater.inflate(R.layout.article_image_insert, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ImgAdapter.MyViewHolder holder, int position) {
            /* 取得顯示預設圖片 */
            final Img img = imgs.get(position);
            //取得文章圖片
            String url = Common.URL_SERVER + "ImgServlet";
            int imgId = img.getImgId();
            int articleId = img.getArticleId();
            ImageTask imageTask = new ImageTask(url, imgId, imageSize, holder.ivArticleImageUpdate, articleId);
            imageTask.execute();
            imageTasks.add(imageTask);
            // position -1 > 因為每增加一筆資料，onBindViewHolder的position會自動加1，(0被PickViewHolder綁住)
            // 但imgList的索引值是從0開始，對不上position的1 ， 所以 position - 1 > 跟
//                Bitmap bitmapPosition = imgList.get(position - 1);
//                myViewHolder.ivArticleImageUpdate.setImageBitmap(bitmapPosition);
        }

        /* 圖片裁減方法 */


        /* 圖片裁剪後的動作 */
        private void handleCropResult(Intent intent) {
            //取得裁減後的圖片
            Uri resultUri = UCrop.getOutput(intent);
            if (resultUri == null) {
                return;
            }
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    //取得resultUri後解碼為bitmap(舊版方法)
                    bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(resultUri));
                } else {
                    //新版方法
                    ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                imgbit = output.toByteArray();//將output解碼成Byte陣列
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (bitmap != null) {
                imgList.add(bitmap);
                showImgList(imgList);
            }
        }
    }

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

    /* 圖片裁減方法 */
    private void crop(Uri sourceImageUri) {
        //取得外部檔案路徑
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg"); //切割過後儲存檔名
        Uri destinationUri = Uri.fromFile(file); //檔案路徑
        //UCrop > 圖片裁剪框
        UCrop.of(sourceImageUri, destinationUri).start(activity, this, REQ_CROP_PICTURE);
    }

    /* 圖片裁剪後的動作 */
    private void handleCropResult(Intent intent) {
        //取得裁減後的圖片
        Uri resultUri = UCrop.getOutput(intent);
        // Bitmap bitmap = null;
        if (resultUri == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                //取得resultUri後解碼為bitmap(舊版方法)
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(resultUri));
            } else {
                //新版方法
                ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            imgbit = output.toByteArray();//將output解碼成Byte陣列
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            imgList.add(bitmap);
            showImgList(imgList);
        }
    }

    /* 取得上述動作執行的結果 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(imageUri);
                    break;

                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;

                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;
            }
        }
    }
}