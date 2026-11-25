package com.example.petshop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.DanhMuc;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ThemSuaSanPhamActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_PRODUCT_IMAGE = 4001;

    private DatabaseHelper dbHelper;
    private long sanPhamId = -1;
    private SanPham currentSanPham;

    private TextView tvTieuDe;
    private ImageView btnXoa;
    private ImageView imgHinhAnhSP;
    private Button btnChonAnhSP;
    private TextView tvTrangThaiAnh;
    private EditText etTenSP;
    private EditText etGiaBan;
    private EditText etSoLuongTon;
    private EditText etMaVach;
    private Spinner spnLoai;
    private EditText etMucCanhBao;
    private EditText etKichCo;
    private EditText etMauSac;
    private EditText etMoTa;

    private String selectedHinhAnh = "";
    private final String[] speciesArray = {"Chó", "Mèo", "Chim", "Cá"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_san_pham);

        dbHelper = DatabaseHelper.getInstance(this);

        if (getIntent().hasExtra("EXTRA_SAN_PHAM_ID")) {
            sanPhamId = getIntent().getLongExtra("EXTRA_SAN_PHAM_ID", -1);
            currentSanPham = dbHelper.getSanPhamById(sanPhamId);
        }

        initViews();
        if (currentSanPham != null) {
            populateData();
        }
    }

    private void initViews() {
        tvTieuDe = findViewById(R.id.tvTieuDeThemSP);
        btnXoa = findViewById(R.id.btnXoaSP);
        imgHinhAnhSP = findViewById(R.id.imgHinhAnhSP);
        btnChonAnhSP = findViewById(R.id.btnChonAnhSP);
        tvTrangThaiAnh = findViewById(R.id.tvTrangThaiAnh);
        etTenSP = findViewById(R.id.etTenSP);
        etGiaBan = findViewById(R.id.etGiaBan);
        etSoLuongTon = findViewById(R.id.etSoLuongTon);
        etMaVach = findViewById(R.id.etMaVach);
        spnLoai = findViewById(R.id.spnLoai);
        etMucCanhBao = findViewById(R.id.etMucCanhBao);
        etKichCo = findViewById(R.id.etKichCo);
        etMauSac = findViewById(R.id.etMauSac);
        etMoTa = findViewById(R.id.etMoTa);

        findViewById(R.id.btnBackThemSP).setOnClickListener(v -> finish());

        // Setup image picker
        btnChonAnhSP.setOnClickListener(v -> chooseImage());
        findViewById(R.id.cardPreviewSP).setOnClickListener(v -> chooseImage());

        // Setup Spinner loài
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speciesArray);
        spnLoai.setAdapter(spinnerAdapter);
        spnLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedHinhAnh == null || selectedHinhAnh.isEmpty() || selectedHinhAnh.equals("sp_default")) {
                    FormatUtils.loadProductImage(imgHinhAnhSP, "", speciesArray[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Setup clickable Kích cỡ
        View.OnClickListener kichCoListener = v -> showKichCoDialog();
        findViewById(R.id.layoutChonKichCo).setOnClickListener(kichCoListener);
        etKichCo.setOnClickListener(kichCoListener);

        // Setup clickable Màu sắc
        View.OnClickListener mauSacListener = v -> showMauSacDialog();
        findViewById(R.id.layoutChonMauSac).setOnClickListener(mauSacListener);
        etMauSac.setOnClickListener(mauSacListener);

        // Save button
        findViewById(R.id.btnLuuSP).setOnClickListener(v -> saveProduct());

        if (sanPhamId > 0) {
            tvTieuDe.setText("Sửa thông tin sản phẩm");
            btnChonAnhSP.setText("📷 Cập nhật ảnh");
            btnXoa.setVisibility(View.VISIBLE);
            btnXoa.setOnClickListener(v -> confirmDelete());
        } else {
            tvTieuDe.setText("Thêm sản phẩm mới");
            btnChonAnhSP.setText("📷 Chọn ảnh từ thiết bị");
            btnXoa.setVisibility(View.GONE);
            // Default image preview
            FormatUtils.loadProductImage(imgHinhAnhSP, "", speciesArray[0]);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh sản phẩm"), REQUEST_CODE_PICK_PRODUCT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PRODUCT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    File dir = new File(getFilesDir(), "products");
                    if (!dir.exists()) dir.mkdirs();
                    File dest = new File(dir, "sp_" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream fos = new FileOutputStream(dest);
                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                    selectedHinhAnh = dest.getAbsolutePath();
                    String loai = spnLoai.getSelectedItem() != null ? spnLoai.getSelectedItem().toString() : "Chó";
                    FormatUtils.loadProductImage(imgHinhAnhSP, selectedHinhAnh, loai);
                    tvTrangThaiAnh.setText("Đã chọn ảnh mới từ máy");
                    Toast.makeText(this, "Đã tải ảnh lên!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showKichCoDialog() {
        List<String> allSizes = dbHelper.getAllDistinctSizes();
        String currentText = etKichCo.getText().toString().trim();
        String[] currentArr = currentText.isEmpty() ? new String[0] : currentText.split("[,;]+");
        List<String> currentSelected = new ArrayList<>();
        for (String s : currentArr) {
            if (!s.trim().isEmpty()) currentSelected.add(s.trim());
        }

        String[] items = allSizes.toArray(new String[0]);
        boolean[] checkedItems = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            for (String sel : currentSelected) {
                if (items[i].equalsIgnoreCase(sel)) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn kích cỡ (Size)")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < items.length; i++) {
                        if (checkedItems[i]) {
                            result.add(items[i]);
                        }
                    }
                    etKichCo.setText(TextUtils.join(", ", result));
                })
                .setNegativeButton("Hủy", null)
                .setNeutralButton("+ Nhập cỡ khác", (dialog, which) -> {
                    showCustomInputDialog("Nhập kích cỡ mới", "VD: 250g, 3XL...", customValue -> {
                        if (!customValue.isEmpty()) {
                            String cur = etKichCo.getText().toString().trim();
                            if (cur.isEmpty()) {
                                etKichCo.setText(customValue);
                            } else {
                                etKichCo.setText(cur + ", " + customValue);
                            }
                        }
                    });
                })
                .show();
    }

    private void showMauSacDialog() {
        List<String> allColors = dbHelper.getAllDistinctColors();
        String currentText = etMauSac.getText().toString().trim();
        String[] currentArr = currentText.isEmpty() ? new String[0] : currentText.split("[,;]+");
        List<String> currentSelected = new ArrayList<>();
        for (String s : currentArr) {
            if (!s.trim().isEmpty()) currentSelected.add(s.trim());
        }

        String[] items = allColors.toArray(new String[0]);
        boolean[] checkedItems = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            for (String sel : currentSelected) {
                if (items[i].equalsIgnoreCase(sel)) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn màu sắc")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < items.length; i++) {
                        if (checkedItems[i]) {
                            result.add(items[i]);
                        }
                    }
                    etMauSac.setText(TextUtils.join(", ", result));
                })
                .setNegativeButton("Hủy", null)
                .setNeutralButton("+ Nhập màu khác", (dialog, which) -> {
                    showCustomInputDialog("Nhập màu sắc mới", "VD: Tím, Cam đào, Xanh mint...", customValue -> {
                        if (!customValue.isEmpty()) {
                            String cur = etMauSac.getText().toString().trim();
                            if (cur.isEmpty()) {
                                etMauSac.setText(customValue);
                            } else {
                                etMauSac.setText(cur + ", " + customValue);
                            }
                        }
                    });
                })
                .show();
    }

    private interface OnInputConfirmed {
        void onInput(String value);
    }

    private void showCustomInputDialog(String title, String hint, OnInputConfirmed callback) {
        EditText input = new EditText(this);
        input.setHint(hint);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String val = input.getText().toString().trim();
                    callback.onInput(val);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void populateData() {
        etTenSP.setText(currentSanPham.getTenSP());
        etGiaBan.setText(String.format("%.0f", currentSanPham.getGiaBan()));
        etSoLuongTon.setText(String.valueOf(currentSanPham.getSoLuongTon()));
        etMaVach.setText(currentSanPham.getMaVach());
        etMucCanhBao.setText(String.valueOf(currentSanPham.getMucCanhBao()));
        etKichCo.setText(currentSanPham.getKichCo());
        etMauSac.setText(currentSanPham.getMauSac());
        etMoTa.setText(currentSanPham.getMoTa());

        selectedHinhAnh = currentSanPham.getHinhAnh();
        FormatUtils.loadProductImage(imgHinhAnhSP, selectedHinhAnh, currentSanPham.getLoaiThuCung());
        if (selectedHinhAnh != null && !selectedHinhAnh.isEmpty() && !selectedHinhAnh.equals("sp_default")) {
            tvTrangThaiAnh.setText("Đã có ảnh sản phẩm");
        }

        for (int i = 0; i < speciesArray.length; i++) {
            if (speciesArray[i].equalsIgnoreCase(currentSanPham.getLoaiThuCung())) {
                spnLoai.setSelection(i);
                break;
            }
        }
    }

    private void saveProduct() {
        String ten = etTenSP.getText().toString().trim();
        if (ten.isEmpty()) {
            etTenSP.setError("Vui lòng nhập tên sản phẩm!");
            etTenSP.requestFocus();
            return;
        }

        String strGia = etGiaBan.getText().toString().trim();
        double gia = 0;
        try {
            gia = Double.parseDouble(strGia);
        } catch (Exception e) {
            etGiaBan.setError("Giá bán không hợp lệ!");
            etGiaBan.requestFocus();
            return;
        }

        String strSL = etSoLuongTon.getText().toString().trim();
        int sl = 0;
        try {
            sl = Integer.parseInt(strSL);
        } catch (Exception ignored) {}

        String maVach = etMaVach.getText().toString().trim();
        if (maVach.isEmpty()) {
            maVach = "893" + System.currentTimeMillis() % 100000;
        }

        String loai = spnLoai.getSelectedItem().toString();
        int canhBao = 5;
        try {
            canhBao = Integer.parseInt(etMucCanhBao.getText().toString().trim());
        } catch (Exception ignored) {}

        String kichCo = etKichCo.getText().toString().trim();
        String mauSac = etMauSac.getText().toString().trim();
        String moTa = etMoTa.getText().toString().trim();

        // Find matching category ID
        long maDM = 1;
        List<DanhMuc> dms = dbHelper.getAllDanhMuc();
        for (DanhMuc dm : dms) {
            if (dm.getTenDM().equalsIgnoreCase(loai)) {
                maDM = dm.getId();
                break;
            }
        }

        String hinhAnhToSave = (selectedHinhAnh != null && !selectedHinhAnh.isEmpty()) ? selectedHinhAnh : "sp_default";

        if (currentSanPham == null) {
            SanPham sp = new SanPham();
            sp.setTenSP(ten);
            sp.setGiaBan(gia);
            sp.setSoLuongTon(sl);
            sp.setMaVach(maVach);
            sp.setLoaiThuCung(loai);
            sp.setMaDM(maDM);
            sp.setMucCanhBao(canhBao);
            sp.setKichCo(kichCo);
            sp.setMauSac(mauSac);
            sp.setMoTa(moTa);
            sp.setHinhAnh(hinhAnhToSave);

            long id = dbHelper.insertSanPham(sp);
            if (id > 0) {
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi thêm!", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentSanPham.setTenSP(ten);
            currentSanPham.setGiaBan(gia);
            currentSanPham.setSoLuongTon(sl);
            currentSanPham.setMaVach(maVach);
            currentSanPham.setLoaiThuCung(loai);
            currentSanPham.setMaDM(maDM);
            currentSanPham.setMucCanhBao(canhBao);
            currentSanPham.setKichCo(kichCo);
            currentSanPham.setMauSac(mauSac);
            currentSanPham.setMoTa(moTa);
            currentSanPham.setHinhAnh(hinhAnhToSave);

            int rows = dbHelper.updateSanPham(currentSanPham);
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi cập nhật!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm " + currentSanPham.getTenSP() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteSanPham(currentSanPham.getId());
                    Toast.makeText(this, "Đã xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
