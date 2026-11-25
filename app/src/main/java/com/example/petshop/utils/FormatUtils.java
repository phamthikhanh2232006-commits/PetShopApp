package com.example.petshop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.example.petshop.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

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

    /**
     * Remove accents/diacritics from Vietnamese string for fuzzy, accent-insensitive search.
     * E.g. "Sữa tắm SOS" -> "sua tam sos"
     */
    public static String removeAccent(String s) {
        if (s == null) return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("")
                .replace('đ', 'd').replace('Đ', 'D')
                .toLowerCase(Locale.getDefault())
                .trim();
        return result;
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

    /**
     * Universal image loader for products: handles file paths, content URIs, and drawable resources.
     */
    public static void loadProductImage(ImageView imageView, String hinhAnh, String loaiThuCung) {
        if (imageView == null) return;
        Context context = imageView.getContext();
        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            String path = hinhAnh.trim();
            // Check if it's a file path
            if (path.startsWith("/") || path.startsWith("file://")) {
                File file = new File(path.replace("file://", ""));
                if (file.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                        return;
                    }
                }
            } else if (path.startsWith("content://")) {
                try {
                    imageView.setImageURI(Uri.parse(path));
                    return;
                } catch (Exception ignored) {}
            }
            // Check drawable resource
            int resId = getProductDrawableRes(context, path, loaiThuCung);
            imageView.setImageResource(resId);
        } else {
            imageView.setImageResource(getProductDrawableRes(context, null, loaiThuCung));
        }
    }

    /**
     * Universal image loader for pets.
     */
    public static void loadPetImage(ImageView imageView, String hinhAnh, String loaiThuCung) {
        if (imageView == null) return;
        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            String path = hinhAnh.trim();
            if (path.startsWith("/") || path.startsWith("file://")) {
                File file = new File(path.replace("file://", ""));
                if (file.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                        return;
                    }
                }
            } else if (path.startsWith("content://")) {
                try {
                    imageView.setImageURI(Uri.parse(path));
                    return;
                } catch (Exception ignored) {}
            }
        }
        imageView.setImageResource(R.drawable.ic_paw);
    }
}
