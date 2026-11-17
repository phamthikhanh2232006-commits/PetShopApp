package com.example.petshop.utils;

import com.example.petshop.models.CartItem;
import com.example.petshop.models.SanPham;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> items;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private final List<OnCartChangeListener> listeners = new ArrayList<>();

    private CartManager() {
        items = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addListener(OnCartChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnCartChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (OnCartChangeListener listener : listeners) {
            if (listener != null) listener.onCartChanged();
        }
    }

    public void addItem(SanPham sanPham, int quantity, String size, String color) {
        if (sanPham == null || quantity <= 0) return;

        // Check if item with same ID, size, and color already exists
        for (CartItem item : items) {
            if (item.getSanPham().getId() == sanPham.getId()) {
                boolean sameSize = (size == null && item.getSelectedSize() == null) ||
                        (size != null && size.equals(item.getSelectedSize()));
                boolean sameColor = (color == null && item.getSelectedColor() == null) ||
                        (color != null && color.equals(item.getSelectedColor()));

                if (sameSize && sameColor) {
                    item.setSoLuong(item.getSoLuong() + quantity);
                    notifyListeners();
                    return;
                }
            }
        }

        items.add(new CartItem(sanPham, quantity, size, color));
        notifyListeners();
    }

    public void updateQuantity(int position, int newQuantity) {
        if (position >= 0 && position < items.size()) {
            if (newQuantity <= 0) {
                items.remove(position);
            } else {
                items.get(position).setSoLuong(newQuantity);
            }
            notifyListeners();
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyListeners();
        }
    }

    public void clearCart() {
        items.clear();
        notifyListeners();
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getThanhTien();
        }
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) {
            count += item.getSoLuong();
        }
        return count;
    }
}
