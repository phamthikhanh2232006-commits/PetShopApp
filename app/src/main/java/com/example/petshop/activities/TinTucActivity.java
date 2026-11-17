package com.example.petshop.activities;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapters.TinTucAdapter;
import com.example.petshop.models.TinTuc;

import java.util.ArrayList;
import java.util.List;

public class TinTucActivity extends AppCompatActivity implements TinTucAdapter.OnReadMoreClickListener {

    private RecyclerView rvTinTuc;
    private TinTucAdapter adapter;
    private List<TinTuc> listTinTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);

        initViews();
        loadData();
    }

    private void initViews() {
        rvTinTuc = findViewById(R.id.rvTinTuc);
        rvTinTuc.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackTinTuc).setOnClickListener(v -> finish());
    }

    private void loadData() {
        listTinTuc = new ArrayList<>();
        listTinTuc.add(new TinTuc(
                1,
                "Bí quyết chăm sóc lông cún cưng luôn óng mượt",
                "Chăm sóc lông cho chó cưng giúp thú cưng trông đáng yêu hơn và bảo vệ da khỏi các bệnh về ký sinh trùng...",
                "Chăm sóc lông cho chó cưng không chỉ giúp thú cưng trông đáng yêu hơn mà còn bảo vệ da khỏi các bệnh về ký sinh trùng. Bạn nên chải lông hàng ngày, sử dụng sữa tắm chuyên dụng phù hợp với từng giống chó và bổ sung các dưỡng chất omega-3 trong bữa ăn hàng ngày để lông luôn khỏe mạnh, bóng mượt.",
                R.drawable.ic_dog
        ));
        listTinTuc.add(new TinTuc(
                2,
                "Chế độ dinh dưỡng khoa học cho mèo Anh lông ngắn",
                "Mèo Anh lông ngắn rất dễ bị béo phì nếu không kiểm soát lượng thức ăn và cung cấp đủ nước sạch mỗi ngày...",
                "Mèo Anh lông ngắn rất dễ bị béo phì nếu không kiểm soát lượng thức ăn và cung cấp đủ nước sạch mỗi ngày. Bạn cần chia nhỏ các bữa ăn, kết hợp giữa hạt khô chất lượng cao và pate tươi, đồng thời khuyến khích chúng vận động nhiều hơn qua các món đồ chơi bắt chuột giả hoặc cần câu mèo.",
                R.drawable.ic_cat
        ));
        listTinTuc.add(new TinTuc(
                3,
                "Top 5 phụ kiện không thể thiếu khi nuôi thú cưng",
                "Chuẩn bị đầy đủ vòng cổ, dây dắt, bát ăn thông minh, nhà ngủ êm ái và đồ chơi gặm nướu sẽ giúp thú cưng phát triển...",
                "Chuẩn bị đầy đủ vòng cổ, dây dắt, bát ăn thông minh, nhà ngủ êm ái và đồ chơi gặm nướu sẽ giúp thú cưng phát triển toàn diện và hạnh phúc. Những phụ kiện này không chỉ phục vụ sinh hoạt hàng ngày mà còn giúp rèn luyện thói quen tốt cho Boss của bạn.",
                R.drawable.ic_paw
        ));
        listTinTuc.add(new TinTuc(
                4,
                "Kinh nghiệm nuôi cá cảnh nước ngọt cho người mới bắt đầu",
                "Nuôi cá cảnh đòi hỏi sự tỉ mỉ trong việc thiết lập hệ thống lọc nước, cân bằng nhiệt độ và kiểm soát lượng thức ăn...",
                "Nuôi cá cảnh đòi hỏi sự tỉ mỉ trong việc thiết lập hệ thống lọc nước, cân bằng nhiệt độ và kiểm soát lượng thức ăn cho cá. Hãy bắt đầu với các dòng cá khỏe mạnh như cá bảy màu, cá betta hoặc cá vàng, và đảm bảo thay nước định kỳ 20-30% mỗi tuần để môi trường nước luôn trong sạch.",
                R.drawable.ic_fish
        ));

        adapter = new TinTucAdapter(this, listTinTuc, this);
        rvTinTuc.setAdapter(adapter);
    }

    @Override
    public void onReadMoreClick(TinTuc tinTuc) {
        new AlertDialog.Builder(this)
                .setTitle(tinTuc.getTieuDe())
                .setMessage(tinTuc.getMoTaChiTiet())
                .setPositiveButton("Đóng", null)
                .show();
    }
}
