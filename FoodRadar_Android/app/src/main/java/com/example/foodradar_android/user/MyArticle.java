package com.example.foodradar_android.user;

import java.sql.Timestamp;

// Date Time: 2020-10-14 13:31:20
// table name: MyArticle
public class MyArticle {
    private int myArticleId;
    private int userId;
    private int articleId;
    private Timestamp modifyDate;

    //	private int articleId;
    private String articleTitle;
    private Timestamp articleTime;
    private String userName;

    public MyArticle(int myArticleId, int userId, int articleId, Timestamp modifyDate) {
        super();
        this.myArticleId = myArticleId;
        this.userId = userId;
        this.articleId = articleId;
        this.modifyDate = modifyDate;
    }

    // getAllById
    public MyArticle(int articleId, String articleTitle, Timestamp articleTime, String userName) {
        super();
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleTime = articleTime;
        this.userName = userName;
    }

    public int getMyArticleId() {
        return myArticleId;
    }
    public void setMyArticleId(int myArticleId) {
        this.myArticleId = myArticleId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getArticleId() {
        return articleId;
    }
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
}
