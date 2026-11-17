package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.DonHangAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.DonHang;
import com.example.petshop.utils.SessionManager;

import java.util.List;

public class DonHangCuaToiActivity extends AppCompatActivity implements DonHangAdapter.OnDonHangClickListener {

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private RecyclerView rvOrders;
    private DonHangAdapter adapter;
    private boolean isAdminView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_cua_toi);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        isAdminView = getIntent().getBooleanExtra("EXTRA_IS_ADMIN_VIEW", false);

        initViews();
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void initViews() {
        rvOrders = findViewById(R.id.rvDonHangCuaToi);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackDonHangCuaToi).setOnClickListener(v -> finish());
    }

    private void loadOrders() {
        List<DonHang> list;
        if (isAdminView || session.isAdmin()) {
            list = dbHelper.getAllDonHang();
        } else {
            list = dbHelper.getDonHangByKhachHang(session.getUserId());
        }

        adapter = new DonHangAdapter(this, list, this);
        rvOrders.setAdapter(adapter);
    }

    @Override
    public void onDonHangClick(DonHang donHang) {
        Intent intent = new Intent(this, ChiTietDonHangActivity.class);
        intent.putExtra("EXTRA_MA_DON_HANG", donHang.getId());
        startActivity(intent);
    }
}
