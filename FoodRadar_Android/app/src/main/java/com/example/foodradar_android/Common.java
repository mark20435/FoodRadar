package com.example.foodradar_android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Common{
    private final static String PREFERENCES_NAME = "foodradar_preference"; //prep偏好設定檔名
    private static final String TAG = "TAG_Common";
    public static String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23支援getActiveNetwork()
                Network network = connectivityManager.getActiveNetwork();
                // API 21支援getNetworkCapabilities()
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    String msg = String.format(Locale.getDefault(),
                            "TRANSPORT_WIFI: %b%nTRANSPORT_CELLULAR: %b%nTRANSPORT_ETHERNET: %b%n",
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    Log.d(TAG, msg);
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // API 29將NetworkInfo列為deprecated
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // 設定Fargement的AppBar是否要顯示返回的箭頭
    public void setBackArrow(Boolean isDisplay, Activity activity){
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(isDisplay);
    }

    // 使用者登入，傳入 Activity、登入的手機號碼(userPhone)、使用者密碼(userPwd)
    // 成功登入回傳使用者ＩＤ(userId)
    // 登入失敗回傳
    //  0=>登入失敗(原因不明)
    // -1=>使用者帳號(手機號碼)不存在
    // -2=>使用者密碼錯誤
    public int userLogin(Activity activity, String userPhone, String userPwd){
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        Integer userId = 0;

        // vvvvvv臨時寫的，用來模擬使用者登入
        String fromDB_userPhone = "0900123456";
        String fromDB_userPwd = "P@ssw0rd";
        Integer fromDB_userId = 3;

        if (fromDB_userPhone == userPhone && fromDB_userPwd == userPwd) {
            userId = fromDB_userId;
            preferences.edit()
                    .putString("userPhone", userPhone)
                    .putString("userPwd", userPwd)
                    .putString("userId", String.valueOf(userId))
                    .apply();
            return userId;
        } else if (fromDB_userPhone != userPhone) {
            return  -1;
        } else {
            return  -2;
        }
        // ^^^^^^臨時寫的，用來模擬使用者登入
    }

    // 取得已登入的使用者ＩＤ (userId)
    // 若未登入或登入狀態不明，則回傳0
    public int getUserLoin(Activity activity){
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String userId = preferences.getString("userId", "0");
        if (userId == "" || userId == null){
            userId = "0";
        }
        return Integer.parseInt(userId);
    }

}
