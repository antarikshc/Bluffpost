package com.antarikshc.theguardiannews;

import android.graphics.Bitmap;

public class NewsData {

    private String title;
    private String author;
    private String category;
    private Long timeInMillis;
    private String webUrl;
    private String imgUrl;
    private Bitmap image;

    NewsData(String title, String author, String category, Long timeInMillis, String webUrl, String imgUrl, Bitmap image) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.timeInMillis = timeInMillis;
        this.webUrl = webUrl;
        this.imgUrl = imgUrl;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
