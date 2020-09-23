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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.res.Res;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ResMaintainFragment extends Fragment {
    private static final String TAG = "TAG_ResMaintainFragment";
    private RecyclerView rvRes;
    private Activity activity;
    private CommonTask resGetAllTask;
    private CommonTask resDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Res> ress;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        new Common().setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController = Navigation.findNavController(activity, R.id.mainFragment);

        imageTasks = new ArrayList<>();
    }

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
        super.onCreateView(inflater, container, savedInstanceState);
        activity.setTitle(R.string.resMaintain);
        return inflater.inflate(R.layout.fragment_res_maintain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.searchView);
        rvRes = view.findViewById(R.id.rvRes);

        rvRes.setLayoutManager(new LinearLayoutManager(activity));
        ress = getRess();
        showRess(ress);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                if (newText.isEmpty()) {
                    showRess(ress);
                } else {
                    List<Res> searchRess = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (Res res : ress) {
                        if (res.getResName().toUpperCase().contains(newText.toUpperCase())) {
                            searchRess.add(res);
                        }
                    }
                    showRess(searchRess);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        FloatingActionButton btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_resMaintainFragment_to_resInsertFragment);
            }
        });
    }

    private void showRess(List<Res> ress) {
        if (ress == null || ress.isEmpty()) {
            Common.showToast(activity, R.string.textNoRessFound);
        }
        ResAdapter resAdapter = (ResAdapter) rvRes.getAdapter();
        // 如果resAdapter不存在就建立新的，否則續用舊有的
        if (resAdapter == null) {
            rvRes.setAdapter(new ResAdapter(activity, ress));
        } else {
            resAdapter.setRess(ress);
            resAdapter.notifyDataSetChanged();
        }
    }

    private List<Res> getRess() {
        List<Res> ress = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ResServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            resGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = resGetAllTask.execute().get();
                Type listType = new TypeToken<List<Res>>() {
                }.getType();
                ress = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return ress;
    }

    private class ResAdapter extends RecyclerView.Adapter<ResAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Res> ress;
        private int imageSize;

        ResAdapter(Context context, List<Res> ress) {
            layoutInflater = LayoutInflater.from(context);
            this.ress = ress;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setRess(List<Res> ress) {
            this.ress = ress;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvResName, tvResAddress, tvResTel, tvResHours, tvResCategoryInfo, tvResEnable, tvUserName, tvModifyDate;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivRes);
                tvResName = itemView.findViewById(R.id.tvResName);
                tvResAddress = itemView.findViewById(R.id.tvResAddress);
                tvResTel = itemView.findViewById(R.id.tvResTel);
                tvResHours = itemView.findViewById(R.id.tvResHours);
                tvResCategoryInfo = itemView.findViewById(R.id.tvResCategoryInfo);
                tvResEnable = itemView.findViewById(R.id.tvResEnable);
                tvUserName = itemView.findViewById(R.id.tvUserName);
                tvModifyDate = itemView.findViewById(R.id.tvModifyDate);
            }
        }

        @Override
        public int getItemCount() {
            return ress == null ? 0 : ress.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_res, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Res res = ress.get(position);
            String url = Common.URL_SERVER + "ResServlet";
            int id = res.getResId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.tvResName.setText(res.getResName());
            myViewHolder.tvResAddress.setText(res.getResAddress());
            myViewHolder.tvResTel.setText(res.getResTel());
            myViewHolder.tvResHours.setText(res.getResHours());
            myViewHolder.tvResCategoryInfo.setText(String.valueOf(res.getResCategoryId()));
            if(res.isResEnable()){
                myViewHolder.tvResEnable.setText(R.string.textResIsEnable);
            }else {
                myViewHolder.tvResEnable.setText(R.string.textResIsNotEnable);
            }
            myViewHolder.tvUserName.setText(String.valueOf(res.getUserId()));
            myViewHolder.tvModifyDate.setText(res.getModifyDate().toString());

//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("res", res);
//                    Navigation.findNavController(view)
//                            .navigate(R.id.action_resListFragment_to_resDetailFragment, bundle);
//                }
//            });
//            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(final View view) {
//                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
//                    popupMenu.inflate(R.menu.popup_menu);
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.update:
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("res", res);
//                                    Navigation.findNavController(view)
//                                            .navigate(R.id.action_resListFragment_to_resUpdateFragment, bundle);
//                                    break;
//                                case R.id.delete:
//                                    if (Common.networkConnected(activity)) {
//                                        String url = Common.URL_SERVER + "resServlet";
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("action", "resDelete");
//                                        jsonObject.addProperty("resId", res.getId());
//                                        int count = 0;
//                                        try {
//                                            resDeleteTask = new CommonTask(url, jsonObject.toString());
//                                            String result = resDeleteTask.execute().get();
//                                            count = Integer.parseInt(result);
//                                        } catch (Exception e) {
//                                            Log.e(TAG, e.toString());
//                                        }
//                                        if (count == 0) {
//                                            Common.showToast(activity, R.string.textDeleteFail);
//                                        } else {
//                                            ress.remove(res);
//                                            resAdapter.this.notifyDataSetChanged();
//                                            // 外面ress也必須移除選取的res
//                                            resListFragment.this.ress.remove(res);
//                                            Common.showToast(activity, R.string.textDeleteSuccess);
//                                        }
//                                    } else {
//                                        Common.showToast(activity, R.string.textNoNetwork);
//                                    }
//                            }
//                            return true;
//                        }
//                    });
//                    popupMenu.show();
//                    return true;
//                }
//            });
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (resGetAllTask != null) {
            resGetAllTask.cancel(true);
            resGetAllTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0) {
            for (ImageTask imageTask : imageTasks) {
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }

        if (resDeleteTask != null) {
            resDeleteTask.cancel(true);
            resDeleteTask = null;
        }
    }
}