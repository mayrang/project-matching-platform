package product;

import io.mobile.conf.Conf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public static Product setProduct(ResultSet rs) throws SQLException {

        String  productNo = rs.getString("product_no");
        String productName = rs.getString("product_name");
        int stock = rs.getInt("stock");

        String manufacturer = rs.getString("manufacturer");
        int unitPrice = rs.getInt("unit_price");

        return new Product(productNo, productName, stock, unitPrice, manufacturer);
    }

    public static List<Product> selectAll() {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        List<Product> productList = new ArrayList<Product>();
        // 데이터베이스의 연결을 설정한다.
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {

            // 전체 고객 정보 검색문
            // vip 고객에 대한 정보를 검색해서 출력하도록 수정
            String query = "SELECT * FROM product";

            // Statement를 가져온다.
            psmtQuery = conn.prepareStatement(query);

            // SQL문을 실행한다.
            // rs : 검색 결과 집합
            rs = psmtQuery.executeQuery();

            // 결과 집합의 내용을 출력한다.
            while (rs.next()) { // 결과 집합으로부터 각 레코드(튜블을) 가져온다
                Product product = setProduct(rs);
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException _) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
        }
    }


    public static Product selectByNo(String productNo) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        // 데이터베이스의 연결을 설정한다.
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {


            String query = "SELECT * FROM product WHERE product_no = ?";

            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, productNo);

            // Statement를 가져온다.


            // SQL문을 실행한다.
            // rs : 검색 결과 집합
            rs = psmtQuery.executeQuery();

            // 결과 집합의 내용을 출력한다.
            if (rs.next()) {
                Product product = setProduct(rs);
                return product;

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException _) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
        return null;

    }

    public static int insert(String productNo, String productName, int stock, int unitPrice, String manufacturer) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        // 데이터베이스의 연결을 설정한다.
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {


            String query = "SELECT * FROM product WHERE product_no = ?";



            // Statement를 가져온다.
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, productNo);
            // SQL문을 실행한다.
            // rs : 검색 결과 집합
            rs = psmtQuery.executeQuery();

            // 결과 집합의 내용을 출력한다.
            if (!rs.next()) {
                String insertStatement = "INSERT INTO product(product_no, product_name, stock, unit_price, manufacturer)" +
                        " VALUES (?, ?, ?, ?, ?)";
                psmtQuery = conn.prepareStatement(insertStatement);
                psmtQuery.setString(1, productNo);
                psmtQuery.setString(2, productName);
                psmtQuery.setInt(3, stock);
                psmtQuery.setInt(4, unitPrice);
                psmtQuery.setString(5, manufacturer);

                psmtQuery.executeUpdate();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException _) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
        return -1;

    }

    public static int update(String productNo, int stock, int unitPrice) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        // 데이터베이스의 연결을 설정한다.
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {


            String query = "SELECT * FROM product WHERE product_no = ?";



            // Statement를 가져온다.
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, productNo);
            // SQL문을 실행한다.
            // rs : 검색 결과 집합
            rs = psmtQuery.executeQuery();

            // 결과 집합의 내용을 출력한다.
            if (rs.next()) {
                String updateStatement = "UPDATE product SET stock = ?, unit_price = ?" +
                        " WHERE product_no = ?";
                psmtQuery = conn.prepareStatement(updateStatement);

                psmtQuery.setInt(1, stock);

                psmtQuery.setInt(2, unitPrice);
                psmtQuery.setString(3, productNo);
                psmtQuery.executeUpdate();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException _) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
        return -1;
    }

    public static int delete(String productNo) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        // 데이터베이스의 연결을 설정한다.
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {

            String query = "SELECT * FROM product WHERE product_no = ?";



            // Statement를 가져온다.
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, productNo);

            rs = psmtQuery.executeQuery();

            // 결과 집합의 내용을 출력한다.
            if (rs.next()) {
                String deleteStatement = "DELETE FROM product" +
                        " WHERE product_no = ?";
                psmtQuery = conn.prepareStatement(deleteStatement);
                psmtQuery.setString(1, productNo);
                psmtQuery.executeUpdate();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException _) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
        return -1;
    }

}
