package com.example.petshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petshop.models.TaiKhoan;

public class SessionManager {
    private static final String PREF_NAME = "PetShopSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_AVATAR_URI = "avatarUri";
    private static final String KEY_PAYMENT_METHOD = "paymentMethod";
    private static final String KEY_PROMO_CODE = "promoCode";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance;

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    private SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(TaiKhoan user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getTenDangNhap());
        editor.putString(KEY_FULLNAME, user.getHoTen());
        editor.putString(KEY_ROLE, user.getVaiTro());
        editor.putString(KEY_PHONE, user.getSoDienThoai());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_ADDRESS, user.getDiaChi());
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return pref.getLong(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "khachhang");
    }

    public String getFullName() {
        return pref.getString(KEY_FULLNAME, "Nguyễn Văn A");
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "KhachHang");
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(getRole());
    }

    public String getPhone() {
        return pref.getString(KEY_PHONE, "0987654321");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "nguyenvana@gmail.com");
    }

    public String getAddress() {
        return pref.getString(KEY_ADDRESS, "123 Đường ABC, Quận 1, TP.HCM");
    }

    public void updateAddress(String address) {
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public void updateAvatarUri(String uri) {
        editor.putString(KEY_AVATAR_URI, uri);
        editor.apply();
    }

    public String getAvatarUri() {
        return pref.getString(KEY_AVATAR_URI, "");
    }

    public void updatePaymentMethod(String method) {
        editor.putString(KEY_PAYMENT_METHOD, method);
        editor.apply();
    }

    public String getPaymentMethod() {
        return pref.getString(KEY_PAYMENT_METHOD, "Thanh toán khi nhận hàng (COD)");
    }

    public void updatePromoCode(String code) {
        editor.putString(KEY_PROMO_CODE, code);
        editor.apply();
    }

    public String getPromoCode() {
        return pref.getString(KEY_PROMO_CODE, "PETSHOP2026");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
