package com.example.myapplication.Models;

public class Posts {

    private String postKey;
    private String userName;
    private String date;
    private String time;
    private String description;
    private String downloadLink;
    private String userPhoto;



    public Posts() {
        super();
    }

    public Posts(String userName, String date, String time, String description, String downloadLink, String userPhoto) {
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.description = description;
        this.downloadLink = downloadLink;
        this.userPhoto = userPhoto;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
