package com.example.foodradar_android.article;

import java.io.Serializable;

public class Article implements Serializable {

    private int articleId;
    private String articleTitle;
    private String articleTime ;
    private String articleText ;
    private String modifyTime ;
    private String resCategoryInfo;
    private int imgId;
    private int resId;
    private int userId;
    private String resName;
    private String userName;
    private int conAmount;
    private int conNum;
    private boolean articleStatus;
    private int goodCount;
    private int commentCount;
    private int favoriteCount;

    private int articleGoodStatus;

    private byte[] articleImg;

    private byte[] userIcon;



    public Article() {
        super();
    }

    //ArticleList頁面(新進榜，排行榜，收藏榜)
    public Article(String userName, String resCategoryInfo, String articleTime, String articleTitle, String articleText,
                   String resName, int goodCount, int commentCount, int favoriteCount) {
        super();
//        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleTime = articleTime;
        this.articleText = articleText;
        this.resCategoryInfo = resCategoryInfo;
        this.resName = resName;
        this.userName = userName;
        this.goodCount = goodCount;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public byte[] getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(byte[] userIcon) {
        this.userIcon = userIcon;
    }

    public String getResCategoryInfo() {
        return resCategoryInfo;
    }

    public void setResCategoryInfo(String resCategoryInfo) {
        this.resCategoryInfo = resCategoryInfo;
    }

    public byte[] getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(byte[] articleImg) {
        this.articleImg = articleImg;
    }

    public int getArticleId() {
        return articleId;
    }


    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }


    public String getArticleTitle() {
        return articleTitle;
    }


    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }


    public String getArticleTime() {
        return articleTime;
    }


    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }


    public String getArticleText() {
        return articleText;
    }


    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }


    public String getModifyTime() {
        return modifyTime;
    }


    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }


    public int getResId() {
        return resId;
    }


    public void setResId(int resId) {
        this.resId = resId;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getConAmount() {
        return conAmount;
    }


    public void setConAmount(int conAmount) {
        this.conAmount = conAmount;
    }


    public int getConNum() {
        return conNum;
    }


    public void setConNum(int conNum) {
        this.conNum = conNum;
    }


    public boolean isArticleStatus() {
        return articleStatus;
    }


    public void setArticleStatus(boolean articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getArticleGoodStatus() {
        return articleGoodStatus;
    }

    public void setArticleGoodStatus(int articleGoodStatus) {
        this.articleGoodStatus = articleGoodStatus;
    }


    @Override   //覆寫方法，取得articleId > 透過id 取得article
    public boolean equals(Object obj) {
        return this.articleId == ((Article) obj).articleId;
    }

}
