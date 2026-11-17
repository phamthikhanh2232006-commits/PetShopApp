package com.example.petshop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.utils.BottomNavHelper;
import com.example.petshop.utils.SessionManager;

public class HoSoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1002;

    private SessionManager session;
    private TextView tvHoTen;
    private TextView tvEmail;
    private TextView tvVaiTro;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);

        session = SessionManager.getInstance(this);

        initViews();
        loadProfileData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_HO_SO);
        loadProfileData();
    }

    private void initViews() {
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_HO_SO);

        tvHoTen = findViewById(R.id.tvHoTenHoSo);
        tvEmail = findViewById(R.id.tvEmailHoSo);
        tvVaiTro = findViewById(R.id.tvVaiTroHoSo);
        imgAvatar = findViewById(R.id.imgAvatarHoSo);

        // Pick avatar from gallery
        findViewById(R.id.layoutAvatarHoSo).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        // Đơn hàng của tôi
        findViewById(R.id.menuDonHang).setOnClickListener(v -> {
            Intent intent = new Intent(HoSoActivity.this, DonHangCuaToiActivity.class);
            startActivity(intent);
        });

        // Địa chỉ giao hàng (editable)
        findViewById(R.id.menuDiaChi).setOnClickListener(v -> showEditAddressDialog());

        // Phương thức thanh toán (editable)
        findViewById(R.id.menuPhuongThuc).setOnClickListener(v -> showEditPaymentDialog());

        // Hồ sơ thú cưng
        findViewById(R.id.menuHoSoThuCung).setOnClickListener(v -> {
            Intent intent = new Intent(HoSoActivity.this, HoSoThuCungActivity.class);
            startActivity(intent);
        });

        // Ưu đãi & khuyến mãi (manual promo code input)
        findViewById(R.id.menuUuDai).setOnClickListener(v -> showPromoCodeDialog());

        // Tin tức & Cẩm nang
        findViewById(R.id.menuTinTuc).setOnClickListener(v -> {
            Intent intent = new Intent(HoSoActivity.this, TinTucActivity.class);
            startActivity(intent);
        });

        // Admin Panel
        View menuAdmin = findViewById(R.id.menuAdminPanel);
        if (session.isAdmin()) {
            menuAdmin.setVisibility(View.VISIBLE);
            menuAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(HoSoActivity.this, AdminPanelActivity.class);
                startActivity(intent);
            });
        } else {
            menuAdmin.setVisibility(View.GONE);
        }

        // Đăng xuất
        findViewById(R.id.menuDangXuat).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất hoặc đổi tài khoản?")
                    .setPositiveButton("Đổi tài khoản", (dialog, which) -> {
                        session.logout();
                        Intent intent = new Intent(HoSoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void showEditAddressDialog() {
        EditText input = new EditText(this);
        input.setText(session.getAddress());
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật địa chỉ giao hàng")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newAddr = input.getText().toString().trim();
                    if (!newAddr.isEmpty()) {
                        session.updateAddress(newAddr);
                        Toast.makeText(this, "Đã cập nhật địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditPaymentDialog() {
        String[] methods = {"Thanh toán khi nhận hàng (COD)", "Chuyển khoản ngân hàng (Vietcombank QR)", "Ví điện tử (MoMo, ZaloPay...)"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn phương thức thanh toán mặc định")
                .setItems(methods, (dialog, which) -> {
                    session.updatePaymentMethod(methods[which]);
                    Toast.makeText(this, "Đã lưu phương thức: " + methods[which], Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showPromoCodeDialog() {
        EditText input = new EditText(this);
        input.setText(session.getPromoCode());
        input.setHint("Nhập mã ưu đãi bằng tay...");
        new AlertDialog.Builder(this)
                .setTitle("Nhập hoặc chỉnh sửa ưu đãi & khuyến mãi")
                .setView(input)
                .setPositiveButton("Áp dụng", (dialog, which) -> {
                    String code = input.getText().toString().trim();
                    if (!code.isEmpty()) {
                        session.updatePromoCode(code);
                        Toast.makeText(this, "Đã áp dụng mã ưu đãi: " + code, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                session.updateAvatarUri(imageUri.toString());
                imgAvatar.setImageURI(imageUri);
                Toast.makeText(this, "Đã cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadProfileData() {
        tvHoTen.setText(session.getFullName());
        tvEmail.setText(session.getEmail());
        if (session.isAdmin()) {
            tvVaiTro.setText("Quản trị viên (Admin)");
        } else {
            tvVaiTro.setText("Khách hàng (" + session.getRole() + ")");
        }

        String avatarUri = session.getAvatarUri();
        if (avatarUri != null && !avatarUri.isEmpty()) {
            try {
                imgAvatar.setImageURI(Uri.parse(avatarUri));
            } catch (Exception ignored) {}
        }
    }
}
