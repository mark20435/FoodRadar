package com.example.foodradar_android.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.user.UserAccount;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.combineMeasuredStates;


public class LoginFragment extends Fragment {
    private Activity activity;
    private NavController navController;
    private final String TAG = "TAG_LoginFragment";
    private final String USERACCOUNT_SERVLET = Common.URL_SERVER + "UserAccountServlet";
    private String userphone;
    private String userPwd;
    List<UserAccount> getUserAccountList = null;
    private Uri contentUri;
    private CommonTask LoginGetAllTask;
    private int loginResultCode;
    private String loginMember;
    private int UserId;

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

        view.findViewById(R.id.userPhone);
        view.findViewById(R.id.userpwd);

        Button login = view.findViewById(R.id.login);
        Button cancel = view.findViewById(R.id.regcancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.popBackStack();
               // Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_userDataSetupFragment);
//                navController.navigate(R.id.action_loginFragment_to_userDataSetupFragment);
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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserAccount userAccount = new Common().getUserLoin(activity);
                Common.USER_ID = userAccount.getUserId();

                if(UserId >0){
                    navController.navigate(R.id.action_loginFragment_to_mainFragment);
                    Common.showToast(activity, "input");
                }if(userphone.length() <= 0 && userphone.length() >= 10 || userPwd.length() >= 10){
                    new Common().showToast(activity, "login error");
                    activity.finish();
                }if(UserId <= 0){
                    Common.showToast(activity, "查無此帳號");

                }else{
                    navController.popBackStack();
                }

               // Common.showToast(activity, "Login lose " + Common.USER_ID);
                //navController.popBackStack();



//                if (username.equals("0900123456") && userPwd.equals("P@ssw0rd") && UserId.equals("9")) {
//                    navController.navigate(R.id.action_loginFragment_to_mainFragment);
//                } else {
//                    new Common().showToast(activity, "登入失敗" + UserId);
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
//
            }
        });
    }
    private boolean isAccountValid(String userphone, String userPwd) {
        String url = Common.URL_SERVER + "UserAccountServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");
        jsonObject.addProperty("account", userphone);
        jsonObject.addProperty("password", userPwd);
        LoginGetAllTask = new CommonTask(url, jsonObject.toString());
        boolean isAccountValid = false;
        try {
            String jsonIn = LoginGetAllTask.execute().get();
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            loginResultCode = jsonObject.get("loginResultCode").getAsInt();
            if (loginResultCode == 1){
                loginMember = jsonObject.get("loginMember").getAsString();
                isAccountValid = true;
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return isAccountValid;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
