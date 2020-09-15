package com.example.foodradar_android.user;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;

public class UserDataSetupFragment extends Fragment {
    private Activity activity;
    private NavController navController;
    private Integer UserId = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common().setBackArrow(true, activity);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.title_of_userdata);
        return inflater.inflate(R.layout.fragment_user_data_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 預設無資料的頭像
        ImageView imageAvatar = view.findViewById(R.id.id_ivAvatar);
        imageAvatar.setImageResource(R.drawable.ic_baseline_account_circle_24);

        // vvvvvv臨時寫的，用來模擬使用者登入
        // String userPhone = "0900123456";
        // String userPwd = "P@ssw0rd";
        // Integer userId = 3;
        UserId= new Common().getUserLoin(activity);
        if(UserId > 0){
            new Common().showToast(activity, "會員資料設定，登入成功，UserId: " + UserId);
        } else {
            new Common().showToast(activity, "會員資料設定，登入失敗，UserId: " + UserId);
        }
        // ^^^^^^臨時寫的，用來模擬使用者登入

    }

}