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
import android.widget.Button;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;


public class UserAreaFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private NavController navController;
    private Integer UserId = 3;

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
        inflater.inflate(R.menu.appbar_menu,menu);  // 從res取用選項的清單“R.menu.my_menu“
        super.onCreateOptionsMenu(menu, inflater);
    }
    // 顯示右上角的OptionMenu選單
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Finish:
                navController.navigate(R.id.action_userAreaFragment_to_userDataSetupFragment);
                break;
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

        view.findViewById(R.id.id_btResMaintain).setOnClickListener(this);
        view.findViewById(R.id.id_btMyRes).setOnClickListener(this);

        // vvvvvv臨時寫的，用來模擬使用者登入
        String userPhone = "0900123456";
        String userPwd = "P@ssw0rd";
//         Integer userId = 3;
//        Integer UserId = new Common().userLogin(activity,userPhone,userPwd);
        if(UserId > 0){
            UserId = Common.USER_ID; //new Common().getUserLoin(activity);
            new Common().showToast(activity, "會員專區，\n登入成功，userId: "  + UserId);
        } else {
            switch (UserId){
                case 0: // 0=>登入失敗(原因不明)
                    new Common().showToast(activity, "會員專區，\n登入失敗(原因不明)，\nuserId: " + UserId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
                    break;
                case -1: // -1=>使用者帳號(手機號碼)不存在
                    new Common().showToast(activity, "會員專區，\n使用者帳號(手機號碼)不存在，\nuserId: " + UserId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
                    break;
                case -2: // -2=>使用者密碼錯誤
                    new Common().showToast(activity, "會員專區，\n使用者密碼錯誤，\nuserId: " + UserId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
                    break;
                default:
                    new Common().showToast(activity, "會員專區，\n登入失敗，\nuserId: " + UserId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
                    break;
            }
        }
        // ^^^^^^臨時寫的，用來模擬使用者登入


//        Button button;
//        button = view.findViewById(R.id.id_btResMaintain);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_userAreaFragment_to_resMaintainFragment);
//
//            }
//        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.id_btResMaintain:
                navController.navigate(R.id.action_userAreaFragment_to_resMaintainFragment);
                break;
            case R.id.id_btMyRes:
                navController.navigate(R.id.action_userAreaFragment_to_userMyResFragment);
                break;
            default:
                break;

        }

    }
}