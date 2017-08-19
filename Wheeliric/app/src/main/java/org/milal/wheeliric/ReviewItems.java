package org.milal.wheeliric;

/**
 * Created by ekgml on 2017-08-14.
 * DB의 리뷰 정보를 저장하는 class입니다.
 */

public class ReviewItems {
    private int photoURL;
    private String nick;

    private String toilet;
    private String threshold;
    private String park;
    private String table;
    private int grade;
    private String comment;

    public ReviewItems(int photoURL, String nick, String toilet, String threshold, String park, String table, int grade, String comment){
        this.photoURL = photoURL;
        this.nick = nick;
        this.toilet = toilet;
        this.threshold = threshold;
        this.park = park;
        this.table = table;
        this.grade = grade;
        this. comment = comment;
    }
    public int getPhotoURL() {return photoURL;}

    public String getNick() {return nick;}

    public String getToilet() {return toilet;}

    public String getThreshold() {return threshold;}

    public String getPark() {return park;}

    public String getTable() {return table;}

    public String getComment() {return comment;}

    public int getGrade() {return grade;}

}
