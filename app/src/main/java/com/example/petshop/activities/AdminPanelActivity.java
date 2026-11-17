package com.example.petshop.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.utils.FormatUtils;
import com.example.petshop.utils.SessionManager;

public class AdminPanelActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private TextView tvStatDoanhThu;
    private TextView tvStatDonHang;
    private TextView tvStatSanPham;
    private TextView tvStatSapHet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!SessionManager.getInstance(this).isAdmin()) {
            Toast.makeText(this, "Bạn không có quyền truy cập khu vực quản trị!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_admin_panel);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void initViews() {
        tvStatDoanhThu = findViewById(R.id.tvStatDoanhThu);
        tvStatDonHang = findViewById(R.id.tvStatDonHang);
        tvStatSanPham = findViewById(R.id.tvStatSanPham);
        tvStatSapHet = findViewById(R.id.tvStatSapHet);

        findViewById(R.id.btnBackAdmin).setOnClickListener(v -> finish());

        // 1. Quản lý sản phẩm & danh mục
        findViewById(R.id.adminMenuSanPham).setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, QuanLySanPhamActivity.class);
            startActivity(intent);
        });

        // 2. Nhập hàng & Tồn kho
        findViewById(R.id.adminMenuNhapKho).setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, NhapKhoActivity.class);
            startActivity(intent);
        });

        // 3. Đổi trả
        findViewById(R.id.adminMenuDoiTra).setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, DoiTraActivity.class);
            startActivity(intent);
        });

        // 4. Báo cáo thống kê
        findViewById(R.id.adminMenuBaoCao).setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, BaoCaoActivity.class);
            startActivity(intent);
        });

        // 5. Quản lý toàn bộ đơn hàng
        findViewById(R.id.adminMenuDonHang).setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, DonHangCuaToiActivity.class);
            intent.putExtra("EXTRA_IS_ADMIN_VIEW", true);
            startActivity(intent);
        });
    }

    private void loadStatistics() {
        Cursor c = dbHelper.getThongKeTongQuan();
        if (c != null && c.moveToFirst()) {
            double doanhThu = c.getDouble(c.getColumnIndexOrThrow("tongDoanhThu"));
            int tongDon = c.getInt(c.getColumnIndexOrThrow("tongDonHang"));
            int tongSP = c.getInt(c.getColumnIndexOrThrow("tongSanPham"));
            int sapHet = c.getInt(c.getColumnIndexOrThrow("sanPhamSapHet"));

            tvStatDoanhThu.setText(FormatUtils.formatVND(doanhThu));
            tvStatDonHang.setText(tongDon + " đơn");
            tvStatSanPham.setText(tongSP + " loại");
            tvStatSapHet.setText(sapHet + " món");
            c.close();
        }
    }
}
