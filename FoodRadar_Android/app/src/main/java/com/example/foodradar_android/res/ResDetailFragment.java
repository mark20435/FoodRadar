package com.example.foodradar_android.res;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.article.ArticleDetailFragment;
import com.example.foodradar_android.article.Img;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class ResDetailFragment extends Fragment {
    private NavController navController;
    private final static String TAG = "TAG_ResDetailFragment";
    private FragmentActivity activity;
    private ImageView ivRes;
    private TextView tvResName, tvResCategoryInfo, tvResAddress, tvResHours, tvResTel, tvImageNumber;
    private RecyclerView rvImage;
    private Res res;
    private JsonObject jsonHours;
    private List<ImageTask> imageTasks;
    private CommonTask imgGetAllTask;
    private List<Img> imgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common();
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity.setTitle(R.string.titleResDetail);
        return inflater.inflate(R.layout.fragment_res_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("res") == null) {
            Common.showToast(activity, R.string.textNoRessFound);
            navController.popBackStack();
            return;
        }
        res = (Res) bundle.getSerializable("res");

        ivRes = view.findViewById(R.id.ivRes);
        tvResName = view.findViewById(R.id.tvResName);
        tvResCategoryInfo = view.findViewById(R.id.tvResCategoryInfo);
        tvResAddress = view.findViewById(R.id.tvResAddress);
        tvResTel = view.findViewById(R.id.tvResTel);

        tvResName.setText(res.getResName());
        tvResCategoryInfo.setText(res.getResCategoryInfo());
        tvResAddress.setText(res.getResAddress());
        tvResTel.setText(res.getResTel());

        //todo 營業時間
        jsonHours = JsonParser.parseString(res.getResHours()).getAsJsonObject();

        rvImage = view.findViewById(R.id.rvImage);    //圖片recyclerView
        rvImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true));
        imgs = getImgs();
        showImgs(imgs);
    }

    private class ImgAdapter extends RecyclerView.Adapter<ResDetailFragment.ImgAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Img> imgs;
        private int imageSize;

        ImgAdapter(Context context, List<Img> imgs) {
            layoutInflater = LayoutInflater.from(context);
            this.imgs = imgs;
            /* 螢幕寬度除以2當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        }

        @Override
        public int getItemCount() {
            return imgs == null ? 0 : imgs.size();
        }

        void setImgs(List<Img> imgs) {
            this.imgs = imgs;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivImage);
            }
        }

        @NonNull
        @Override
        public ResDetailFragment.ImgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_res_detail_image, parent, false);
            return new ResDetailFragment.ImgAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ResDetailFragment.ImgAdapter.MyViewHolder myViewHolder, int position) {
            final Img img = imgs.get(position);
            String url = Common.URL_SERVER + "ImgServlet";
            int imgId = img.getImagId();
            ImageTask imageTask = new ImageTask(url, imgId, imageSize, myViewHolder.ivImage);
            imageTask.execute();
            imageTasks.add(imageTask);
        }

    }

    private List<Img> getImgs() {
        List<Img> imgs = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ResServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImgByResId");
            String jsonOut = jsonObject.toString();
            imgGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = imgGetAllTask.execute().get();
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
        ResDetailFragment.ImgAdapter imgAdapter = (ResDetailFragment.ImgAdapter) rvImage.getAdapter();
        if (imgAdapter == null) {
            rvImage.setAdapter(new ResDetailFragment.ImgAdapter(activity, imgs));
        } else {
            imgAdapter.setImgs(imgs);
            imgAdapter.notifyDataSetChanged();
        }
    }
}