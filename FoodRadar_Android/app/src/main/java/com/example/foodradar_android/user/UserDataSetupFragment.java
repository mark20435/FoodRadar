package com.example.foodradar_android.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class UserDataSetupFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{
    private Activity activity;
    private NavController navController;
    public final String TAG = "TAG_UserDataSetupFrag";

    private ImageView ivAvatar;

    private final String USERACCOUNT_SERVLET = Common.URL_SERVER + "UserAccountServlet";
    private final String userAvatraFileName = Common.USER_AVATAR_FILENAME;
    List<UserAccount> getUserAccountList = null;

    private File file;
    private Uri contentUri;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
//    private static final int REQ_TAKE_PICTURE = 101;
//    private static final int REQ_PICK_PICTURE = 102;
//    private static final int REQ_CROP_PICTURE = 103;
    private static final int PER_EXTERNAL_STORAGE = 201;

    private Bitmap bitmapAvatra = null;

    private ImageButton btImgCamera;
    private Button btImgPickPhoto;

    private TextView tvUserPhone;
    private EditText etUserPhone;

    private TextView tvPassword;
    private EditText etPassword;

    private ImageView ivRedStarPasswordConfirm;
    private TextView tvPasswordConfirm;
    private EditText etPasswordConfirm;

    private TextView tvUserName;
    private EditText etUserName;

    private TextView tvUserBirth;
    private EditText etUserBirth;

    private Button btnLogInOut;
    private Button btUserChangConfrim;

    private UserAccount userAccount;

    private Integer signupFlag = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

        // 顯示左上角的返回箭頭
        Common.setBackArrow(true, activity);
        setHasOptionsMenu(true);

        navController =
                Navigation.findNavController(activity, R.id.mainFragment);
    }

    // 顯示右上角的OptionMenu選單
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.appbar_menu,menu);  // 從res取用選項的清單“R.menu.my_menu“
//        super.onCreateOptionsMenu(menu, inflater);
    }
    // 顯示右上角的OptionMenu選單
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
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
        activity.setTitle(R.string.title_of_userdata);
        return inflater.inflate(R.layout.fragment_user_data_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 預設無資料的頭像

        ImageView imageAvatar = view.findViewById(R.id.ivAvatar);
        imageAvatar.setImageResource(R.drawable.ic_baseline_account_circle_24);

        ivAvatar = view.findViewById(R.id.ivAvatar);
        ivAvatar.setImageResource(R.drawable.ic_baseline_account_circle_24);
        btImgCamera = view.findViewById(R.id.btImgCamera);
        btImgPickPhoto = view.findViewById(R.id.btImgPickPhoto);
//        askExternalStoragePermission();

        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        etUserPhone = view.findViewById(R.id.etUserPhone);

        tvPassword = view.findViewById(R.id.tvPassword);
        etPassword = view.findViewById(R.id.etPassword);

        ivRedStarPasswordConfirm = view.findViewById(R.id.ivRedStarPasswordConfirm);
        tvPasswordConfirm = view.findViewById(R.id.tvPasswordConfirm);
        etPasswordConfirm = view.findViewById(R.id.etPasswordConfirm);

        tvUserName = view.findViewById(R.id.tvUserName);
        etUserName = view.findViewById(R.id.etUserName);

        tvUserBirth = view.findViewById(R.id.tvUserBirth);
        etUserBirth = view.findViewById(R.id.etUserBirth);

        btnLogInOut = view.findViewById(R.id.btnLogInOut);
        btUserChangConfrim = view.findViewById(R.id.btSignupOrChang);


        // 使用者登入畫面顯示控制
        setUI();

        // 拍照功能
        view.findViewById(R.id.btImgCamera).setOnClickListener(this);
        view.findViewById(R.id.btImgPickPhoto).setOnClickListener(this);

        // 登入/登出功能
        view.findViewById(R.id.btnLogInOut).setOnClickListener(this);

        // 變更密碼功能
        view.findViewById(R.id.btSignupOrChang).setOnClickListener(this);


        // vvvvvv臨時寫的，用來模擬使用者登入
        view.findViewById(R.id.btTestData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = "0900123456";
                String userPwd = "P@ssw0rd";
                // Integer userId = 3;
                etUserPhone.setText(userPhone);
                etPassword.setText(userPwd);

//                if(getUserId() > 0){
//                    UserAccount userAccount = new UserAccount();
//                    userAccount = Common.getUserLoin(activity);
//                    Common.showToast(activity, "會員專區，\n登入成功，\nCommon.USER_ID: "  + userAccount.getUserId());
//                    Common.showToast(activity, "會員專區，\n登入成功，\nuserId: "  + getUserId());
//                } else {
//                    switch (getUserId()){
//                        case 0: // 0=>登入失敗(原因不明)
//                            Common.showToast(activity, "會員專區，\n登入失敗(原因不明)，\nuserId: " + userId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
//                            break;
//                        case -1: // -1=>使用者帳號(手機號碼)不存在
//                            Common.showToast(activity, "會員專區，\n使用者帳號(手機號碼)不存在，\nuserId: " + userId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
//                            break;
//                        case -2: // -2=>使用者密碼錯誤
//                            Common.showToast(activity, "會員專區，\n使用者密碼錯誤，\nuserId: " + userId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
//                            break;
//                        default:
//                            Common.showToast(activity, "會員專區，\n登入失敗，\nuserId: " + userId +",\nuserPhone:" + userPhone + ",\nuserPwd: " + userPwd);
//                            break;
//                    }
//                }
            }
        });
        // ^^^^^^臨時寫的，用來模擬使用者登入

    }

    private int getUserId(){
        return Common.USER_ID;
    }

    private void setUI() {
        if(getUserId() > 0) { // 登入狀態
            Common.showToast(activity, "會員資料設定\n登入成功\nUserId: " + getUserId());
            // 設定註冊欄位是(VISIBLE)否(INVISIBLE)顯示
            SetSignUpUI(VISIBLE);

            userAccount = Common.getUserLoin(activity);
            etUserPhone.setText(userAccount.getUserPhone());
            etPassword.setText(userAccount.getUserPwd());
            etPasswordConfirm.setText(userAccount.getUserPwd());
            etUserName.setText(userAccount.getUserName());
            String userBirth = new SimpleDateFormat("yyyy-MM-dd").format(userAccount.getUserBirth());
            etUserBirth.setText(userBirth);

            btnLogInOut.setText(R.string.textLogout);
            btUserChangConfrim.setText(R.string.textUserChangConfrim);
            bitmapAvatra = showUserAvatra();
            ivAvatar.setImageBitmap(bitmapAvatra);
        } else { // 已登出狀態
            Common.showToast(activity, "會員資料設定\n登入失敗\nUserId: " + getUserId());
            // 設定註冊欄位是(VISIBLE)否(INVISIBLE)顯示
            SetSignUpUI(INVISIBLE);

            etUserPhone.setText("");
            etPassword.setText("");
            etPasswordConfirm.setText("");

            btnLogInOut.setText(R.string.textLogin);
            btUserChangConfrim.setText(R.string.textSignUp);
//            ivAvatar.setImageResource(R.drawable.ic_awesome_user_circle);
            bitmapAvatra = showUserAvatra();
            ivAvatar.setImageBitmap(bitmapAvatra);
        }
    }

    // 設定註冊欄位是(VISIBLE)否(INVISIBLE)顯示
    private void SetSignUpUI(Integer visibStaus) {
        ivRedStarPasswordConfirm.setVisibility(visibStaus);
        tvPasswordConfirm.setVisibility(visibStaus);
        etPasswordConfirm.setText("");
        etPasswordConfirm.setVisibility(visibStaus);

        tvUserName.setVisibility(visibStaus);
        etUserName.setText("");
        etUserName.setVisibility(visibStaus);

        tvUserBirth.setVisibility(visibStaus);
        etUserBirth.setText("");
        etUserBirth.setVisibility(visibStaus);

        btImgCamera.setVisibility(visibStaus);

        if (visibStaus == VISIBLE) {
            btnLogInOut.setText(R.string.textSignUpSend);
            btUserChangConfrim.setText(R.string.textSignUpCancel);
        } else {
            btnLogInOut.setText(R.string.textLogin);
            btUserChangConfrim.setText(R.string.textSignUp);
        }

    }


//    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            // 登入/登出 功能
            case R.id.btnLogInOut:
                if(getUserId() > 0) { // 登出
                    new Common().userLogout(activity);
                    Common.showToast(activity,"登出");
                    setUI();
                } else { // 登入
                    if(signupFlag != 1) { // 登入作業
                        new Common().userLogin(activity, etUserPhone.getText().toString(), etPassword.getText().toString());
                        setUI();
                        Common.showToast(activity,"登入");
                    } else { //註冊送出作業
                        Common.showToast(activity,"註冊送出");
//                        Common.showToast(activity,"etUserPhone=>" + etUserPhone.getText().toString() + "<=");
                        if (etUserPhone.getText().toString().trim().equals("")) {
                            Common.showToast(activity,tvUserPhone.getText() + "不可為空白");
                            tvUserPhone.setTextColor(getResources().getColor(R.color.mainPink));
                            etUserPhone.setHintTextColor(getResources().getColor(R.color.mainPink));
                            etUserPhone.setHint(getResources().getString(R.string.textPlsInpup) + getResources().getString(R.string.textUserPhone));
                        }

                        String userPhone = etUserPhone.getText().toString();
                        String userPwd = etPassword.getText().toString();
                        String userBirth = etUserBirth.getText().toString();
                        Timestamp userBirth_Timestamp = null;
                        if (userBirth == null) {
                            userBirth_Timestamp = Timestamp.valueOf(userBirth);
                        }
                        String userName = etUserName.getText().toString();
                        Boolean allowNotifi_Boolean = true;
                        Bitmap userAvatarBitmap = null; // new Common().getUserAvatra();

                        userAccount = new UserAccount(userPhone, userPwd, userBirth_Timestamp, userName
                                , allowNotifi_Boolean, userAvatarBitmap);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "userAccountSignup");
                        jsonObject.addProperty("userAccount", new Gson().toJson(userAccount));
                        // 有圖才上傳
                        if (bitmapAvatra != null) {
                            jsonObject.addProperty("imageBase64", Base64.encodeToString(bitmapToByteArray(bitmapAvatra), Base64.DEFAULT));
                        }
                        int count = 0;
                        try {
                            String result = new CommonTask(USERACCOUNT_SERVLET, jsonObject.toString()).execute().get(); // Insert可等待回應確認是否新增成功
                            count = Integer.parseInt(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) {
                            Common.showToast(activity, "註冊失敗");
                        } else {
                            Common.showToast(activity, "註冊成功");
                        }



                    }
                }
                return;

            // 資料修改/註冊 功能
            case R.id.btSignupOrChang:
                // 若沒有網路連線不進行作業
                if (!Common.networkConnected(activity)) {
                    Common.showToast(activity,R.string.textNetworkConnectedFail);
                    return;
                }

                if(getUserId() > 0) { // 資料修改
                    Common.showToast(activity,"確認修改");
                    BitmapDrawable drawable = (BitmapDrawable) ivAvatar.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    new Common().setUserAvatra(activity, bitmap);
//                    ivAvatar.setImageBitmap(new Common().getUserAvatra());
                    bitmapAvatra = showUserAvatra();
                    ivAvatar.setImageBitmap(bitmapAvatra);
                    Common.showToast(activity,"頭像存檔完成");

                } else if ( 1 == 1) { // 註冊
                    // 設定註冊欄位是(VISIBLE)否(INVISIBLE)顯示
                    if (etPasswordConfirm.getVisibility() == INVISIBLE) {
                        signupFlag = 1;
                        SetSignUpUI(VISIBLE);
                        Common.showToast(activity, R.string.textSignUp);
                    } else {
                        signupFlag = 0;
                        SetSignUpUI(INVISIBLE);
                        Common.showToast(activity, R.string.textSignUpCancel);
                    }


                    return;
                } else {
//                    byte[] imgBytes = new byte[1];
//                    MyRes myRes = new MyRes(1,"resName","resHours","resTel","resAddress", imgBytes);
//                    getMyResList = new ArrayList<>();
//                    getMyResList.add(0,myRes);
//                    getMyResList.add(1,new MyRes(2,"resName2","resHours","resTel","resAddress", imgBytes));
//                    for (int i = 2 ; i <= 13 ; i++){
//                    getMyResList.add(i,new MyRes(i,"resName" + String.valueOf(i),"resHours","resTel","resAddress", imgBytes));
//
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "findById");
                    jsonObject.addProperty("id", getUserId());
                    String jsonOut = jsonObject.toString();

                    CommonTask getAllTask;
                    getAllTask = new CommonTask(USERACCOUNT_SERVLET, jsonOut);
                    try {
                        String jsonIn = getAllTask.execute().get();
                        Type listType = new TypeToken<List<UserAccount>>() {
                        }.getType();
                        getUserAccountList = new Gson().fromJson(jsonIn, listType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return;

            // 拍照功能
            case R.id.btImgCamera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (dir != null && !dir.exists()) {
                    if (!dir.mkdirs()) {
                        return;
                    }
                }
                file = new File(dir, "avatar_" + getUserId() + ".jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(activity, R.string.textNoCameraApp);
                }
                return;

            // 選取照片功能
            case R.id.btImgPickPhoto:
                Intent intentPickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intentPickPhoto.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intentPickPhoto, REQ_PICK_PICTURE);
                } else {
                    Common.showToast(activity, R.string.textNoImagePickerApp);
                }
                return;

            default:
                return;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.etUserPhone:
                if (hasFocus) {
                    etUserPhone.setHintTextColor(getResources().getColor(R.color.colorTextHint));
                    etUserPhone.setHint(R.string.textUserPhone);
                }
                return;
            case R.id.etPassword:
                if (hasFocus) {
                    etPassword.setHintTextColor(getResources().getColor(R.color.colorTextHint));
                    etPassword.setHint(R.string.textUserPassword);
                }
                return;
            case R.id.etPasswordConfirm:
                if (hasFocus) {
                    etPasswordConfirm.setHintTextColor(getResources().getColor(R.color.colorTextHint));
                    etPasswordConfirm.setHint(R.string.textPasswordConfirm);
                }
                return;
            default:
                return;
        }

    }


    // 拍照後裁切圖片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;
                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .start(activity, this, REQ_CROP_PICTURE);
    }

    @SuppressLint("LongLogTag")
    private void handleCropResult(Intent intent) {
//        byte[] imageByteAvatar;

        Uri resultUri = UCrop.getOutput(intent);
        if (resultUri == null) {
            return;
        }

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                bitmapAvatra = BitmapFactory.decodeStream(
                        activity.getContentResolver().openInputStream(resultUri));
            } else {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmapAvatra = ImageDecoder.decodeBitmap(source);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmapAvatra.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            imageByteAvatar = out.toByteArray();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        if (bitmapAvatra != null) {
            ivAvatar.setImageBitmap(bitmapAvatra);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_awesome_user_circle);
        }
    }

    private Bitmap showUserAvatra (){
        UserAccountAvatra userAccountAvatraBitmapIn = new UserAccountAvatra();
        try (ObjectInputStream soiIn = new ObjectInputStream(
                activity.openFileInput(userAvatraFileName))) {
            userAccountAvatraBitmapIn = (UserAccountAvatra) soiIn.readObject();
        } catch (Exception e) {
            Log.d(TAG,"getUserAvatra: Exception");
            Log.d(TAG, e.toString());
        }
        return userAccountAvatraBitmapIn.getBitmapObject();
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // quality設100代表不壓縮，範圍值0~100
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            Log.d(TAG,"bitmapToByteArray: Exception");
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        askExternalStoragePermission();
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        askExternalStoragePermission();
//    }


    private void askExternalStoragePermission() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == PER_EXTERNAL_STORAGE) {
            // 如果user不同意將資料儲存至外部儲存體的公開檔案，就將儲存按鈕設為disable
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Common.showToast(activity,"需存取內部儲存空間權限");
                btImgPickPhoto.setEnabled(false);
            } else {
                btImgPickPhoto.setEnabled(true);
            }
        }
    }

}