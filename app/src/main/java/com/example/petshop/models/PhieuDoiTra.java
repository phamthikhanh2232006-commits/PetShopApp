package com.example.petshop.models;

import java.io.Serializable;

public class PhieuDoiTra implements Serializable {
    private long id;
    private long maDH;
    private long maSP;
    private String tenSP;
    private String lyDo;
    private int soLuong;
    private double soTienHoan;
    private String ngayDoiTra;
    private String trangThai; // "DaXuLy", "ChoXuLy"

    public PhieuDoiTra() {
        this.trangThai = "DaXuLy";
    }

    public PhieuDoiTra(long id, long maDH, long maSP, String tenSP, String lyDo, int soLuong, double soTienHoan, String ngayDoiTra, String trangThai) {
        this.id = id;
        this.maDH = maDH;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.lyDo = lyDo;
        this.soLuong = soLuong;
        this.soTienHoan = soTienHoan;
        this.ngayDoiTra = ngayDoiTra;
        this.trangThai = trangThai;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getMaDH() { return maDH; }
    public void setMaDH(long maDH) { this.maDH = maDH; }

    public long getMaSP() { return maSP; }
    public void setMaSP(long maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getSoTienHoan() { return soTienHoan; }
    public void setSoTienHoan(double soTienHoan) { this.soTienHoan = soTienHoan; }

    public String getNgayDoiTra() { return ngayDoiTra; }
    public void setNgayDoiTra(String ngayDoiTra) { this.ngayDoiTra = ngayDoiTra; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
