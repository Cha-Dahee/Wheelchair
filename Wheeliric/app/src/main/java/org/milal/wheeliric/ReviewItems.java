package org.milal.wheeliric;

/**
 * Created by ekgml on 2017-08-14.
 * DB의 리뷰 정보를 저장하는 class입니다.
 */

public class ReviewItems {
    private int photoURL;
    private String nick;
    private String date;
    private int toilet;
    private int entrance;
    private int park;
    private int table;
    private String grade;
    private String comment;
    private int Like;
    private int LikeNum;

    public ReviewItems(int photoURL, String nick, String date, int toilet, int entrance, int park, int table, String grade, String comment, int like, int likeNum) {
        this.photoURL = photoURL;
        this.nick = nick;
        this.date = date;
        this.toilet = toilet;
        this.entrance = entrance;
        this.park = park;
        this.table = table;
        this.grade = grade;
        this.comment = comment;
        Like = like;
        LikeNum = likeNum;
    }

    public int getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(int photoURL) {
        this.photoURL = photoURL;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getToilet() {
        return toilet;
    }

    public void setToilet(int toilet) {
        this.toilet = toilet;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    public int getPark() {
        return park;
    }

    public void setPark(int park) {
        this.park = park;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLike() {
        return Like;
    }

    public void setLike(int like) {
        Like = like;
    }

    public int getLikeNum() {
        return LikeNum;
    }

    public void setLikeNum(int likeNum) {
        LikeNum = likeNum;
    }
}
