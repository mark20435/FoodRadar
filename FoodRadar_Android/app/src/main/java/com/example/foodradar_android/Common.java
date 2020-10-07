package com.example.foodradar_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodradar_android.task.CommonTask;
import com.example.foodradar_android.user.MyRes;
import com.example.foodradar_android.user.UserAccountAvatra;
import com.example.foodradar_android.user.UserAccount;
import com.example.foodradar_android.user.UserMyResImage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Common{
    private final static String PREFERENCES_NAME = "foodradar_preference"; //prep偏好設定檔名
    private static final String TAG = "TAG_Common";
    public static String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";
    public static String USERACCOUNT_SERVLET = URL_SERVER + "UserAccountServlet";

    // 使用者登入後的ID(UserAccount.userId)，若 USER_ID <= 0 代表未登入或沒登入成功
    public static Integer USER_ID = 0;
    private Activity activity;
    public static final String USER_AVATAR_FILENAME = "foodradar_avatar.byte";

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
    public static int userLogin(@NonNull Activity activity, String userPhone, String userPwd){
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        Log.d(TAG,"userLogin.userPhone: " + userPhone);
        Log.d(TAG,"userLogin.userPwd: " + userPwd);

        // vvvvvv臨時寫的，用來模擬使用者登入
//        String fromDB_userPhone = "0900123456";
//        String fromDB_userPwd = "P@ssw0rd";
//        Integer fromDB_userId = 3;
////
////        if (fromDB_userPhone == userPhone && fromDB_userPwd == userPwd) {
////            userId = fromDB_userId;
////            preferences.edit()
//
//        Timestamp fromDB_userBirth = Timestamp.valueOf("2020-06-15" + " 00:00:00.000");
//        String fromDB_userName = "CloseBeta";
//        Boolean fromDB_allowNotifi = false;
////        Boolean fromDB_isEnable = true;
////        Boolean fromDB_isAdmin = false;
//        byte[] fromDB_userAvatar = null;
//        Calendar rightNow = Calendar.getInstance();
//        rightNow.add(Calendar.DATE, -30 );
//        Timestamp fromDB_createDate = new Timestamp(rightNow.getTimeInMillis());
//        Timestamp fromDB_modifyDate = new Timestamp(System.currentTimeMillis());
        // ^^^^^^臨時寫的，用來模擬使用者登入

        String fromDB_userPhone = "";
        String fromDB_userPwd = "";
        Integer fromDB_userId = 0;
        Timestamp fromDB_userBirth = null;
        String fromDB_userName = "";
        Boolean fromDB_allowNotifi = null;
        Boolean fromDB_isEnable;
        Boolean fromDB_isAdmin;
        byte[] fromDB_userAvatar = null;
        Timestamp fromDB_createDate = null;
        Timestamp fromDB_modifyDate = null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "userLogin");
        jsonObject.addProperty("userPhone", userPhone);
        jsonObject.addProperty("userPwd", userPwd);
        String jsonOut = jsonObject.toString();
//            Log.d(TAG,"getBooks.Common.URL_SERVLET: " + Common.URL_SERVLET);
        CommonTask getAllTask;
        UserAccount userAccountFromJsonList = null;
        List<UserAccount> getUserAccountList = new ArrayList<>();
        getAllTask = new CommonTask(USERACCOUNT_SERVLET, jsonOut);
        try {
            String jsonIn = getAllTask.execute().get();
            Type listType = new TypeToken<List<UserAccount>>() {
            }.getType();
            getUserAccountList = new Gson().fromJson(jsonIn, listType);

            Log.d(TAG,"getUserAccountList: " + getUserAccountList);
            userAccountFromJsonList = getUserAccountList.get(0);

            for(UserAccount ua : getUserAccountList){
                fromDB_userPhone = ua.getUserPhone();
                fromDB_userPwd = ua.getUserPwd();
                fromDB_userId = ua.getUserId();
                fromDB_userBirth = ua.getUserBirth();
                fromDB_userName = ua.getUserName();
                fromDB_allowNotifi = ua.getAllowNotifi();
                fromDB_isEnable = true;
                fromDB_isAdmin = false;
//                fromDB_userAvatar = ua.getUserAvatar();
                fromDB_createDate = ua.getCreateDate();
                fromDB_modifyDate = ua.getModifyDate();
            }
        } catch (Exception e) {
            Log.e(TAG, "userLogin: EXCEPTION");
            e.printStackTrace();
        }

        if (fromDB_userPhone.equals(userPhone) && fromDB_userPwd.equals(userPwd)) {
            USER_ID = fromDB_userId;
            Log.d(TAG,"userLogin.USER_ID: " + USER_ID);
            new Common().setPreferences(activity, userAccountFromJsonList);
//            preferences.edit()
//                    .putString("userPhone", fromDB_userPhone)
////                    .putString("userPwd", userPwd)
//                    .putString("userId", String.valueOf(USER_ID))
//                    .putString("userBirth", String.valueOf(fromDB_userBirth))
//                    .putString("userName", fromDB_userName)
//                    .putString("allowNotifi", String.valueOf(fromDB_allowNotifi))
////                    .putString("userAvatar", String.valueOf(fromDB_userAvatar))
//                    .putString("createDate", String.valueOf(fromDB_createDate))
//                    .putString("modifyDate", String.valueOf(fromDB_modifyDate))
//                    .apply();

            ImageView imageView = activity.findViewById(R.id.ivAvatar);
            UserMyResImage userMyResImage = new UserMyResImage(USERACCOUNT_SERVLET,USER_ID,activity,imageView);
            userMyResImage.execute(); // .execute() => UserImage.doInBackground

            return USER_ID;
        } else if (fromDB_userPhone != userPhone) {
            return  -1;
        } else {
            return  -2;
        }
    }

    public void setPreferences(Activity activity, UserAccount ua) {
        String fromDB_userPhone = "";
        String fromDB_userPwd = "";
        Integer fromDB_userId = 0;
        Timestamp fromDB_userBirth = null;
        String fromDB_userName = "";
        Boolean fromDB_allowNotifi = null;
        Boolean fromDB_isEnable;
        Boolean fromDB_isAdmin;
        byte[] fromDB_userAvatar = null;
        Timestamp fromDB_createDate = null;
        Timestamp fromDB_modifyDate = null;

        fromDB_userPhone = ua.getUserPhone();
        fromDB_userPwd = ua.getUserPwd();
        fromDB_userId = ua.getUserId();
        fromDB_userBirth = ua.getUserBirth();
        String strFromDB_userBirth = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(fromDB_userBirth);
        fromDB_userName = ua.getUserName();
        fromDB_allowNotifi = ua.getAllowNotifi();
        fromDB_isEnable = true;
        fromDB_isAdmin = false;
        fromDB_userAvatar = ua.getUserAvatar();
        fromDB_createDate = ua.getCreateDate();
        String strFromDB_createDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(fromDB_createDate);
        fromDB_modifyDate = ua.getModifyDate();
        String strFromDB_modifyDate = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(fromDB_modifyDate);

        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        preferences.edit()
                .putString("userPhone", fromDB_userPhone)
//                    .putString("userPwd", userPwd)
                .putString("userId", String.valueOf(fromDB_userId))
                .putString("userBirth", strFromDB_userBirth)
                .putString("userName", fromDB_userName)
                .putString("allowNotifi", String.valueOf(fromDB_allowNotifi))
//                    .putString("userAvatar", String.valueOf(fromDB_userAvatar))
                .putString("createDate", strFromDB_createDate)
                .putString("modifyDate", strFromDB_modifyDate)
                .apply();

        ImageView imageView = activity.findViewById(R.id.ivAvatar);
        UserMyResImage userMyResImage = new UserMyResImage(USERACCOUNT_SERVLET,USER_ID,activity,imageView);
        userMyResImage.execute(); // .execute() => UserImage.doInBackground
    }


    // 取得推播功能狀態
    public static Boolean getUserAllowNotifi(Activity activity){
        SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String userId = preferences.getString("userId", "0");

        if (userId.equals("") || userId == null || userId.equals("0")){
            return false;
        } else {
            String allowNotifi = preferences.getString("allowNotifi", "0");
            return Boolean.parseBoolean(allowNotifi);
        }
    }

    // 設定推播功能狀態
    public Boolean setUserAllowNotifi(Activity activity, Boolean notifiStatus){

        String strAllowNotifi = "0";
        if (notifiStatus == true) {
            strAllowNotifi = "1";
        }
        Log.d(TAG,"setUserAllowNotifi.USER_ID: " + USER_ID);
//        userAccount = new UserAccount(userId, userPhone, userPwd, userBirth_Timestamp, userName, allowNotifi_Boolean);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "setNotifiStatus");
        jsonObject.addProperty("id", USER_ID);
        jsonObject.addProperty("notifiStatus", notifiStatus);
//        jsonObject.addProperty("userAccount", new Gson().toJson(userAccount));
        // vvvvvv 直接把物件經GSON傳到後端Servlet的寫法，其中日期時間，有特別進行格式處理以免解析時格式無法確認
//        jsonObject.addProperty("userAccount", new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create().toJson(userAccount));

//        bitmapAvatra = new Common().getImageView(ivAvatar);
//        // 有圖才上傳
//        if (bitmapAvatra != null) {
//            jsonObject.addProperty("imageBase64", Base64.encodeToString(Common.bitmapToByte(bitmapAvatra), Base64.DEFAULT));
//        }
        int count = 0;
        try {
            String result = new CommonTask(USERACCOUNT_SERVLET, jsonObject.toString()).execute().get(); // Insert可等待回應確認是否新增成功
            count = Integer.parseInt(result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (count == 0) {
            return false;
        } else {
            SharedPreferences preferences; // 定義一個存取偏好設定檔的Preferences
            preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            preferences.edit().putString(strAllowNotifi, "0");
//            Common.userLogin(activity, userPhone, userPwd);
//            new Common().setPreferences(activity, userAccount);
//            Common.setUserAvatra(activity, bitmapAvatra);
            return true;
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

//        Bitmap userAvatarBitmap = null; // new Common().getUserAvatra();

        UserAccount userAccount = null;
        Log.d(TAG,"userId: " + userId);
        if (userId.equals("") || userId == null || userId.equals("0")){
            userAccount = new UserAccount(0, "", null, ""
                    , false, null, null, null);

            // 使用者登入後，把BottomNavigationView會員專區文字改暫時改為 使用者ID值 或 "會員專區" 供識別
            BottomNavigationView bottomNavigationView =  activity.findViewById(R.id.BottomNavigation);
            bottomNavigationView.getMenu().getItem(4).setTitle(R.string.user);

        } else {
            Integer userId_Int = Integer.parseInt(userId);
            USER_ID = userId_Int;

            // 使用者登入後，把BottomNavigationView會員專區文字改暫時改為 使用者ID值 或 "會員專區" 供識別
            BottomNavigationView bottomNavigationView =  activity.findViewById(R.id.BottomNavigation);
            bottomNavigationView.getMenu().getItem(4).setTitle(String.valueOf(USER_ID));
            Timestamp userBirth_Timestamp = null;
            try {
                userBirth_Timestamp = Timestamp.valueOf(userBirth);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Boolean allowNotifi_Boolean = Boolean.parseBoolean(allowNotifi);

            Timestamp createDate_Timestamp = null;
            if (!createDate.equals(null)) { createDate_Timestamp = Timestamp.valueOf(createDate); }
            Timestamp modifyDate_Timestamp = null;
            if (!modifyDate.equals(null)) { modifyDate_Timestamp = Timestamp.valueOf(modifyDate); }

            userAccount = new UserAccount(userId_Int, userPhone, userBirth_Timestamp, userName
                    , allowNotifi_Boolean, null, createDate_Timestamp, modifyDate_Timestamp);
        }
        return userAccount;
    }


    public static void setUserAvatra(Activity activity, Bitmap avatraBitmap){
        UserAccountAvatra userAccountAvatraOut = new UserAccountAvatra();
        userAccountAvatraOut.setByteObject(bitmapToByte(avatraBitmap));
        try (ObjectOutputStream oisOut = new ObjectOutputStream(
                activity.openFileOutput(USER_AVATAR_FILENAME, Context.MODE_PRIVATE))) {
            oisOut.writeObject(userAccountAvatraOut);
        } catch (IOException e) {
            Log.d(TAG,"setUserAvatra: Exception");
            Log.d(TAG, e.toString());
        }
    }

    // 取得使用者頭像，回傳Bitmap
    public static Bitmap getUserAvatra(Activity activity){
        UserAccountAvatra userAccountAvatraIn;
        Resources res = activity.getResources();

        try (ObjectInputStream oisIn = new ObjectInputStream(
                activity.openFileInput(USER_AVATAR_FILENAME))) {
            userAccountAvatraIn = (UserAccountAvatra) oisIn.readObject();

            if (userAccountAvatraIn.getByteObject().equals(null) || USER_ID <= 0) {
                Log.d(TAG,"userAccountAvatraIn.getByteObject().equals(null) || USER_ID <= 0: 沒取到頭像");
                // 若取不到頭像，則回傳預設頭像
                setUserAvatra(activity,BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle));
                return BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle);
            } else {
                Log.d(TAG,"userAccountAvatraIn.getByteObject(): 取到頭像");
                return byteToBitmap(userAccountAvatraIn.getByteObject());
            }

        } catch (Exception e) {
            Log.d(TAG,"getUserAvatra: Exception，回傳預設頭像");
            Log.d(TAG, e.toString());
            // 若取不到頭像，則回傳預設頭像
            setUserAvatra(activity,BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle));
            return BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle);
        }
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

        // 使用者登入後，把BottomNavigationView會員專區文字改暫時改為 使用者ID值 或 "會員專區" 供識別
        BottomNavigationView bottomNavigationView =  activity.findViewById(R.id.BottomNavigation);
        bottomNavigationView.getMenu().getItem(4).setTitle(R.string.user);

        // 頭像設為預設值
        Resources res = activity.getResources();
        Bitmap account_circle_bitmap = BitmapFactory.decodeResource(res,R.drawable.ic_awesome_user_circle);
        Common.setUserAvatra(activity, account_circle_bitmap);
    }

    public Bitmap getImageView (ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
//        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // quality設100代表不壓縮，範圍值0~100
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
//        } catch (Exception e) {
//            Log.d(TAG,"bitmapToByte: Exception");
//            e.printStackTrace();
//            return null;
//        }
    }

    public static Bitmap byteToBitmap(byte[] bitmapOfByte) {
//        try {
            return BitmapFactory.decodeByteArray(bitmapOfByte, 0, bitmapOfByte.length);
//        } catch (Exception e) {
//            Log.d(TAG,"byteToBitmap: Exception");
//            e.printStackTrace();
//            return null;
//        }
    }


}
