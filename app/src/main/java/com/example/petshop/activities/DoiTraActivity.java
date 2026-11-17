package com.example.petshop.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.DoiTraAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.DonHang;
import com.example.petshop.models.PhieuDoiTra;
import com.example.petshop.models.SanPham;

import java.util.ArrayList;
import java.util.List;

public class DoiTraActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private Spinner spnDonHang;
    private Spinner spnSanPham;
    private EditText etSoLuong;
    private EditText etSoTienHoan;
    private EditText etLyDo;
    private RecyclerView rvLichSu;

    private List<DonHang> allOrders = new ArrayList<>();
    private List<SanPham> allProducts = new ArrayList<>();
    private DoiTraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_tra);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        loadSpinners();
        loadDoiTraHistory();
    }

    private void initViews() {
        spnDonHang = findViewById(R.id.spnDonHangDoiTra);
        spnSanPham = findViewById(R.id.spnSanPhamDoiTra);
        etSoLuong = findViewById(R.id.etSoLuongDoiTra);
        etSoTienHoan = findViewById(R.id.etSoTienHoan);
        etLyDo = findViewById(R.id.etLyDoDoiTra);
        rvLichSu = findViewById(R.id.rvLichSuDoiTra);

        rvLichSu.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackDoiTra).setOnClickListener(v -> finish());

        findViewById(R.id.btnXacNhanDoiTra).setOnClickListener(v -> submitReturnRequest());
    }

    private void loadSpinners() {
        allOrders = dbHelper.getAllDonHang();
        List<String> orderLabels = new ArrayList<>();
        for (DonHang dh : allOrders) {
            orderLabels.add(dh.getMaDonHangHienThi() + " - " + dh.getTenKhachHang());
        }
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, orderLabels);
        spnDonHang.setAdapter(orderAdapter);

        allProducts = dbHelper.getAllSanPham();
        List<String> productLabels = new ArrayList<>();
        for (SanPham sp : allProducts) {
            productLabels.add(sp.getTenSP() + " (" + sp.getLoaiThuCung() + ")");
        }
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productLabels);
        spnSanPham.setAdapter(productAdapter);
    }

    private void loadDoiTraHistory() {
        List<PhieuDoiTra> list = dbHelper.getAllPhieuDoiTra();
        adapter = new DoiTraAdapter(this, list);
        rvLichSu.setAdapter(adapter);
    }

    private void submitReturnRequest() {
        if (allOrders.isEmpty() || allProducts.isEmpty()) {
            Toast.makeText(this, "Chưa có đủ dữ liệu đơn hàng hoặc sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        DonHang selectedDH = allOrders.get(spnDonHang.getSelectedItemPosition());
        SanPham selectedSP = allProducts.get(spnSanPham.getSelectedItemPosition());

        String strSL = etSoLuong.getText().toString().trim();
        int sl = 1;
        try {
            sl = Integer.parseInt(strSL);
        } catch (Exception ignored) {}

        String strTien = etSoTienHoan.getText().toString().trim();
        double tien = selectedSP.getGiaBan() * sl;
        try {
            if (!strTien.isEmpty()) tien = Double.parseDouble(strTien);
        } catch (Exception ignored) {}

        String lyDo = etLyDo.getText().toString().trim();
        if (lyDo.isEmpty()) {
            lyDo = "Đổi size cún cưng / Lỗi sản phẩm";
        }

        long id = dbHelper.taoPhieuDoiTra(selectedDH.getId(), selectedSP.getId(), lyDo, sl, tien);
        if (id > 0) {
            Toast.makeText(this, "Đã ghi nhận đổi trả & Hoàn " + sl + " sản phẩm vào kho!", Toast.LENGTH_LONG).show();
            etSoLuong.setText("1");
            etSoTienHoan.setText("");
            etLyDo.setText("");
            loadDoiTraHistory();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi tạo phiếu đổi trả!", Toast.LENGTH_SHORT).show();
        }
    }
}
