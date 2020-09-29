package com.example.foodradar_android.article;

public class Img {

    private int imagId;
    private int articleId;

    public Img() {
        super();
    }


    public Img(int imagId, int articleId) {
        super();
        this.imagId = imagId;
        this.articleId = articleId;
    }



    public int getImagId() {
        return imagId;
    }



    public void setImagId(int imagId) {
        this.imagId = imagId;
    }



    public int getArticleId() {
        return articleId;
    }



    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

}
