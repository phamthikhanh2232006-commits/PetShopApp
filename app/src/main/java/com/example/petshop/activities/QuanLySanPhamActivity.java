package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.SanPhamAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;

import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity implements SanPhamAdapter.OnSanPhamClickListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvSP;
    private SanPhamAdapter adapter;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        loadData("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(etSearch.getText().toString());
    }

    private void initViews() {
        rvSP = findViewById(R.id.rvQLSP);
        etSearch = findViewById(R.id.etSearchQLSP);

        rvSP.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackQLSP).setOnClickListener(v -> finish());

        findViewById(R.id.btnThemSPMoi).setOnClickListener(v -> {
            Intent intent = new Intent(QuanLySanPhamActivity.this, ThemSuaSanPhamActivity.class);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadData(String keyword) {
        List<SanPham> list;
        if (keyword == null || keyword.trim().isEmpty()) {
            list = dbHelper.getAllSanPham();
        } else {
            list = dbHelper.searchSanPham(keyword);
        }

        if (adapter == null) {
            adapter = new SanPhamAdapter(this, list, this);
            rvSP.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
    }



    @Override
    public void onSanPhamClick(SanPham sanPham) {
        Intent intent = new Intent(this, ThemSuaSanPhamActivity.class);
        intent.putExtra("EXTRA_SAN_PHAM_ID", sanPham.getId());
        startActivity(intent);
    }

    @Override
    public void onQuickAddToCart(SanPham sanPham) {
        onSanPhamClick(sanPham);
    }
}
