package com.example.petshop.models;

import java.io.Serializable;

public class ChiTietDonHang implements Serializable {
    private long id;
    private long maDH;
    private long maSP;
    private String tenSP;
    private String hinhAnh;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String sizeDaChon;
    private String mauDaChon;

    public ChiTietDonHang() {}

    public ChiTietDonHang(long id, long maDH, long maSP, String tenSP, int soLuong,
                         double donGia, double thanhTien, String sizeDaChon, String mauDaChon) {
        this.id = id;
        this.maDH = maDH;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.sizeDaChon = sizeDaChon;
        this.mauDaChon = mauDaChon;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getMaDH() { return maDH; }
    public void setMaDH(long maDH) { this.maDH = maDH; }

    public long getMaSP() { return maSP; }
    public void setMaSP(long maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    public String getSizeDaChon() { return sizeDaChon; }
    public void setSizeDaChon(String sizeDaChon) { this.sizeDaChon = sizeDaChon; }

    public String getMauDaChon() { return mauDaChon; }
    public void setMauDaChon(String mauDaChon) { this.mauDaChon = mauDaChon; }

    public String getPhanLoaiHienThi() {
        StringBuilder sb = new StringBuilder();
        if (sizeDaChon != null && !sizeDaChon.isEmpty()) {
            sb.append("Size: ").append(sizeDaChon);
        }
        if (mauDaChon != null && !mauDaChon.isEmpty()) {
            if (sb.length() > 0) sb.append(" • ");
            sb.append("Màu: ").append(mauDaChon);
        }
        return sb.toString();
    }
}
