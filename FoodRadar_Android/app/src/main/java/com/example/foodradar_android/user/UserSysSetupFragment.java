package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.res.Resources;
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
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;

public class UserSysSetupFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private NavController navController;
    private Switch swUsArNotifi;

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
        activity.setTitle(R.string.title_of_sys_setup);
        return inflater.inflate(R.layout.fragment_user_sys_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btUsArResＭaintain).setOnClickListener(this);

        Resources res = getResources();
        swUsArNotifi = view.findViewById(R.id.swUsArNotifi);
        Boolean allowNotifi = Common.getUserAllowNotifi(activity);
        if (allowNotifi == true) {
            swUsArNotifi.setText(res.getString(R.string.textNotifi) + " ("+ res.getString(R.string.textOn) + ")");
        } else {
            swUsArNotifi.setText(res.getString(R.string.textNotifi) + " ("+ res.getString(R.string.textOff) + ")");
        }
        swUsArNotifi.setChecked(allowNotifi);
        swUsArNotifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (new Common().setUserAllowNotifi(activity,isChecked)) {

                    Common.showToast(activity, "設定成功");
                    if (isChecked == true) {
                        swUsArNotifi.setText(res.getString(R.string.textNotifi) + " ("+ res.getString(R.string.textOn) + ")");
                    } else {
                        new Common().setUserAllowNotifi(activity, false);
                        swUsArNotifi.setText(res.getString(R.string.textNotifi) + " (" + res.getString(R.string.textOff) + ")");
                    }

                } else {
                    swUsArNotifi.setChecked(!isChecked); //switch設定還原
                    Common.showToast(activity, "設定未完成");
                }
            }
        });

//        Button button;
//        button = view.findViewById(R.id.id_btResMaintain);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_userAreaFragment_to_userSysSetupFragment);
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 餐廳資訊維護
            case R.id.btUsArResＭaintain:
                navController.navigate(R.id.resMaintainFragment);
                break;
            default:
                break;
        }


        }
}