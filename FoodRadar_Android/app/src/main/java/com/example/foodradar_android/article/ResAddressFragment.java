package com.example.foodradar_android.article;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.res.Res;
import com.example.foodradar_android.task.CommonTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResAddressFragment extends Fragment {
    private static final String TAG = "TAG_ResListFragment";
    private Activity activity;
    private RecyclerView rvResAddress;
    private SearchView svResAddress;
    private List<Res> ress;
    private CommonTask resGetAllTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle("選餐廳");
        return inflater.inflate(R.layout.fragment_res_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svResAddress = view.findViewById(R.id.svResAddress);    //搜尋
        rvResAddress = view.findViewById(R.id.rvResAddress);    //餐廳列表

        rvResAddress.setLayoutManager(new LinearLayoutManager(activity));
        ress = getRes();
        showRess(ress);

        //  searchView
        svResAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showRess(ress);
                } else {
                    List<Res> searchRes = new ArrayList<>();
                    for (Res res : ress) {
                        if ((res.getResName().toUpperCase().contains(newText.toUpperCase())) ||
                                (res.getResAddress().toUpperCase().contains(newText.toUpperCase()))) {
                            searchRes.add(res);
                        }
                    }
                    showRess(searchRes);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }

    private List<Res> getRes() {
        List<Res> ress = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ResServlet"; //連線server端
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllEnable");
            String jsonOut = jsonObject.toString();
            resGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonInp = resGetAllTask.execute().get();
                Type listType = new TypeToken<List<Res>>() {    //Type > 拆解封箱，能取出server回傳的物件
                }.getType();
                Log.e(TAG, "listType:" + listType);
                ress = new Gson().fromJson(jsonInp, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, "連線失敗");
        }
        return ress;
    }

    private void showRess(List<Res> ress) {
        if (ress == null || ress.isEmpty()) {
            Common.showToast(activity, R.string.textNoRessFound);
            Log.e(TAG, "ress:" + ress);
        }
        ResAdapter resAdapter = (ResAdapter) rvResAddress.getAdapter();
        if (resAdapter == null) {
            rvResAddress.setAdapter(new ResAdapter(activity, ress));
        } else {
            resAdapter.setRess(ress);
            resAdapter.notifyDataSetChanged();
        }
    }

    private class ResAdapter extends RecyclerView.Adapter<ResAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Res> ress;

        ResAdapter(Context context, List<Res> ress) {
            layoutInflater = LayoutInflater.from(context);
            this.ress = ress;
        }

        public LayoutInflater getLayoutInflater() {
            return layoutInflater;
        }

        public void setLayoutInflater(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        public List<Res> getRess() {
            return ress;
        }

        public void setRess(List<Res> ress) {
            this.ress = ress;
        }

        @Override
        public int getItemCount() {
            return ress == null ? 0 : ress.size();
        }

        @NonNull
        @Override
        public ResAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.res_adress_view, parent, false);
            return new MyViewHolder(itemView);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvAdressResName, tvResAdress;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAdressResName = itemView.findViewById(R.id.tvAdressResName);
                tvResAdress = itemView.findViewById(R.id.tvResAddress);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ResAdapter.MyViewHolder myViewHolder, int position) {
            final Res res = ress.get(position);
            myViewHolder.tvAdressResName.setText(res.getResName());
            myViewHolder.tvResAdress.setText(res.getResAddress());

            myViewHolder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("res", res);
                Navigation.findNavController(v).navigate(R.id.action_resAddressFragment_to_articleInsertFragment, bundle);
            });
        }
    }
}