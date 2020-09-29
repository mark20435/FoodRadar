package com.example.foodradar_android.res;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.ImageTask;

import java.util.List;


public class ResDetailFragment extends Fragment {
    private NavController navController;
    private final static String TAG = "TAG_ResDetailFragment";
    private FragmentActivity activity;
    private List<ImageTask> imageTasks;
    private ImageTask imageTask;
    private ImageView imageView;
    private int imageSize;
    private Res res;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        res = (Res) (getArguments() != null ? getArguments().getSerializable("res") : null);

        // 顯示左上角的返回箭頭
        new Common();
        Common.setBackArrow(true, activity);
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
        activity.setTitle(R.string.titleResDetail);
        return inflater.inflate(R.layout.fragment_res_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);


        String url = Common.URL_SERVER + "ResServlet";
        int id = res.getResId();
        imageSize = getResources().getDisplayMetrics().widthPixels /4;
        imageTask = new ImageTask(url, id, imageSize, imageView);
        imageTask.execute();
//        Log.d(TAG, "imageTask: " + imageTaskBVH);

    }
}