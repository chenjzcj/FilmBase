package succ7.com.filmbase.bean.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 18:34
 * 电影实体类
 */
public class Film extends BmobObject implements Serializable {
    private String filmPicUrl;//电影主图
    private String filmName;//电影名称
    private User user;//电影贡献者
    private String filmSource;//电影来源,比如网盘,其他等
    private double filmLength;//电影长度
    private String unit;//单位,kb,mb,gb
    private String filmTypes;//电影类型
    private String filmArea;//电影地区
    private String filmYear;//电影年代
    private String filmActors;//电影主演
    private String filmAddress;//资源地址
    private String otherInfo;//提取码等其他信息
    private long likes;//给资源点赞的用户
    private long disLikes;//给资源点踩的用户

    public String getFilmPicUrl() {
        return filmPicUrl;
    }

    public void setFilmPicUrl(String filmPicUrl) {
        this.filmPicUrl = filmPicUrl;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFilmSource() {
        return filmSource;
    }

    public void setFilmSource(String filmSource) {
        this.filmSource = filmSource;
    }

    public double getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(double filmLength) {
        this.filmLength = filmLength;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFilmTypes() {
        return filmTypes;
    }

    public void setFilmTypes(String filmTypes) {
        this.filmTypes = filmTypes;
    }

    public String getFilmArea() {
        return filmArea;
    }

    public void setFilmArea(String filmArea) {
        this.filmArea = filmArea;
    }

    public String getFilmYear() {
        return filmYear;
    }

    public void setFilmYear(String filmYear) {
        this.filmYear = filmYear;
    }

    public String getFilmActors() {
        return filmActors;
    }

    public void setFilmActors(String filmActors) {
        this.filmActors = filmActors;
    }

    public String getFilmAddress() {
        return filmAddress;
    }

    public void setFilmAddress(String filmAddress) {
        this.filmAddress = filmAddress;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(long disLikes) {
        this.disLikes = disLikes;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmPicUrl='" + filmPicUrl + '\'' +
                ", filmName='" + filmName + '\'' +
                ", user=" + user +
                ", filmSource='" + filmSource + '\'' +
                ", filmLength=" + filmLength +
                ", unit='" + unit + '\'' +
                ", filmTypes='" + filmTypes + '\'' +
                ", filmArea='" + filmArea + '\'' +
                ", filmYear='" + filmYear + '\'' +
                ", filmActors='" + filmActors + '\'' +
                ", filmAddress='" + filmAddress + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", likes=" + likes +
                ", disLikes=" + disLikes +
                '}';
    }
}
