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
import com.example.petshop.models.DanhMuc;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(DanhMuc danhMuc);
    }

    private final Context context;
    private final List<DanhMuc> list;
    private final OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public DanhMucAdapter(Context context, List<DanhMuc> list, OnCategoryClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setSelectedPosition(int pos) {
        this.selectedPosition = pos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc item = list.get(position);
        holder.tvTenDM.setText(item.getTenDM());

        // Gán icon tương ứng theo loài
        String icon = item.getIconName();
        if ("ic_dog".equalsIgnoreCase(icon) || item.getTenDM().contains("Chó")) {
            holder.imgIcon.setImageResource(R.drawable.ic_dog);
        } else if ("ic_cat".equalsIgnoreCase(icon) || item.getTenDM().contains("Mèo")) {
            holder.imgIcon.setImageResource(R.drawable.ic_cat);
        } else if ("ic_bird".equalsIgnoreCase(icon) || item.getTenDM().contains("Chim")) {
            holder.imgIcon.setImageResource(R.drawable.ic_bird);
        } else if ("ic_fish".equalsIgnoreCase(icon) || item.getTenDM().contains("Cá")) {
            holder.imgIcon.setImageResource(R.drawable.ic_fish);
        } else {
            holder.imgIcon.setImageResource(R.drawable.ic_paw);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTenDM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIconDM);
            tvTenDM = itemView.findViewById(R.id.tvTenDM);
        }
    }
}
