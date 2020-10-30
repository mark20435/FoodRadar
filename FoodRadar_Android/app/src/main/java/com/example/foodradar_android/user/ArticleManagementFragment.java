package com.example.foodradar_android.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodradar_android.Common;
import com.example.foodradar_android.R;
import com.example.foodradar_android.article.Article;
import com.example.foodradar_android.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ArticleManagementFragment extends Fragment {

    private Activity activity;
    private NavController navController;
    private List<MyArticle> myArticleList = new ArrayList<>();
    private List<UserImageTask> imageTasks= new ArrayList<>();
    private RecyclerView rcvArticleManagement;
//    private RecyclerView rcvMyArticleIsMy;
//    private RecyclerView rcvMyComment;

    //    private Integer userId;
    private String URL_SERVER = "http://10.0.2.2:8080/FoodRadar_Web/";
    private String MYARTICLE_SERVLET = URL_SERVER + "MyArticleServlet";
    private Integer imageSize = 200;
    final private String TAG = "ArticleManagementFrag";
    private EditText etUsManageUserPhone;
    private TextView etManageArticleDate;
//    private ColorStateList edTextdefaultColor;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
//    private Button btUsManageSearchArticle, btUsManageCancel;

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
        activity.setTitle(R.string.title_of_res_article_management);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // item_view_article_management

        etUsManageUserPhone = view.findViewById(R.id.etUsManageUserPhone);
        etManageArticleDate = view.findViewById(R.id.tvManageArticleDate);
//        tvUserBirth = view.findViewById(R.id.tvUsManageArticleDate);
//        etUserBirth = view.findViewById(R.id.tvManageArticleDate);
//        tvUserBirthDivider = view.findViewById(R.id.tvUserBirthDivider);
//        edTextdefaultColor =  etManageArticleDate.getTextColors();
        etManageArticleDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 一月的值是0而非1，所以「month + 1」後才顯示
                month++;
                String yyyyMMdd = new StringBuilder()
                        .append(year).append("-")
                        .append((month < 10 ? "0" + month : month)).append("-")
                        .append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth))
                        .toString();etManageArticleDate.setText(yyyyMMdd);
//                etManageArticleDate.setTextColor(edTextdefaultColor);
            }
        };

        etManageArticleDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    // 最後要呼叫show()方能顯示
                    datePickerDialog.show();
                }
                return false;
            }
        });

        etManageArticleDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etManageArticleDate.getText().equals("")) {
//                    etManageArticleDate.setTextColor(getResources().getColor(R.color.colorTextHint));
//                    etManageArticleDate.setText(getResources().getString(R.string.textArticleDate));
                    etManageArticleDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
                } else {
//                    etManageArticleDate.setTextColor(edTextdefaultColor);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        setDatePicker();


        // 搜尋會員發文
        view.findViewById(R.id.btUsManageSearchArticle)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = etUsManageUserPhone.getText().toString();
                String articleDate = etManageArticleDate.getText().toString();
                showArticleByUserPhone(getArticleByUserPhone(userPhone, articleDate));
            }
        });

        view.findViewById(R.id.btUsManageCancel)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsManageUserPhone.setText("");
                etManageArticleDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
            }
        });


        rcvArticleManagement = view.findViewById(R.id.id_rcvArticleManagement);
        rcvArticleManagement.setLayoutManager(new LinearLayoutManager(activity));
//        rcvArticleManagement.setLayoutManager(
//                new StaggeredGridLayoutManager(1,
//                        StaggeredGridLayoutManager.HORIZONTAL));

//        rcvMyArticleIsMy = view.findViewById(R.id.rcvMyArticleIsMe);
//        rcvMyArticleIsMy.setLayoutManager(
//                new StaggeredGridLayoutManager(1,
//                        StaggeredGridLayoutManager.HORIZONTAL));

//        rcvMyComment = view.findViewById(R.id.rcvMyComment);
//        rcvMyComment.setLayoutManager(
//                new StaggeredGridLayoutManager(1,
//                        StaggeredGridLayoutManager.HORIZONTAL));

//        myArticleList = getMyArticleCollect();
//        Log.d("TAG","myArticleList: " + myArticleList);
//        showMyArticle(myArticleList, getMyArticleIsMe(),getMyArticleMyComment());

//        String userPhone = "0900123456";
//        showArticleByUserPhone(getArticleByUserPhone(userPhone));


    }

//    private int getUserId() { return Common.USER_ID; }

    private void setDatePicker() {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        int year = calendar.get(Calendar.YEAR) - 12; // 年份預設顯示減12年
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (!etManageArticleDate.getText().equals("")
                && !etManageArticleDate.getText().equals(getResources().getString(R.string.textArticleDate))) {

            //欲轉換的日期字串
            String dateString = etManageArticleDate.getText().toString();
            //設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //進行轉換
            Date dateBirth = new Date();
            try {
                dateBirth = sdf.parse(dateString);
            } catch (ParseException e) {
                Log.d(TAG,"DateParse: Exeception");
                e.printStackTrace();
            }
            calendar.setTime(dateBirth);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        datePickerDialog = new DatePickerDialog(activity, DatePickerDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();


        Calendar calendarMin = Calendar.getInstance();
        calendarMin.add(Calendar.YEAR, -100); // 設定可選取的起始日為前130年
        datePicker.setMinDate(calendarMin.getTimeInMillis());

        Calendar calendarMax = Calendar.getInstance();
//        calendarMax.add(Calendar.MONTH, -1); // 設定可選取的結束日為一個月前
        datePicker.setMaxDate(calendarMax.getTimeInMillis());
    }


    // 自訂的內部類別(MyArticleAdapter) 繼承 RecyclerView.Adapter
    // 並同時自己建立 MyViewHolder 繼承 RecyclerView.ViewHolder 用來放要呈現在 ItemView 裡物件的資料
    private class MyArticleAdapter extends RecyclerView.Adapter<ArticleManagementFragment.MyArticleAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater; // 放外面傳進來的 Context / Activity
        private List<MyArticle> myArticleListInAdp; // 放外面傳進來的 物件List
        private Integer imageSize;

        // 自訂的內部類別(MyArticleAdapter)的建構子，提供外面傳入 Context 跟要呈現資料的List 用
        MyArticleAdapter (Context context, List<MyArticle> myArticleListAdp) {
            layoutInflater = LayoutInflater.from(context);
            this.myArticleListInAdp = myArticleListAdp;
            this.imageSize = getResources().getDisplayMetrics().widthPixels / 4; // 取得螢幕寬度當圖片尺寸的基準
        }

        // 自訂內部類別(MyArticleAdapter)的方法(setMyArticleListAdp)，接外面傳進來的 物件List
        public void setMyArticleListAdp (List<MyArticle> myArticleListAdp) {
            this.myArticleListInAdp = myArticleListAdp;
        }

        // 自訂內部類別(MyViewHolder) 繼承 RecyclerView.ViewHolder，
        // 用來初始化在Item View裡呈現資料的元件，之後給 onBindViewHolder 呼叫並設定資料用
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imMyAtResImg;
            TextView tvMyAtArticleTitle;
            TextView tvMyAtArticleTime;
            TextView tvMyAtUserName;
            TextView tvMyAtArticleText;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imMyAtResImg = itemView.findViewById(R.id.imMyAtResImg);
                tvMyAtArticleTitle = itemView.findViewById(R.id.tvMyAtArticleTitle);
                tvMyAtArticleTime = itemView.findViewById(R.id.tvMyAtArticleTime);
                tvMyAtUserName = itemView.findViewById(R.id.tvMyAtUserName);
                tvMyAtArticleText = itemView.findViewById(R.id.tvMyAtArticleText);
            }
        }

        @NonNull
        @Override
        public ArticleManagementFragment.MyArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 載入自己建立在 res\layout 裡的 Item view XML檔
            View myItemView = layoutInflater
                    .inflate(R.layout.item_view_article_management,parent,false);
            // 把 layout檔的view，傳入RecyclerView的ViewHolder(MyViewHolder)
            return new ArticleManagementFragment.MyArticleAdapter.MyViewHolder(myItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleManagementFragment.MyArticleAdapter.MyViewHolder holder, int position) {
            MyArticle myArticleInBindViewHolder = myArticleListInAdp.get(position);
            Resources res = activity.getResources();
            holder.tvMyAtArticleTitle.setText(myArticleInBindViewHolder.getArticleTitle());

            if ( myArticleInBindViewHolder.getCommentId() == 0) {

                String strAtArticleTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(myArticleInBindViewHolder.getArticleTime());
                holder.tvMyAtArticleTime.setText(res.getString(R.string.textArticleDate) + strAtArticleTime);
                holder.tvMyAtUserName.setText(res.getString(R.string.textArticleWirter) + myArticleInBindViewHolder.getUserName());
                holder.tvMyAtArticleText.setText(res.getString(R.string.textArticleText) + myArticleInBindViewHolder.getArticleText());
            } else {
                String strCommentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(myArticleInBindViewHolder.getCommentTime());
                holder.tvMyAtArticleTime.setText(res.getString(R.string.textCommentDate) + strCommentTime);
                holder.tvMyAtUserName.setText(res.getString(R.string.textArticleWirter) + myArticleInBindViewHolder.getUserName());
                holder.tvMyAtArticleText.setText(res.getString(R.string.textCommentText) + myArticleInBindViewHolder.getCommentText());
            }

//            holder.imMyAtResImg.setImageBitmap();
            final Integer articleId = myArticleInBindViewHolder.getArticleId(); // 文章ID
            Log.d(TAG,"articleId: " + articleId);
            Log.d(TAG,"MYARTICLE_SERVLET: " + MYARTICLE_SERVLET);
            UserImageTask userImageTask = new UserImageTask(MYARTICLE_SERVLET, articleId, imageSize, holder.imMyAtResImg);
            userImageTask.execute(); // .execute() => UserImage.doInBackground
            imageTasks.add(userImageTask);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 從“我的社群活動”頁面，跳轉到“討論區”頁面並轉到文章detail頁面
                    Article.ARTICLE_ID = articleId;
                    Article.USER_ID = myArticleInBindViewHolder.getUserId();
                    MyArticle.goToMyArticleDetail = true;
                    navController.navigate(R.id.articleFragment);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (myArticleListInAdp == null) {
                return 0;
            } else {
                return myArticleListInAdp.size();
            }
        }
    }


    // 會員發文管理
    private List<MyArticle> getArticleByUserPhone(String userPhone, String articleDate){
        List<MyArticle> getArticleByUserPhoneList = null;

        if (Common.networkConnected(activity)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getArticleByUserPhone");
            jsonObject.addProperty("userPhone", userPhone);
            jsonObject.addProperty("articleDate", articleDate);
            String jsonOut = jsonObject.toString();
            CommonTask getAllTask;
            getAllTask = new CommonTask(MYARTICLE_SERVLET, jsonOut);
            try {
                String jsonIn = getAllTask.execute().get();
                Type listType = new TypeToken<List<MyArticle>>() {
                }.getType();
                getArticleByUserPhoneList = new Gson().fromJson(jsonIn, listType);
                Log.d(TAG,"getArticleByUserPhoneList: " + getArticleByUserPhoneList);
            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

        } else {
            Common.showToast(activity,"NetworkConnected: fail");
        }

        return getArticleByUserPhoneList;
    }


    private void showArticleByUserPhone(List<MyArticle> showArticleByPhoneList) {

        ArticleManagementFragment.MyArticleAdapter myArticleAdapter
                = (ArticleManagementFragment.MyArticleAdapter) rcvArticleManagement.getAdapter();
//        Log.d(TAG,"myArticleAdapter: " + myArticleAdapter);
        if (myArticleAdapter == null){
//            Log.d(TAG,"showArticleByPhoneList: " + showArticleByPhoneList);
            rcvArticleManagement.setAdapter(new ArticleManagementFragment.MyArticleAdapter(activity, showArticleByPhoneList));
        } else {
//            Log.d(TAG,"showArticleByPhoneList: " + showArticleByPhoneList);
            myArticleAdapter.setMyArticleListAdp(showArticleByPhoneList);
            myArticleAdapter.notifyDataSetChanged();
        }

//        // 我的發文
//        UserMyArticleFragment.MyArticleAdapter myArticleAdapterIsMe
//                = (UserMyArticleFragment.MyArticleAdapter) rcvMyArticleIsMy.getAdapter();
////        Log.d(TAG,"rcvMyArticleIsMy: " + myArticleAdapterIsMy);
//        if (myArticleAdapterIsMe == null){
////            Log.d(TAG,"showMyArticleIsMeList: " + showMyArticleIsMeList);
//            rcvMyArticleIsMy.setAdapter(new UserMyArticleFragment.MyArticleAdapter(activity, showMyArticleIsMeList));
//        }else{
////            Log.d(TAG,"showMyArticleIsMeList: " + showMyArticleIsMeList);
//            myArticleAdapterIsMe.setMyArticleListAdp(showMyArticleIsMeList);
//            myArticleAdapterIsMe.notifyDataSetChanged();
//        }

//        // 我的回文
//        UserMyArticleFragment.MyArticleAdapter myArticleAdapterComment
//                = (UserMyArticleFragment.MyArticleAdapter) rcvMyComment.getAdapter();
////        Log.d(TAG,"rcvMyArticleIsMy: " + myArticleAdapterIsMy);
//        if (myArticleAdapterComment == null){
////            Log.d(TAG,"showMyArticleMyComment: " + showMyArticleMyComment);
//            rcvMyComment.setAdapter(new UserMyArticleFragment.MyArticleAdapter(activity, showMyArticleMyComment));
//        }else{
////            Log.d(TAG,"showMyArticleMyComment: " + showMyArticleMyComment);
//            myArticleAdapterComment.setMyArticleListAdp(showMyArticleMyComment);
//            myArticleAdapterComment.notifyDataSetChanged();
//        }

    }

}
