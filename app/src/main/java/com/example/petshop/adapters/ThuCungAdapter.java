package com.example.petshop.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.ThuCung;

import java.util.List;

public class ThuCungAdapter extends RecyclerView.Adapter<ThuCungAdapter.ViewHolder> {

    public interface OnThuCungClickListener {
        void onThuCungClick(ThuCung thuCung);
        void onThuCungDelete(ThuCung thuCung);
    }

    private final Context context;
    private final List<ThuCung> list;
    private final OnThuCungClickListener listener;

    public ThuCungAdapter(Context context, List<ThuCung> list, OnThuCungClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thu_cung, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuCung tc = list.get(position);
        holder.tvTenTC.setText(tc.getTenTC());
        holder.tvMoTa.setText(tc.getMoTaNgan());
        holder.tvGioiTinh.setText(tc.getGioiTinh() != null ? tc.getGioiTinh() : "Không rõ");

        com.example.petshop.utils.FormatUtils.loadPetImage(holder.imgTC, tc.getHinhAnh(), tc.getLoaiThuCung());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onThuCungClick(tc);
        });

        if (holder.btnXoa != null) {
            holder.btnXoa.setOnClickListener(v -> {
                if (listener != null) listener.onThuCungDelete(tc);
            });
        }
    }

    private void setSpeciesIcon(ImageView img, String loai) {
        if ("Chó".equalsIgnoreCase(loai)) {
            img.setImageResource(R.drawable.ic_dog);
        } else if ("Mèo".equalsIgnoreCase(loai)) {
            img.setImageResource(R.drawable.ic_cat);
        } else if ("Chim".equalsIgnoreCase(loai)) {
            img.setImageResource(R.drawable.ic_bird);
        } else if ("Cá".equalsIgnoreCase(loai)) {
            img.setImageResource(R.drawable.ic_fish);
        } else {
            img.setImageResource(R.drawable.ic_paw);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTC;
        TextView tvTenTC;
        TextView tvMoTa;
        TextView tvGioiTinh;
        ImageView btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTC = itemView.findViewById(R.id.imgTC);
            tvTenTC = itemView.findViewById(R.id.tvTenTC);
            tvMoTa = itemView.findViewById(R.id.tvMoTaTC);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinhTC);
            btnXoa = itemView.findViewById(R.id.btnXoaTC);
        }
    }
}
