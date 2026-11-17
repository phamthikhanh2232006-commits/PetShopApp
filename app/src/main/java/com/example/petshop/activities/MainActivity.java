package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.DanhMucAdapter;
import com.example.petshop.adapters.SanPhamAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.DanhMuc;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.BottomNavHelper;
import com.example.petshop.utils.CartManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SanPhamAdapter.OnSanPhamClickListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvDanhMuc;
    private RecyclerView rvSanPhamNoiBat;
    private DanhMucAdapter danhMucAdapter;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        loadDanhMuc();
        loadSanPhamNoiBat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_HOME);
        loadSanPhamNoiBat();
    }

    private void initViews() {
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_HOME);

        rvDanhMuc = findViewById(R.id.rvDanhMuc);
        rvSanPhamNoiBat = findViewById(R.id.rvSanPhamNoiBat);

        rvDanhMuc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSanPhamNoiBat.setLayoutManager(new GridLayoutManager(this, 2));

        // Click on search bar -> open TimKiemActivity
        findViewById(R.id.searchBarHome).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimKiemActivity.class);
            startActivity(intent);
        });

        // Click on Notification
        findViewById(R.id.btnNotification).setOnClickListener(v ->
                Toast.makeText(this, "Bạn không có thông báo mới nào.", Toast.LENGTH_SHORT).show()
        );

        // Xem tất cả danh mục / sản phẩm
        findViewById(R.id.tvXemTatCaDanhMuc).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DanhSachSanPhamActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tvXemTatCaSanPham).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DanhSachSanPhamActivity.class);
            startActivity(intent);
        });
    }

    private void loadDanhMuc() {
        List<DanhMuc> listDM = dbHelper.getAllDanhMuc();
        danhMucAdapter = new DanhMucAdapter(this, listDM, dm -> {
            Intent intent = new Intent(MainActivity.this, DanhSachSanPhamActivity.class);
            intent.putExtra("EXTRA_SPECIES", dm.getTenDM());
            intent.putExtra("EXTRA_CATEGORY_ID", dm.getId());
            startActivity(intent);
        });
        rvDanhMuc.setAdapter(danhMucAdapter);
    }

    private void loadSanPhamNoiBat() {
        List<SanPham> listSP = dbHelper.getFeaturedSanPham(10);
        sanPhamAdapter = new SanPhamAdapter(this, listSP, this);
        rvSanPhamNoiBat.setAdapter(sanPhamAdapter);
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
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_HOME);
    }
}
