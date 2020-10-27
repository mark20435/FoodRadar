package com.example.foodradar_android.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.foodradar_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        NavController navController = Navigation.findNavController(this, R.id.mainFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void enableBottomBar(Activity activity, int i) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.BottomNavigation);
        int k = bottomNavigationView.getSelectedItemId();
        if (i == k) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(false);
        } else {
            bottomNavigationView.getMenu().getItem(i).setEnabled(true);
        }
    }

}


// Main Activity右上角選單
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater(); //inflater載入器
//        inflater.inflate(R.menu.appbar_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//
//        return super.onContextItemSelected(item);
//    }


