package com.example.petshop.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.SanPham;

public class ScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1001;

    private DatabaseHelper dbHelper;
    private EditText etManualBarcode;
    private Button btnLookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        dbHelper = DatabaseHelper.getInstance(this);

        initViews();
        checkCameraPermission();
    }

    private void initViews() {
        etManualBarcode = findViewById(R.id.etManualBarcode);
        btnLookup = findViewById(R.id.btnLookupBarcode);

        findViewById(R.id.btnBackScanner).setOnClickListener(v -> finish());

        btnLookup.setOnClickListener(v -> {
            String barcode = etManualBarcode.getText().toString().trim();
            if (barcode.isEmpty()) {
                etManualBarcode.setError("Vui lòng nhập mã vạch!");
                return;
            }
            returnBarcodeResult(barcode);
        });

        // Quick sample barcodes for demo
        findViewById(R.id.btnBarcodeSample1).setOnClickListener(v -> returnBarcodeResult("893001"));
        findViewById(R.id.btnBarcodeSample2).setOnClickListener(v -> returnBarcodeResult("893003"));
        findViewById(R.id.btnBarcodeSample3).setOnClickListener(v -> returnBarcodeResult("893004"));
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền Camera quét mã vạch!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Chưa cấp quyền Camera. Bạn có thể nhập mã vạch bằng tay.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void returnBarcodeResult(String barcode) {
        SanPham sp = dbHelper.getSanPhamByBarcode(barcode);
        if (sp != null) {
            Toast.makeText(this, "Đã tìm thấy: " + sp.getTenSP(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mã vạch: " + barcode, Toast.LENGTH_SHORT).show();
        }

        Intent resultData = new Intent();
        resultData.putExtra("SCAN_RESULT", barcode);
        setResult(RESULT_OK, resultData);
        finish();
    }
}
