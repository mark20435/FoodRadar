package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class UserDataSetupFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private NavController navController;
    private final static String TAG = "TAG_UserDataSetupFragment";
    private Integer userId = Common.USER_ID;

    private ImageView ivAvatar;
    private byte[] image;
    private ImageButton btImgCamera;

    private String USERACCOUNT_SERVLET = Common.URL_SERVER + "UserAccountServlet";
    List<UserAccount> getUserAccountList = null;

    private File file;
    private Uri contentUri;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;

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
//        userId= new Common().getUserLoin(activity);
        if(userId > 0){
            Common.showToast(activity, "會員資料設定\n登入成功\nUserId: " + userId);
        } else {
            Common.showToast(activity, "會員資料設定\n登入失敗\nUserId: " + userId);
        }
        // ^^^^^^臨時寫的，用來模擬使用者登入

        // 拍照功能
        view.findViewById(R.id.btImgCamera).setOnClickListener(this);

        // 登入/登出功能
        view.findViewById(R.id.btnLogInOut).setOnClickListener(this);

        // 變更密碼功能
        view.findViewById(R.id.btUserChangConfrim).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            // 登入/登出/註冊功能
            case R.id.btnLogInOut:
                return;

            // 資料修改功能
            case R.id.btUserChangConfrim:

                if (Common.networkConnected(activity)) {

                    Common.showToast(activity,"NetworkConnected: fail");
                } else {
//            byte[] imgBytes = new byte[1];
//            MyRes myRes = new MyRes(1,"resName","resHours","resTel","resAddress", imgBytes);
//            getMyResList = new ArrayList<>();
//            getMyResList.add(0,myRes);
//            getMyResList.add(1,new MyRes(2,"resName2","resHours","resTel","resAddress", imgBytes));
//            for (int i = 2 ; i <= 13 ; i++){
//                getMyResList.add(i,new MyRes(i,"resName" + String.valueOf(i),"resHours","resTel","resAddress", imgBytes));
//            }

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "findById");
                    jsonObject.addProperty("id", userId);
                    String jsonOut = jsonObject.toString();

                    CommonTask getAllTask;
                    getAllTask = new CommonTask(USERACCOUNT_SERVLET, jsonOut);
                    try {
                        String jsonIn = getAllTask.execute().get();
                        Type listType = new TypeToken<List<UserAccount>>() {
                        }.getType();
                        getUserAccountList = new Gson().fromJson(jsonIn, listType);
//                        Log.d(TAG,"getMyResList: " + getUserAccountList);
                    } catch (Exception e) {
//                Log.e(TAG, e.toString());
                        e.printStackTrace();
                    }

                }



                return;

            // 拍照功能
            case R.id.btImgCamera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (dir != null && !dir.exists()) {
                    if (!dir.mkdirs()) {
                        return;
                    }
                }
                file = new File(dir, "avatar_" + userId + ".jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(activity, R.string.textNoCameraApp);
                }
                return;

            default:
                return;

        }


    }
}