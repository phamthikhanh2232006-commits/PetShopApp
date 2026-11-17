package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.CartManager;
import com.example.petshop.utils.FormatUtils;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SanPham sanPham;
    private int quantity = 1;
    private String selectedSize = "";
    private String selectedColor = "";

    private ImageView imgSP;
    private TextView tvTenSP;
    private TextView tvGiaSP;
    private TextView tvTonKho;
    private TextView tvMaVach;
    private TextView tvMoTa;
    private TextView tvSoLuong;
    private LinearLayout layoutSizeChips;
    private LinearLayout layoutMauSacChips;
    private Button btnThemVaoGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        dbHelper = DatabaseHelper.getInstance(this);

        long spId = getIntent().getLongExtra("EXTRA_SAN_PHAM_ID", 1);
        sanPham = dbHelper.getSanPhamById(spId);

        if (sanPham == null) {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayProductDetails();
    }

    private void initViews() {
        imgSP = findViewById(R.id.imgChiTietSP);
        tvTenSP = findViewById(R.id.tvChiTietTenSP);
        tvGiaSP = findViewById(R.id.tvChiTietGiaSP);
        tvTonKho = findViewById(R.id.tvChiTietTonKho);
        tvMaVach = findViewById(R.id.tvChiTietMaVach);
        tvMoTa = findViewById(R.id.tvChiTietMoTa);
        tvSoLuong = findViewById(R.id.tvChiTietSoLuong);
        layoutSizeChips = findViewById(R.id.layoutSizeChips);
        layoutMauSacChips = findViewById(R.id.layoutMauSacChips);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        findViewById(R.id.btnBackChiTiet).setOnClickListener(v -> finish());

        findViewById(R.id.btnYeuThich).setOnClickListener(v ->
                Toast.makeText(this, "Đã lưu vào danh sách yêu thích!", Toast.LENGTH_SHORT).show()
        );

        // Quantity controls
        findViewById(R.id.btnChiTietGiam).setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvSoLuong.setText(String.valueOf(quantity));
            }
        });

        findViewById(R.id.btnChiTietTang).setOnClickListener(v -> {
            if (quantity < sanPham.getSoLuongTon()) {
                quantity++;
                tvSoLuong.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Đã đạt số lượng tồn kho tối đa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnThemVaoGioHang.setOnClickListener(v -> {
            if (sanPham.getSoLuongTon() <= 0) {
                Toast.makeText(this, "Sản phẩm hiện đang tạm hết hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            CartManager.getInstance().addItem(sanPham, quantity, selectedSize, selectedColor);
            Toast.makeText(this, "Đã thêm " + quantity + " sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void displayProductDetails() {
        tvTenSP.setText(sanPham.getTenSP());
        tvGiaSP.setText(FormatUtils.formatVND(sanPham.getGiaBan()));
        tvTonKho.setText("Tồn kho: " + sanPham.getSoLuongTon() + " sản phẩm");
        tvMaVach.setText("Mã: " + (sanPham.getMaVach() != null ? sanPham.getMaVach() : "SP" + sanPham.getId()));
        tvMoTa.setText(sanPham.getMoTa() != null ? sanPham.getMoTa() : "Sản phẩm phụ kiện chất lượng cao dành cho thú cưng.");

        // Ảnh sản phẩm từ drawable
        int drawableRes = FormatUtils.getProductDrawableRes(this, sanPham.getHinhAnh(), sanPham.getLoaiThuCung());
        imgSP.setImageResource(drawableRes);

        // Render size chips
        String[] sizes = sanPham.getDanhSachSize();
        if (sizes.length > 0) {
            selectedSize = sizes[0].trim();
        }
        for (String size : sizes) {
            final String s = size.trim();
            TextView chip = createChip(s, s.equals(selectedSize));
            chip.setOnClickListener(v -> {
                selectedSize = s;
                refreshSizeChips(sizes);
            });
            layoutSizeChips.addView(chip);
        }

        // Render color chips
        String[] colors = sanPham.getDanhSachMau();
        if (colors.length > 0) {
            selectedColor = colors[0].trim();
        }
        for (String color : colors) {
            final String c = color.trim();
            TextView chip = createChip(c, c.equals(selectedColor));
            chip.setOnClickListener(v -> {
                selectedColor = c;
                refreshColorChips(colors);
            });
            layoutMauSacChips.addView(chip);
        }
    }

    private TextView createChip(String text, boolean isSelected) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (38 * getResources().getDisplayMetrics().density));
        lp.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
        tv.setLayoutParams(lp);
        tv.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density), 0,
                (int) (16 * getResources().getDisplayMetrics().density), 0);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setTextSize(13);

        if (isSelected) {
            tv.setBackgroundResource(R.drawable.bg_chip_selected);
            tv.setTextColor(getColor(R.color.white));
        } else {
            tv.setBackgroundResource(R.drawable.bg_chip_unselected);
            tv.setTextColor(getColor(R.color.text_primary));
        }
        return tv;
    }

    private void refreshSizeChips(String[] sizes) {
        layoutSizeChips.removeAllViews();
        for (String size : sizes) {
            final String s = size.trim();
            TextView chip = createChip(s, s.equals(selectedSize));
            chip.setOnClickListener(v -> {
                selectedSize = s;
                refreshSizeChips(sizes);
            });
            layoutSizeChips.addView(chip);
        }
    }

    private void refreshColorChips(String[] colors) {
        layoutMauSacChips.removeAllViews();
        for (String color : colors) {
            final String c = color.trim();
            TextView chip = createChip(c, c.equals(selectedColor));
            chip.setOnClickListener(v -> {
                selectedColor = c;
                refreshColorChips(colors);
            });
            layoutMauSacChips.addView(chip);
        }
    }
}
