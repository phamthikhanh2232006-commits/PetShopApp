package com.example.petshop.models;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    private long id;
    private String tenDM;
    private String moTa;
    private String iconName;

    public DanhMuc() {}

    public DanhMuc(long id, String tenDM, String moTa, String iconName) {
        this.id = id;
        this.tenDM = tenDM;
        this.moTa = moTa;
        this.iconName = iconName;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    @Override
    public String toString() {
        return tenDM;
    }
}
