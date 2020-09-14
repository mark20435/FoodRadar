package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ResInsertFragment extends Fragment {
    private NavController navController;
    private final static String TAG = "TAG_ResInsertFragment";
    private FragmentActivity activity;
    private ImageView ivRes;
    private EditText etResName, etResAddress, etResTel;
    //星期一
    private Spinner spMonStartTime, spMonEndTime, spMonStartTime2, spMonEndTime2, spMonStartTime3, spMonEndTime3;
    private TextView tvTo, tvTo2, tvTo3;
    private Button btMonAddHours, btMonDeleteHours2, btMonAddHours2, btMonDeleteHours3;
    //星期二
    private Spinner spTueStartTime, spTueEndTime, spTueStartTime2, spTueEndTime2, spTueStartTime3, spTueEndTime3;
    private TextView tvTo4, tvTo5, tvTo6;
    private Button btTueAddHours, btTueDeleteHours2, btTueAddHours2, btTueDeleteHours3;
    //星期三
    private Spinner spWedStartTime, spWedEndTime, spWedStartTime2, spWedEndTime2, spWedStartTime3, spWedEndTime3;
    private TextView tvTo7, tvTo8, tvTo9;
    private Button btWedAddHours, btWedDeleteHours2, btWedAddHours2, btWedDeleteHours3;
    //星期四
    private Spinner spThuStartTime, spThuEndTime, spThuStartTime2, spThuEndTime2, spThuStartTime3, spThuEndTime3;
    private TextView tvTo10, tvTo11, tvTo12;
    private Button btThuAddHours, btThuDeleteHours2, btThuAddHours2, btThuDeleteHours3;
    //星期五
    private Spinner spFriStartTime, spFriEndTime, spFriStartTime2, spFriEndTime2, spFriStartTime3, spFriEndTime3;
    private TextView tvTo13, tvTo14, tvTo15;
    private Button btFriAddHours, btFriDeleteHours2, btFriAddHours2, btFriDeleteHours3;
    //星期六
    private Spinner spSatStartTime, spSatEndTime, spSatStartTime2, spSatEndTime2, spSatStartTime3, spSatEndTime3;
    private TextView tvTo16, tvTo17, tvTo18;
    private Button btSatAddHours, btSatDeleteHours2, btSatAddHours2, btSatDeleteHours3;
    //星期日
    private Spinner spSunStartTime, spSunEndTime, spSunStartTime2, spSunEndTime2, spSunStartTime3, spSunEndTime3;
    private TextView tvTo19, tvTo20, tvTo21;
    private Button btSunAddHours, btSunDeleteHours2, btSunAddHours2, btSunDeleteHours3;

    private Map<String, Boolean> hoursVisibility;
    private Spinner spCategory;
    private Switch swResEnable;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common().setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
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
        super.onCreateView(inflater, container, savedInstanceState);
        activity.setTitle(R.string.titleResInsert);
        return inflater.inflate(R.layout.fragment_res_insert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivRes = view.findViewById(R.id.ivRes);
        etResName = view.findViewById(R.id.etResName);
        etResAddress = view.findViewById(R.id.etResAddress);
        etResTel = view.findViewById(R.id.etResTel);

        hoursVisibility = getHoursVisibility();

        spMonStartTime = view.findViewById(R.id.spMonStartTime);
        spMonEndTime = view.findViewById(R.id.spMonEndTime);
        spMonStartTime2 = view.findViewById(R.id.spMonStartTime2);
        spMonEndTime2 = view.findViewById(R.id.spMonEndTime2);
        spMonStartTime3 = view.findViewById(R.id.spMonStartTime3);
        spMonEndTime3 = view.findViewById(R.id.spMonEndTime3);

        tvTo = view.findViewById(R.id.tvTo);
        tvTo2 = view.findViewById(R.id.tvTo2);
        tvTo3 = view.findViewById(R.id.tvTo3);

        btMonAddHours = view.findViewById(R.id.btMonAddHours);
        btMonDeleteHours2 = view.findViewById(R.id.btMonDeleteHours2);
        btMonAddHours2 = view.findViewById(R.id.btMonAddHours2);
        btMonDeleteHours3 = view.findViewById(R.id.btMonDeleteHours3);

        spMonStartTime.setSelection(0, true);
        spMonEndTime.setVisibility(View.GONE);
        tvTo.setVisibility(View.GONE);
        btMonAddHours.setVisibility(View.GONE);

        spMonStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    spMonEndTime.setVisibility(View.VISIBLE);
                    tvTo.setVisibility(View.VISIBLE);
                    btMonAddHours.setVisibility(View.VISIBLE);
                }else {
                    spMonEndTime.setVisibility(View.GONE);
                    tvTo.setVisibility(View.GONE);
                    btMonAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btMonAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("MonHours2") == false){
                hoursVisibility.put("MonHours2", true);
                btMonDeleteHours2.setVisibility(View.VISIBLE);
                spMonStartTime2.setVisibility(View.VISIBLE);
                tvTo2.setVisibility(View.VISIBLE);
                spMonEndTime2.setVisibility(View.VISIBLE);
                btMonAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btMonDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("MonHours2") == true){
                hoursVisibility.put("MonHours2", false);
                btMonDeleteHours2.setVisibility(View.GONE);
                spMonStartTime2.setVisibility(View.GONE);
                tvTo2.setVisibility(View.GONE);
                spMonEndTime2.setVisibility(View.GONE);
                btMonAddHours2.setVisibility(View.GONE);
            }
        });

        btMonAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("MonHours3") == false){
                hoursVisibility.put("MonHours3", true);
                btMonDeleteHours3.setVisibility(View.VISIBLE);
                spMonStartTime3.setVisibility(View.VISIBLE);
                tvTo3.setVisibility(View.VISIBLE);
                spMonEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btMonDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("MonHours3") == true){
                hoursVisibility.put("MonHours3", false);
                btMonDeleteHours3.setVisibility(View.GONE);
                spMonStartTime3.setVisibility(View.GONE);
                tvTo3.setVisibility(View.GONE);
                spMonEndTime3.setVisibility(View.GONE);
            }
        });

        //星期二
        spTueStartTime = view.findViewById(R.id.spTueStartTime);
        spTueEndTime = view.findViewById(R.id.spTueEndTime);
        spTueStartTime2 = view.findViewById(R.id.spTueStartTime2);
        spTueEndTime2 = view.findViewById(R.id.spTueEndTime2);
        spTueStartTime3 = view.findViewById(R.id.spTueStartTime3);
        spTueEndTime3 = view.findViewById(R.id.spTueEndTime3);

        tvTo4 = view.findViewById(R.id.tvTo4);
        tvTo5 = view.findViewById(R.id.tvTo5);
        tvTo6 = view.findViewById(R.id.tvTo6);

        btTueAddHours = view.findViewById(R.id.btTueAddHours);
        btTueDeleteHours2 = view.findViewById(R.id.btTueDeleteHours2);
        btTueAddHours2 = view.findViewById(R.id.btTueAddHours2);
        btTueDeleteHours3 = view.findViewById(R.id.btTueDeleteHours3);

        spTueStartTime.setSelection(0, true);
        spTueEndTime.setVisibility(View.GONE);
        tvTo4.setVisibility(View.GONE);
        btTueAddHours.setVisibility(View.GONE);

        spTueStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spTueEndTime.setVisibility(View.VISIBLE);
                    tvTo4.setVisibility(View.VISIBLE);
                    btTueAddHours.setVisibility(View.VISIBLE);
                }else {
                    spTueEndTime.setVisibility(View.GONE);
                    tvTo4.setVisibility(View.GONE);
                    btTueAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btTueAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("TueHours2") == false){
                hoursVisibility.put("TueHours2", true);
                btTueDeleteHours2.setVisibility(View.VISIBLE);
                spTueStartTime2.setVisibility(View.VISIBLE);
                tvTo5.setVisibility(View.VISIBLE);
                spTueEndTime2.setVisibility(View.VISIBLE);
                btTueAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btTueDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("TueHours2") == true){
                hoursVisibility.put("TueHours2", false);
                btTueDeleteHours2.setVisibility(View.GONE);
                spTueStartTime2.setVisibility(View.GONE);
                tvTo5.setVisibility(View.GONE);
                spTueEndTime2.setVisibility(View.GONE);
                btTueAddHours2.setVisibility(View.GONE);
            }
        });

        btTueAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("TueHours3") == false){
                hoursVisibility.put("TueHours3", true);
                btTueDeleteHours3.setVisibility(View.VISIBLE);
                spTueStartTime3.setVisibility(View.VISIBLE);
                tvTo6.setVisibility(View.VISIBLE);
                spTueEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btTueDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("TueHours3") == true){
                hoursVisibility.put("TueHours3", false);
                btTueDeleteHours3.setVisibility(View.GONE);
                spTueStartTime3.setVisibility(View.GONE);
                tvTo6.setVisibility(View.GONE);
                spTueEndTime3.setVisibility(View.GONE);
            }
        });

        //星期三
        spWedStartTime = view.findViewById(R.id.spWedStartTime);
        spWedEndTime = view.findViewById(R.id.spWedEndTime);
        spWedStartTime2 = view.findViewById(R.id.spWedStartTime2);
        spWedEndTime2 = view.findViewById(R.id.spWedEndTime2);
        spWedStartTime3 = view.findViewById(R.id.spWedStartTime3);
        spWedEndTime3 = view.findViewById(R.id.spWedEndTime3);

        tvTo7 = view.findViewById(R.id.tvTo7);
        tvTo8 = view.findViewById(R.id.tvTo8);
        tvTo9 = view.findViewById(R.id.tvTo9);

        btWedAddHours = view.findViewById(R.id.btWedAddHours);
        btWedDeleteHours2 = view.findViewById(R.id.btWedDeleteHours2);
        btWedAddHours2 = view.findViewById(R.id.btWedAddHours2);
        btWedDeleteHours3 = view.findViewById(R.id.btWedDeleteHours3);

        spWedStartTime.setSelection(0, true);
        spWedEndTime.setVisibility(View.GONE);
        tvTo7.setVisibility(View.GONE);
        btWedAddHours.setVisibility(View.GONE);

        spWedStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spWedEndTime.setVisibility(View.VISIBLE);
                    tvTo7.setVisibility(View.VISIBLE);
                    btWedAddHours.setVisibility(View.VISIBLE);
                }else {
                    spWedEndTime.setVisibility(View.GONE);
                    tvTo7.setVisibility(View.GONE);
                    btWedAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btWedAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("WedHours2") == false){
                hoursVisibility.put("WedHours2", true);
                btWedDeleteHours2.setVisibility(View.VISIBLE);
                spWedStartTime2.setVisibility(View.VISIBLE);
                tvTo8.setVisibility(View.VISIBLE);
                spWedEndTime2.setVisibility(View.VISIBLE);
                btWedAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btWedDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("WedHours2") == true){
                hoursVisibility.put("WedHours2", false);
                btWedDeleteHours2.setVisibility(View.GONE);
                spWedStartTime2.setVisibility(View.GONE);
                tvTo8.setVisibility(View.GONE);
                spWedEndTime2.setVisibility(View.GONE);
                btWedAddHours2.setVisibility(View.GONE);
            }
        });

        btWedAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("WedHours3") == false){
                hoursVisibility.put("WedHours3", true);
                btWedDeleteHours3.setVisibility(View.VISIBLE);
                spWedStartTime3.setVisibility(View.VISIBLE);
                tvTo9.setVisibility(View.VISIBLE);
                spWedEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btWedDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("WedHours3") == true){
                hoursVisibility.put("WedHours3", false);
                btWedDeleteHours3.setVisibility(View.GONE);
                spWedStartTime3.setVisibility(View.GONE);
                tvTo9.setVisibility(View.GONE);
                spWedEndTime3.setVisibility(View.GONE);
            }
        });

        //星期四
        spThuStartTime = view.findViewById(R.id.spThuStartTime);
        spThuEndTime = view.findViewById(R.id.spThuEndTime);
        spThuStartTime2 = view.findViewById(R.id.spThuStartTime2);
        spThuEndTime2 = view.findViewById(R.id.spThuEndTime2);
        spThuStartTime3 = view.findViewById(R.id.spThuStartTime3);
        spThuEndTime3 = view.findViewById(R.id.spThuEndTime3);

        tvTo10 = view.findViewById(R.id.tvTo10);
        tvTo11 = view.findViewById(R.id.tvTo11);
        tvTo12 = view.findViewById(R.id.tvTo12);

        btThuAddHours = view.findViewById(R.id.btThuAddHours);
        btThuDeleteHours2 = view.findViewById(R.id.btThuDeleteHours2);
        btThuAddHours2 = view.findViewById(R.id.btThuAddHours2);
        btThuDeleteHours3 = view.findViewById(R.id.btThuDeleteHours3);

        spThuStartTime.setSelection(0, true);
        spThuEndTime.setVisibility(View.GONE);
        tvTo10.setVisibility(View.GONE);
        btThuAddHours.setVisibility(View.GONE);

        spThuStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spThuEndTime.setVisibility(View.VISIBLE);
                    tvTo10.setVisibility(View.VISIBLE);
                    btThuAddHours.setVisibility(View.VISIBLE);
                }else {
                    spThuEndTime.setVisibility(View.GONE);
                    tvTo10.setVisibility(View.GONE);
                    btThuAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btThuAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("ThuHours2") == false){
                hoursVisibility.put("ThuHours2", true);
                btThuDeleteHours2.setVisibility(View.VISIBLE);
                spThuStartTime2.setVisibility(View.VISIBLE);
                tvTo11.setVisibility(View.VISIBLE);
                spThuEndTime2.setVisibility(View.VISIBLE);
                btThuAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btThuDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("ThuHours2") == true){
                hoursVisibility.put("ThuHours2", false);
                btThuDeleteHours2.setVisibility(View.GONE);
                spThuStartTime2.setVisibility(View.GONE);
                tvTo11.setVisibility(View.GONE);
                spThuEndTime2.setVisibility(View.GONE);
                btThuAddHours2.setVisibility(View.GONE);
            }
        });

        btThuAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("ThuHours3") == false){
                hoursVisibility.put("ThuHours3", true);
                btThuDeleteHours3.setVisibility(View.VISIBLE);
                spThuStartTime3.setVisibility(View.VISIBLE);
                tvTo12.setVisibility(View.VISIBLE);
                spThuEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btThuDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("ThuHours3") == true){
                hoursVisibility.put("ThuHours3", false);
                btThuDeleteHours3.setVisibility(View.GONE);
                spThuStartTime3.setVisibility(View.GONE);
                tvTo12.setVisibility(View.GONE);
                spThuEndTime3.setVisibility(View.GONE);
            }
        });

        //星期五
        spFriStartTime = view.findViewById(R.id.spFriStartTime);
        spFriEndTime = view.findViewById(R.id.spFriEndTime);
        spFriStartTime2 = view.findViewById(R.id.spFriStartTime2);
        spFriEndTime2 = view.findViewById(R.id.spFriEndTime2);
        spFriStartTime3 = view.findViewById(R.id.spFriStartTime3);
        spFriEndTime3 = view.findViewById(R.id.spFriEndTime3);

        tvTo13 = view.findViewById(R.id.tvTo13);
        tvTo14 = view.findViewById(R.id.tvTo14);
        tvTo15 = view.findViewById(R.id.tvTo15);

        btFriAddHours = view.findViewById(R.id.btFriAddHours);
        btFriDeleteHours2 = view.findViewById(R.id.btFriDeleteHours2);
        btFriAddHours2 = view.findViewById(R.id.btFriAddHours2);
        btFriDeleteHours3 = view.findViewById(R.id.btFriDeleteHours3);

        spFriStartTime.setSelection(0, true);
        spFriEndTime.setVisibility(View.GONE);
        tvTo13.setVisibility(View.GONE);
        btFriAddHours.setVisibility(View.GONE);

        spFriStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spFriEndTime.setVisibility(View.VISIBLE);
                    tvTo13.setVisibility(View.VISIBLE);
                    btFriAddHours.setVisibility(View.VISIBLE);
                }else {
                    spFriEndTime.setVisibility(View.GONE);
                    tvTo13.setVisibility(View.GONE);
                    btFriAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btFriAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("FriHours2") == false){
                hoursVisibility.put("FriHours2", true);
                btFriDeleteHours2.setVisibility(View.VISIBLE);
                spFriStartTime2.setVisibility(View.VISIBLE);
                tvTo14.setVisibility(View.VISIBLE);
                spFriEndTime2.setVisibility(View.VISIBLE);
                btFriAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btFriDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("FriHours2") == true){
                hoursVisibility.put("FriHours2", false);
                btFriDeleteHours2.setVisibility(View.GONE);
                spFriStartTime2.setVisibility(View.GONE);
                tvTo14.setVisibility(View.GONE);
                spFriEndTime2.setVisibility(View.GONE);
                btFriAddHours2.setVisibility(View.GONE);
            }
        });

        btFriAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("FriHours3") == false){
                hoursVisibility.put("FriHours3", true);
                btFriDeleteHours3.setVisibility(View.VISIBLE);
                spFriStartTime3.setVisibility(View.VISIBLE);
                tvTo15.setVisibility(View.VISIBLE);
                spFriEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btFriDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("FriHours3") == true){
                hoursVisibility.put("FriHours3", false);
                btFriDeleteHours3.setVisibility(View.GONE);
                spFriStartTime3.setVisibility(View.GONE);
                tvTo15.setVisibility(View.GONE);
                spFriEndTime3.setVisibility(View.GONE);
            }
        });

        //星期六
        spSatStartTime = view.findViewById(R.id.spSatStartTime);
        spSatEndTime = view.findViewById(R.id.spSatEndTime);
        spSatStartTime2 = view.findViewById(R.id.spSatStartTime2);
        spSatEndTime2 = view.findViewById(R.id.spSatEndTime2);
        spSatStartTime3 = view.findViewById(R.id.spSatStartTime3);
        spSatEndTime3 = view.findViewById(R.id.spSatEndTime3);

        tvTo16 = view.findViewById(R.id.tvTo16);
        tvTo17 = view.findViewById(R.id.tvTo17);
        tvTo18 = view.findViewById(R.id.tvTo18);

        btSatAddHours = view.findViewById(R.id.btSatAddHours);
        btSatDeleteHours2 = view.findViewById(R.id.btSatDeleteHours2);
        btSatAddHours2 = view.findViewById(R.id.btSatAddHours2);
        btSatDeleteHours3 = view.findViewById(R.id.btSatDeleteHours3);

        spSatStartTime.setSelection(0, true);
        spSatEndTime.setVisibility(View.GONE);
        tvTo16.setVisibility(View.GONE);
        btSatAddHours.setVisibility(View.GONE);

        spSatStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spSatEndTime.setVisibility(View.VISIBLE);
                    tvTo16.setVisibility(View.VISIBLE);
                    btSatAddHours.setVisibility(View.VISIBLE);
                }else {
                    spSatEndTime.setVisibility(View.GONE);
                    tvTo16.setVisibility(View.GONE);
                    btSatAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btSatAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("SatHours2") == false){
                hoursVisibility.put("SatHours2", true);
                btSatDeleteHours2.setVisibility(View.VISIBLE);
                spSatStartTime2.setVisibility(View.VISIBLE);
                tvTo17.setVisibility(View.VISIBLE);
                spSatEndTime2.setVisibility(View.VISIBLE);
                btSatAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btSatDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("SatHours2") == true){
                hoursVisibility.put("SatHours2", false);
                btSatDeleteHours2.setVisibility(View.GONE);
                spSatStartTime2.setVisibility(View.GONE);
                tvTo17.setVisibility(View.GONE);
                spSatEndTime2.setVisibility(View.GONE);
                btSatAddHours2.setVisibility(View.GONE);
            }
        });

        btSatAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("SatHours3") == false){
                hoursVisibility.put("SatHours3", true);
                btSatDeleteHours3.setVisibility(View.VISIBLE);
                spSatStartTime3.setVisibility(View.VISIBLE);
                tvTo18.setVisibility(View.VISIBLE);
                spSatEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btSatDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("SatHours3") == true){
                hoursVisibility.put("SatHours3", false);
                btSatDeleteHours3.setVisibility(View.GONE);
                spSatStartTime3.setVisibility(View.GONE);
                tvTo18.setVisibility(View.GONE);
                spSatEndTime3.setVisibility(View.GONE);
            }
        });

        //星期日
        spSunStartTime = view.findViewById(R.id.spSunStartTime);
        spSunEndTime = view.findViewById(R.id.spSunEndTime);
        spSunStartTime2 = view.findViewById(R.id.spSunStartTime2);
        spSunEndTime2 = view.findViewById(R.id.spSunEndTime2);
        spSunStartTime3 = view.findViewById(R.id.spSunStartTime3);
        spSunEndTime3 = view.findViewById(R.id.spSunEndTime3);

        tvTo19 = view.findViewById(R.id.tvTo19);
        tvTo20 = view.findViewById(R.id.tvTo20);
        tvTo21 = view.findViewById(R.id.tvTo21);

        btSunAddHours = view.findViewById(R.id.btSunAddHours);
        btSunDeleteHours2 = view.findViewById(R.id.btSunDeleteHours2);
        btSunAddHours2 = view.findViewById(R.id.btSunAddHours2);
        btSunDeleteHours3 = view.findViewById(R.id.btSunDeleteHours3);

        spSunStartTime.setSelection(0, true);
        spSunEndTime.setVisibility(View.GONE);
        tvTo19.setVisibility(View.GONE);
        btSunAddHours.setVisibility(View.GONE);

        spSunStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0 && position != 1){
                    spSunEndTime.setVisibility(View.VISIBLE);
                    tvTo19.setVisibility(View.VISIBLE);
                    btSunAddHours.setVisibility(View.VISIBLE);
                }else {
                    spSunEndTime.setVisibility(View.GONE);
                    tvTo19.setVisibility(View.GONE);
                    btSunAddHours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //點擊加時段出現第二列
        btSunAddHours.setOnClickListener(v -> {
            if(hoursVisibility.get("SunHours2") == false){
                hoursVisibility.put("SunHours2", true);
                btSunDeleteHours2.setVisibility(View.VISIBLE);
                spSunStartTime2.setVisibility(View.VISIBLE);
                tvTo20.setVisibility(View.VISIBLE);
                spSunEndTime2.setVisibility(View.VISIBLE);
                btSunAddHours2.setVisibility(View.VISIBLE);
            }
        });

        //點擊刪時段，隱藏該列
        btSunDeleteHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("SunHours2") == true){
                hoursVisibility.put("SunHours2", false);
                btSunDeleteHours2.setVisibility(View.GONE);
                spSunStartTime2.setVisibility(View.GONE);
                tvTo20.setVisibility(View.GONE);
                spSunEndTime2.setVisibility(View.GONE);
                btSunAddHours2.setVisibility(View.GONE);
            }
        });

        btSunAddHours2.setOnClickListener(v -> {
            if(hoursVisibility.get("SunHours3") == false){
                hoursVisibility.put("SunHours3", true);
                btSunDeleteHours3.setVisibility(View.VISIBLE);
                spSunStartTime3.setVisibility(View.VISIBLE);
                tvTo21.setVisibility(View.VISIBLE);
                spSunEndTime3.setVisibility(View.VISIBLE);
            }
        });

        btSunDeleteHours3.setOnClickListener(v -> {
            if(hoursVisibility.get("SunHours3") == true){
                hoursVisibility.put("SunHours3", false);
                btSunDeleteHours3.setVisibility(View.GONE);
                spSunStartTime3.setVisibility(View.GONE);
                tvTo21.setVisibility(View.GONE);
                spSunEndTime3.setVisibility(View.GONE);
            }
        });



        spCategory = view.findViewById(R.id.spCategory);
        swResEnable = view.findViewById(R.id.swResEnable);

        Button btTakePicture = view.findViewById(R.id.btTakePicture);
        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(activity, R.string.textNoCameraApp);
                }
            }
        });

        Button btPickPicture = view.findViewById(R.id.btPickPicture);
        btPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_PICTURE);
            }
        });


    }

    private Map<String, Boolean> getHoursVisibility() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("MonHours2", false);
        map.put("MonHours3", false);
        map.put("TueHours2", false);
        map.put("TueHours3", false);
        map.put("WedHours2", false);
        map.put("WedHours3", false);
        map.put("ThuHours2", false);
        map.put("ThuHours3", false);
        map.put("FriHours2", false);
        map.put("FriHours3", false);
        map.put("SatHours2", false);
        map.put("SatHours3", false);
        map.put("SunHours2", false);
        map.put("SunHours3", false);

        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;
                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .start(activity, this, REQ_CROP_PICTURE);
    }

    private void handleCropResult(Intent intent) {
        Uri resultUri = UCrop.getOutput(intent);
        if (resultUri == null) {
            return;
        }
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                bitmap = BitmapFactory.decodeStream(
                        activity.getContentResolver().openInputStream(resultUri));
            } else {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivRes.setImageBitmap(bitmap);
        } else {
            ivRes.setImageResource(R.drawable.no_image);
        }
    }
}