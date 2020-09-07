package com.example.foodradar_android.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodradar_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.BottomNavigation);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //List<MainFragment> mainFragments = getMainFragments();
        //recyclerView.setAdapter(new MainFragmentAdapter(this, mainFragments));
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

<<<<<<< HEAD:FoodRadar_Android/app/src/main/java/com/example/foodradar_android/main/MainActivity.java



        }





=======
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
}
>>>>>>> b679b4860bd6a67c974087044b42a3a70b981f64:FoodRadar_Android/app/src/main/java/com/example/foodradar_android/MainActivity.java
