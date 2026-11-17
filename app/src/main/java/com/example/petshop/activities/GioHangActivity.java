package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.CartAdapter;
import com.example.petshop.models.CartItem;
import com.example.petshop.utils.BottomNavHelper;
import com.example.petshop.utils.CartManager;
import com.example.petshop.utils.FormatUtils;

import java.util.List;

public class GioHangActivity extends AppCompatActivity implements CartAdapter.OnCartItemInteractionListener {

    private TextView tvTieuDeGioHang;
    private TextView tvTongTien;
    private RecyclerView rvGioHang;
    private LinearLayout layoutEmpty;
    private LinearLayout bottomCheckoutBar;
    private Button btnThanhToan;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        initViews();
        loadCartData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_GIO_HANG);
        loadCartData();
    }

    private void initViews() {
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_GIO_HANG);

        tvTieuDeGioHang = findViewById(R.id.tvTieuDeGioHang);
        tvTongTien = findViewById(R.id.tvTongTienGioHang);
        rvGioHang = findViewById(R.id.rvGioHang);
        layoutEmpty = findViewById(R.id.layoutEmptyCart);
        bottomCheckoutBar = findViewById(R.id.bottomCheckoutBar);
        btnThanhToan = findViewById(R.id.btnThanhToan);

        rvGioHang.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackGioHang).setOnClickListener(v -> finish());

        findViewById(R.id.btnTiepTucMua).setOnClickListener(v -> {
            Intent intent = new Intent(GioHangActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnXoaHetGioHang).setOnClickListener(v -> {
            if (CartManager.getInstance().getItems().isEmpty()) return;
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn làm trống giỏ hàng?")
                    .setPositiveButton("Xóa hết", (d, w) -> {
                        CartManager.getInstance().clearCart();
                        loadCartData();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnThanhToan.setOnClickListener(v -> {
            if (CartManager.getInstance().getItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(GioHangActivity.this, ThanhToanActivity.class);
            startActivity(intent);
        });
    }

    private void loadCartData() {
        List<CartItem> items = CartManager.getInstance().getItems();
        int count = CartManager.getInstance().getItemCount();
        tvTieuDeGioHang.setText("Giỏ hàng (" + count + ")");

        if (items.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvGioHang.setVisibility(View.GONE);
            bottomCheckoutBar.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
            bottomCheckoutBar.setVisibility(View.VISIBLE);

            adapter = new CartAdapter(this, items, this);
            rvGioHang.setAdapter(adapter);

            tvTongTien.setText(FormatUtils.formatVND(CartManager.getInstance().getTotalAmount()));
        }
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_GIO_HANG);
    }

    @Override
    public void onCartUpdated() {
        loadCartData();
    }
}
