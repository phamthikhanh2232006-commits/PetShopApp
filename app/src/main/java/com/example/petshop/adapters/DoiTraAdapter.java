package com.example.petshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.PhieuDoiTra;
import com.example.petshop.utils.FormatUtils;

import java.util.List;

public class DoiTraAdapter extends RecyclerView.Adapter<DoiTraAdapter.ViewHolder> {

    private final Context context;
    private final List<PhieuDoiTra> list;

    public DoiTraAdapter(Context context, List<PhieuDoiTra> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doi_tra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhieuDoiTra item = list.get(position);
        holder.tvMaDoiTra.setText("Mã đổi trả: #DT" + item.getId() + " (Đơn #" + item.getMaDH() + ")");
        holder.tvTenSP.setText(item.getTenSP() != null ? item.getTenSP() : "Sản phẩm #" + item.getMaSP());
        holder.tvLyDo.setText("Lý do: " + item.getLyDo());
        holder.tvSoLuongHoan.setText("Số lượng hoàn: " + item.getSoLuong() + " món");
        holder.tvSoTienHoan.setText("Hoàn tiền: " + FormatUtils.formatVND(item.getSoTienHoan()));
        holder.tvNgayDoiTra.setText(FormatUtils.formatDisplayDate(item.getNgayDoiTra()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDoiTra;
        TextView tvTenSP;
        TextView tvLyDo;
        TextView tvSoLuongHoan;
        TextView tvSoTienHoan;
        TextView tvNgayDoiTra;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDoiTra = itemView.findViewById(R.id.tvMaDoiTra);
            tvTenSP = itemView.findViewById(R.id.tvTenSPDoiTra);
            tvLyDo = itemView.findViewById(R.id.tvLyDoDoiTra);
            tvSoLuongHoan = itemView.findViewById(R.id.tvSoLuongHoan);
            tvSoTienHoan = itemView.findViewById(R.id.tvSoTienHoan);
            tvNgayDoiTra = itemView.findViewById(R.id.tvNgayDoiTra);
        }
    }
}
