package com.example.petshop.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    private SanPham sanPham;
    private int soLuong;
    private String selectedSize;
    private String selectedColor;

    public CartItem(SanPham sanPham, int soLuong, String selectedSize, String selectedColor) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.selectedSize = selectedSize;
        this.selectedColor = selectedColor;
    }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }

    public String getSelectedColor() { return selectedColor; }
    public void setSelectedColor(String selectedColor) { this.selectedColor = selectedColor; }

    public double getThanhTien() {
        if (sanPham != null) {
            return sanPham.getGiaBan() * soLuong;
        }
        return 0;
    }

    public String getPhanLoaiHienThi() {
        StringBuilder sb = new StringBuilder();
        if (selectedSize != null && !selectedSize.isEmpty()) {
            sb.append("Size: ").append(selectedSize);
        }
        if (selectedColor != null && !selectedColor.isEmpty()) {
            if (sb.length() > 0) sb.append(" • ");
            sb.append("Màu: ").append(selectedColor);
        }
        return sb.toString();
    }
}
