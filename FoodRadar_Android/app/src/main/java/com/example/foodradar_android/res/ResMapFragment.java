package com.example.foodradar_android.res;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ResMapFragment extends Fragment {
    private static final int PER_ACCESS_LOCATION = 0;
    private static final String TAG = "TAG_ResMapFragment";
    private static final int REQ_CHECK_SETTINGS = 101;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Activity activity;
    private MapView mapView;
    private GoogleMap map;
    private CommonTask resGetAllTask;
    private CommonTask resDeleteTask;
    private List<ImageTask> imageTasks;
    private List<Res> ress;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.map);
        return inflater.inflate(R.layout.fragment_res_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync((googleMap) -> {
            map = googleMap;
            checkLocationSettings();
            ress = getRess();
            if(ress != null) {
                for (Res res : ress) {
                    addMarker(new LatLng(res.getResLat(), res.getResLon()), res.getResName());
                }
            }
        });


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

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        askAccessLocationPermission();
    }

    private void askAccessLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_ACCESS_LOCATION);
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
            map.setMyLocationEnabled(true);
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
                        if(lastLocation != null) {
                            moveMap(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                            //lLisNull = false;
                        }else {
                            //lLisNull = true;
                        }
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private void moveMap(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    // 打標記
    private void addMarker(LatLng latLng, String resName) {
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        Address address = reverseGeocode(latLng.latitude, latLng.longitude);
        if (address == null) {
            //Toast.makeText(activity, R.string.textLocationNotFound, Toast.LENGTH_SHORT).show();
            return;
        }
        // 取得道路名稱當做標題
        String title = resName;
        // 取得地址當作說明文字
        String snippet = address.getAddressLine(0);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet));
        //        .icon(icon));
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
}