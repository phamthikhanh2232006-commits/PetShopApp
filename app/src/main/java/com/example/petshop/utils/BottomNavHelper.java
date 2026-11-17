package com.example.petshop.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petshop.R;
import com.example.petshop.activities.DanhSachSanPhamActivity;
import com.example.petshop.activities.GioHangActivity;
import com.example.petshop.activities.HoSoActivity;
import com.example.petshop.activities.MainActivity;

public class BottomNavHelper {

    public static final int TAB_HOME = 1;
    public static final int TAB_DANH_MUC = 2;
    public static final int TAB_GIO_HANG = 3;
    public static final int TAB_HO_SO = 4;

    public static void setupBottomNav(Activity activity, int currentTab) {
        View navHome = activity.findViewById(R.id.navHome);
        View navDanhMuc = activity.findViewById(R.id.navDanhMuc);
        View navGioHang = activity.findViewById(R.id.navGioHang);
        View navHoSo = activity.findViewById(R.id.navHoSo);

        TextView tvNavHome = activity.findViewById(R.id.tvNavHome);
        TextView tvNavDanhMuc = activity.findViewById(R.id.tvNavDanhMuc);
        TextView tvNavGioHang = activity.findViewById(R.id.tvNavGioHang);
        TextView tvNavHoSo = activity.findViewById(R.id.tvNavHoSo);

        ImageView imgNavHome = activity.findViewById(R.id.imgNavHome);
        ImageView imgNavDanhMuc = activity.findViewById(R.id.imgNavDanhMuc);
        ImageView imgNavGioHang = activity.findViewById(R.id.imgNavGioHang);
        ImageView imgNavHoSo = activity.findViewById(R.id.imgNavHoSo);

        TextView tvCartBadge = activity.findViewById(R.id.tvCartBadge);

        // Update cart badge
        if (tvCartBadge != null) {
            int count = CartManager.getInstance().getItemCount();
            if (count > 0) {
                tvCartBadge.setVisibility(View.VISIBLE);
                tvCartBadge.setText(String.valueOf(count));
            } else {
                tvCartBadge.setVisibility(View.GONE);
            }
        }

        int activeColor = activity.getColor(R.color.primary);
        int inactiveColor = activity.getColor(R.color.text_muted);

        if (tvNavHome != null) tvNavHome.setTextColor(currentTab == TAB_HOME ? activeColor : inactiveColor);
        if (tvNavDanhMuc != null) tvNavDanhMuc.setTextColor(currentTab == TAB_DANH_MUC ? activeColor : inactiveColor);
        if (tvNavGioHang != null) tvNavGioHang.setTextColor(currentTab == TAB_GIO_HANG ? activeColor : inactiveColor);
        if (tvNavHoSo != null) tvNavHoSo.setTextColor(currentTab == TAB_HO_SO ? activeColor : inactiveColor);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                if (currentTab != TAB_HOME) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    activity.startActivity(intent);
                }
            });
        }

        if (navDanhMuc != null) {
            navDanhMuc.setOnClickListener(v -> {
                if (currentTab != TAB_DANH_MUC) {
                    Intent intent = new Intent(activity, DanhSachSanPhamActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

        if (navGioHang != null) {
            navGioHang.setOnClickListener(v -> {
                if (currentTab != TAB_GIO_HANG) {
                    Intent intent = new Intent(activity, GioHangActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

        if (navHoSo != null) {
            navHoSo.setOnClickListener(v -> {
                if (currentTab != TAB_HO_SO) {
                    Intent intent = new Intent(activity, HoSoActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
