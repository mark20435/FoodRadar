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
    private NavController navController; //設定MainActivity導覽圖的Controller
    private Button btSysSetup;
    private Button btMyRes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        setHasOptionsMenu(true);
        // 顯示返回的箭頭
//        new Common().setBackArrow(true, activity);
        // 設定MainActivity導覽圖的Controller，導覽圖的id是“fragment”
        navController = Navigation.findNavController(activity, R.id.fragment);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // 臨時加的，會員專區沒有用到OptionMenu，先加上去當程式參考用
        inflater.inflate(R.menu.appbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // “appbarmenu.xml”裡，MenuItem的id叫“Finish”
            case R.id.Finish:
                // 臨時加的，會員專區=>系統設定，應該用畫面上的選項，這裡是程式參考用
//                navController.navigate(R.id.action_userAreaFragment_to_userSysSetupFragment);
                break;
            // 這是 android 預設的返回鍵id
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
        // 設定AppBar抬頭(Title)的文字
//        activity.setTitle(R.string.title_of_user);
        return inflater.inflate(R.layout.fragment_user_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btMyRes).setOnClickListener(this);
        view.findViewById(R.id.btSysSetup).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                case R.id.btMyRes:
//                    Navigation.findNavController(v).navigate(R.id.action_userAreaFragment_to_userMyResFragment);
                    break;
                case R.id.btSysSetup:
//                Navigation.findNavController(v).navigate(R.id.action_userAreaFragment_to_userSysSetupFragment);
                    break;
                default:
                break;
        }


    }
}