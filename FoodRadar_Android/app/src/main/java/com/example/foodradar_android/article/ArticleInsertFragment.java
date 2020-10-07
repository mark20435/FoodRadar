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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ArticleInsertFragment extends Fragment {
    private final static String TAG = "ArticleInsertFragment";
    private AppCompatActivity appCompatActivity;
    public Activity activity;
    private EditText etConNum, etArticleTitle, etArticleText, etConAmount;
    private TextView tvResName;
    private RecyclerView rvInsertImage;
    private ImageView ivPlaceIcon;
    private ImageView ivArticleImageInsert;
    //    private List<ResAddress> resAddresses;
    private ResAddress resAddresses;
    private static final int TYPE_PICK = 0;
    private static final int TYPE_IMAGE = 1;
    private List<ImageTask> imageTasks;
    private byte[] imgbit;
    private List<Img> imgs;
    private Img img;
    private List<Bitmap> imgList;
    private Uri imageUri;
    private static final int REQ_TAKE_PICTURE = 0;  //設定拍照圖片請求狀態碼
    private static final int REQ_PICK_PICTURE = 1;  //設定取得圖片請求狀態碼
    private static final int REQ_CROP_PICTURE = 2;  //設定裁切圖片請求狀態碼
    private Bitmap bitmap = null;
    private NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        setHasOptionsMenu(true);

        // 顯示左上角的返回箭頭
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);
        navController =
                Navigation.findNavController(activity, R.id.mainFragment);

        //三元 > bundle取得餐廳資訊
        resAddresses = (ResAddress) (getArguments() != null ? getArguments().getSerializable("ResAddress") : null);

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
        tvResName = view.findViewById(R.id.tvResName);
        imgList = new ArrayList<>();


        //顯示餐廳資訊
        if (resAddresses != null) {
            String resName = resAddresses.getResName();
            String resCategoryInfo = resAddresses.getResCategoryInfo();
            tvResName.setText(resCategoryInfo + "\n" + "餐廳：" + resName);
        }

        //跳轉至，選擇餐廳頁面(外)
        ivPlaceIcon = view.findViewById(R.id.ivPlaceIcon);
        tvResName = view.findViewById(R.id.tvResName);
        ivPlaceIcon.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_articleInsertFragment_to_resAddressFragment));

        //橫向顯示圖片
        rvInsertImage = view.findViewById(R.id.rvInsertImage);
        rvInsertImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
//        imgs = getImgs();
        showImgs(imgList);
    }

//    private List<Img> getImgs() {
//        List<Img> imgs = null;
//        return imgs;
//    }

    private void showImgs(List<Bitmap> imgList) {
        if (imgs == null || imgs.isEmpty()) {
            ImgAdpter imgAdpter = (ImgAdpter) rvInsertImage.getAdapter();
            if (imgAdpter == null) {
                rvInsertImage.setAdapter(new ImgAdpter(ivPlaceIcon, activity, imgList));
            } else {
                imgAdpter.setImgList(imgList);
                imgAdpter.notifyDataSetChanged();
            }
        }
    }

    //取得UserID方法
    private Integer getUserID() {
        return Common.USER_ID;
    }

    //右上角，送出按鈕
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.article_insert_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //左上返回鍵
            case android.R.id.home:
                navController.popBackStack();
                break;
            //右上送出發文
            case R.id.menuSend:
                Integer userId = getUserID();
//                String resName = tvResName.toString();
                String conNumStr = etConNum.getText().toString().trim();   //輸入人數轉為字串
                Log.d(TAG, "conNumStr:::::::: " + conNumStr);
                int conNum = Integer.parseInt(conNumStr);  //輸入人數，int型態
                if (conNumStr.length() <= 0 || conNum <= 0) {
                    etConNum.setError("請輸入正確消費人數");
                    return false;
                }
                String conAmountStr = etConAmount.getText().toString().trim();   //輸入消費轉為字串
                int conAmount = Integer.parseInt(conAmountStr);   //輸入消費金額，int型態
                if (conAmountStr.length() <= 0 || conAmount <= 0) {
                    etConAmount.setError("請輸入正確消費金額");
                    return false;
                }
                String articleTitle = etArticleTitle.getText().toString().trim();   //輸入文章主題
                if (articleTitle.length() <= 0) {
                    etArticleTitle.setError("請輸入文章主題");
                    return false;
                }
                String articleText = etArticleText.getText().toString().trim();     //輸入文章內文
                if (articleText.length() <= 0) {
                    etArticleTitle.setError("請輸入文章內文");
                    return false;
                }
                //取得餐廳的ID
                int resId = resAddresses.getResId();
                //送出文章資料(不含圖片)
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "ArticleServlet";
                    Article article = new Article(0 ,articleTitle ,articleText ,conNum, conAmount ,resId , 1, true);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "articleInsert");
                    jsonObject.addProperty("article", new Gson().toJson(article));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(activity, "上傳失敗");
                    } else {
                        Common.showToast(activity, "發文成功");
                    }
                } else {
                    Common.showToast(activity, "連線失敗");
                }

                //上傳圖片(Insert)
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "ImgServlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "findByIdMax");
                    Img img = new Img (0, 0);
                    jsonObject.addProperty("img", new Gson().toJson(img));

                    //確認是否有取得圖檔才會上傳
                    if (imgbit != null) {
                        jsonObject.addProperty("imageBase64", Base64.encodeToString(imgbit, Base64.DEFAULT));
                    }
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(activity, "上傳失敗");
                    } else {
                        Common.showToast(activity, "發文成功");
                    }
                } else {
                    Common.showToast(activity, "連線失敗");
                }
                //結束動作後返回前一頁
                navController.navigate(R.id.action_articleInsertFragment_to_articleFragment);
//                navController.popBackStack();
                break;
            //預設
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class ImgAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Img> imgs;
        private ImageView pickIcon;
        private int imageSize;
        private List<Bitmap> imgList;

        public List<Bitmap> getImgList() {
            return imgList;
        }

        public void setImgList(List<Bitmap> imgList) {
            this.imgList = imgList;
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

        public ImageView getPickIcon() {
            return pickIcon;
        }

        public void setPickIcon(ImageView pickIcon) {
            this.pickIcon = pickIcon;
        }

        public int getImageSize() {
            return imageSize;
        }

        public void setImageSize(int imageSize) {
            this.imageSize = imageSize;
        }

        ImgAdpter(ImageView pickIcon, Context context, List<Bitmap> imgList) {
            layoutInflater = LayoutInflater.from(context);
            this.imgs = imgs;
            this.pickIcon = pickIcon;
            this.imgList = imgList;
            /* 螢幕寬度除以2當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        }

        //圖片ViewHolder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivArticleImageInsert;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivArticleImageInsert = itemView.findViewById(R.id.ivArticleImageInsert);
            }
        }

        //加入圖片ViewHolder > header
        public class PickViewHolder extends RecyclerView.ViewHolder {
            ImageView ivArticleImagePick;

            public PickViewHolder(@NonNull View itemView) {
                super(itemView);
                ivArticleImagePick = itemView.findViewById(R.id.ivArticleImagePick);
            }
        }

        //區分顯示的種類
        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_PICK;   //選擇圖片
            } else {
                return TYPE_IMAGE;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (viewType == TYPE_PICK) {
                itemView = layoutInflater.inflate(R.layout.article_image_pick, parent, false);
                return new PickViewHolder(itemView);
            } else {
                itemView = layoutInflater.inflate(R.layout.article_image_insert, parent, false);
                return new MyViewHolder(itemView);
            }
        }

        //設定長度
        @Override
        public int getItemCount() {
            return imgList == null ? 1 : (1 + imgList.size());
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof PickViewHolder) {
                PickViewHolder pickViewHolder = (PickViewHolder) holder; //要強轉！！
                pickViewHolder.ivArticleImagePick.setImageResource(R.drawable.ic_add);
                pickViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        final String[] photo = {"相機", "相簿"};
                        builder.setItems(photo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //使用相機
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //intent物件 > 意圖取得圖片檔
                                    File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);   //File取得外部圖檔
                                    file = new File(file, "picture.jpg");
                                    imageUri =  FileProvider.getUriForFile(activity, activity.getOpPackageName() + ".provider", file);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    //叫Intent.resolveActivity()檢查有無⽀援拍照的app
                                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                        startActivityForResult(intent, REQ_TAKE_PICTURE);   //找到相機app就發出需求，取得照片
                                    } else {
                                        Common.showToast(activity, "找不到相機");
                                    }
                                } else {
                                    Navigation.findNavController(v).navigate(R.id.action_articleInsertFragment_to_insertImageFragment);
                                }
                            }
                        })
                                .setTitle("選擇圖片來源")
                                .setCancelable(true) // 必須點擊按鈕方能關閉，預設為true
                                .show();
                    }
                });
           } else if (holder instanceof MyViewHolder) {
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                    // position -1 > 因為每增加一筆資料，onBindViewHolder的position會自動加1，(0被PickViewHolder綁住)
                    // 但imgList的索引值是從0開始，對不上position的1 ， 所以 position - 1 > 跟
                    Bitmap bitmapPosition = imgList.get(position - 1);
                    myViewHolder.ivArticleImageInsert.setImageBitmap(bitmapPosition);
            }

        }
    }

//    圖片裁減方法
    private void crop(Uri sourceImageUri) {
        //取得外部檔案路徑
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg"); //切割過後儲存檔名
        Uri destinationUri = Uri.fromFile(file); //檔案路徑
        //UCrop > 圖片裁剪框
        UCrop.of(sourceImageUri, destinationUri).start(activity, this, REQ_CROP_PICTURE);
    }

    //圖片裁剪後的動作
    private void handleCropResult(Intent intent) {
        //取得裁減後的圖片
        Uri resultUri = UCrop.getOutput(intent);
//        Bitmap bitmap = null;
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
        imgList.add(bitmap);
        showImgs(imgList);
    }


    //取得上述動作執行的結果
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