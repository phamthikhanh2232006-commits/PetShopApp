package com.example.petshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petshop.models.ChiTietDonHang;
import com.example.petshop.models.DanhMuc;
import com.example.petshop.models.DonHang;
import com.example.petshop.models.KhachHang;
import com.example.petshop.models.PhieuDoiTra;
import com.example.petshop.models.PhieuNhap;
import com.example.petshop.models.SanPham;
import com.example.petshop.models.TaiKhoan;
import com.example.petshop.models.ThuCung;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "petshop.db";
    private static final int DATABASE_VERSION = 1;

    // Table names matching ERD and DFD
    public static final String TABLE_TAIKHOAN = "TaiKhoan";
    public static final String TABLE_KHACHHANG = "KhachHang";
    public static final String TABLE_THUCUNG = "ThuCung";
    public static final String TABLE_DANHMUC = "DanhMuc";
    public static final String TABLE_SANPHAM = "SanPham";
    public static final String TABLE_DONHANG = "DonHang";
    public static final String TABLE_CT_DONHANG = "CT_DonHang";
    public static final String TABLE_PHIEUNHAP = "PhieuNhap";
    public static final String TABLE_CT_PHIEUNHAP = "CT_PhieuNhap";
    public static final String TABLE_PHIEUDOITRA = "PhieuDoiTra";
    public static final String TABLE_LICHSUKHO = "LichSuKho";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tài khoản (Admin, Nhân viên, Khách hàng)
        db.execSQL("CREATE TABLE " + TABLE_TAIKHOAN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenDangNhap TEXT UNIQUE NOT NULL, " +
                "matKhau TEXT NOT NULL, " +
                "hoTen TEXT, " +
                "soDienThoai TEXT, " +
                "email TEXT, " +
                "vaiTro TEXT DEFAULT 'KhachHang', " + // Admin, NhanVien, KhachHang
                "diaChi TEXT, " +
                "ngayTao TEXT DEFAULT CURRENT_TIMESTAMP)");

        // 2. Khách hàng
        db.execSQL("CREATE TABLE " + TABLE_KHACHHANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "hoTen TEXT NOT NULL, " +
                "soDienThoai TEXT, " +
                "email TEXT, " +
                "diaChi TEXT, " +
                "ngayTao TEXT DEFAULT CURRENT_TIMESTAMP)");

        // 3. Danh mục (Loài: Chó, Mèo, Chim, Cá & Thú nhỏ)
        db.execSQL("CREATE TABLE " + TABLE_DANHMUC + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenDM TEXT NOT NULL, " +
                "moTa TEXT, " +
                "iconName TEXT)");

        // 4. Sản phẩm / Phụ kiện
        db.execSQL("CREATE TABLE " + TABLE_SANPHAM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenSP TEXT NOT NULL, " +
                "giaBan REAL DEFAULT 0, " +
                "soLuongTon INTEGER DEFAULT 0, " +
                "kichCo TEXT, " +        // S, M, L, XL hoặc 500g, 1kg
                "mauSac TEXT, " +        // Đỏ, Xanh, Vàng...
                "maVach TEXT, " +        // Phục vụ quét Camera barcode
                "maDM INTEGER, " +
                "loaiThuCung TEXT, " +   // Chó, Mèo, Chim, Cá
                "moTa TEXT, " +
                "hinhAnh TEXT, " +
                "mucCanhBao INTEGER DEFAULT 5, " +
                "FOREIGN KEY(maDM) REFERENCES " + TABLE_DANHMUC + "(id) ON DELETE SET NULL)");

        // 5. Hồ sơ thú cưng (Theo Khách hàng)
        db.execSQL("CREATE TABLE " + TABLE_THUCUNG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maKH INTEGER, " +
                "tenTC TEXT NOT NULL, " +
                "loaiThuCung TEXT NOT NULL, " + // Chó, Mèo, Chim, Cá, Khác
                "giongLoai TEXT, " +            // Poodle, Corgi, Mèo Ba Tư...
                "kichCo TEXT, " +               // S, M, L...
                "canNang REAL DEFAULT 0, " +
                "gioiTinh TEXT DEFAULT 'Đực', " + // Đực, Cái
                "ngaySinh TEXT, " +
                "ghiChu TEXT, " +
                "hinhAnh TEXT, " +
                "FOREIGN KEY(maKH) REFERENCES " + TABLE_KHACHHANG + "(id) ON DELETE CASCADE)");

        // 6. Đơn hàng (Hóa đơn)
        db.execSQL("CREATE TABLE " + TABLE_DONHANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maKH INTEGER, " +
                "ngayDat TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "tongTien REAL DEFAULT 0, " +
                "giamGia REAL DEFAULT 0, " +
                "phuongThucTT TEXT DEFAULT 'COD', " +
                "trangThai TEXT DEFAULT 'ChoXuLy', " + // ChoXuLy, DaThanhToan, DangGiao, HoanThanh, DaHuy, DoiTra
                "diaChiGiaoHang TEXT, " +
                "sdtNguoiNhan TEXT, " +
                "tenNguoiNhan TEXT, " +
                "ghiChu TEXT, " +
                "FOREIGN KEY(maKH) REFERENCES " + TABLE_KHACHHANG + "(id) ON DELETE SET NULL)");

        // 7. Chi tiết đơn hàng
        db.execSQL("CREATE TABLE " + TABLE_CT_DONHANG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maDH INTEGER NOT NULL, " +
                "maSP INTEGER NOT NULL, " +
                "soLuong INTEGER DEFAULT 1, " +
                "donGia REAL DEFAULT 0, " +
                "thanhTien REAL DEFAULT 0, " +
                "sizeDaChon TEXT, " +
                "mauDaChon TEXT, " +
                "FOREIGN KEY(maDH) REFERENCES " + TABLE_DONHANG + "(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(maSP) REFERENCES " + TABLE_SANPHAM + "(id))");

        // 8. Phiếu nhập kho (Admin)
        db.execSQL("CREATE TABLE " + TABLE_PHIEUNHAP + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ngayNhap TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "tongTienNhap REAL DEFAULT 0, " +
                "nhaCungCap TEXT, " +
                "nguoiNhap TEXT, " +
                "ghiChu TEXT)");

        // 9. Chi tiết phiếu nhập
        db.execSQL("CREATE TABLE " + TABLE_CT_PHIEUNHAP + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maPN INTEGER NOT NULL, " +
                "maSP INTEGER NOT NULL, " +
                "soLuongNhap INTEGER DEFAULT 1, " +
                "giaNhap REAL DEFAULT 0, " +
                "FOREIGN KEY(maPN) REFERENCES " + TABLE_PHIEUNHAP + "(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(maSP) REFERENCES " + TABLE_SANPHAM + "(id))");

        // 10. Phiếu đổi trả (Admin & Khách hàng)
        db.execSQL("CREATE TABLE " + TABLE_PHIEUDOITRA + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maDH INTEGER NOT NULL, " +
                "maSP INTEGER NOT NULL, " +
                "lyDo TEXT, " +
                "soLuong INTEGER DEFAULT 1, " +
                "soTienHoan REAL DEFAULT 0, " +
                "ngayDoiTra TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "trangThai TEXT DEFAULT 'DaXuLy', " +
                "FOREIGN KEY(maDH) REFERENCES " + TABLE_DONHANG + "(id), " +
                "FOREIGN KEY(maSP) REFERENCES " + TABLE_SANPHAM + "(id))");

        // 11. Lịch sử biến động kho
        db.execSQL("CREATE TABLE " + TABLE_LICHSUKHO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maSP INTEGER NOT NULL, " +
                "loaiThaoTac TEXT NOT NULL, " + // NhapKho, BanHang, DoiTra
                "soLuong INTEGER DEFAULT 0, " +
                "nguoiThucHien TEXT, " +
                "thoiGian TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "ghiChu TEXT, " +
                "FOREIGN KEY(maSP) REFERENCES " + TABLE_SANPHAM + "(id) ON DELETE CASCADE)");

        seedInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICHSUKHO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHIEUDOITRA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CT_PHIEUNHAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHIEUNHAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CT_DONHANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONHANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUCUNG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DANHMUC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHACHHANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAIKHOAN);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    private void seedInitialData(SQLiteDatabase db) {
        // Tài khoản mặc định
        ContentValues admin = new ContentValues();
        admin.put("tenDangNhap", "admin");
        admin.put("matKhau", "123456");
        admin.put("hoTen", "Quản trị viên Pet Shop");
        admin.put("soDienThoai", "0900123456");
        admin.put("email", "admin@petshop.vn");
        admin.put("vaiTro", "Admin");
        admin.put("diaChi", "Trụ sở Pet Shop, Hà Nội");
        db.insert(TABLE_TAIKHOAN, null, admin);

        ContentValues nv = new ContentValues();
        nv.put("tenDangNhap", "nhanvien");
        nv.put("matKhau", "123456");
        nv.put("hoTen", "Nhân Viên Tư Vấn");
        nv.put("soDienThoai", "0911223344");
        nv.put("email", "nhanvien@petshop.vn");
        nv.put("vaiTro", "NhanVien");
        nv.put("diaChi", "Chi nhánh 1, TP.HCM");
        db.insert(TABLE_TAIKHOAN, null, nv);

        ContentValues user = new ContentValues();
        user.put("tenDangNhap", "khachhang");
        user.put("matKhau", "123456");
        user.put("hoTen", "Nguyễn Văn A");
        user.put("soDienThoai", "0987654321");
        user.put("email", "nguyenvana@gmail.com");
        user.put("vaiTro", "KhachHang");
        user.put("diaChi", "Số 123, Đường ABC, Quận 1, TP.HCM");
        db.insert(TABLE_TAIKHOAN, null, user);

        // Khách hàng mẫu
        ContentValues kh1 = new ContentValues();
        kh1.put("hoTen", "Nguyễn Văn A");
        kh1.put("soDienThoai", "0987654321");
        kh1.put("email", "nguyenvana@gmail.com");
        kh1.put("diaChi", "Số 123, Đường ABC, Quận 1, TP.HCM");
        long maKH1 = db.insert(TABLE_KHACHHANG, null, kh1);

        ContentValues kh2 = new ContentValues();
        kh2.put("hoTen", "Trần Thị Mai");
        kh2.put("soDienThoai", "0912345678");
        kh2.put("email", "maimai@gmail.com");
        kh2.put("diaChi", "45 Lê Lợi, Quận 1, TP.HCM");
        long maKH2 = db.insert(TABLE_KHACHHANG, null, kh2);

        ContentValues kh3 = new ContentValues();
        kh3.put("hoTen", "Lê Hoàng Long");
        kh3.put("soDienThoai", "0933445566");
        kh3.put("email", "longlh@gmail.com");
        kh3.put("diaChi", "78 Hai Bà Trưng, Hà Nội");
        db.insert(TABLE_KHACHHANG, null, kh3);

        // Danh mục theo loài (Wireframe 2)
        long dmCho = insertDanhMuc(db, "Chó", "Phụ kiện, trang phục & dinh dưỡng cho cún", "ic_dog");
        long dmMeo = insertDanhMuc(db, "Mèo", "Đồ dùng, đồ chơi & thức ăn cho mèo", "ic_cat");
        long dmChim = insertDanhMuc(db, "Chim", "Lồng nuôi, cóng ăn & hạt dinh dưỡng", "ic_bird");
        long dmCa = insertDanhMuc(db, "Cá", "Bể cá mini, máy lọc nước & phụ kiện cảnh", "ic_fish");

        // Hồ sơ thú cưng mẫu (Use Case 5 & Wireframe 10)
        insertThuCung(db, maKH1, "Lucky", "Chó", "Poodle", "Nhỏ", 4.5, "Đực", "2023-05-15", "Thích ăn hạt vị gà, rất hiếu động");
        insertThuCung(db, maKH1, "Mimi", "Mèo", "Mèo Anh lông ngắn", "Vừa", 3.8, "Cái", "2023-08-20", "Thích nằm lười, cào móng");
        insertThuCung(db, maKH2, "Bo", "Chó", "Corgi", "Vừa", 9.2, "Đực", "2022-01-10", "Chân ngắn đáng yêu, ăn khỏe");

        // Sản phẩm mẫu phong phú (Wireframes 2, 4, 5)
        // 1. Áo thun cho chó mùa hè
        insertSanPham(db, "Áo thun cho chó mùa hè", 120000, 25, "S, M, L, XL", "Xanh, Đỏ, Vàng", "893001", dmCho, "Chó",
                "Chất vải cotton thoáng mát, thấm hút mồ hôi tốt cho cún cưng vận động cả ngày hè.", "sp_aothun.png", 5);

        // 2. Áo khoác mùa đông cho thú cưng
        insertSanPham(db, "Áo khoác mùa đông thú cưng", 180000, 15, "M, L, XL", "Nâu, Đỏ đậm", "893002", dmCho, "Chó",
                "Lớp nỉ ấm áp kết hợp bo viền cản gió, thích hợp cho thời tiết lạnh giá.", "sp_aokhoac.png", 4);

        // 3. Vòng cổ thú cưng đính chuông
        insertSanPham(db, "Vòng cổ chuông thú cưng", 80000, 40, "Freesize", "Đỏ, Xanh, Hồng", "893003", dmCho, "Chó",
                "Dây dù chắc chắn có nấc điều chỉnh độ rộng, kèm chuông đồng leng keng vui tai.", "sp_vongco.png", 8);

        // 4. Thức ăn cho chó Royal Canin
        insertSanPham(db, "Thức ăn hạt cho chó Royal Canin", 150000, 30, "500g, 1kg, 2kg", "Tiêu chuẩn", "893004", dmCho, "Chó",
                "Công thức cân bằng protein và vitamin giúp lông bóng mượt và hệ tiêu hóa khỏe mạnh.", "sp_thucancho.png", 5);

        // 5. Áo len cao cấp cho chó mèo
        insertSanPham(db, "Áo len cao cấp cho mèo", 150000, 18, "S, M", "Trắng kem, Xám", "893005", dmMeo, "Mèo",
                "Sợi len mềm mại, co giãn tốt không gây ngứa cho thú cưng.", "sp_aolen.png", 5);

        // 6. Áo họa tiết dễ thương
        insertSanPham(db, "Áo họa tiết hoạt hình", 130000, 22, "S, M, L", "Vàng, Xanh mint", "893006", dmCho, "Chó",
                "Họa tiết đáng yêu, đường may tinh tế phù hợp cho các buổi đi dạo và chụp ảnh.", "sp_aohoatiet.png", 5);

        // 7. Cần câu chuột đồ chơi mèo
        insertSanPham(db, "Cần câu chuột cào móng", 45000, 3, "Chuẩn", "Nhiều màu", "893007", dmMeo, "Mèo",
                "Đồ chơi tương tác giúp kích thích bản năng săn mồi và xả stress cho mèo (Sắp hết hàng).", "sp_cancau.png", 5);

        // 8. Thức ăn hạt Whiskas cho mèo
        insertSanPham(db, "Thức ăn cho mèo Whiskas cá hồi", 135000, 20, "1.2kg", "Tiêu chuẩn", "893008", dmMeo, "Mèo",
                "Giàu Omega 3 & 6 từ cá hồi tươi giúp sáng mắt và mượt lông mèo con lẫn mèo trưởng thành.", "sp_whiskas.png", 5);

        // 9. Dây dắt chó phản quang tự động
        insertSanPham(db, "Dây dắt chó phản quang tự động", 220000, 12, "3m, 5m", "Đen, Đỏ", "893009", dmCho, "Chó",
                "Khóa chốt tiện lợi, dây cuốn tự thu gọn thông minh, có sợi phản quang an toàn ban đêm.", "sp_daydat.png", 4);

        // 10. Lồng chim Inox nan mau
        insertSanPham(db, "Lồng chim cảnh Inox cao cấp", 320000, 8, "Size M", "Bạc inox", "893010", dmChim, "Chim",
                "Inox chống rỉ sét, nan dày dặn kèm khay hứng phân và cóng nước tiện lợi.", "sp_longchim.png", 3);

        // 11. Hạt dinh dưỡng cho vẹt & chim cảnh
        insertSanPham(db, "Hạt thức ăn cao cấp cho chim", 65000, 2, "500g", "Tiêu chuẩn", "893011", dmChim, "Chim",
                "Hạt kê, hạt hướng dương và ngũ cốc dinh dưỡng bồi bổ tiếng hót (Sắp hết hàng).", "sp_hatchim.png", 5);

        // 12. Máy lọc nước hồ cá mini siêu êm
        insertSanPham(db, "Máy lọc thác hồ cá mini", 160000, 15, "Tiêu chuẩn", "Trắng trong", "893012", dmCa, "Cá",
                "Tạo dòng chảy oxy tự nhiên, vận hành êm ái, tiết kiệm điện năng cho bể cá để bàn.", "sp_mayloc.png", 4);

        // 13. Bát ăn đôi chống trượt
        insertSanPham(db, "Bát ăn đôi Inox đế chống trượt", 95000, 28, "Size M, Size L", "Xanh pastel, Hồng", "893013", dmCho, "Chó",
                "Thiết kế 2 ngăn vừa đựng thức ăn vừa đựng nước, đế cao su hít chặt mặt sàn.", "sp_batan.png", 5);

        // 14. Sữa tắm SOS mượt lông thơm lâu
        insertSanPham(db, "Sữa tắm dưỡng lông SOS Pet", 140000, 19, "530ml", "Xanh dương, Đỏ", "893014", dmCho, "Chó",
                "Khử mùi hôi hiệu quả, lưu hương thơm tự nhiên đến 7 ngày, an toàn dịu nhẹ.", "sp_suatam.png", 5);

        // Tạo sẵn đơn hàng mẫu (Wireframes 7, 8)
        long dh1 = insertDonHangMau(db, maKH1, "2026-10-17 14:30:00", 200000, 0, "COD", "HoanThanh",
                "Số 123, Đường ABC, Quận 1, TP.HCM", "0987654321", "Nguyễn Văn A", "Giao trong giờ hành chính");
        insertCTDonHangMau(db, dh1, 1, 1, 120000, 120000, "M", "Xanh");
        insertCTDonHangMau(db, dh1, 3, 1, 80000, 80000, "Freesize", "Đỏ");

        long dh2 = insertDonHangMau(db, maKH2, "2026-10-18 09:15:00", 350000, 0, "COD", "ChoXuLy",
                "45 Lê Lợi, Quận 1, TP.HCM", "0912345678", "Trần Thị Mai", "Gọi trước khi giao");
        insertCTDonHangMau(db, dh2, 1, 1, 120000, 120000, "M", "Xanh");
        insertCTDonHangMau(db, dh2, 3, 1, 80000, 80000, "Freesize", "Đỏ");
        insertCTDonHangMau(db, dh2, 4, 1, 150000, 150000, "1kg", "Tiêu chuẩn");

        // Tạo sẵn phiếu nhập mẫu (Process 5.0 in DFD)
        long pn1 = insertPhieuNhapMau(db, "2026-10-10 10:00:00", 2500000, "Công ty Phụ kiện Pet Việt Nam", "Admin", "Nhập hàng đầu tháng");
        insertCTPhieuNhapMau(db, pn1, 1, 20, 80000);
        insertCTPhieuNhapMau(db, pn1, 3, 30, 50000);

        // Tạo sẵn phiếu đổi trả mẫu (Process 3.0 in DFD)
        insertPhieuDoiTraMau(db, dh1, 1, "Không vừa size cún cưng", 1, 120000, "2026-10-18 11:00:00", "DaXuLy");
    }

    private long insertDanhMuc(SQLiteDatabase db, String tenDM, String moTa, String iconName) {
        ContentValues cv = new ContentValues();
        cv.put("tenDM", tenDM);
        cv.put("moTa", moTa);
        cv.put("iconName", iconName);
        return db.insert(TABLE_DANHMUC, null, cv);
    }

    private long insertSanPham(SQLiteDatabase db, String tenSP, double giaBan, int soLuongTon,
                              String kichCo, String mauSac, String maVach, long maDM,
                              String loaiThuCung, String moTa, String hinhAnh, int mucCanhBao) {
        ContentValues cv = new ContentValues();
        cv.put("tenSP", tenSP);
        cv.put("giaBan", giaBan);
        cv.put("soLuongTon", soLuongTon);
        cv.put("kichCo", kichCo);
        cv.put("mauSac", mauSac);
        cv.put("maVach", maVach);
        cv.put("maDM", maDM);
        cv.put("loaiThuCung", loaiThuCung);
        cv.put("moTa", moTa);
        cv.put("hinhAnh", hinhAnh);
        cv.put("mucCanhBao", mucCanhBao);
        return db.insert(TABLE_SANPHAM, null, cv);
    }

    private long insertThuCung(SQLiteDatabase db, long maKH, String tenTC, String loaiTC,
                              String giongLoai, String kichCo, double canNang, String gioiTinh,
                              String ngaySinh, String ghiChu) {
        ContentValues cv = new ContentValues();
        cv.put("maKH", maKH);
        cv.put("tenTC", tenTC);
        cv.put("loaiThuCung", loaiTC);
        cv.put("giongLoai", giongLoai);
        cv.put("kichCo", kichCo);
        cv.put("canNang", canNang);
        cv.put("gioiTinh", gioiTinh);
        cv.put("ngaySinh", ngaySinh);
        cv.put("ghiChu", ghiChu);
        return db.insert(TABLE_THUCUNG, null, cv);
    }

    private long insertDonHangMau(SQLiteDatabase db, long maKH, String ngayDat, double tongTien,
                                 double giamGia, String phuongThucTT, String trangThai,
                                 String diaChi, String sdt, String tenNguoiNhan, String ghiChu) {
        ContentValues cv = new ContentValues();
        cv.put("maKH", maKH);
        cv.put("ngayDat", ngayDat);
        cv.put("tongTien", tongTien);
        cv.put("giamGia", giamGia);
        cv.put("phuongThucTT", phuongThucTT);
        cv.put("trangThai", trangThai);
        cv.put("diaChiGiaoHang", diaChi);
        cv.put("sdtNguoiNhan", sdt);
        cv.put("tenNguoiNhan", tenNguoiNhan);
        cv.put("ghiChu", ghiChu);
        return db.insert(TABLE_DONHANG, null, cv);
    }

    private void insertCTDonHangMau(SQLiteDatabase db, long maDH, long maSP, int soLuong,
                                   double donGia, double thanhTien, String size, String mau) {
        ContentValues cv = new ContentValues();
        cv.put("maDH", maDH);
        cv.put("maSP", maSP);
        cv.put("soLuong", soLuong);
        cv.put("donGia", donGia);
        cv.put("thanhTien", thanhTien);
        cv.put("sizeDaChon", size);
        cv.put("mauDaChon", mau);
        db.insert(TABLE_CT_DONHANG, null, cv);
    }

    private long insertPhieuNhapMau(SQLiteDatabase db, String ngayNhap, double tongTien, String ncc, String nguoiNhap, String ghiChu) {
        ContentValues cv = new ContentValues();
        cv.put("ngayNhap", ngayNhap);
        cv.put("tongTienNhap", tongTien);
        cv.put("nhaCungCap", ncc);
        cv.put("nguoiNhap", nguoiNhap);
        cv.put("ghiChu", ghiChu);
        return db.insert(TABLE_PHIEUNHAP, null, cv);
    }

    private void insertCTPhieuNhapMau(SQLiteDatabase db, long maPN, long maSP, int soLuong, double giaNhap) {
        ContentValues cv = new ContentValues();
        cv.put("maPN", maPN);
        cv.put("maSP", maSP);
        cv.put("soLuongNhap", soLuong);
        cv.put("giaNhap", giaNhap);
        db.insert(TABLE_CT_PHIEUNHAP, null, cv);
    }

    private void insertPhieuDoiTraMau(SQLiteDatabase db, long maDH, long maSP, String lyDo, int soLuong, double soTienHoan, String ngay, String trangThai) {
        ContentValues cv = new ContentValues();
        cv.put("maDH", maDH);
        cv.put("maSP", maSP);
        cv.put("lyDo", lyDo);
        cv.put("soLuong", soLuong);
        cv.put("soTienHoan", soTienHoan);
        cv.put("ngayDoiTra", ngay);
        cv.put("trangThai", trangThai);
        db.insert(TABLE_PHIEUDOITRA, null, cv);
    }

    // =========================================================================
    //                            PHƯƠNG THỨC XÁC THỰC
    // =========================================================================

    public TaiKhoan login(String username, String password) {
        username = username != null ? username.trim() : "";
        password = password != null ? password.trim() : "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TAIKHOAN + " WHERE LOWER(TRIM(tenDangNhap)) = LOWER(TRIM(?)) AND matKhau = ?",
                new String[]{username, password});
        TaiKhoan tk = null;
        if (c.moveToFirst()) {
            tk = cursorToTaiKhoan(c);
        }
        c.close();
        return tk;
    }

    public long register(String username, String password, String hoTen, String phone, String email, String diaChi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenDangNhap", username);
        cv.put("matKhau", password);
        cv.put("hoTen", hoTen);
        cv.put("soDienThoai", phone);
        cv.put("email", email);
        cv.put("vaiTro", "KhachHang");
        cv.put("diaChi", diaChi);
        long userId = db.insert(TABLE_TAIKHOAN, null, cv);

        if (userId > 0) {
            // Cũng tạo bản ghi bên bảng KhachHang
            ContentValues kh = new ContentValues();
            kh.put("hoTen", hoTen);
            kh.put("soDienThoai", phone);
            kh.put("email", email);
            kh.put("diaChi", diaChi);
            db.insert(TABLE_KHACHHANG, null, kh);
        }
        return userId;
    }

    private TaiKhoan cursorToTaiKhoan(Cursor c) {
        TaiKhoan tk = new TaiKhoan();
        tk.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        tk.setTenDangNhap(c.getString(c.getColumnIndexOrThrow("tenDangNhap")));
        tk.setMatKhau(c.getString(c.getColumnIndexOrThrow("matKhau")));
        tk.setHoTen(c.getString(c.getColumnIndexOrThrow("hoTen")));
        tk.setSoDienThoai(c.getString(c.getColumnIndexOrThrow("soDienThoai")));
        tk.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        tk.setVaiTro(c.getString(c.getColumnIndexOrThrow("vaiTro")));
        tk.setDiaChi(c.getString(c.getColumnIndexOrThrow("diaChi")));
        tk.setNgayTao(c.getString(c.getColumnIndexOrThrow("ngayTao")));
        return tk;
    }

    // =========================================================================
    //                            DANH MỤC & SẢN PHẨM
    // =========================================================================

    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DANHMUC + " ORDER BY id ASC", null);
        if (c.moveToFirst()) {
            do {
                DanhMuc dm = new DanhMuc();
                dm.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                dm.setTenDM(c.getString(c.getColumnIndexOrThrow("tenDM")));
                dm.setMoTa(c.getString(c.getColumnIndexOrThrow("moTa")));
                dm.setIconName(c.getString(c.getColumnIndexOrThrow("iconName")));
                list.add(dm);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp " +
                "LEFT JOIN " + TABLE_DANHMUC + " dm ON sp.maDM = dm.id " +
                "ORDER BY sp.id DESC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                list.add(cursorToSanPham(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<SanPham> getFeaturedSanPham(int limit) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp " +
                "LEFT JOIN " + TABLE_DANHMUC + " dm ON sp.maDM = dm.id " +
                "ORDER BY sp.id ASC LIMIT " + limit;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                list.add(cursorToSanPham(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public SanPham getSanPhamById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp " +
                "LEFT JOIN " + TABLE_DANHMUC + " dm ON sp.maDM = dm.id " +
                "WHERE sp.id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(id)});
        SanPham sp = null;
        if (c.moveToFirst()) {
            sp = cursorToSanPham(c);
        }
        c.close();
        return sp;
    }

    public SanPham getSanPhamByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) return null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp " +
                "LEFT JOIN " + TABLE_DANHMUC + " dm ON sp.maDM = dm.id " +
                "WHERE sp.maVach = ? OR sp.id = ?";
        Cursor c = db.rawQuery(sql, new String[]{barcode.trim(), barcode.trim()});
        SanPham sp = null;
        if (c.moveToFirst()) {
            sp = cursorToSanPham(c);
        }
        c.close();
        return sp;
    }

    public List<SanPham> searchSanPham(String keyword) {
        List<SanPham> all = getAllSanPham();
        if (keyword == null || keyword.trim().isEmpty()) {
            return all;
        }
        List<SanPham> list = new ArrayList<>();
        String normKeyword = com.example.petshop.utils.FormatUtils.removeAccent(keyword);
        String rawLowerKey = keyword.trim().toLowerCase();

        for (SanPham sp : all) {
            String normTen = com.example.petshop.utils.FormatUtils.removeAccent(sp.getTenSP());
            String normMoTa = com.example.petshop.utils.FormatUtils.removeAccent(sp.getMoTa());
            String normLoai = com.example.petshop.utils.FormatUtils.removeAccent(sp.getLoaiThuCung());
            String normDanhMuc = com.example.petshop.utils.FormatUtils.removeAccent(sp.getTenDM());
            String normKichCo = com.example.petshop.utils.FormatUtils.removeAccent(sp.getKichCo());
            String normMauSac = com.example.petshop.utils.FormatUtils.removeAccent(sp.getMauSac());
            String maVach = sp.getMaVach() != null ? sp.getMaVach().toLowerCase() : "";

            if (normTen.contains(normKeyword) ||
                normMoTa.contains(normKeyword) ||
                normLoai.contains(normKeyword) ||
                normDanhMuc.contains(normKeyword) ||
                normKichCo.contains(normKeyword) ||
                normMauSac.contains(normKeyword) ||
                maVach.contains(rawLowerKey) ||
                (sp.getTenSP() != null && sp.getTenSP().toLowerCase().contains(rawLowerKey))) {
                list.add(sp);
            }
        }
        return list;
    }

    public List<String> getAllDistinctColors() {
        List<String> result = new ArrayList<>();
        result.add("Tất cả");
        String[] defaultColors = {"Đỏ", "Xanh", "Xanh dương", "Xanh lá", "Vàng", "Hồng", "Nâu", "Trắng", "Đen", "Tím", "Cam", "Xám"};
        for (String dc : defaultColors) {
            if (!result.contains(dc)) result.add(dc);
        }
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT DISTINCT mauSac FROM " + TABLE_SANPHAM + " WHERE mauSac IS NOT NULL AND mauSac != ''", null);
            if (c != null && c.moveToFirst()) {
                do {
                    String raw = c.getString(0);
                    if (raw != null) {
                        String[] parts = raw.split("[,;/-]");
                        for (String p : parts) {
                            String trimmed = p.trim();
                            if (!trimmed.isEmpty() && !result.contains(trimmed)) {
                                result.add(trimmed);
                            }
                        }
                    }
                } while (c.moveToNext());
                c.close();
            }
        } catch (Exception ignored) {}
        return result;
    }

    public List<String> getAllDistinctSizes() {
        List<String> result = new ArrayList<>();
        result.add("Tất cả");
        String[] defaultSizes = {"S", "M", "L", "XL", "XXL", "Freesize", "500g", "1kg", "2kg", "5kg", "500ml", "530ml", "1 lít"};
        for (String ds : defaultSizes) {
            if (!result.contains(ds)) result.add(ds);
        }
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT DISTINCT kichCo FROM " + TABLE_SANPHAM + " WHERE kichCo IS NOT NULL AND kichCo != ''", null);
            if (c != null && c.moveToFirst()) {
                do {
                    String raw = c.getString(0);
                    if (raw != null) {
                        String[] parts = raw.split("[,;/-]");
                        for (String p : parts) {
                            String trimmed = p.trim();
                            if (!trimmed.isEmpty() && !result.contains(trimmed)) {
                                result.add(trimmed);
                            }
                        }
                    }
                } while (c.moveToNext());
                c.close();
            }
        } catch (Exception ignored) {}
        return result;
    }

    public List<SanPham> getSanPhamBySpeciesOrCategory(String species, Long categoryId, String sizeFilter, String colorFilter, String sortOrder) {
        List<SanPham> rawList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder sql = new StringBuilder("SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp ");
        sql.append("LEFT JOIN ").append(TABLE_DANHMUC).append(" dm ON sp.maDM = dm.id WHERE 1=1 ");

        List<String> args = new ArrayList<>();

        if (species != null && !species.isEmpty() && !species.equalsIgnoreCase("Tất cả")) {
            sql.append("AND (sp.loaiThuCung = ? OR dm.tenDM = ?) ");
            args.add(species);
            args.add(species);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND sp.maDM = ? ");
            args.add(String.valueOf(categoryId));
        }

        if ("gia_tang".equalsIgnoreCase(sortOrder)) {
            sql.append("ORDER BY sp.giaBan ASC");
        } else if ("gia_giam".equalsIgnoreCase(sortOrder)) {
            sql.append("ORDER BY sp.giaBan DESC");
        } else {
            sql.append("ORDER BY sp.id DESC");
        }

        Cursor c = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (c.moveToFirst()) {
            do {
                rawList.add(cursorToSanPham(c));
            } while (c.moveToNext());
        }
        c.close();

        // Accent-insensitive and flexible filtering for size and color
        List<SanPham> finalList = new ArrayList<>();
        String normSize = (sizeFilter != null && !sizeFilter.equalsIgnoreCase("Tất cả")) ? com.example.petshop.utils.FormatUtils.removeAccent(sizeFilter) : null;
        String normColor = (colorFilter != null && !colorFilter.equalsIgnoreCase("Tất cả")) ? com.example.petshop.utils.FormatUtils.removeAccent(colorFilter) : null;

        for (SanPham sp : rawList) {
            boolean sizeMatch = true;
            if (normSize != null) {
                String spSize = com.example.petshop.utils.FormatUtils.removeAccent(sp.getKichCo());
                sizeMatch = spSize.contains(normSize);
            }
            boolean colorMatch = true;
            if (normColor != null) {
                String spColor = com.example.petshop.utils.FormatUtils.removeAccent(sp.getMauSac());
                colorMatch = spColor.contains(normColor);
            }
            if (sizeMatch && colorMatch) {
                finalList.add(sp);
            }
        }
        return finalList;
    }

    public long insertSanPham(SanPham sp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenSP", sp.getTenSP());
        cv.put("giaBan", sp.getGiaBan());
        cv.put("soLuongTon", sp.getSoLuongTon());
        cv.put("kichCo", sp.getKichCo());
        cv.put("mauSac", sp.getMauSac());
        cv.put("maVach", sp.getMaVach());
        cv.put("maDM", sp.getMaDM());
        cv.put("loaiThuCung", sp.getLoaiThuCung());
        cv.put("moTa", sp.getMoTa());
        cv.put("hinhAnh", sp.getHinhAnh());
        cv.put("mucCanhBao", sp.getMucCanhBao());
        long id = db.insert(TABLE_SANPHAM, null, cv);

        if (id > 0 && sp.getSoLuongTon() > 0) {
            // Ghi lịch sử nhập kho ban đầu
            ghiLichSuKho(id, "NhapKho", sp.getSoLuongTon(), "Admin", "Khởi tạo sản phẩm");
        }
        return id;
    }

    public int updateSanPham(SanPham sp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenSP", sp.getTenSP());
        cv.put("giaBan", sp.getGiaBan());
        cv.put("soLuongTon", sp.getSoLuongTon());
        cv.put("kichCo", sp.getKichCo());
        cv.put("mauSac", sp.getMauSac());
        cv.put("maVach", sp.getMaVach());
        cv.put("maDM", sp.getMaDM());
        cv.put("loaiThuCung", sp.getLoaiThuCung());
        cv.put("moTa", sp.getMoTa());
        cv.put("hinhAnh", sp.getHinhAnh());
        cv.put("mucCanhBao", sp.getMucCanhBao());
        return db.update(TABLE_SANPHAM, cv, "id = ?", new String[]{String.valueOf(sp.getId())});
    }

    public int deleteSanPham(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_SANPHAM, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<SanPham> getSanPhamSapHetHang() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.*, dm.tenDM FROM " + TABLE_SANPHAM + " sp " +
                "LEFT JOIN " + TABLE_DANHMUC + " dm ON sp.maDM = dm.id " +
                "WHERE sp.soLuongTon <= sp.mucCanhBao ORDER BY sp.soLuongTon ASC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                list.add(cursorToSanPham(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private SanPham cursorToSanPham(Cursor c) {
        SanPham sp = new SanPham();
        sp.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        sp.setTenSP(c.getString(c.getColumnIndexOrThrow("tenSP")));
        sp.setGiaBan(c.getDouble(c.getColumnIndexOrThrow("giaBan")));
        sp.setSoLuongTon(c.getInt(c.getColumnIndexOrThrow("soLuongTon")));
        sp.setKichCo(c.getString(c.getColumnIndexOrThrow("kichCo")));
        sp.setMauSac(c.getString(c.getColumnIndexOrThrow("mauSac")));
        sp.setMaVach(c.getString(c.getColumnIndexOrThrow("maVach")));
        sp.setMaDM(c.getLong(c.getColumnIndexOrThrow("maDM")));
        int idxTenDM = c.getColumnIndex("tenDM");
        if (idxTenDM >= 0) {
            sp.setTenDM(c.getString(idxTenDM));
        }
        sp.setLoaiThuCung(c.getString(c.getColumnIndexOrThrow("loaiThuCung")));
        sp.setMoTa(c.getString(c.getColumnIndexOrThrow("moTa")));
        sp.setHinhAnh(c.getString(c.getColumnIndexOrThrow("hinhAnh")));
        sp.setMucCanhBao(c.getInt(c.getColumnIndexOrThrow("mucCanhBao")));
        return sp;
    }

    // =========================================================================
    //                            HỒ SƠ THÚ CƯNG (Use Case 5)
    // =========================================================================

    public List<ThuCung> getThuCungByKhachHang(long maKH) {
        List<ThuCung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT tc.*, kh.hoTen AS tenKH FROM " + TABLE_THUCUNG + " tc " +
                "LEFT JOIN " + TABLE_KHACHHANG + " kh ON tc.maKH = kh.id " +
                "WHERE tc.maKH = ? ORDER BY tc.id DESC";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(maKH)});
        if (c.moveToFirst()) {
            do {
                list.add(cursorToThuCung(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<ThuCung> getAllThuCung() {
        List<ThuCung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT tc.*, kh.hoTen AS tenKH FROM " + TABLE_THUCUNG + " tc " +
                "LEFT JOIN " + TABLE_KHACHHANG + " kh ON tc.maKH = kh.id " +
                "ORDER BY tc.id DESC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                list.add(cursorToThuCung(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long insertThuCung(ThuCung tc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maKH", tc.getMaKH());
        cv.put("tenTC", tc.getTenTC());
        cv.put("loaiThuCung", tc.getLoaiThuCung());
        cv.put("giongLoai", tc.getGiongLoai());
        cv.put("kichCo", tc.getKichCo());
        cv.put("canNang", tc.getCanNang());
        cv.put("gioiTinh", tc.getGioiTinh());
        cv.put("ngaySinh", tc.getNgaySinh());
        cv.put("ghiChu", tc.getGhiChu());
        cv.put("hinhAnh", tc.getHinhAnh());
        return db.insert(TABLE_THUCUNG, null, cv);
    }

    public int updateThuCung(ThuCung tc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maKH", tc.getMaKH());
        cv.put("tenTC", tc.getTenTC());
        cv.put("loaiThuCung", tc.getLoaiThuCung());
        cv.put("giongLoai", tc.getGiongLoai());
        cv.put("kichCo", tc.getKichCo());
        cv.put("canNang", tc.getCanNang());
        cv.put("gioiTinh", tc.getGioiTinh());
        cv.put("ngaySinh", tc.getNgaySinh());
        cv.put("ghiChu", tc.getGhiChu());
        cv.put("hinhAnh", tc.getHinhAnh());
        return db.update(TABLE_THUCUNG, cv, "id = ?", new String[]{String.valueOf(tc.getId())});
    }

    public int deleteThuCung(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_THUCUNG, "id = ?", new String[]{String.valueOf(id)});
    }

    private ThuCung cursorToThuCung(Cursor c) {
        ThuCung tc = new ThuCung();
        tc.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        tc.setMaKH(c.getLong(c.getColumnIndexOrThrow("maKH")));
        int idxTenKH = c.getColumnIndex("tenKH");
        if (idxTenKH >= 0) {
            tc.setTenKhachHang(c.getString(idxTenKH));
        }
        tc.setTenTC(c.getString(c.getColumnIndexOrThrow("tenTC")));
        tc.setLoaiThuCung(c.getString(c.getColumnIndexOrThrow("loaiThuCung")));
        tc.setGiongLoai(c.getString(c.getColumnIndexOrThrow("giongLoai")));
        tc.setKichCo(c.getString(c.getColumnIndexOrThrow("kichCo")));
        tc.setCanNang(c.getDouble(c.getColumnIndexOrThrow("canNang")));
        tc.setGioiTinh(c.getString(c.getColumnIndexOrThrow("gioiTinh")));
        tc.setNgaySinh(c.getString(c.getColumnIndexOrThrow("ngaySinh")));
        tc.setGhiChu(c.getString(c.getColumnIndexOrThrow("ghiChu")));
        tc.setHinhAnh(c.getString(c.getColumnIndexOrThrow("hinhAnh")));
        return tc;
    }

    // =========================================================================
    //                            ĐƠN HÀNG & THANH TOÁN (Use Case 7, 8)
    // =========================================================================

    public long taoDonHang(DonHang donHang) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long maDH = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("maKH", donHang.getMaKH());
            cv.put("ngayDat", donHang.getNgayDat() != null ? donHang.getNgayDat() : "datetime('now', 'localtime')");
            cv.put("tongTien", donHang.getTongTien());
            cv.put("giamGia", donHang.getGiamGia());
            cv.put("phuongThucTT", donHang.getPhuongThucTT());
            cv.put("trangThai", donHang.getTrangThai());
            cv.put("diaChiGiaoHang", donHang.getDiaChiGiaoHang());
            cv.put("sdtNguoiNhan", donHang.getSoDienThoai());
            cv.put("tenNguoiNhan", donHang.getTenKhachHang());
            cv.put("ghiChu", donHang.getGhiChu());

            maDH = db.insert(TABLE_DONHANG, null, cv);

            if (maDH > 0 && donHang.getDanhSachChiTiet() != null) {
                for (ChiTietDonHang ct : donHang.getDanhSachChiTiet()) {
                    ContentValues cvCT = new ContentValues();
                    cvCT.put("maDH", maDH);
                    cvCT.put("maSP", ct.getMaSP());
                    cvCT.put("soLuong", ct.getSoLuong());
                    cvCT.put("donGia", ct.getDonGia());
                    cvCT.put("thanhTien", ct.getThanhTien());
                    cvCT.put("sizeDaChon", ct.getSizeDaChon());
                    cvCT.put("mauDaChon", ct.getMauDaChon());
                    db.insert(TABLE_CT_DONHANG, null, cvCT);

                    // Trừ tồn kho tự động theo DFD 2.0
                    db.execSQL("UPDATE " + TABLE_SANPHAM +
                            " SET soLuongTon = MAX(0, soLuongTon - ?) WHERE id = ?",
                            new Object[]{ct.getSoLuong(), ct.getMaSP()});

                    // Ghi lịch sử xuất kho
                    ContentValues cvKho = new ContentValues();
                    cvKho.put("maSP", ct.getMaSP());
                    cvKho.put("loaiThaoTac", "BanHang");
                    cvKho.put("soLuong", ct.getSoLuong());
                    cvKho.put("nguoiThucHien", "HeThong");
                    cvKho.put("ghiChu", "Bán hàng theo đơn #" + maDH);
                    db.insert(TABLE_LICHSUKHO, null, cvKho);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return maDH;
    }

    public List<DonHang> getAllDonHang() {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT dh.*, kh.hoTen AS tenKH, kh.soDienThoai AS sdtKH, " +
                "(SELECT COUNT(*) FROM " + TABLE_CT_DONHANG + " WHERE maDH = dh.id) AS soLuongSP " +
                "FROM " + TABLE_DONHANG + " dh " +
                "LEFT JOIN " + TABLE_KHACHHANG + " kh ON dh.maKH = kh.id " +
                "ORDER BY dh.id DESC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                list.add(cursorToDonHang(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<DonHang> getDonHangByKhachHang(long maKH) {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT dh.*, kh.hoTen AS tenKH, kh.soDienThoai AS sdtKH, " +
                "(SELECT COUNT(*) FROM " + TABLE_CT_DONHANG + " WHERE maDH = dh.id) AS soLuongSP " +
                "FROM " + TABLE_DONHANG + " dh " +
                "LEFT JOIN " + TABLE_KHACHHANG + " kh ON dh.maKH = kh.id " +
                "WHERE dh.maKH = ? ORDER BY dh.id DESC";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(maKH)});
        if (c.moveToFirst()) {
            do {
                list.add(cursorToDonHang(c));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public DonHang getDonHangById(long maDH) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT dh.*, kh.hoTen AS tenKH, kh.soDienThoai AS sdtKH, " +
                "(SELECT COUNT(*) FROM " + TABLE_CT_DONHANG + " WHERE maDH = dh.id) AS soLuongSP " +
                "FROM " + TABLE_DONHANG + " dh " +
                "LEFT JOIN " + TABLE_KHACHHANG + " kh ON dh.maKH = kh.id " +
                "WHERE dh.id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(maDH)});
        DonHang dh = null;
        if (c.moveToFirst()) {
            dh = cursorToDonHang(c);
            dh.setDanhSachChiTiet(getChiTietByDonHang(maDH));
        }
        c.close();
        return dh;
    }

    public List<ChiTietDonHang> getChiTietByDonHang(long maDH) {
        List<ChiTietDonHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT ct.*, sp.tenSP, sp.hinhAnh FROM " + TABLE_CT_DONHANG + " ct " +
                "LEFT JOIN " + TABLE_SANPHAM + " sp ON ct.maSP = sp.id " +
                "WHERE ct.maDH = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(maDH)});
        if (c.moveToFirst()) {
            do {
                ChiTietDonHang item = new ChiTietDonHang();
                item.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                item.setMaDH(c.getLong(c.getColumnIndexOrThrow("maDH")));
                item.setMaSP(c.getLong(c.getColumnIndexOrThrow("maSP")));
                item.setTenSP(c.getString(c.getColumnIndexOrThrow("tenSP")));
                item.setHinhAnh(c.getString(c.getColumnIndexOrThrow("hinhAnh")));
                item.setSoLuong(c.getInt(c.getColumnIndexOrThrow("soLuong")));
                item.setDonGia(c.getDouble(c.getColumnIndexOrThrow("donGia")));
                item.setThanhTien(c.getDouble(c.getColumnIndexOrThrow("thanhTien")));
                item.setSizeDaChon(c.getString(c.getColumnIndexOrThrow("sizeDaChon")));
                item.setMauDaChon(c.getString(c.getColumnIndexOrThrow("mauDaChon")));
                list.add(item);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public int capNhatTrangThaiDonHang(long maDH, String trangThaiMoi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("trangThai", trangThaiMoi);
        return db.update(TABLE_DONHANG, cv, "id = ?", new String[]{String.valueOf(maDH)});
    }

    private DonHang cursorToDonHang(Cursor c) {
        DonHang dh = new DonHang();
        dh.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        dh.setMaKH(c.getLong(c.getColumnIndexOrThrow("maKH")));
        int idxTen = c.getColumnIndex("tenKH");
        if (idxTen >= 0 && c.getString(idxTen) != null) {
            dh.setTenKhachHang(c.getString(idxTen));
        } else {
            dh.setTenKhachHang(c.getString(c.getColumnIndexOrThrow("tenNguoiNhan")));
        }
        int idxSdt = c.getColumnIndex("sdtKH");
        if (idxSdt >= 0 && c.getString(idxSdt) != null) {
            dh.setSoDienThoai(c.getString(idxSdt));
        } else {
            dh.setSoDienThoai(c.getString(c.getColumnIndexOrThrow("sdtNguoiNhan")));
        }
        dh.setNgayDat(c.getString(c.getColumnIndexOrThrow("ngayDat")));
        dh.setTongTien(c.getDouble(c.getColumnIndexOrThrow("tongTien")));
        dh.setGiamGia(c.getDouble(c.getColumnIndexOrThrow("giamGia")));
        dh.setPhuongThucTT(c.getString(c.getColumnIndexOrThrow("phuongThucTT")));
        dh.setTrangThai(c.getString(c.getColumnIndexOrThrow("trangThai")));
        dh.setDiaChiGiaoHang(c.getString(c.getColumnIndexOrThrow("diaChiGiaoHang")));
        dh.setGhiChu(c.getString(c.getColumnIndexOrThrow("ghiChu")));
        int idxSL = c.getColumnIndex("soLuongSP");
        if (idxSL >= 0) {
            dh.setSoLuongSP(c.getInt(idxSL));
        }
        return dh;
    }

    // =========================================================================
    //                        NHẬP HÀNG & TỒN KHO (Process 5.0)
    // =========================================================================

    public long taoPhieuNhap(long maSP, int soLuongNhap, double giaNhap, String nhaCungCap, String nguoiNhap, String ghiChu) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long maPN = -1;
        try {
            double tongTien = soLuongNhap * giaNhap;
            ContentValues cvPN = new ContentValues();
            cvPN.put("tongTienNhap", tongTien);
            cvPN.put("nhaCungCap", nhaCungCap);
            cvPN.put("nguoiNhap", nguoiNhap);
            cvPN.put("ghiChu", ghiChu);
            maPN = db.insert(TABLE_PHIEUNHAP, null, cvPN);

            if (maPN > 0) {
                ContentValues cvCT = new ContentValues();
                cvCT.put("maPN", maPN);
                cvCT.put("maSP", maSP);
                cvCT.put("soLuongNhap", soLuongNhap);
                cvCT.put("giaNhap", giaNhap);
                db.insert(TABLE_CT_PHIEUNHAP, null, cvCT);

                // Cộng tồn kho vào SanPham
                db.execSQL("UPDATE " + TABLE_SANPHAM + " SET soLuongTon = soLuongTon + ? WHERE id = ?",
                        new Object[]{soLuongNhap, maSP});

                // Ghi nhận lịch sử kho
                ghiLichSuKho(maSP, "NhapKho", soLuongNhap, nguoiNhap, "Phiếu nhập #" + maPN + " từ " + nhaCungCap);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return maPN;
    }

    public List<PhieuNhap> getAllPhieuNhap() {
        List<PhieuNhap> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT pn.*, (SELECT SUM(soLuongNhap) FROM " + TABLE_CT_PHIEUNHAP + " WHERE maPN = pn.id) AS tongSL " +
                "FROM " + TABLE_PHIEUNHAP + " pn ORDER BY pn.id DESC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                PhieuNhap pn = new PhieuNhap();
                pn.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                pn.setNgayNhap(c.getString(c.getColumnIndexOrThrow("ngayNhap")));
                pn.setTongTienNhap(c.getDouble(c.getColumnIndexOrThrow("tongTienNhap")));
                pn.setNhaCungCap(c.getString(c.getColumnIndexOrThrow("nhaCungCap")));
                pn.setNguoiNhap(c.getString(c.getColumnIndexOrThrow("nguoiNhap")));
                pn.setGhiChu(c.getString(c.getColumnIndexOrThrow("ghiChu")));
                int idxSL = c.getColumnIndex("tongSL");
                if (idxSL >= 0) {
                    pn.setTongSoLuong(c.getInt(idxSL));
                }
                list.add(pn);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // =========================================================================
    //                        QUẢN LÝ ĐỔI TRẢ (Process 3.0)
    // =========================================================================

    public long taoPhieuDoiTra(long maDH, long maSP, String lyDo, int soLuong, double soTienHoan) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long maPDT = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("maDH", maDH);
            cv.put("maSP", maSP);
            cv.put("lyDo", lyDo);
            cv.put("soLuong", soLuong);
            cv.put("soTienHoan", soTienHoan);
            cv.put("trangThai", "DaXuLy");
            maPDT = db.insert(TABLE_PHIEUDOITRA, null, cv);

            if (maPDT > 0) {
                // Hoàn lại số lượng vào tồn kho D1 theo DFD 3.0
                db.execSQL("UPDATE " + TABLE_SANPHAM + " SET soLuongTon = soLuongTon + ? WHERE id = ?",
                        new Object[]{soLuong, maSP});

                // Cập nhật trạng thái đơn hàng thành DoiTra
                ContentValues cvDH = new ContentValues();
                cvDH.put("trangThai", "DoiTra");
                db.update(TABLE_DONHANG, cvDH, "id = ?", new String[]{String.valueOf(maDH)});

                // Ghi lịch sử kho
                ghiLichSuKho(maSP, "DoiTra", soLuong, "KhachHang", "Hoàn hàng phiếu đổi trả #" + maPDT + " (Đơn #" + maDH + ")");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return maPDT;
    }

    public List<PhieuDoiTra> getAllPhieuDoiTra() {
        List<PhieuDoiTra> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT pdt.*, sp.tenSP FROM " + TABLE_PHIEUDOITRA + " pdt " +
                "LEFT JOIN " + TABLE_SANPHAM + " sp ON pdt.maSP = sp.id " +
                "ORDER BY pdt.id DESC";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                PhieuDoiTra pdt = new PhieuDoiTra();
                pdt.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                pdt.setMaDH(c.getLong(c.getColumnIndexOrThrow("maDH")));
                pdt.setMaSP(c.getLong(c.getColumnIndexOrThrow("maSP")));
                pdt.setTenSP(c.getString(c.getColumnIndexOrThrow("tenSP")));
                pdt.setLyDo(c.getString(c.getColumnIndexOrThrow("lyDo")));
                pdt.setSoLuong(c.getInt(c.getColumnIndexOrThrow("soLuong")));
                pdt.setSoTienHoan(c.getDouble(c.getColumnIndexOrThrow("soTienHoan")));
                pdt.setNgayDoiTra(c.getString(c.getColumnIndexOrThrow("ngayDoiTra")));
                pdt.setTrangThai(c.getString(c.getColumnIndexOrThrow("trangThai")));
                list.add(pdt);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private void ghiLichSuKho(long maSP, String loaiThaoTac, int soLuong, String nguoiThucHien, String ghiChu) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maSP", maSP);
        cv.put("loaiThaoTac", loaiThaoTac);
        cv.put("soLuong", soLuong);
        cv.put("nguoiThucHien", nguoiThucHien);
        cv.put("ghiChu", ghiChu);
        db.insert(TABLE_LICHSUKHO, null, cv);
    }

    // =========================================================================
    //                        BÁO CÁO & THỐNG KÊ (Process 6.0)
    // =========================================================================

    public Cursor getThongKeTongQuan() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM " + TABLE_DONHANG + ") AS tongDonHang, " +
                "(SELECT COUNT(*) FROM " + TABLE_DONHANG + " WHERE trangThai = 'ChoXuLy') AS donChoXuLy, " +
                "(SELECT COUNT(*) FROM " + TABLE_SANPHAM + ") AS tongSanPham, " +
                "(SELECT COUNT(*) FROM " + TABLE_SANPHAM + " WHERE soLuongTon <= mucCanhBao) AS sanPhamSapHet, " +
                "(SELECT IFNULL(SUM(tongTien), 0) FROM " + TABLE_DONHANG + " WHERE trangThai != 'DaHuy') AS tongDoanhThu, " +
                "(SELECT COUNT(*) FROM " + TABLE_KHACHHANG + ") AS tongKhachHang, " +
                "(SELECT COUNT(*) FROM " + TABLE_THUCUNG + ") AS tongThuCung";
        return db.rawQuery(sql, null);
    }

    public Cursor getTopSanPhamBanChay(int limit) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.tenSP, sp.giaBan, SUM(ct.soLuong) AS tongBan, SUM(ct.thanhTien) AS doanhThuSP " +
                "FROM " + TABLE_CT_DONHANG + " ct " +
                "JOIN " + TABLE_SANPHAM + " sp ON ct.maSP = sp.id " +
                "JOIN " + TABLE_DONHANG + " dh ON ct.maDH = dh.id " +
                "WHERE dh.trangThai != 'DaHuy' " +
                "GROUP BY ct.maSP ORDER BY tongBan DESC LIMIT " + limit;
        return db.rawQuery(sql, null);
    }
}
