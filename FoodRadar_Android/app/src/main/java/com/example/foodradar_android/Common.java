package com.example.foodradar_android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodradar_android.user.UserAccountAvatra;
import com.example.foodradar_android.user.UserAccount;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Common{
    private final static String PREFERENCES_NAME = "foodradar_preference"; //prep偏好設定檔名
    private static final String TAG = "TAG_Common";
    public static String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";

    // 使用者登入後的ID(UserAccount.userId)，若 USER_ID <= 0 代表未登入或沒登入成功
    public static Integer USER_ID = 0 ;
    private Activity activity;
    public static final String USER_AVATAR_FILENAME = "foodradar_avatar.bitmap";

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
    public static void setBackArrow(Boolean isDisplay, Activity activity){
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
        //String userphone = preferences.getString("userphone", "0900123456");


        //String userpwd = preferences.getString("userpwd", "P@ssw0rd");

        Integer userId = 3;

        Log.d(TAG,"userLogin.userPhone: " + userPhone);
        Log.d(TAG,"userLogin.userPwd: " + userPwd);


        // vvvvvv臨時寫的，用來模擬使用者登入
        String fromDB_userPhone = "0900123456";
        String fromDB_userPwd = "P@ssw0rd";
        Integer fromDB_userId = 3;
//
//        if (fromDB_userPhone == userPhone && fromDB_userPwd == userPwd) {
//            userId = fromDB_userId;
//            preferences.edit()

        Timestamp fromDB_userBirth = Timestamp.valueOf("2020-06-15" + " 00:00:00.000");
        String fromDB_userName = "CloseBeta";
        Boolean fromDB_allowNotifi = false;
//        Boolean fromDB_isEnable = true;
//        Boolean fromDB_isAdmin = false;
        byte[] fromDB_userAvatar = null;
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, -30 );
        Timestamp fromDB_createDate = new Timestamp(rightNow.getTimeInMillis());
        Timestamp fromDB_modifyDate = new Timestamp(System.currentTimeMillis());
        // ^^^^^^臨時寫的，用來模擬使用者登入

        if (fromDB_userPhone.equals(userPhone) && fromDB_userPwd.equals(userPwd)) {
            USER_ID = fromDB_userId;
            Log.d(TAG,"userLogin.USER_ID: " + USER_ID);
            preferences.edit()
                    .putString("userId", String.valueOf(USER_ID))
                    .putString("userPhone", userPhone)
                    .putString("userPwd", userPwd)
                    .putString("userId", String.valueOf(userId))
                    .putString("userBirth", String.valueOf(fromDB_userBirth))
                    .putString("userName", fromDB_userName)
                    .putString("allowNotifi", String.valueOf(fromDB_allowNotifi))
                    .putString("userAvatar", String.valueOf(fromDB_userAvatar))
                    .putString("createDate", String.valueOf(fromDB_createDate))
                    .putString("modifyDate", String.valueOf(fromDB_modifyDate))
                    .apply();
            return userId;
        } else if (fromDB_userPhone != userPhone) {
            return  -1;
        } else {
            return  -2;
        }

    }

    // 取得已登入的使用者ＩＤ (userId)
    // 若未登入或登入狀態不明，則回傳0
    public static UserAccount getUserLoin(Activity activity){
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        String userId = preferences.getString("userId", "0");
        String userPhone = preferences.getString("userPhone", "");
        String userBirth = preferences.getString("userBirth", "");
        String userName = preferences.getString("userName", "");

        String allowNotifi = preferences.getString("allowNotifi","0");
        String createDate = preferences.getString("createDate", "");
        String modifyDate = preferences.getString("modifyDate", "");

        Bitmap userAvatarBitmap = null; // new Common().getUserAvatra();

        UserAccount userAccount = null;
        Log.d(TAG,"userId: " + userId);
        if (userId.equals("") || userId == null || userId.equals("0")){
            userAccount = new UserAccount(0, "", null, ""
                    , false, null, null, null);
        } else {
            Integer userId_Int = Integer.parseInt(userId);
            Timestamp userBirth_Timestamp = Timestamp.valueOf(userBirth);
            Boolean allowNotifi_Boolean = Boolean.parseBoolean(allowNotifi);
            Timestamp createDate_Timestamp = Timestamp.valueOf(createDate);
            Timestamp modifyDate_Timestamp = Timestamp.valueOf(modifyDate);

            userAccount = new UserAccount(userId_Int, userPhone, userBirth_Timestamp, userName
                    , allowNotifi_Boolean, userAvatarBitmap, createDate_Timestamp, modifyDate_Timestamp);
        }
        return userAccount;
    }


    public void setUserAvatra(Activity activity, Bitmap avatraBitmap){
        UserAccountAvatra userAccountAvatraBitmapOut = new UserAccountAvatra(avatraBitmap);
        try (ObjectOutputStream oisOut = new ObjectOutputStream(
                activity.openFileOutput(USER_AVATAR_FILENAME, Context.MODE_PRIVATE))) {
            oisOut.writeObject(userAccountAvatraBitmapOut);
        } catch (IOException e) {
            Log.d(TAG,"setUserAvatra: Exception");
            Log.d(TAG, e.toString());
        }
    }

    // 取得使用者頭像，回傳Bitmap
    public Bitmap getUserAvatra(){
        UserAccountAvatra userAccountAvatraBitmapIn = new UserAccountAvatra();
        try (ObjectInputStream oisIn = new ObjectInputStream(
                activity.openFileInput(USER_AVATAR_FILENAME))) {
                userAccountAvatraBitmapIn = (UserAccountAvatra) oisIn.readObject();
//            }
        } catch (Exception e) {
            Log.d(TAG,"getUserAvatra: Exception");
            Log.d(TAG, e.toString());
        }
        return userAccountAvatraBitmapIn.getBitmapObject();
    }

    // 使用者登出，傳入 Activity
    public void userLogout(Activity activity){
        String userPhone= "logout", userPwd = "logout";
        // 清除使用者偏好設定
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        preferences.edit()
                .putString("userId", "0")
                .putString("userPhone", "")
                .putString("userPwd", "")
                .putString("userBirth", "")
                .putString("userName", "")
                .putString("allowNotifi", "0")
                .putString("userAvatar", "")
                .putString("createDate", "")
                .putString("modifyDate", "")
                .apply();
        // USER_ID 設為 0(未登入)
        USER_ID = 0;
        // 頭像設為預設值
        Resources res = activity.getResources();
        Bitmap account_circle_bitmap = BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle);
        new Common().setUserAvatra(activity, account_circle_bitmap);
    }

}
