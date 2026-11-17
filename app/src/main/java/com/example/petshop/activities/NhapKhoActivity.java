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
import com.example.petshop.adapters.CanhBaoTonKhoAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class NhapKhoActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager session;

    private RecyclerView rvCanhBao;
    private Spinner spnSanPham;
    private EditText etSoLuongNhap;
    private EditText etGiaNhap;
    private EditText etNhaCungCap;
    private EditText etGhiChu;

    private List<SanPham> allProducts = new ArrayList<>();
    private CanhBaoTonKhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_kho);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        loadLowStockProducts();
        setupProductSpinner();
    }

    private void initViews() {
        rvCanhBao = findViewById(R.id.rvCanhBaoTonKho);
        spnSanPham = findViewById(R.id.spnSanPhamNhap);
        etSoLuongNhap = findViewById(R.id.etSoLuongNhap);
        etGiaNhap = findViewById(R.id.etGiaNhap);
        etNhaCungCap = findViewById(R.id.etNhaCungCap);
        etGhiChu = findViewById(R.id.etGhiChuNhap);

        rvCanhBao.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackNhapKho).setOnClickListener(v -> finish());

        findViewById(R.id.btnXacNhanNhapKho).setOnClickListener(v -> performStockIn());
    }

    private void loadLowStockProducts() {
        List<SanPham> lowStock = dbHelper.getSanPhamSapHetHang();
        adapter = new CanhBaoTonKhoAdapter(this, lowStock, sp -> {
            // Select this product in spinner
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == sp.getId()) {
                    spnSanPham.setSelection(i);
                    etSoLuongNhap.requestFocus();
                    break;
                }
            }
        });
        rvCanhBao.setAdapter(adapter);
    }

    private void setupProductSpinner() {
        allProducts = dbHelper.getAllSanPham();
        List<String> names = new ArrayList<>();
        for (SanPham sp : allProducts) {
            names.add(sp.getTenSP() + " (Tồn: " + sp.getSoLuongTon() + ")");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
        spnSanPham.setAdapter(spinnerAdapter);
    }

    private void performStockIn() {
        if (allProducts.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm nào trong hệ thống!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIdx = spnSanPham.getSelectedItemPosition();
        if (selectedIdx < 0 || selectedIdx >= allProducts.size()) return;

        SanPham selectedSP = allProducts.get(selectedIdx);

        String strSL = etSoLuongNhap.getText().toString().trim();
        if (strSL.isEmpty()) {
            etSoLuongNhap.setError("Vui lòng nhập số lượng!");
            etSoLuongNhap.requestFocus();
            return;
        }

        int sl = 0;
        try {
            sl = Integer.parseInt(strSL);
            if (sl <= 0) throw new Exception();
        } catch (Exception e) {
            etSoLuongNhap.setError("Số lượng phải lớn hơn 0!");
            etSoLuongNhap.requestFocus();
            return;
        }

        double giaNhap = 0;
        try {
            giaNhap = Double.parseDouble(etGiaNhap.getText().toString().trim());
        } catch (Exception ignored) {}

        String ncc = etNhaCungCap.getText().toString().trim();
        if (ncc.isEmpty()) ncc = "Công ty Phụ kiện Thú cưng";

        String ghiChu = etGhiChu.getText().toString().trim();

        long maPN = dbHelper.taoPhieuNhap(selectedSP.getId(), sl, giaNhap, ncc, session.getFullName(), ghiChu);
        if (maPN > 0) {
            Toast.makeText(this, "Đã nhập thành công " + sl + " " + selectedSP.getTenSP() + " vào kho!", Toast.LENGTH_LONG).show();
            etSoLuongNhap.setText("");
            etGiaNhap.setText("");
            etGhiChu.setText("");

            loadLowStockProducts();
            setupProductSpinner();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi nhập kho!", Toast.LENGTH_SHORT).show();
        }
    }
}
