package com.example.foodradar_android.main;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    private static final String TAG = "TAG_MainFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMain;
    private Activity activity;
    private CommonTask mainGetAllTask;
    private CommonTask mainDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Main> mains;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        imageTasks = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMain = view.findViewById(R.id.rvMain);
        rvMain.setLayoutManager(new LinearLayoutManager(activity));
        List<Main> mains = getMains();
        rvMain.setAdapter(new MainAdapter(activity,mains));
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);



        mains = getMains();
        showMains(mains);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showMains(mains);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<Main> getMains() {
        List<Main> mains = null;
        if (Common.networkConnected(activity)) {

            String url = Common.URL_SERVER + "ImgServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            mainGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = mainGetAllTask.execute().get();
                Type listType = new TypeToken<List<Main>>() {
                }.getType();
                mains = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return mains;
    }

    private void showMains(List<Main> mains) {
        if (mains == null || mains.isEmpty()) {
            Common.showToast(activity, R.string.textNoMainFound);
        }
        MainAdapter mainAdapter = (MainAdapter) rvMain.getAdapter();
        if (mainAdapter == null) {
            rvMain.setAdapter(new MainAdapter(activity, mains));
        } else {
            mainAdapter.setMains(mains);
            mainAdapter.notifyDataSetChanged();
        }
    }


    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<Main> mains;
        private int imageSize;

        MainAdapter(Context context, List<Main> mains) {
            layoutInflater = LayoutInflater.from(context);
            this.mains = mains;
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        public void setMains(List<Main> mains) {
            this.mains = mains;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView ctName;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivImage);
                ctName = itemView.findViewById(R.id.caName);
            }
        }

        @Override
        public int getItemCount() {
            return mains == null ? 0 : mains.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_main, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Main main = mains.get(position);

            String url = Common.URL_SERVER + "ImgServlet";
            int id = main.getImageId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.ctName.setText(main.getCateName());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("main", main);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_couponFragment, bundle);
                }
            });

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mainGetAllTask != null) {
            mainGetAllTask.cancel(true);
            mainGetAllTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0) {
            for (ImageTask imageTask : imageTasks) {
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }

        if (mainDeleteTask != null) {
            mainDeleteTask.cancel(true);
            mainDeleteTask = null;
        }
    }
}


