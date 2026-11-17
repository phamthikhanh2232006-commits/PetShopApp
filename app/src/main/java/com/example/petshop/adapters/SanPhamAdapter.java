package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.SanPham;
import com.example.petshop.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    public interface OnSanPhamClickListener {
        void onSanPhamClick(SanPham sanPham);
        void onQuickAddToCart(SanPham sanPham);
    }

    private final Context context;
    private List<SanPham> list;
    private final OnSanPhamClickListener listener;

    public SanPhamAdapter(Context context, List<SanPham> list, OnSanPhamClickListener listener) {
        this.context = context;
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<SanPham> newList) {
        this.list = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.tvTenSP.setText(sp.getTenSP());
        holder.tvGiaSP.setText(FormatUtils.formatVND(sp.getGiaBan()));

        // Species tag / category
        if (sp.getLoaiThuCung() != null && !sp.getLoaiThuCung().isEmpty()) {
            holder.tvLoaiThuCung.setText(sp.getLoaiThuCung());
            holder.tvLoaiThuCung.setVisibility(View.VISIBLE);
        } else {
            holder.tvLoaiThuCung.setVisibility(View.GONE);
        }

        // Tồn kho cảnh báo
        if (sp.getSoLuongTon() <= 0) {
            holder.tvTonKho.setText("Hết hàng");
            holder.tvTonKho.setTextColor(context.getColor(R.color.danger));
        } else if (sp.isSapHetHang()) {
            holder.tvTonKho.setText("Còn " + sp.getSoLuongTon() + " món");
            holder.tvTonKho.setTextColor(context.getColor(R.color.warning));
        } else {
            holder.tvTonKho.setText("Còn " + sp.getSoLuongTon() + " món");
            holder.tvTonKho.setTextColor(context.getColor(R.color.text_muted));
        }

        // Icon theo loài hoặc ảnh từ drawable
        int drawableRes = FormatUtils.getProductDrawableRes(context, sp.getHinhAnh(), sp.getLoaiThuCung());
        holder.imgSP.setImageResource(drawableRes);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSanPhamClick(sp);
        });

        if (holder.btnAddToCart != null) {
            holder.btnAddToCart.setOnClickListener(v -> {
                if (listener != null) listener.onQuickAddToCart(sp);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSP;
        TextView tvTenSP;
        TextView tvGiaSP;
        TextView tvLoaiThuCung;
        TextView tvTonKho;
        View btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSP = itemView.findViewById(R.id.imgSP);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvGiaSP = itemView.findViewById(R.id.tvGiaSP);
            tvLoaiThuCung = itemView.findViewById(R.id.tvLoaiThuCung);
            tvTonKho = itemView.findViewById(R.id.tvTonKho);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
