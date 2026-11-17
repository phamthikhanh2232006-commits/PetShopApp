package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.TinTuc;

import java.util.List;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.ViewHolder> {

    public interface OnReadMoreClickListener {
        void onReadMoreClick(TinTuc tinTuc);
    }

    private final Context context;
    private final List<TinTuc> list;
    private final OnReadMoreClickListener listener;

    public TinTucAdapter(Context context, List<TinTuc> list, OnReadMoreClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tin_tuc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TinTuc item = list.get(position);
        holder.tvTieuDe.setText(item.getTieuDe());
        holder.tvMoTaNgan.setText(item.getMoTaNgan());
        holder.imgTinTuc.setImageResource(item.getHinhAnhRes() != 0 ? item.getHinhAnhRes() : R.drawable.ic_paw);

        holder.btnReadMore.setOnClickListener(v -> {
            if (listener != null) listener.onReadMoreClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTinTuc;
        TextView tvTieuDe;
        TextView tvMoTaNgan;
        Button btnReadMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTinTuc = itemView.findViewById(R.id.imgTinTuc);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDeTinTuc);
            tvMoTaNgan = itemView.findViewById(R.id.tvMoTaNganTinTuc);
            btnReadMore = itemView.findViewById(R.id.btnReadMore);
        }
    }
}
