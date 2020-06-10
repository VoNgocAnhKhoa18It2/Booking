package com.vnakhoa.hotel.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Phong implements Serializable {
    private String idPhong;
    private String tenPhong;
    private int gia;
    private String urlImg;
    private Bitmap img;
    private String nDung;
    private String nDungGT;

    public Phong(String idPhong, String tenPhong, int gia, String urlImg, Bitmap img, String nDung, String nDungGT) {
        this.idPhong = idPhong;
        this.tenPhong = tenPhong;
        this.gia = gia;
        this.urlImg = urlImg;
        this.img = img;
        this.nDung = nDung;
        this.nDungGT = nDungGT;
    }

    public String getnDungGT() {
        return nDungGT;
    }

    public void setnDungGT(String nDungGT) {
        this.nDungGT = nDungGT;
    }

    public String getnDung() {
        return nDung;
    }

    public void setnDung(String nDung) {
        this.nDung = nDung;
    }

    public Phong() {
    }

    public String getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(String idPhong) {
        this.idPhong = idPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
