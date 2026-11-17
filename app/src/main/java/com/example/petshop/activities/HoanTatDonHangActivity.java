package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;

public class HoanTatDonHangActivity extends AppCompatActivity {

    private long maDH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoan_tat_don_hang);

        maDH = getIntent().getLongExtra("EXTRA_MA_DON_HANG", 1);

        TextView tvMaDH = findViewById(R.id.tvMaDonHangHoanTat);
        tvMaDH.setText(String.format("#DH%06d", maDH));

        findViewById(R.id.btnXemChiTietDonHang).setOnClickListener(v -> {
            Intent intent = new Intent(HoanTatDonHangActivity.this, ChiTietDonHangActivity.class);
            intent.putExtra("EXTRA_MA_DON_HANG", maDH);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnVeTrangChu).setOnClickListener(v -> {
            Intent intent = new Intent(HoanTatDonHangActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
