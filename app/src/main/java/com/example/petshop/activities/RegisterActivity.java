package com.example.petshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.database.DatabaseHelper;
import com.example.petshop.models.TaiKhoan;
import com.example.petshop.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager session;

    private EditText etHoTen;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etDiaChi;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
    }

    private void initViews() {
        etHoTen = findViewById(R.id.etRegHoTen);
        etPhone = findViewById(R.id.etRegPhone);
        etEmail = findViewById(R.id.etRegEmail);
        etDiaChi = findViewById(R.id.etRegDiaChi);
        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        btnSubmit = findViewById(R.id.btnSubmitRegister);

        findViewById(R.id.btnBackRegister).setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> performRegister());
    }

    private void performRegister() {
        String hoTen = etHoTen.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String diaChi = etDiaChi.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (hoTen.isEmpty()) {
            etHoTen.setError("Vui lòng nhập họ tên!");
            etHoTen.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại!");
            etPhone.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập!");
            etUsername.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu cần tối thiểu 6 ký tự!");
            etPassword.requestFocus();
            return;
        }

        if (diaChi.isEmpty()) {
            diaChi = "Chưa cập nhật địa chỉ";
        }

        long userId = dbHelper.register(username, password, hoTen, phone, email, diaChi);
        if (userId > 0) {
            TaiKhoan tk = new TaiKhoan(userId, username, password, hoTen, phone, email, "KhachHang", diaChi);
            session.saveUser(tk);
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_LONG).show();
        }
    }
}
