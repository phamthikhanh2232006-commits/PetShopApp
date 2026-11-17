package com.example.petshop.models;

import java.io.Serializable;

public class PhieuNhap implements Serializable {
    private long id;
    private String ngayNhap;
    private double tongTienNhap;
    private String nhaCungCap;
    private String nguoiNhap;
    private String ghiChu;
    private int tongSoLuong;

    public PhieuNhap() {}

    public PhieuNhap(long id, String ngayNhap, double tongTienNhap, String nhaCungCap, String nguoiNhap, String ghiChu) {
        this.id = id;
        this.ngayNhap = ngayNhap;
        this.tongTienNhap = tongTienNhap;
        this.nhaCungCap = nhaCungCap;
        this.nguoiNhap = nguoiNhap;
        this.ghiChu = ghiChu;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(String ngayNhap) { this.ngayNhap = ngayNhap; }

    public double getTongTienNhap() { return tongTienNhap; }
    public void setTongTienNhap(double tongTienNhap) { this.tongTienNhap = tongTienNhap; }

    public String getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(String nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public String getNguoiNhap() { return nguoiNhap; }
    public void setNguoiNhap(String nguoiNhap) { this.nguoiNhap = nguoiNhap; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public int getTongSoLuong() { return tongSoLuong; }
    public void setTongSoLuong(int tongSoLuong) { this.tongSoLuong = tongSoLuong; }
}
