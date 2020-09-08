package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;

import java.util.ArrayList;
import java.util.List;

public class UserMyResFragment extends Fragment {
    private Activity activity;
    private NavController navController;
    private List<MyRes> myResList = new ArrayList<>();
    private RecyclerView rcvMyRes;

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
        activity.setTitle(R.string.title_of_myres);
        return inflater.inflate(R.layout.fragment_user_my_res, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvMyRes = view.findViewById(R.id.id_rcvMyRes);
        rcvMyRes.setLayoutManager(new LinearLayoutManager(activity));

        myResList = getMyRes();
//        Log.d("TAG","myResList: " + myResList);
        showMyRes(myResList);



    }

    private class MyResAdapter extends  RecyclerView.Adapter<MyResAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<MyRes> myResListAdpt;
        private int imgSize;

        MyResAdapter(Context context, List<MyRes> myResMRAList) {
            layoutInflater = LayoutInflater.from(context);
            this.myResListAdpt = myResMRAList;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imgSize = getResources().getDisplayMetrics().widthPixels / 4;
        }


        public void setMyResListAdpt(List<MyRes> myResListSetAdpt){
            this.myResListAdpt = myResListSetAdpt;
        }

        class MyViewHolder extends  RecyclerView.ViewHolder{
            ImageView imResImg;
            TextView tvResName, tvResHours, tvResTel, tvResAddress;
            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imResImg = itemView.findViewById(R.id.imResImg);
                tvResName = itemView.findViewById(R.id.tvResName);
                tvResHours = itemView.findViewById(R.id.tvResHours);
                tvResTel = itemView.findViewById(R.id.tvResTel);
                tvResAddress = itemView.findViewById(R.id.tvResAddress);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemViewMyRes = layoutInflater
                    .inflate(R.layout.item_view_user_myres,parent,false);
            return new MyViewHolder(itemViewMyRes);
        }

        @Override
        public void onBindViewHolder(@NonNull MyResAdapter.MyViewHolder holder, int position) {
            MyRes myResBidVH = myResListAdpt.get(position);
            holder.tvResName.setText(myResBidVH.getResName());
            holder.tvResHours.setText(getResources().getString(R.string.textResHours) + ": " + myResBidVH.getResHours());
            holder.tvResTel.setText(getResources().getString(R.string.textResPhone) + ": " + myResBidVH.getResTel());
            holder.tvResAddress.setText(getResources().getString(R.string.textResAddress) + ": " + myResBidVH.getResAddress());

            Integer resId = myResBidVH.getResId(); // 餐廳ID

        }

        @Override
        public int getItemCount()
        {
            if (myResListAdpt == null) {
                return 0;
            }else {
                return myResListAdpt.size();
            }
        }


    }

    private List<MyRes> getMyRes(){
        List<MyRes> getMyResList = null;

        if (Common.networkConnected(activity)) {
            byte[] imgBytes = new byte[1];
            MyRes myRes = new MyRes(1,"resName","resHours","resTel","resAddress", imgBytes);
            getMyResList = new ArrayList<>();
            getMyResList.add(0,myRes);
            getMyResList.add(1,new MyRes(2,"resName2","resHours","resTel","resAddress", imgBytes));
            for (int i = 2 ; i <= 13 ; i++){
                getMyResList.add(i,new MyRes(i,"resName" + String.valueOf(i),"resHours","resTel","resAddress", imgBytes));
            }




        }else{
            Common.showToast(activity,"NetworkConnected: fail");

        }

        return getMyResList;
    }

    private void showMyRes(List<MyRes> showMyResList){
        MyResAdapter myResAdapter = (MyResAdapter) rcvMyRes.getAdapter();
        Log.d("TAG","myResAdapter: " + myResAdapter);
        if (myResAdapter == null){
            Log.d("TAG","showMyResList: " + showMyResList);
            rcvMyRes.setAdapter(new MyResAdapter(activity, showMyResList));
        }else{
            Log.d("TAG","showMyResList: " + showMyResList);
            myResAdapter.setMyResListAdpt(showMyResList);
            myResAdapter.notifyDataSetChanged();

        }

    }

}