package com.example.petshop.models;

import java.io.Serializable;

public class TinTuc implements Serializable {
    private long id;
    private String tieuDe;
    private String moTaNgan;
    private String moTaChiTiet;
    private int hinhAnhRes;

    public TinTuc(long id, String tieuDe, String moTaNgan, String moTaChiTiet, int hinhAnhRes) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.moTaNgan = moTaNgan;
        this.moTaChiTiet = moTaChiTiet;
        this.hinhAnhRes = hinhAnhRes;
    }

    public long getId() { return id; }
    public String getTieuDe() { return tieuDe; }
    public String getMoTaNgan() { return moTaNgan; }
    public String getMoTaChiTiet() { return moTaChiTiet; }
    public int getHinhAnhRes() { return hinhAnhRes; }
}
