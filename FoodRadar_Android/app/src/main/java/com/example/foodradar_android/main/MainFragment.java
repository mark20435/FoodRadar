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
import androidx.appcompat.widget.Toolbar;

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

import static com.example.foodradar_android.R.id.toolbar;


public class MainFragment extends Fragment {
    private MainActivity activity;
    private RecyclerView recyclerView;
    private List<Main> mains;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        mains = getMains();
        setHasOptionsMenu(true);
    }

    private List<Main> getMains() {
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
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("首頁");
        activity.setSupportActionBar(toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        List<Main> mains = getMains();
        recyclerView.setAdapter(new MainAdapter(activity, mains));
    }


    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
        Context context;
        List<Main> mains;
        public MainAdapter(Context context, List<Main> mains) {
            this.context = context;
            this.mains = mains;
        }
        @Override
        public int getItemCount() {
            return mains.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView caName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivImage);
                caName = itemView.findViewById(R.id.caName);
            }
        }

        @NonNull
        @Override
        public MainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MainAdapter.MyViewHolder holder, int position) {

        }




    }
}



