package com.example.petshop.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonHang implements Serializable {
    private long id;
    private long maKH;
    private String tenKhachHang;
    private String soDienThoai;
    private String diaChiGiaoHang;
    private String ngayDat;
    private double tongTien;
    private double giamGia;
    private String phuongThucTT; // "COD", "BankTransfer", "EWallet"
    private String trangThai;   // "ChoXuLy", "DaThanhToan", "DangGiao", "HoanThanh", "DaHuy", "DoiTra"
    private String ghiChu;
    private int soLuongSP;

    private List<ChiTietDonHang> danhSachChiTiet = new ArrayList<>();

    public DonHang() {
        this.trangThai = "ChoXuLy";
        this.phuongThucTT = "COD";
    }

    public DonHang(long id, long maKH, String ngayDat, double tongTien, double giamGia,
                   String phuongThucTT, String trangThai, String diaChiGiaoHang, String ghiChu) {
        this.id = id;
        this.maKH = maKH;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        this.phuongThucTT = phuongThucTT;
        this.trangThai = trangThai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.ghiChu = ghiChu;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getMaKH() { return maKH; }
    public void setMaKH(long maKH) { this.maKH = maKH; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChiGiaoHang() { return diaChiGiaoHang; }
    public void setDiaChiGiaoHang(String diaChiGiaoHang) { this.diaChiGiaoHang = diaChiGiaoHang; }

    public String getNgayDat() { return ngayDat; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public String getPhuongThucTT() { return phuongThucTT; }
    public void setPhuongThucTT(String phuongThucTT) { this.phuongThucTT = phuongThucTT; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public int getSoLuongSP() { return soLuongSP; }
    public void setSoLuongSP(int soLuongSP) { this.soLuongSP = soLuongSP; }

    public List<ChiTietDonHang> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }

    public String getMaDonHangHienThi() {
        return "#DH" + String.format("%06d", id);
    }

    public String getTenTrangThaiTiengViet() {
        if ("ChoXuLy".equalsIgnoreCase(trangThai)) return "Chờ xử lý";
        if ("DaThanhToan".equalsIgnoreCase(trangThai)) return "Đã thanh toán";
        if ("DangGiao".equalsIgnoreCase(trangThai)) return "Đang giao";
        if ("HoanThanh".equalsIgnoreCase(trangThai)) return "Hoàn thành";
        if ("DoiTra".equalsIgnoreCase(trangThai)) return "Đổi trả";
        if ("DaHuy".equalsIgnoreCase(trangThai)) return "Đã hủy";
        return trangThai;
    }
}
