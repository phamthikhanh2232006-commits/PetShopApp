package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.DonHang;
import com.example.petshop.utils.FormatUtils;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    public interface OnDonHangClickListener {
        void onDonHangClick(DonHang donHang);
    }

    private final Context context;
    private final List<DonHang> list;
    private final OnDonHangClickListener listener;

    public DonHangAdapter(Context context, List<DonHang> list, OnDonHangClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang item = list.get(position);
        holder.tvMaDH.setText(item.getMaDonHangHienThi());
        holder.tvTenKH.setText(item.getTenKhachHang() != null ? item.getTenKhachHang() : "Khách lẻ");
        holder.tvNgayDat.setText(FormatUtils.formatDisplayDate(item.getNgayDat()));
        holder.tvTongTien.setText(FormatUtils.formatVND(item.getTongTien()));
        holder.tvSoLuongSP.setText(item.getSoLuongSP() + " sản phẩm");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDonHangClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDH;
        TextView tvTenKH;
        TextView tvNgayDat;
        TextView tvTongTien;
        TextView tvSoLuongSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);
            tvTenKH = itemView.findViewById(R.id.tvTenKH);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvSoLuongSP = itemView.findViewById(R.id.tvSoLuongSP);
        }
    }
}
