package com.example.petshop.models;

import java.io.Serializable;

public class KhachHang implements Serializable {
    private long id;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String ngayTao;

    public KhachHang() {}

    public KhachHang(long id, String hoTen, String soDienThoai, String email, String diaChi) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    @Override
    public String toString() {
        return hoTen + " (" + soDienThoai + ")";
    }
}
