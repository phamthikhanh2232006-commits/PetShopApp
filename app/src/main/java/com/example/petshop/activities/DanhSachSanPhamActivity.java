package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.SanPhamAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.BottomNavHelper;
import com.example.petshop.utils.CartManager;

import java.util.List;

public class DanhSachSanPhamActivity extends AppCompatActivity implements SanPhamAdapter.OnSanPhamClickListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvDanhSach;
    private SanPhamAdapter adapter;
    private TextView tvTieuDe;

    private TextView chipTatCa;
    private TextView chipSize;
    private TextView chipMauSac;
    private TextView chipGia;

    private String selectedSpecies = null;
    private Long selectedCategoryId = null;
    private String selectedSize = null;
    private String selectedColor = null;
    private String selectedSort = null; // "gia_tang" or "gia_giam"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);

        dbHelper = DatabaseHelper.getInstance(this);

        if (getIntent().hasExtra("EXTRA_SPECIES")) {
            selectedSpecies = getIntent().getStringExtra("EXTRA_SPECIES");
        }
        if (getIntent().hasExtra("EXTRA_CATEGORY_ID")) {
            selectedCategoryId = getIntent().getLongExtra("EXTRA_CATEGORY_ID", 0);
        }

        initViews();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_DANH_MUC);
        loadData();
    }

    private void initViews() {
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_DANH_MUC);

        tvTieuDe = findViewById(R.id.tvTieuDeDanhSach);
        rvDanhSach = findViewById(R.id.rvDanhSachSanPham);
        rvDanhSach.setLayoutManager(new GridLayoutManager(this, 2));

        if (selectedSpecies != null && !selectedSpecies.isEmpty()) {
            tvTieuDe.setText("Phụ kiện cho " + selectedSpecies);
        } else {
            tvTieuDe.setText("Tất cả phụ kiện thú cưng");
        }

        findViewById(R.id.btnBackDanhSach).setOnClickListener(v -> finish());

        chipTatCa = findViewById(R.id.chipTatCa);
        chipSize = findViewById(R.id.chipSize);
        chipMauSac = findViewById(R.id.chipMauSac);
        chipGia = findViewById(R.id.chipGia);

        chipTatCa.setOnClickListener(v -> {
            selectedSize = null;
            selectedColor = null;
            selectedSort = null;
            resetChipStyles();
            chipTatCa.setBackgroundResource(R.drawable.bg_chip_selected);
            chipTatCa.setTextColor(getColor(R.color.white));
            loadData();
        });

        chipSize.setOnClickListener(v -> showSizeFilterDialog());
        chipMauSac.setOnClickListener(v -> showColorFilterDialog());
        chipGia.setOnClickListener(v -> showSortDialog());

        findViewById(R.id.btnFilterSort).setOnClickListener(v -> showSpeciesFilterDialog());
    }

    private void resetChipStyles() {
        chipTatCa.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipTatCa.setTextColor(getColor(R.color.text_primary));
        chipSize.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipSize.setTextColor(getColor(R.color.text_primary));
        chipMauSac.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipMauSac.setTextColor(getColor(R.color.text_primary));
        chipGia.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipGia.setTextColor(getColor(R.color.text_primary));
    }

    private void showSizeFilterDialog() {
        String[] sizes = {"Tất cả", "S", "M", "L", "XL", "500g", "1kg", "2kg"};
        new AlertDialog.Builder(this)
                .setTitle("Lọc theo kích cỡ (Size)")
                .setItems(sizes, (dialog, which) -> {
                    selectedSize = which == 0 ? null : sizes[which];
                    chipSize.setText(which == 0 ? "Kích cỡ" : "Size: " + selectedSize);
                    chipSize.setBackgroundResource(which == 0 ? R.drawable.bg_chip_unselected : R.drawable.bg_chip_selected);
                    chipSize.setTextColor(getColor(which == 0 ? R.color.text_primary : R.color.white));
                    loadData();
                })
                .show();
    }

    private void showColorFilterDialog() {
        String[] colors = {"Tất cả", "Đỏ", "Xanh", "Vàng", "Hồng", "Nâu", "Trắng", "Đen"};
        new AlertDialog.Builder(this)
                .setTitle("Lọc theo màu sắc")
                .setItems(colors, (dialog, which) -> {
                    selectedColor = which == 0 ? null : colors[which];
                    chipMauSac.setText(which == 0 ? "Màu sắc" : "Màu: " + selectedColor);
                    chipMauSac.setBackgroundResource(which == 0 ? R.drawable.bg_chip_unselected : R.drawable.bg_chip_selected);
                    chipMauSac.setTextColor(getColor(which == 0 ? R.color.text_primary : R.color.white));
                    loadData();
                })
                .show();
    }

    private void showSortDialog() {
        String[] sorts = {"Mặc định", "Giá: Thấp đến Cao", "Giá: Cao đến Thấp"};
        new AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo giá")
                .setItems(sorts, (dialog, which) -> {
                    if (which == 1) {
                        selectedSort = "gia_tang";
                        chipGia.setText("Giá: Tăng dần");
                    } else if (which == 2) {
                        selectedSort = "gia_giam";
                        chipGia.setText("Giá: Giảm dần");
                    } else {
                        selectedSort = null;
                        chipGia.setText("Giá: Mặc định");
                    }
                    chipGia.setBackgroundResource(which == 0 ? R.drawable.bg_chip_unselected : R.drawable.bg_chip_selected);
                    chipGia.setTextColor(getColor(which == 0 ? R.color.text_primary : R.color.white));
                    loadData();
                })
                .show();
    }

    private void showSpeciesFilterDialog() {
        String[] speciesList = {"Tất cả các loài", "Chó", "Mèo", "Chim", "Cá"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn loài vật nuôi")
                .setItems(speciesList, (dialog, which) -> {
                    if (which == 0) {
                        selectedSpecies = null;
                        tvTieuDe.setText("Tất cả phụ kiện thú cưng");
                    } else {
                        selectedSpecies = speciesList[which];
                        tvTieuDe.setText("Phụ kiện cho " + selectedSpecies);
                    }
                    loadData();
                })
                .show();
    }

    private void loadData() {
        List<SanPham> list = dbHelper.getSanPhamBySpeciesOrCategory(
                selectedSpecies, selectedCategoryId, selectedSize, selectedColor, selectedSort);
        if (adapter == null) {
            adapter = new SanPhamAdapter(this, list, this);
            rvDanhSach.setAdapter(adapter);
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
        BottomNavHelper.setupBottomNav(this, BottomNavHelper.TAB_DANH_MUC);
    }
}
