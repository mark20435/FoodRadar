package com.example.foodradar_android.user;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;


public class UserAreaFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private NavController navController;
    private TextView tvMyResUserArea;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    // 顯示右上角的OptionMenu選單
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.appbar_menu,menu);  // 從res取用選項的清單“R.menu.my_menu“
//        super.onCreateOptionsMenu(menu, inflater);
    }
    // 顯示右上角的OptionMenu選單
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
//            case R.id.Finish:
//                navController.navigate(R.id.action_userAreaFragment_to_userDataSetupFragment);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.user);
        return inflater.inflate(R.layout.fragment_user_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 我的餐廳收藏
        view.findViewById(R.id.tvMyResUserArea).setOnClickListener(this);
        // 我的優惠券收藏
        view.findViewById(R.id.tvMyCouponUserArea).setOnClickListener(this);
        // 我的社群活動
        view.findViewById(R.id.tvMyArticleUserArea).setOnClickListener(this);
        // 會員資料設定
        view.findViewById(R.id.tvUserDataSetupUserArea).setOnClickListener(this::onClick);
        // 系統設定
        view.findViewById(R.id.tvSystemSetupUserArea).setOnClickListener(this);
        // 聯繫我們
        view.findViewById(R.id.tvContactUsUserArea).setOnClickListener(this);

        view.findViewById(R.id.id_btResMaintain).setOnClickListener(this);

        // vvvvvv 臨時加的
        UserAccount userAccount = new Common().getUserLoin(activity);
        Common.USER_ID = userAccount.getUserId();
        Common.showToast(activity,"TAG_ UserAreaFragment.USER_ID: " + String.valueOf(getUserId()));
        // ^^^^^^^ 臨時加的

    }

    private int getUserId(){
        return Common.USER_ID;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btResMaintain:
                navController.navigate(R.id.action_userAreaFragment_to_resMaintainFragment);
                break;

            // 我的餐廳收藏
            case R.id.tvMyResUserArea:
                navController.navigate(R.id.action_userAreaFragment_to_userMyResFragment);
                break;
            // 我的優惠券收藏
            case R.id.tvMyCouponUserArea:
                break;
            // 我的社群活動
            case R.id.tvMyArticleUserArea:
                break;
            // 會員資料設定
            case R.id.tvUserDataSetupUserArea:
                navController.navigate(R.id.action_userAreaFragment_to_userDataSetupFragment);
                break;
            // 系統設定
            case R.id.tvSystemSetupUserArea:
                break;
            // 聯繫我們
            case R.id.tvContactUsUserArea:
                break;
            default:
                break;
        }

    }
}