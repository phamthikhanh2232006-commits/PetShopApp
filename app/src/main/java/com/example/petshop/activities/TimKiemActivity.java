package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.SanPhamAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.CartManager;

import java.util.List;

public class TimKiemActivity extends AppCompatActivity implements SanPhamAdapter.OnSanPhamClickListener {

    private DatabaseHelper dbHelper;
    private EditText etSearchQuery;
    private ImageView btnClearSearch;
    private TextView tvKetQuaCount;
    private RecyclerView rvKetQuaTimKiem;
    private SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        handleIntentData(getIntent());
    }

    private void initViews() {
        etSearchQuery = findViewById(R.id.etSearchQuery);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        tvKetQuaCount = findViewById(R.id.tvKetQuaCount);
        rvKetQuaTimKiem = findViewById(R.id.rvKetQuaTimKiem);

        rvKetQuaTimKiem.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackSearch).setOnClickListener(v -> finish());

        btnClearSearch.setOnClickListener(v -> {
            etSearchQuery.setText("");
            performSearch("");
        });



        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString();
                btnClearSearch.setVisibility(q.isEmpty() ? View.GONE : View.VISIBLE);
                performSearch(q);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Initial empty search or default list
        performSearch("");
    }

    private void handleIntentData(Intent intent) {
        if (intent != null && intent.hasExtra("EXTRA_QUERY")) {
            String query = intent.getStringExtra("EXTRA_QUERY");
            etSearchQuery.setText(query);
            performSearch(query);
        }
    }

    private void performSearch(String keyword) {
        List<SanPham> list;
        if (keyword == null || keyword.trim().isEmpty()) {
            list = dbHelper.getAllSanPham();
            tvKetQuaCount.setText("Tất cả sản phẩm (" + list.size() + ")");
        } else {
            list = dbHelper.searchSanPham(keyword);
            tvKetQuaCount.setText("Tìm thấy " + list.size() + " kết quả cho \"" + keyword + "\"");
        }

        if (adapter == null) {
            adapter = new SanPhamAdapter(this, list, this);
            rvKetQuaTimKiem.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
    }



    @Override
    public void onSanPhamClick(SanPham sanPham) {
        Intent intent = new Intent(this, ChiTietSanPhamActivity.class);
        intent.putExtra("EXTRA_SAN_PHAM_ID", sanPham.getId());
        startActivity(intent);
    }

    @Override
    public void onQuickAddToCart(SanPham sanPham) {
        String defaultSize = sanPham.getDanhSachSize().length > 0 ? sanPham.getDanhSachSize()[0].trim() : "Tiêu chuẩn";
        String defaultColor = sanPham.getDanhSachMau().length > 0 ? sanPham.getDanhSachMau()[0].trim() : "Mặc định";
        CartManager.getInstance().addItem(sanPham, 1, defaultSize, defaultColor);
        Toast.makeText(this, "Đã thêm vào giỏ: " + sanPham.getTenSP(), Toast.LENGTH_SHORT).show();
    }
}
