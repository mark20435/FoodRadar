package com.example.foodradar_android.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;


public class LoginFragment extends Fragment  {
    private Activity activity;
    private NavController navController;
    private String username;
    private String userPwd;
    private Integer UserId = 9;
//    private Boolean loggedin = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity.setTitle("登入頁面");
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.username);
        view.findViewById(R.id.userpwd);

        Button login = view.findViewById(R.id.login);
        Button cancel = view.findViewById(R.id.regcancel);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (username.equals("0900123456") && userPwd.equals("P@ssw0rd") && UserId.equals("9")){
//                    navController.navigate(R.id.action_loginFragment_to_mainFragment);
//                }else{
//                    new Common().showToast(activity, "登入失敗"  + UserId);
//
//                }
//                String username = "0900123456";
//                String userPwd = "P@ssw0rd";


//                    if(UserId > 3){
//                        UserId = Common.USER_ID; //new Common().getUserLoin(activity);
//                        //new Common().showToast(activity, "會員專區，\n登入成功，userId: " + UserId);
//                        navController.navigate(R.id.action_loginFragment_to_mainFragment);
//
//                    }
//
//                 else {
//                    switch (UserId) {
//                        case 2: // 0=>登入失敗(原因不明)
//                            new Common().showToast(activity, "會員專區，\n登入失敗(原因不明)，\nuserId: " + UserId + ",\nuserPhone:" + username + ",\nuserPwd: " + userPwd);
//                            break;
//                        case 1: // -1=>使用者帳號(手機號碼)不存在
//                            new Common().showToast(activity, "會員專區，\n使用者帳號(手機號碼)不存在，\nuserId: " + UserId + ",\nuserPhone:" + username + ",\nuserPwd: " + userPwd);
//                            break;
//                        case 0: // -2=>使用者密碼錯誤
//                            new Common().showToast(activity, "會員專區，\n使用者密碼錯誤，\nuserId: " + UserId + ",\nuserPhone:" + username + ",\nuserPwd: " + userPwd);
//                            break;
//                        default:
//                            new Common().showToast(activity, "會員專區，\n登入失敗，\nuserId: " + UserId + ",\nuserPhone:" + username + ",\nuserPwd: " + userPwd);
//                            break;
//                    }
//                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_register_Fragment);
//                AlertDialog.Builder d = new AlertDialog.Builder(activity);
//                d.setTitle("警告！")
//                        .setMessage("已註冊")
//                        .setCancelable(false);
//                d.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
//                d.show();
            }
        });

//        Integer userId = 3;
//


//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        });
//    }
//
//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        if (getContext() != null && getContext().getApplicationContext() != null) {
//            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        if (getContext() != null && getContext().getApplicationContext() != null) {
//            Toast.makeText(
//                    getContext().getApplicationContext(),
//                    errorString,
//                    Toast.LENGTH_LONG).show();
//        }
//    }

    }
}