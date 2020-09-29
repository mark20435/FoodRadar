package com.example.foodradar_android.res;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.article.ArticleDetailFragment;
import com.example.foodradar_android.article.Img;
import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.task.ImageTask;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ResDetailFragment extends Fragment {
    private NavController navController;
    private final static String TAG = "TAG_ResDetailFragment";
    private FragmentActivity activity;
    private ImageView ivRes, ivImage;
    private TextView tvResName, tvResCategoryInfo, tvResAddress, tvResHours, tvResTel, tvImageNumber;
    private RecyclerView rvImage;
    private Res res;
    private JsonObject jsonHours;
    private List<ImageTask> imageTasks;
    private CommonTask imgGetAllTask;
    private List<Img> imgs;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Button btDirect;
    private static final int PER_ACCESS_LOCATION = 0;
    private static final int REQ_CHECK_SETTINGS = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setSmallestDisplacement(1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
            }
        };

        imageTasks = new ArrayList<>();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity.setTitle(R.string.titleResDetail);
        return inflater.inflate(R.layout.fragment_res_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("res") == null) {
            Common.showToast(activity, R.string.textNoRessFound);
            navController.popBackStack();
            return;
        }
        res = (Res) bundle.getSerializable("res");

        ivRes = view.findViewById(R.id.ivRes);
        tvResName = view.findViewById(R.id.tvResName);
        tvResCategoryInfo = view.findViewById(R.id.tvResCategoryInfo);
        tvResAddress = view.findViewById(R.id.tvResAddress);
        tvResTel = view.findViewById(R.id.tvResTel);
        ivImage = view.findViewById(R.id.ivImage);
        tvImageNumber = view.findViewById(R.id.tvImageNumber);

        tvResName.setText(res.getResName());
        tvResCategoryInfo.setText(res.getResCategoryInfo());
        tvResAddress.setText(res.getResAddress());
        tvResTel.setText(res.getResTel());

        showResImg();

        //todo 營業時間
        jsonHours = JsonParser.parseString(res.getResHours()).getAsJsonObject();

        rvImage = view.findViewById(R.id.rvImage);    //圖片recyclerView
        rvImage.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        imgs = getImgs();
        if (imgs.size() == 0) {
            rvImage.setVisibility(View.GONE);
            tvImageNumber.setVisibility(View.GONE);
            ivImage.setVisibility(View.GONE);
        } else {
            tvImageNumber.setText(String.valueOf(imgs.size()) + "張照片");
            showImgs(imgs);
        }

        btDirect = view.findViewById(R.id.btDirect);

        checkLocationSettings();

        btDirect.setOnClickListener(v -> {
            String destination = res.getResAddress();
            if (destination.isEmpty() || lastLocation == null) {
                Toast.makeText(activity, R.string.textLocationEmpty, Toast.LENGTH_SHORT).show();
                return;
            }

            // reverse geocode
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            Address addressOrigin = reverseGeocode(latitude, longitude);
            Address addressDestination = getAddress(destination);
            boolean notFound = false;
            // 檢查是否可將起點、目的地轉成address
            if (addressOrigin == null) {
                Toast.makeText(activity, R.string.textOriginNotFound, Toast.LENGTH_SHORT).show();
                notFound = true;
            }
            if (addressDestination == null) {
                Toast.makeText(activity, R.string.textDestinationNotFound, Toast.LENGTH_SHORT).show();
                notFound = true;
            }
            if (notFound) {
                return;
            }

            double fromLat = addressOrigin.getLatitude();
            double fromLng = addressOrigin.getLongitude();
            double toLat = addressDestination.getLatitude();
            double toLng = addressDestination.getLongitude();

            direct(fromLat, fromLng, toLat, toLng);
        });
    }

    private class ImgAdapter extends RecyclerView.Adapter<ResDetailFragment.ImgAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Img> imgs;
        private int imageSize;

        ImgAdapter(Context context, List<Img> imgs) {
            layoutInflater = LayoutInflater.from(context);
            this.imgs = imgs;
            /* 螢幕寬度除以2當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        }

        @Override
        public int getItemCount() {
            return imgs == null ? 0 : imgs.size();
        }

        void setImgs(List<Img> imgs) {
            this.imgs = imgs;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivImage);
            }
        }

        @NonNull
        @Override
        public ResDetailFragment.ImgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_res_detail_image, parent, false);
            return new ResDetailFragment.ImgAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ResDetailFragment.ImgAdapter.MyViewHolder myViewHolder, int position) {
            final Img img = imgs.get(position);
            String url = Common.URL_SERVER + "ImgServlet";
            int imgId = img.getImgId();
            ImageTask imageTask = new ImageTask(url, imgId, imageSize, myViewHolder.ivImage);
            imageTask.execute();
            imageTasks.add(imageTask);
        }

    }

    private List<Img> getImgs() {
        List<Img> imgs = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "ResServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImgByResId");
            jsonObject.addProperty("resId", res.getResId());
            String jsonOut = jsonObject.toString();
            imgGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = imgGetAllTask.execute().get();
                Type listType = new TypeToken<List<Img>>() {
                }.getType();
                imgs = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return imgs;
    }

    //文章圖片 > 顯示imgs
    private void showImgs(List<Img> imgs) {
        if (imgs == null || imgs.isEmpty()) {
            Common.showToast(activity, R.string.textNoImgFound);
        }
        ResDetailFragment.ImgAdapter imgAdapter = (ResDetailFragment.ImgAdapter) rvImage.getAdapter();
        if (imgAdapter == null) {
            rvImage.setAdapter(new ResDetailFragment.ImgAdapter(activity, imgs));
        } else {
            imgAdapter.setImgs(imgs);
            imgAdapter.notifyDataSetChanged();
        }
    }

    private void showResImg() {
        String url = Common.URL_SERVER + "ResServlet";
        int id = res.getResId();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivRes.setImageBitmap(bitmap);
        } else {
            ivRes.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (imgGetAllTask != null) {
            imgGetAllTask.cancel(true);
            imgGetAllTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0) {
            for (ImageTask imageTask : imageTasks) {
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }
    }

    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // 取得並顯示最新位置
                showMyLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(activity, REQ_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        //Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void showMyLocation() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
        }

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
                //todo 如果拒絕
                return;
            }
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private Address reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            return null;
        } else {
            return addressList.get(0);
        }
    }

    private void direct(double fromLat, double fromLng, double toLat,
                        double toLng) {
        String uriStr = String.format(Locale.CHINESE,
                "https://www.google.com/maps/dir/?api=1" +
                        "&origin=%f,%f&destination=%f,%f&travelmode=driving",
                fromLat, fromLng, toLat, toLng);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriStr));
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private Address getAddress(String locationName) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            return null;
        } else {
            return addressList.get(0);
        }
    }
}