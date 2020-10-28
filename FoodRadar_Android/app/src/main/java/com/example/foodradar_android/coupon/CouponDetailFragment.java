package com.example.foodradar_android.coupon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowAnimationFrameStats;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class CouponDetailFragment extends Fragment {
    private final static String TAG = "TAG_CouponDetailFragment";
    private NavController navController;
    private FragmentActivity activity;
    private List<ImageTask> imageTasks;
    private ImageTask imageTask;
    private int imageSize;
    private TextView couPonEndDate, couPonStartDate;
    private ImageView ivCoupon, ivNoUse;
    private CommonTask couponGetAllTask;
    private CommonTask couponDeleteTask;
    private Coupon coupon;
    private byte[] image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        navController = Navigation.findNavController(activity, R.id.mainFragment);
        //coupon = (Coupon) (getArguments() != null ? getArguments().getSerializable("coupon") : null);
        new Common().setBackArrow(true, activity);
        setHasOptionsMenu(true);
        }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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

        return inflater.inflate(R.layout.fragment_coupon_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        Coupon coupon =(Coupon) bundle.getSerializable("coupon");
        ivCoupon = view.findViewById(R.id.ivDemo);
        ivCoupon.setImageResource(R.drawable.no_image);
        couPonStartDate = view.findViewById(R.id.couPonStartDate);
        couPonEndDate = view.findViewById(R.id.couPonEndDate);

        ivNoUse = view.findViewById(R.id.ivNoUse);
        String url = Common.URL_SERVER + "CouponServlet";
        int id = coupon.getId();
        imageSize = getResources().getDisplayMetrics().widthPixels;
        ImageTask imageTask = new ImageTask(url, id, imageSize, ivCoupon);
        imageTask.execute();

        FloatingActionButton btUse = view.findViewById(R.id.btUse);
        btUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getUserId() > 0){
                    ivNoUse.setVisibility(View.VISIBLE);
                }else if (getUserId() <= 0){
                    navController.popBackStack();
                }

            }
        });


    }




    public CouponDetailFragment() {
        // Required empty public constructor
    }

    private int getUserId(){
        return Common.USER_ID;
    }
}
