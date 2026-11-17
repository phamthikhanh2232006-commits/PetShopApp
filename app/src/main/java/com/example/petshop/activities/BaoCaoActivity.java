package com.example.petshop.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.utils.FormatUtils;

public class BaoCaoActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private TextView tvTongDoanhThu;
    private TextView tvTongDon;
    private TextView tvTongKhach;
    private LinearLayout layoutTopBanChay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        loadBaoCao();
    }

    private void initViews() {
        tvTongDoanhThu = findViewById(R.id.tvBaoCaoTongDoanhThu);
        tvTongDon = findViewById(R.id.tvBaoCaoTongDon);
        tvTongKhach = findViewById(R.id.tvBaoCaoTongKhach);
        layoutTopBanChay = findViewById(R.id.layoutTopBanChay);

        findViewById(R.id.btnBackBaoCao).setOnClickListener(v -> finish());
    }

    private void loadBaoCao() {
        Cursor c = dbHelper.getThongKeTongQuan();
        if (c != null && c.moveToFirst()) {
            double doanhThu = c.getDouble(c.getColumnIndexOrThrow("tongDoanhThu"));
            int tongDon = c.getInt(c.getColumnIndexOrThrow("tongDonHang"));
            int tongKhach = c.getInt(c.getColumnIndexOrThrow("tongKhachHang"));

            tvTongDoanhThu.setText(FormatUtils.formatVND(doanhThu));
            tvTongDon.setText("Đơn hàng: " + tongDon);
            tvTongKhach.setText("Khách hàng: " + tongKhach);
            c.close();
        }

        // Top bán chạy
        layoutTopBanChay.removeAllViews();
        Cursor cTop = dbHelper.getTopSanPhamBanChay(5);
        if (cTop != null && cTop.moveToFirst()) {
            int rank = 1;
            do {
                String tenSP = cTop.getString(cTop.getColumnIndexOrThrow("tenSP"));
                int tongBan = cTop.getInt(cTop.getColumnIndexOrThrow("tongBan"));
                double dt = cTop.getDouble(cTop.getColumnIndexOrThrow("doanhThuSP"));

                View item = LayoutInflater.from(this).inflate(R.layout.item_canh_bao_ton_kho, layoutTopBanChay, false);
                TextView tvTen = item.findViewById(R.id.tvTenSPCanhBao);
                TextView tvSub = item.findViewById(R.id.tvLoaiCanhBao);
                TextView tvTon = item.findViewById(R.id.tvSoLuongTonCanhBao);
                TextView tvBadge = item.findViewById(R.id.tvTrangThaiCanhBao);

                tvTen.setText("#" + rank + ". " + tenSP);
                tvSub.setText("Đã bán: " + tongBan + " sản phẩm");
                tvTon.setText("Doanh thu: " + FormatUtils.formatVND(dt));
                tvBadge.setText("TOP " + rank);
                tvBadge.setTextColor(getColor(R.color.primary));

                layoutTopBanChay.addView(item);
                rank++;
            } while (cTop.moveToNext());
            cTop.close();
        } else {
            TextView empty = new TextView(this);
            empty.setText("Chưa có số liệu sản phẩm bán chạy.");
            empty.setPadding(16, 16, 16, 16);
            layoutTopBanChay.addView(empty);
        }
    }
}
