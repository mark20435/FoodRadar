package com.example.foodradar_android.user;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class UserManagementFragment extends Fragment {
    private Activity activity;
    private NavController navController;

    private List<UserAccount> userAccountList = new ArrayList<>();
    private List<UserImageTask> imageTasks= new ArrayList<>();
    private RecyclerView rcvUserManagement;
    private String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";
    private String USERACCOUNT_SERVLET = URL_SERVER + "UserAccountServlet";
    private Integer imageSize = 400;
    final private String TAG = "UserManagementFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common();
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.title_of_res_user_management);
        return inflater.inflate(R.layout.fragment_user_management, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvUserManagement = view.findViewById(R.id.id_rcvUserManagement);
        rcvUserManagement.setLayoutManager(new LinearLayoutManager(activity));

        SearchView searchViewUserManagement = view.findViewById(R.id.searchViewUserManagement);
        searchViewUserManagement.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    showUserAccount(userAccountList);
                } else {
                    List<UserAccount> searchUserAccountList = new ArrayList<>();
                    for(UserAccount userAccount : userAccountList){
                        if(userAccount.getUserPhone().toUpperCase().contains(newText.toUpperCase())){
                            searchUserAccountList.add(userAccount);
                        }
                    }
                    showUserAccount(searchUserAccountList);
                }
                return true;
            }
        });

        userAccountList = getUserAccount();
//        Log.d("TAG","myResList: " + myResList);
        showUserAccount(userAccountList);

    }

    private int getUserId(){ return Common.USER_ID; }

    private class UserAccountAdapter extends RecyclerView.Adapter<UserAccountAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<UserAccount> userAccountListAdp;
        /* 螢幕寬度除以4當作將圖的尺寸 */
        private final int imgSize = getResources().getDisplayMetrics().widthPixels / 4;

        UserAccountAdapter(Context context, List<UserAccount> userAccountListAdp) {
            this.layoutInflater = LayoutInflater.from(context);
            this.userAccountListAdp = userAccountListAdp;
        }

        public void setUserAccountListAdp(List<UserAccount> userAccountListAdp) {
            this.userAccountListAdp = userAccountListAdp;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imUserManageAvatar;
            TextView tvUserManagePhone,tvUserManageRegisterTime,tvUserManageEditTime,tvUserManageAccountStatus;
            Switch swUserManageStatus;
            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imUserManageAvatar = itemView.findViewById(R.id.imUserManageAvatar);
                tvUserManagePhone = itemView.findViewById(R.id.tvUserManagePhone);
                tvUserManageRegisterTime = itemView.findViewById(R.id.tvUserManageRegisterTime);
                tvUserManageEditTime = itemView.findViewById(R.id.tvUserManageEditTime);
                tvUserManageAccountStatus = itemView.findViewById(R.id.tvUserManageAccountStatus);
                swUserManageStatus = itemView.findViewById(R.id.swUserManageStatus);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater
                    .inflate(R.layout.item_view_user_management,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserAccountAdapter.MyViewHolder holder, int position) {
            UserAccount uA = userAccountListAdp.get(position);
            Resources res = getResources();
            holder.tvUserManagePhone.setText(getResources().getString(R.string.textUserPhone) + ": " + uA.getUserPhone());
            holder.tvUserManageRegisterTime.setText(getResources().getString(R.string.textRegisterDate) + ": " + uA.getCreateDate().toString());
            holder.tvUserManageEditTime.setText(getResources().getString(R.string.textModifyDate) + ": " + uA.getModifyDate().toString());

            String strAccountStatus = getResources().getString(R.string.textAccountStatus) + ": "
                    + (uA.getEnable() ? getResources().getString(R.string.textOfEnable) : getResources().getString(R.string.textOfDisable) );
            holder.tvUserManageAccountStatus.setText(strAccountStatus);
            holder.swUserManageStatus.setChecked(uA.getEnable());
            if (uA.getEnable()) {
                holder.tvUserManageAccountStatus.setTextColor(Color.BLUE);
            } else {
                holder.tvUserManageAccountStatus.setTextColor(Color.RED);
            }

            Integer userId = uA.getUserId(); // UserID
//            final MyRes bookOnBVH = books.get(position);
            Log.d(TAG,"userId: " + userId);
            Log.d(TAG,"USERACCOUNT_SERVLET: " + USERACCOUNT_SERVLET);
            UserImageTask userImageTask = new UserImageTask(USERACCOUNT_SERVLET, userId, imageSize, holder.imUserManageAvatar);
            userImageTask.execute(); // .execute() => UserImage.doInBackground
            imageTasks.add(userImageTask);

            holder.swUserManageStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.tvUserManageAccountStatus.setTextColor(Color.BLUE);
                    } else {
                        holder.tvUserManageAccountStatus.setTextColor(Color.RED);
                    }
                    String strAccountStatus = getResources().getString(R.string.textAccountStatus) + ": "
                        + (isChecked ? getResources().getString(R.string.textOfEnable) : getResources().getString(R.string.textOfDisable) );
                    holder.tvUserManageAccountStatus.setText(strAccountStatus);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (userAccountListAdp == null) {
                return 0;
            } else {
                return userAccountListAdp.size();
            }
        }

    }

    private List<UserAccount> getUserAccount(){
        List<UserAccount> getUserAccountList = null;

        if (Common.networkConnected(activity)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
//            jsonObject.addProperty("id", getUserId());
            String jsonOut = jsonObject.toString();
//            Log.d(TAG,"getBooks.Common.URL_SERVLET: " + Common.URL_SERVLET);
            CommonTask getAllTask;
            getAllTask = new CommonTask(USERACCOUNT_SERVLET, jsonOut);
            try {
                String jsonIn = getAllTask.execute().get();
                Type listType = new TypeToken<List<UserAccount>>() {
                }.getType();
                getUserAccountList = new Gson().fromJson(jsonIn, listType);
//                Log.d(TAG,"getUserAccountList: " + getUserAccountList);
            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

        }else{
            Common.showToast(activity,"NetworkConnected: fail");
        }

        return getUserAccountList;
    }

    private void showUserAccount(List<UserAccount> showUAList){
        UserManagementFragment.UserAccountAdapter uAAdapter
                = (UserManagementFragment.UserAccountAdapter) rcvUserManagement.getAdapter();
        Log.d(TAG,"userAccountAdapter: " + uAAdapter);
        if (uAAdapter == null) {
            Log.d(TAG,"uAAdapter = null.showUserAccountList: " + showUAList);
            rcvUserManagement.setAdapter(new UserAccountAdapter(activity, showUAList));
        } else {
            Log.d(TAG,"uAAdapter.showUserAccountList: " + showUAList);
            uAAdapter.setUserAccountListAdp (showUAList);
            uAAdapter.notifyDataSetChanged();
        }

    }



}