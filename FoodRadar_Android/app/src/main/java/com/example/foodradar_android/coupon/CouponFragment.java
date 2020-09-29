package com.example.foodradar_android.coupon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CouponFragment extends Fragment {
    private static final String TAG = "TAG_CouponFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvCoupon;
    private RecyclerView rvSample;
    private Activity activity;
    private Timestamp Date;
    private CommonTask couponGetAllTask;
    private CommonTask couponDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Coupon> coupons;



    public CouponFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        imageTasks = new ArrayList<>();

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coupon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvSample = view.findViewById(R.id.rvSample);
        rvCoupon = view.findViewById(R.id.rvCoupon);

        rvSample.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        coupons = getCoupons();
        showCoupons(coupons);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showCoupons(coupons);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }
    private List<Coupon> getCoupons() {
        List<Coupon> coupons = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "CouponServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            couponGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = couponGetAllTask.execute().get();
                Type listType = new TypeToken<List<Coupon>>() {
                }.getType();
                coupons = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return coupons;

    }

    private void showCoupons(List<Coupon> coupons) {
        if (coupons == null || coupons.isEmpty()) {
            Common.showToast(activity, R.string.textNoCouponsFound);
        }
        CouponAdapter spotAdapter = (CouponAdapter) rvCoupon.getAdapter();

        if (spotAdapter == null) {
            rvCoupon.setAdapter(new CouponAdapter(activity, coupons));
        } else {
            spotAdapter.setCoupons(coupons);
            spotAdapter.notifyDataSetChanged();
        }
    }
    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Coupon> coupons;
        private int imageSize;

        public CouponAdapter(Context context, List<Coupon> coupons) {
            layoutInflater = LayoutInflater.from(context);
            this.coupons = coupons;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }


        public void setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }


         class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView resName, tvCouInfo;

             public MyViewHolder(@NonNull View itemView) {
                 super(itemView);

             }
         }
        @Override
        public int getItemCount() {
            return coupons == null ? 0 : coupons.size();
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_coupon, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Coupon coupon = coupons.get(position);
            String url = Common.URL_SERVER + "CouponServlet";
            int id = coupon.getCouPonId();



        }


    }





    private class ImageTask {
    }



}