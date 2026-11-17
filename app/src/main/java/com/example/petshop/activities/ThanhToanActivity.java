package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.ChiTietDonHangAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.CartItem;
import com.example.petshop.models.ChiTietDonHang;
import com.example.petshop.models.DonHang;
import com.example.petshop.utils.CartManager;
import com.example.petshop.utils.FormatUtils;
import com.example.petshop.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager session;

    private TextView tvNguoiNhan;
    private TextView tvDiaChiNhan;
    private TextView tvTongTien;
    private RadioGroup radioGroupPayment;
    private RecyclerView rvItems;
    private EditText etGhiChu;
    private Button btnDatHang;

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        receiverName = session.getFullName();
        receiverPhone = session.getPhone();
        receiverAddress = session.getAddress();

        initViews();
        loadOrderItems();
    }

    private void initViews() {
        tvNguoiNhan = findViewById(R.id.tvNguoiNhan);
        tvDiaChiNhan = findViewById(R.id.tvDiaChiNhan);
        tvTongTien = findViewById(R.id.tvTongTienThanhToan);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        rvItems = findViewById(R.id.rvThanhToanItems);
        etGhiChu = findViewById(R.id.etGhiChuThanhToan);
        btnDatHang = findViewById(R.id.btnDatHang);

        rvItems.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackThanhToan).setOnClickListener(v -> finish());

        updateAddressView();

        // Allow editing address on click
        findViewById(R.id.layoutDiaChiGiaoHang).setOnClickListener(v -> showEditAddressDialog());

        btnDatHang.setOnClickListener(v -> processCheckout());
    }

    private void updateAddressView() {
        tvNguoiNhan.setText(receiverName + " • " + receiverPhone);
        tvDiaChiNhan.setText(receiverAddress);
    }

    private void showEditAddressDialog() {
        EditText input = new EditText(this);
        input.setText(receiverAddress);
        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa địa chỉ nhận hàng")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newAddr = input.getText().toString().trim();
                    if (!newAddr.isEmpty()) {
                        receiverAddress = newAddr;
                        session.updateAddress(newAddr);
                        updateAddressView();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadOrderItems() {
        List<CartItem> cartItems = CartManager.getInstance().getItems();
        List<ChiTietDonHang> chiTietList = new ArrayList<>();

        for (CartItem item : cartItems) {
            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setMaSP(item.getSanPham().getId());
            ct.setTenSP(item.getSanPham().getTenSP());
            ct.setSoLuong(item.getSoLuong());
            ct.setDonGia(item.getSanPham().getGiaBan());
            ct.setThanhTien(item.getThanhTien());
            ct.setSizeDaChon(item.getSelectedSize());
            ct.setMauDaChon(item.getSelectedColor());
            chiTietList.add(ct);
        }

        ChiTietDonHangAdapter adapter = new ChiTietDonHangAdapter(this, chiTietList);
        rvItems.setAdapter(adapter);

        tvTongTien.setText(FormatUtils.formatVND(CartManager.getInstance().getTotalAmount()));
    }

    private void processCheckout() {
        List<CartItem> cartItems = CartManager.getInstance().getItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = "COD";
        int checkedId = radioGroupPayment.getCheckedRadioButtonId();
        if (checkedId == R.id.rbBankTransfer) {
            paymentMethod = "Chuyển khoản ngân hàng";
        } else if (checkedId == R.id.rbEWallet) {
            paymentMethod = "Ví điện tử MoMo/ZaloPay";
        }

        DonHang dh = new DonHang();
        dh.setMaKH(session.getUserId());
        dh.setTenKhachHang(receiverName);
        dh.setSoDienThoai(receiverPhone);
        dh.setDiaChiGiaoHang(receiverAddress);
        dh.setTongTien(CartManager.getInstance().getTotalAmount());
        dh.setGiamGia(0);
        dh.setPhuongThucTT(paymentMethod);
        dh.setTrangThai("ChoXuLy");
        dh.setNgayDat(FormatUtils.getCurrentDateTime());
        dh.setGhiChu(etGhiChu.getText().toString().trim());

        List<ChiTietDonHang> chiTietList = new ArrayList<>();
        for (CartItem item : cartItems) {
            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setMaSP(item.getSanPham().getId());
            ct.setTenSP(item.getSanPham().getTenSP());
            ct.setSoLuong(item.getSoLuong());
            ct.setDonGia(item.getSanPham().getGiaBan());
            ct.setThanhTien(item.getThanhTien());
            ct.setSizeDaChon(item.getSelectedSize());
            ct.setMauDaChon(item.getSelectedColor());
            chiTietList.add(ct);
        }
        dh.setDanhSachChiTiet(chiTietList);

        long maDH = dbHelper.taoDonHang(dh);

        if (maDH > 0) {
            CartManager.getInstance().clearCart();
            Intent intent = new Intent(ThanhToanActivity.this, ChiTietDonHangActivity.class);
            intent.putExtra("EXTRA_MA_DON_HANG", maDH);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi tạo đơn hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}
