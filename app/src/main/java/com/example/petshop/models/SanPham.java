package com.example.petshop.models;

import java.io.Serializable;

public class SanPham implements Serializable {
    private long id;
    private String tenSP;
    private double giaBan;
    private int soLuongTon;
    private String kichCo;     // e.g. "S, M, L, XL" or "500g, 1kg"
    private String mauSac;     // e.g. "Đỏ, Xanh, Vàng"
    private String maVach;     // Barcode e.g. "8935001"
    private long maDM;
    private String tenDM;
    private String loaiThuCung; // "Chó", "Mèo", "Chim", "Cá"
    private String moTa;
    private String hinhAnh;
    private int mucCanhBao;    // Low stock warning threshold

    public SanPham() {
        this.mucCanhBao = 5;
    }

    public SanPham(long id, String tenSP, double giaBan, int soLuongTon, String kichCo, String mauSac,
                   String maVach, long maDM, String loaiThuCung, String moTa, String hinhAnh, int mucCanhBao) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.maVach = maVach;
        this.maDM = maDM;
        this.loaiThuCung = loaiThuCung;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.mucCanhBao = mucCanhBao;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public String getKichCo() { return kichCo; }
    public void setKichCo(String kichCo) { this.kichCo = kichCo; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public String getMaVach() { return maVach; }
    public void setMaVach(String maVach) { this.maVach = maVach; }

    public long getMaDM() { return maDM; }
    public void setMaDM(long maDM) { this.maDM = maDM; }

    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }
    public String getTenDanhMuc() { return tenDM != null ? tenDM : ""; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDM = tenDanhMuc; }

    public String getLoaiThuCung() { return loaiThuCung; }
    public void setLoaiThuCung(String loaiThuCung) { this.loaiThuCung = loaiThuCung; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getMucCanhBao() { return mucCanhBao; }
    public void setMucCanhBao(int mucCanhBao) { this.mucCanhBao = mucCanhBao; }

    public boolean isSapHetHang() {
        return soLuongTon <= mucCanhBao;
    }

    public String[] getDanhSachSize() {
        if (kichCo == null || kichCo.trim().isEmpty()) {
            return new String[]{"Tiêu chuẩn"};
        }
        return kichCo.split(",");
    }

    public String[] getDanhSachMau() {
        if (mauSac == null || mauSac.trim().isEmpty()) {
            return new String[]{"Mặc định"};
        }
        return mauSac.split(",");
    }
}
