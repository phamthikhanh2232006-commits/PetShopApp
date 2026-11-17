package com.example.petshop.utils;

import android.content.Context;

import com.example.petshop.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

    public static String formatVND(double amount) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###", symbols);
            return df.format(amount) + " đ";
        } catch (Exception e) {
            return String.format(Locale.getDefault(), "%.0f đ", amount);
        }
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String formatDisplayDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "";
        try {
            if (dateStr.contains(" ")) {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = parser.parse(dateStr);
                return date != null ? formatter.format(date) : dateStr;
            } else if (dateStr.contains("-")) {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = parser.parse(dateStr);
                return date != null ? formatter.format(date) : dateStr;
            }
        } catch (Exception ignored) {}
        return dateStr;
    }

    public static int getProductDrawableRes(Context context, String hinhAnh, String loaiThuCung) {
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            String resName = hinhAnh.trim();
            if (resName.contains(".")) {
                resName = resName.substring(0, resName.lastIndexOf('.'));
            }
            int resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
            if (resId != 0) {
                return resId;
            }
        }
        if ("Chó".equalsIgnoreCase(loaiThuCung)) {
            return R.drawable.ic_dog;
        } else if ("Mèo".equalsIgnoreCase(loaiThuCung)) {
            return R.drawable.ic_cat;
        } else if ("Chim".equalsIgnoreCase(loaiThuCung)) {
            return R.drawable.ic_bird;
        } else if ("Cá".equalsIgnoreCase(loaiThuCung)) {
            return R.drawable.ic_fish;
        }
        return R.drawable.ic_paw;
    }
}
