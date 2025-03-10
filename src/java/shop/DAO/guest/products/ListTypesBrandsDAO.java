package shop.DAO.guest.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Type;

public class ListTypesBrandsDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Type> getTypes() {
        List<Type> list = new ArrayList<>();
        String sql = "SELECT type_id, type_name FROM [Type] ORDER BY type_id";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Type t = new Type();
                t.setType_id(rs.getInt("type_id"));
                t.setType_name(rs.getString("type_name"));
                list.add(t);
                System.out.println("Type found: ID=" + t.getType_id() + ", Name=" + t.getType_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM Product WHERE status = 'active' AND brand IS NOT NULL AND brand != '' ORDER BY brand";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String brand = rs.getString("brand");
                if (brand != null && !brand.trim().isEmpty()) {
                    brands.add(brand.trim());
                    System.out.println("Brand found: " + brand.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return brands;
    }
}
