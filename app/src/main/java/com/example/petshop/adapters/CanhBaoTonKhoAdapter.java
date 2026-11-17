package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.SanPham;

import java.util.List;

public class CanhBaoTonKhoAdapter extends RecyclerView.Adapter<CanhBaoTonKhoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onNhapKhoClick(SanPham sanPham);
    }

    private final Context context;
    private final List<SanPham> list;
    private final OnItemClickListener listener;

    public CanhBaoTonKhoAdapter(Context context, List<SanPham> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_canh_bao_ton_kho, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.tvTenSP.setText(sp.getTenSP());
        holder.tvLoaiThuCung.setText(sp.getLoaiThuCung());
        holder.tvSoLuongTon.setText("Tồn: " + sp.getSoLuongTon() + " (Ngưỡng: " + sp.getMucCanhBao() + ")");

        if (sp.getSoLuongTon() <= 0) {
            holder.tvTrangThai.setText("ĐÃ HẾT HÀNG");
            holder.tvTrangThai.setTextColor(context.getColor(R.color.danger));
        } else {
            holder.tvTrangThai.setText("SẮP HẾT HÀNG");
            holder.tvTrangThai.setTextColor(context.getColor(R.color.warning));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onNhapKhoClick(sp);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSP;
        TextView tvLoaiThuCung;
        TextView tvSoLuongTon;
        TextView tvTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSPCanhBao);
            tvLoaiThuCung = itemView.findViewById(R.id.tvLoaiCanhBao);
            tvSoLuongTon = itemView.findViewById(R.id.tvSoLuongTonCanhBao);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiCanhBao);
        }
    }
}
