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
import com.example.petshop.models.CartItem;
import com.example.petshop.utils.CartManager;
import com.example.petshop.utils.FormatUtils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface OnCartItemInteractionListener {
        void onCartUpdated();
    }

    private final Context context;
    private final List<CartItem> list;
    private final OnCartItemInteractionListener listener;

    public CartAdapter(Context context, List<CartItem> list, OnCartItemInteractionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = list.get(position);
        holder.tvTenSP.setText(item.getSanPham().getTenSP());
        holder.tvPhanLoai.setText(item.getPhanLoaiHienThi());
        holder.tvGiaSP.setText(FormatUtils.formatVND(item.getSanPham().getGiaBan()));
        holder.tvSoLuong.setText(String.valueOf(item.getSoLuong()));

        // Hiển thị ảnh sản phẩm
        FormatUtils.loadProductImage(holder.imgSP, item.getSanPham().getHinhAnh(), item.getSanPham().getLoaiThuCung());

        // Minus
        holder.btnGiam.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                int currentQty = item.getSoLuong();
                if (currentQty > 1) {
                    CartManager.getInstance().updateQuantity(currentPos, currentQty - 1);
                    notifyItemChanged(currentPos);
                } else {
                    CartManager.getInstance().removeItem(currentPos);
                    notifyItemRemoved(currentPos);
                }
                if (listener != null) listener.onCartUpdated();
            }
        });

        // Plus
        holder.btnTang.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                int currentQty = item.getSoLuong();
                if (currentQty < item.getSanPham().getSoLuongTon()) {
                    CartManager.getInstance().updateQuantity(currentPos, currentQty + 1);
                    notifyItemChanged(currentPos);
                    if (listener != null) listener.onCartUpdated();
                }
            }
        });

        // Xóa
        holder.btnXoa.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                CartManager.getInstance().removeItem(currentPos);
                notifyItemRemoved(currentPos);
                if (listener != null) listener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSP;
        TextView tvTenSP;
        TextView tvPhanLoai;
        TextView tvGiaSP;
        TextView tvSoLuong;
        View btnGiam;
        View btnTang;
        ImageView btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSP = itemView.findViewById(R.id.imgCartItem);
            tvTenSP = itemView.findViewById(R.id.tvCartTenSP);
            tvPhanLoai = itemView.findViewById(R.id.tvCartPhanLoai);
            tvGiaSP = itemView.findViewById(R.id.tvCartGiaSP);
            tvSoLuong = itemView.findViewById(R.id.tvCartSoLuong);
            btnGiam = itemView.findViewById(R.id.btnCartGiam);
            btnTang = itemView.findViewById(R.id.btnCartTang);
            btnXoa = itemView.findViewById(R.id.btnCartXoa);
        }
    }
}
