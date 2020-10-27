package com.example.foodradar_android.coupon;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.example.foodradar_android.task.ImageTask;

public class CouponFragment extends Fragment {
    private static final String TAG = "TAG_CouponFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swRf;
    private RecyclerView rvCoupon;
    private RecyclerView rvSample;
    private FragmentActivity activity;
    private NavController navController;
    private TextView tvCouInfo, couPonStartDate, couPonEndDate;
    private ImageView imageAlert, imageAlert2, ivNoUse;
    private ImageButton ibUseCard;
    private Timestamp Date;
    private boolean couPonType;
    private boolean couPonEnable;
    private CommonTask couponGetAllTask;
    private CommonTask couponDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Coupon> coupons;
    private List<Coupon> couacts;
    private int UserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        imageTasks = new ArrayList<>();
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(activity, R.id.mainFragment);
        //new Common().setBackArrow(false,activity);
    }
    private void showDialog(int imageId, int imageId2) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        new Common().setBackArrow(true, activity);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.popupAnimation);
        View view = View.inflate(activity, R.layout.alert_dialog_view, null);
        imageAlert = view.findViewById(R.id.imageAlert);
        imageAlert.setImageResource(imageId);
        imageAlert2 = view.findViewById(R.id.imageAlert2);
        imageAlert2.setImageResource(imageId2);
        ibUseCard = view.findViewById(R.id.ibUseCard);
        ibUseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
//        alertView = view.findViewById(R.id.tvUseInfo1);
//        alertView.setText(tvUseInfo1);
//        alertView2 = view.findViewById(R.id.tvUseInfo2);
//        alertView2.setText(tvUseInfo2);

        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //navController.navigate(R.id.action_couponFragment_to_fcmFragment);
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.appbar_ezfcm,menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.ezFcm:
                showDialog(R.drawable.chinacate, R.drawable.no_image);
                return true;
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.coupon);
        return inflater.inflate(R.layout.fragment_coupon, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvSample = view.findViewById(R.id.rvSample);
        rvCoupon = view.findViewById(R.id.rvCoupon);
//        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swRf = view.findViewById(R.id.swRf);
        rvSample.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        rvCoupon.setLayoutManager(new LinearLayoutManager(activity));

        couacts = getCouacts();
        showCouacts(couacts);

        coupons = getCoupons();
        showCoupons(coupons);

        swRf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swRf.setRefreshing(true);
                showCoupons(couacts);
                swRf.setRefreshing(false);
            }
        });
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showCoupons(coupons);
//                swipeRefreshLayout.setRefreshing(false);
//
//            }
//        });

    }



    private List<Coupon> getCouacts() {
        List<Coupon> couacts = null;
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
                couacts = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return couacts;
    }

    private void showCouacts(List<Coupon> couacts) {
        if (couacts == null || couacts.isEmpty()) {
            Common.showToast(activity, R.string.textNoCouponsFound);
        }
        CouactAdapter couactAdapter = (CouactAdapter) rvCoupon.getAdapter();

        if (couactAdapter == null) {
            rvCoupon.setAdapter(new CouactAdapter(activity, couacts));
        } else {
            couactAdapter.setCouacts(couacts);
            couactAdapter.notifyDataSetChanged();
        }
    }
    private class CouactAdapter extends RecyclerView.Adapter<CouactAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<Coupon> couacts;
        private int imageSize;

        public CouactAdapter(Context context, List<Coupon> couacts) {
            layoutInflater = LayoutInflater.from(context);
            this.couacts = couacts;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        public void setCouacts(List<Coupon> couacts) {
            this.couacts = couacts;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView couPonInfo, couPonStartDate, couPonEndDate;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivDemo);
                couPonInfo = itemView.findViewById(R.id.tvCouInfo);
                couPonStartDate = itemView.findViewById(R.id.couPonStartDate);
                couPonEndDate = itemView.findViewById(R.id.couPonEndDate);

            }
        }

        @NonNull
        @Override
        public CouactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_couact, parent, false);
            return new CouactAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CouactAdapter.MyViewHolder holder, int position) {
            //final Couact couact = couacts.get(position);
            final Coupon coupon = coupons.get(position);
            String url = Common.URL_SERVER + "CouponServlet";
            int id = coupon.getId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, holder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);

            holder.couPonInfo.setText(coupon.getCouPonInfo());
            holder.couPonStartDate.setText(coupon.getCouPonStartDate() + "~" + coupon.getCouPonEndDate());
            //holder.couPonEndDate.setText(coupon.getCouPonEndDate());
            //Log.d(TAG,"coupon.getTvCouInfo(): " + coupon.getTvCouInfo());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("coupon", coupon);
                    Navigation.findNavController(v)
                            .navigate(R.id.action_couponFragment_to_couponDetailFragment, bundle);

                }
            });
        }
        protected RecyclerView.LayoutManager initLayoutManger(){
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            return manager;
        }
        @Override
        public int getItemViewType(int position) {

            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return couacts == null ? 0 : couacts.size();
        }


    }
        //以下是上面的rvsample.recyclerview
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
        CouponAdapter couponAdapter = (CouponAdapter) rvSample.getAdapter();

        if (couponAdapter == null) {
            rvSample.setAdapter(new CouponAdapter(activity, coupons));
        } else {
            couponAdapter.setCoupons(coupons);
            couponAdapter.notifyDataSetChanged();
        }
    }
    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Coupon> coupons;
        private int imageSize;


        CouponAdapter(Context context, List<Coupon> coupons) {
            layoutInflater = LayoutInflater.from(context);
            this.coupons = coupons;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }


        public void setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }


         class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView upcouPonInfo, couPonStartDateUp, couPonEndDateUp;

             public MyViewHolder(@NonNull View itemView) {
                 super(itemView);
                 imageView = itemView.findViewById(R.id.ivDemoUp);
                 upcouPonInfo = itemView.findViewById(R.id.upCouInfo);
                 couPonStartDateUp = itemView.findViewById(R.id.couPonStartDateUp);
                 couPonEndDateUp = itemView.findViewById(R.id.couPonEndDateUp);

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
            //UserAccount userAccount = new Common().getUserLoin(activity);
            //Common.USER_ID = userAccount.getUserId();
            //Common.showToast(activity,"TAG_ UserAreaFragment.USER_ID: " + String.valueOf(getUserId()));

            final Coupon coupon = coupons.get(position);
            String url = Common.URL_SERVER + "CouponServlet";
            int id = coupon.getId();

            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
//            myViewHolder.tvCouName.setText(coupon.getResName());
           // Log.d(TAG, "resName" + coupon);
            myViewHolder.upcouPonInfo.setText(coupon.getCouPonInfo());
            myViewHolder.couPonStartDateUp.setText(coupon.getCouPonStartDate());
            myViewHolder.couPonEndDateUp.setText(coupon.getCouPonEndDate());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("coupon", coupon);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_couponFragment_to_couponDetailFragment, bundle);
                }
            });
        }


    }

//    private void schedule(TimerTask task, long delay, long period){
//        final int[] position = {0};
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                if (position[0] == coupons.size()){
//                    position[0] = 0;
//                    position[0]++;
//                }else{
//                    position[0]++;
//                }
//                rvSample.getLayoutManager().scrollToPosition(position[0]);
//                timer.scheduleAtFixedRate(task, 1000, 3000);
//                //timer.schedule(task, 1000, 3000);
//
//            }
//
//        };

        //timer.schedule(timerTask, 1000, 3000);
//          List<Coupon> coupon = new ArrayList<>();
//          coupons = getCoupons();
//          int index = coupon.indexOf(coupons);
//          rvSample.smoothScrollToPosition(index);

}