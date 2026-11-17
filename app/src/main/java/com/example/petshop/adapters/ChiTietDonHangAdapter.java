package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.ChiTietDonHang;
import com.example.petshop.utils.FormatUtils;

import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder> {

    private final Context context;
    private final List<ChiTietDonHang> list;

    public ChiTietDonHangAdapter(Context context, List<ChiTietDonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chi_tiet_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietDonHang item = list.get(position);
        holder.tvTenSP.setText(item.getTenSP());
        holder.tvPhanLoai.setText(item.getPhanLoaiHienThi());
        holder.tvDonGia.setText(FormatUtils.formatVND(item.getDonGia()) + " x " + item.getSoLuong());
        holder.tvThanhTien.setText(FormatUtils.formatVND(item.getThanhTien()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSP;
        TextView tvPhanLoai;
        TextView tvDonGia;
        TextView tvThanhTien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSPChiTiet);
            tvPhanLoai = itemView.findViewById(R.id.tvPhanLoaiChiTiet);
            tvDonGia = itemView.findViewById(R.id.tvDonGiaChiTiet);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTienChiTiet);
        }
    }
}
