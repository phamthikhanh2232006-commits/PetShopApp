package com.example.petshop.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.ThuCungAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.ThuCung;
import com.example.petshop.utils.SessionManager;

import java.util.Calendar;
import java.util.List;

public class HoSoThuCungActivity extends AppCompatActivity implements ThuCungAdapter.OnThuCungClickListener {

    private static final int REQUEST_CODE_PICK_PET_IMAGE = 3002;

    private DatabaseHelper dbHelper;
    private SessionManager session;

    private EditText etTenTC;
    private Spinner spnLoai;
    private EditText etGiongLoai;
    private TextView tvNgaySinh;
    private RadioGroup rgGioiTinh;
    private RadioButton rbDuc;
    private RadioButton rbCai;
    private EditText etCanNang;
    private EditText etGhiChu;
    private RecyclerView rvThuCung;
    private ThuCungAdapter adapter;
    private ImageView imgThuCungAvatar;

    private String selectedBirthDate = "";
    private String selectedPetAvatarUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_thu_cung);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        loadThuCungList();
    }

    private void initViews() {
        etTenTC = findViewById(R.id.etTenThuCung);
        spnLoai = findViewById(R.id.spnLoaiThuCung);
        etGiongLoai = findViewById(R.id.etGiongLoai);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbDuc = findViewById(R.id.rbDuc);
        rbCai = findViewById(R.id.rbCai);
        etCanNang = findViewById(R.id.etCanNang);
        etGhiChu = findViewById(R.id.etGhiChuThuCung);
        rvThuCung = findViewById(R.id.rvThuCungCuaToi);
        imgThuCungAvatar = findViewById(R.id.imgThuCungAvatar);

        rvThuCung.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackHoSoThuCung).setOnClickListener(v -> finish());

        // Pick pet image from device storage
        findViewById(R.id.layoutThuCungAvatarPicker).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_PET_IMAGE);
        });

        // Setup Spinner loài
        String[] loaiOptions = {"Chó", "Mèo", "Chim", "Cá", "Thú cưng khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiOptions);
        spnLoai.setAdapter(spinnerAdapter);

        // Date Picker
        findViewById(R.id.layoutChonNgaySinh).setOnClickListener(v -> showDatePicker());

        // Nút Lưu
        findViewById(R.id.btnLuuThuCung).setOnClickListener(v -> saveThuCung());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedBirthDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            tvNgaySinh.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PET_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                selectedPetAvatarUri = uri.toString();
                imgThuCungAvatar.setImageURI(uri);
                Toast.makeText(this, "Đã chọn ảnh đại diện thú cưng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveThuCung() {
        String ten = etTenTC.getText().toString().trim();
        if (ten.isEmpty()) {
            etTenTC.setError("Vui lòng nhập tên thú cưng!");
            etTenTC.requestFocus();
            return;
        }

        String loai = spnLoai.getSelectedItem().toString();
        String giong = etGiongLoai.getText().toString().trim();
        if (giong.isEmpty()) giong = "Thuần chủng";

        String gioiTinh = rbDuc.isChecked() ? "Đực" : "Cái";

        double canNang = 0;
        try {
            canNang = Double.parseDouble(etCanNang.getText().toString().trim());
        } catch (Exception ignored) {}

        String ghiChu = etGhiChu.getText().toString().trim();

        ThuCung tc = new ThuCung();
        tc.setMaKH(session.getUserId());
        tc.setTenTC(ten);
        tc.setLoaiThuCung(loai);
        tc.setGiongLoai(giong);
        tc.setCanNang(canNang);
        tc.setGioiTinh(gioiTinh);
        tc.setNgaySinh(selectedBirthDate);
        tc.setGhiChu(ghiChu);
        tc.setKichCo(canNang < 5 ? "Nhỏ" : (canNang < 15 ? "Vừa" : "Lớn"));
        tc.setHinhAnh(selectedPetAvatarUri);

        long id = dbHelper.insertThuCung(tc);
        if (id > 0) {
            Toast.makeText(this, "Đã lưu hồ sơ thú cưng thành công!", Toast.LENGTH_SHORT).show();
            clearForm();
            loadThuCungList();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi lưu hồ sơ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etTenTC.setText("");
        etGiongLoai.setText("");
        etCanNang.setText("");
        etGhiChu.setText("");
        tvNgaySinh.setText("Chọn ngày sinh");
        selectedBirthDate = "";
        selectedPetAvatarUri = "";
        imgThuCungAvatar.setImageResource(R.drawable.ic_paw);
        rbDuc.setChecked(true);
    }

    private void loadThuCungList() {
        List<ThuCung> list = dbHelper.getThuCungByKhachHang(session.getUserId());
        adapter = new ThuCungAdapter(this, list, this);
        rvThuCung.setAdapter(adapter);
    }

    @Override
    public void onThuCungClick(ThuCung thuCung) {
        new AlertDialog.Builder(this)
                .setTitle(thuCung.getTenTC() + " (" + thuCung.getLoaiThuCung() + ")")
                .setMessage("Giống loài: " + thuCung.getGiongLoai() +
                        "\nCân nặng: " + thuCung.getCanNang() + " kg" +
                        "\nGiới tính: " + thuCung.getGioiTinh() +
                        "\nNgày sinh: " + (thuCung.getNgaySinh() != null ? thuCung.getNgaySinh() : "Chưa rõ") +
                        "\nGhi chú: " + (thuCung.getGhiChu() != null ? thuCung.getGhiChu() : "Không có"))
                .setPositiveButton("Đóng", null)
                .show();
    }

    @Override
    public void onThuCungDelete(ThuCung thuCung) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa hồ sơ của thú cưng " + thuCung.getTenTC() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteThuCung(thuCung.getId());
                    loadThuCungList();
                    Toast.makeText(this, "Đã xóa hồ sơ thú cưng!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
