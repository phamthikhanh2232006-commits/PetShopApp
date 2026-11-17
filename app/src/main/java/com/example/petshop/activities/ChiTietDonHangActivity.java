package com.example.petshop.activities;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.ChiTietDonHangAdapter;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.ChiTietDonHang;
import com.example.petshop.models.DonHang;
import com.example.petshop.utils.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private long maDH;
    private DonHang donHang;

    private TextView tvTieuDe;
    private TextView tvNgayDat;
    private TextView tvNguoiNhan;
    private TextView tvDiaChi;
    private TextView tvPhuongThuc;
    private TextView tvTongTien;
    private RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        maDH = getIntent().getLongExtra("EXTRA_MA_DON_HANG", 1);
        donHang = dbHelper.getDonHangById(maDH);

        initViews();
        if (donHang != null) {
            displayDetails();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvTieuDe = findViewById(R.id.tvTieuDeChiTietDH);
        tvNgayDat = findViewById(R.id.tvNgayDatChiTietDH);
        tvNguoiNhan = findViewById(R.id.tvNguoiNhanChiTietDH);
        tvDiaChi = findViewById(R.id.tvDiaChiChiTietDH);
        tvPhuongThuc = findViewById(R.id.tvPhuongThucChiTietDH);
        tvTongTien = findViewById(R.id.tvTongTienChiTietDH);
        rvItems = findViewById(R.id.rvSanPhamChiTietDH);

        rvItems.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackChiTietDH).setOnClickListener(v -> finish());

        findViewById(R.id.btnXuatHoaDon).setOnClickListener(v -> exportInvoiceFile());
    }

    private void exportInvoiceFile() {
        if (donHang == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("          PET SHOP - HÓA ĐƠN MUA HÀNG     \n");
        sb.append("========================================\n");
        sb.append("Mã đơn hàng: ").append(donHang.getMaDonHangHienThi()).append("\n");
        sb.append("Thời gian đặt: ").append(FormatUtils.formatDisplayDate(donHang.getNgayDat())).append("\n");
        sb.append("Khách hàng: ").append(donHang.getTenKhachHang()).append("\n");
        sb.append("Số điện thoại: ").append(donHang.getSoDienThoai()).append("\n");
        sb.append("Địa chỉ giao: ").append(donHang.getDiaChiGiaoHang()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DANH SÁCH SẢN PHẨM:\n");
        if (donHang.getDanhSachChiTiet() != null) {
            for (ChiTietDonHang ct : donHang.getDanhSachChiTiet()) {
                sb.append("- ").append(ct.getTenSP())
                        .append(" (Size: ").append(ct.getSizeDaChon()).append(", Màu: ").append(ct.getMauDaChon()).append(")\n")
                        .append("  SL: ").append(ct.getSoLuong()).append(" x ").append(FormatUtils.formatVND(ct.getDonGia()))
                        .append(" = ").append(FormatUtils.formatVND(ct.getThanhTien())).append("\n");
            }
        }
        sb.append("----------------------------------------\n");
        sb.append("Phương thức thanh toán: ").append(donHang.getPhuongThucTT()).append("\n");
        sb.append("TỔNG TIỀN THANH TOÁN: ").append(FormatUtils.formatVND(donHang.getTongTien())).append("\n");
        sb.append("========================================\n");
        sb.append("Cảm ơn quý khách đã mua sắm tại Pet Shop!\n");

        String fileName = "HoaDon_" + donHang.getMaDonHangHienThi().replace("#", "") + ".txt";

        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }
            File file = new File(downloadsDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.close();

            new AlertDialog.Builder(this)
                    .setTitle("Xuất hóa đơn thành công")
                    .setMessage("Đã tải xuống hóa đơn thành công về máy!\nĐường dẫn: Downloads/" + fileName)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (Exception e) {
            try {
                File file = new File(getExternalFilesDir(null), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes("UTF-8"));
                fos.close();

                new AlertDialog.Builder(this)
                        .setTitle("Xuất hóa đơn thành công")
                        .setMessage("Đã lưu file hóa đơn vào máy:\n" + file.getAbsolutePath())
                        .setPositiveButton("OK", null)
                        .show();
            } catch (Exception ex) {
                Toast.makeText(this, "Lỗi khi lưu file hóa đơn: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayDetails() {
        tvTieuDe.setText("Chi tiết đơn hàng " + donHang.getMaDonHangHienThi());
        tvNgayDat.setText("Thời gian đặt: " + FormatUtils.formatDisplayDate(donHang.getNgayDat()));
        tvNguoiNhan.setText("Người nhận: " + donHang.getTenKhachHang() + " (" + donHang.getSoDienThoai() + ")");
        tvDiaChi.setText("Địa chỉ: " + donHang.getDiaChiGiaoHang());
        tvPhuongThuc.setText(donHang.getPhuongThucTT());
        tvTongTien.setText(FormatUtils.formatVND(donHang.getTongTien()));

        List<ChiTietDonHang> items = donHang.getDanhSachChiTiet();
        ChiTietDonHangAdapter adapter = new ChiTietDonHangAdapter(this, items);
        rvItems.setAdapter(adapter);
    }
}
