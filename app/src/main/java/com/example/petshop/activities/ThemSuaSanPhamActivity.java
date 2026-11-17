package com.example.petshop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.DanhMuc;
import com.example.petshop.models.SanPham;

import java.util.List;

public class ThemSuaSanPhamActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private long sanPhamId = -1;
    private SanPham currentSanPham;

    private TextView tvTieuDe;
    private ImageView btnXoa;
    private EditText etTenSP;
    private EditText etGiaBan;
    private EditText etSoLuongTon;
    private EditText etMaVach;
    private Spinner spnLoai;
    private EditText etMucCanhBao;
    private EditText etKichCo;
    private EditText etMauSac;
    private EditText etMoTa;

    private final String[] speciesArray = {"Chó", "Mèo", "Chim", "Cá"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_san_pham);

        dbHelper = DatabaseHelper.getInstance(this);

        if (getIntent().hasExtra("EXTRA_SAN_PHAM_ID")) {
            sanPhamId = getIntent().getLongExtra("EXTRA_SAN_PHAM_ID", -1);
            currentSanPham = dbHelper.getSanPhamById(sanPhamId);
        }

        initViews();
        if (currentSanPham != null) {
            populateData();
        }
    }

    private void initViews() {
        tvTieuDe = findViewById(R.id.tvTieuDeThemSP);
        btnXoa = findViewById(R.id.btnXoaSP);
        etTenSP = findViewById(R.id.etTenSP);
        etGiaBan = findViewById(R.id.etGiaBan);
        etSoLuongTon = findViewById(R.id.etSoLuongTon);
        etMaVach = findViewById(R.id.etMaVach);
        spnLoai = findViewById(R.id.spnLoai);
        etMucCanhBao = findViewById(R.id.etMucCanhBao);
        etKichCo = findViewById(R.id.etKichCo);
        etMauSac = findViewById(R.id.etMauSac);
        etMoTa = findViewById(R.id.etMoTa);

        findViewById(R.id.btnBackThemSP).setOnClickListener(v -> finish());

        // Setup Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speciesArray);
        spnLoai.setAdapter(spinnerAdapter);



        // Save button
        findViewById(R.id.btnLuuSP).setOnClickListener(v -> saveProduct());

        if (sanPhamId > 0) {
            tvTieuDe.setText("Sửa thông tin sản phẩm");
            btnXoa.setVisibility(View.VISIBLE);
            btnXoa.setOnClickListener(v -> confirmDelete());
        } else {
            tvTieuDe.setText("Thêm sản phẩm mới");
            btnXoa.setVisibility(View.GONE);
        }
    }

    private void populateData() {
        etTenSP.setText(currentSanPham.getTenSP());
        etGiaBan.setText(String.format("%.0f", currentSanPham.getGiaBan()));
        etSoLuongTon.setText(String.valueOf(currentSanPham.getSoLuongTon()));
        etMaVach.setText(currentSanPham.getMaVach());
        etMucCanhBao.setText(String.valueOf(currentSanPham.getMucCanhBao()));
        etKichCo.setText(currentSanPham.getKichCo());
        etMauSac.setText(currentSanPham.getMauSac());
        etMoTa.setText(currentSanPham.getMoTa());

        for (int i = 0; i < speciesArray.length; i++) {
            if (speciesArray[i].equalsIgnoreCase(currentSanPham.getLoaiThuCung())) {
                spnLoai.setSelection(i);
                break;
            }
        }
    }

    private void saveProduct() {
        String ten = etTenSP.getText().toString().trim();
        if (ten.isEmpty()) {
            etTenSP.setError("Vui lòng nhập tên sản phẩm!");
            etTenSP.requestFocus();
            return;
        }

        String strGia = etGiaBan.getText().toString().trim();
        double gia = 0;
        try {
            gia = Double.parseDouble(strGia);
        } catch (Exception e) {
            etGiaBan.setError("Giá bán không hợp lệ!");
            etGiaBan.requestFocus();
            return;
        }

        String strSL = etSoLuongTon.getText().toString().trim();
        int sl = 0;
        try {
            sl = Integer.parseInt(strSL);
        } catch (Exception ignored) {}

        String maVach = etMaVach.getText().toString().trim();
        if (maVach.isEmpty()) {
            maVach = "893" + System.currentTimeMillis() % 100000;
        }

        String loai = spnLoai.getSelectedItem().toString();
        int canhBao = 5;
        try {
            canhBao = Integer.parseInt(etMucCanhBao.getText().toString().trim());
        } catch (Exception ignored) {}

        String kichCo = etKichCo.getText().toString().trim();
        String mauSac = etMauSac.getText().toString().trim();
        String moTa = etMoTa.getText().toString().trim();

        // Find matching category ID
        long maDM = 1;
        List<DanhMuc> dms = dbHelper.getAllDanhMuc();
        for (DanhMuc dm : dms) {
            if (dm.getTenDM().equalsIgnoreCase(loai)) {
                maDM = dm.getId();
                break;
            }
        }

        if (currentSanPham == null) {
            SanPham sp = new SanPham();
            sp.setTenSP(ten);
            sp.setGiaBan(gia);
            sp.setSoLuongTon(sl);
            sp.setMaVach(maVach);
            sp.setLoaiThuCung(loai);
            sp.setMaDM(maDM);
            sp.setMucCanhBao(canhBao);
            sp.setKichCo(kichCo);
            sp.setMauSac(mauSac);
            sp.setMoTa(moTa);
            sp.setHinhAnh("sp_default");

            long id = dbHelper.insertSanPham(sp);
            if (id > 0) {
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi thêm!", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentSanPham.setTenSP(ten);
            currentSanPham.setGiaBan(gia);
            currentSanPham.setSoLuongTon(sl);
            currentSanPham.setMaVach(maVach);
            currentSanPham.setLoaiThuCung(loai);
            currentSanPham.setMaDM(maDM);
            currentSanPham.setMucCanhBao(canhBao);
            currentSanPham.setKichCo(kichCo);
            currentSanPham.setMauSac(mauSac);
            currentSanPham.setMoTa(moTa);

            int rows = dbHelper.updateSanPham(currentSanPham);
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi cập nhật!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm " + currentSanPham.getTenSP() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteSanPham(currentSanPham.getId());
                    Toast.makeText(this, "Đã xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


}
