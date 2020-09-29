package com.example.foodradar_android.article;

public class Img {

    private int imgId;
    private int articleId;

    public Img() {
        super();
    }


    public Img(int imgId, int articleId) {
        super();
        this.imgId = imgId;
        this.articleId = articleId;
    }



    public int getImgId() {
        return imgId;
    }



    public void setImgId(int imgId) {
        this.imgId = imgId;
    }



    public int getArticleId() {
        return articleId;
    }



    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

}
