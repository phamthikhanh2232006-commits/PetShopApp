package com.example.petshop.models;

import java.io.Serializable;

public class ThuCung implements Serializable {
    private long id;
    private long maKH;
    private String tenKhachHang;
    private String tenTC;
    private String loaiThuCung; // "Chó", "Mèo", "Chim", "Cá", "Khác"
    private String giongLoai;   // "Poodle", "Corgi", "Mèo Anh lông ngắn"...
    private String kichCo;      // "Nhỏ", "Vừa", "Lớn"
    private double canNang;     // kg
    private String gioiTinh;    // "Đực", "Cái"
    private String ngaySinh;    // yyyy-MM-dd or dd/MM/yyyy
    private String ghiChu;
    private String hinhAnh;

    public ThuCung() {}

    public ThuCung(long id, long maKH, String tenTC, String loaiThuCung, String giongLoai,
                   String kichCo, double canNang, String gioiTinh, String ngaySinh, String ghiChu) {
        this.id = id;
        this.maKH = maKH;
        this.tenTC = tenTC;
        this.loaiThuCung = loaiThuCung;
        this.giongLoai = giongLoai;
        this.kichCo = kichCo;
        this.canNang = canNang;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.ghiChu = ghiChu;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getMaKH() { return maKH; }
    public void setMaKH(long maKH) { this.maKH = maKH; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getTenTC() { return tenTC; }
    public void setTenTC(String tenTC) { this.tenTC = tenTC; }

    public String getLoaiThuCung() { return loaiThuCung; }
    public void setLoaiThuCung(String loaiThuCung) { this.loaiThuCung = loaiThuCung; }

    public String getGiongLoai() { return giongLoai; }
    public void setGiongLoai(String giongLoai) { this.giongLoai = giongLoai; }

    public String getKichCo() { return kichCo; }
    public void setKichCo(String kichCo) { this.kichCo = kichCo; }

    public double getCanNang() { return canNang; }
    public void setCanNang(double canNang) { this.canNang = canNang; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getMoTaNgan() {
        return loaiThuCung + " • " + giongLoai + " • " + canNang + " kg";
    }
}
