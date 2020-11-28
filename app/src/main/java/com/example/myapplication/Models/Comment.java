package com.example.myapplication.Models;

public class Comment {

    private String userPhoto;
    private String userName;
    private String comment;
    private String time;

    public Comment(String userPhoto, String userName, String comment, String time) {
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.comment = comment;
        this.time = time;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
