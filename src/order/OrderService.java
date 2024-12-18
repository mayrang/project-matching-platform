package order;

import conf.Conf;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderService {

    private static Order setOrder(ResultSet rs) throws SQLException {
        String orderNo = rs.getString("order_no");
        String customerId = rs.getString("customer_id");
        String customerName = rs.getString("customer_name");
        String productNo = rs.getString("product_no");
        String productName = rs.getString("product_name");
        int quantity = rs.getInt("quantity");
        String destination = rs.getString("destination");
        Date orderDate = rs.getDate("order_date");

        return new Order(orderNo, customerId, customerName, productNo, productName, quantity, destination, orderDate);
    }

    public static List<Order> selectAll() {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Order> orderList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT PO.*, customer_name, product_name FROM customer C, porder PO, product P "
                    + "WHERE C.customer_id = PO.customer_id AND PO.product_no = P.product_no";
            psmtQuery = conn.prepareStatement(query);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Order order = setOrder(rs);
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return orderList;
    }

    public static Order selectById(final String orderNo) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT PO.*, customer_name, product_name FROM customer C, porder PO, product P "
                    + "WHERE C.customer_id = PO.customer_id AND PO.product_no = P.product_no AND order_no = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, orderNo);
            rs = psmtQuery.executeQuery();
            if (rs.next()) {
                return setOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return null;
    }

    public static List<Order> selectByCustomerId(final String customerId) {

        List<Order> orderList = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT PO.*, customer_name, product_name FROM customer C, porder PO, product P "
                    + "WHERE C.customer_id = PO.customer_id AND PO.product_no = P.product_no AND C.customer_id = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, customerId);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Order product = setOrder(rs);
                orderList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return orderList;
    }

    public static List<Order> selectByProductNo(final String productNo) {

        List<Order> orderList = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT PO.*, customer_name, product_name FROM customer C, porder PO, product P "
                    + "WHERE C.customer_id = PO.customer_id AND PO.product_no = P.product_no AND P.product_no = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, productNo);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Order product = setOrder(rs);
                orderList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }

        return orderList;
    }
    public static String insert(final String customerId, final String productNo, final int quantity,
                                final String destination, final Date orderDate) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;
        PreparedStatement psmtUpdate2 = null;
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);
            System.out.println("auto commit" + conn.getAutoCommit());
            String query = "SELECT MAX(order_no) FROM porder";
            psmtQuery = conn.prepareStatement(query);
            rs = psmtQuery.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String lastOrderNo = rs.getString(1);
            String newOrderNo;
            if (lastOrderNo == null) {
                newOrderNo = "o01";
            } else {
                newOrderNo = "o" + (Integer.parseInt(lastOrderNo.substring(1)) + 1);
            }
            String insertStatement = "INSERT INTO porder VALUES (?,?,?,?,?,?)";

            psmtUpdate = conn.prepareStatement(insertStatement);

            psmtUpdate.setString(1, newOrderNo);
            psmtUpdate.setString(2, customerId);
            psmtUpdate.setString(3, productNo);
            psmtUpdate.setInt(4, quantity);
            psmtUpdate.setString(5, destination);
            psmtUpdate.setDate(6, new java.sql.Date(orderDate.getTime()));

            if (psmtUpdate.executeUpdate() > 0) {
                // 제품 테이블에서 재고량을 수량만큼 제외하는 update 코드 완성
                String updateStatement = "UPDATE product SET stock = stock - ? WHERE product_no = ?";
                psmtUpdate2 = conn.prepareStatement(updateStatement);
                psmtUpdate2.setInt(1, quantity);
                psmtUpdate2.setString(2, productNo);

                if(psmtUpdate2.executeUpdate() > 0){
                    conn.commit();
                    return newOrderNo;
                }else{
                    conn.rollback();
                    return null;
                }



            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (psmtUpdate != null) {
                try {
                    psmtUpdate.close();
                } catch (SQLException ignored) {
                }
            }
            if (psmtUpdate2 != null) {
                try {
                    psmtUpdate2.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static int updateById(final String orderNo, final int quantity, final String destination) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT * FROM porder WHERE order_no = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, orderNo);
            rs = psmtQuery.executeQuery();
            if (rs.next()) {
                String updateStatement = "UPDATE porder SET quantity = ?, destination = ? WHERE order_no = ?";
                psmtUpdate = conn.prepareStatement(updateStatement);

                psmtUpdate.setInt(1, quantity);
                psmtUpdate.setString(2, destination);
                psmtUpdate.setString(3, orderNo);
                return psmtUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (psmtUpdate != null) {
                try {
                    psmtUpdate.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return 0;
    }

    public static int deleteById(final String orderNo) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;
        PreparedStatement psmtUpdate2 = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);
            String query = "SELECT * FROM porder WHERE order_no = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, orderNo);
            rs = psmtQuery.executeQuery();
            if (rs.next()) {
                String deleteStatement = "DELETE FROM porder WHERE order_no = ?";
                psmtUpdate = conn.prepareStatement(deleteStatement);
                psmtUpdate.setString(1, orderNo);

                if(psmtUpdate.executeUpdate() > 0){
                    String updateStatement = "UPDATE product SET stock = stock + ? FROM porder O WHERE product_no = ? AND O.order_no = ?";
                    psmtUpdate2 = conn.prepareStatement(updateStatement);
                    psmtUpdate2.setInt(1, rs.getInt("quantity"));
                    psmtUpdate2.setString(2, rs.getString("product_no"));
                    psmtUpdate2.setString(3, orderNo);


                    if(psmtUpdate2.executeUpdate() > 0){
                        conn.commit();
                        return psmtUpdate2.executeUpdate();
                    }else{
                        conn.rollback();
                        return -1;
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psmtQuery != null) {
                try {
                    psmtQuery.close();
                } catch (SQLException ignored) {
                }
            }
            if (psmtUpdate != null) {
                try {
                    psmtUpdate.close();
                } catch (SQLException ignored) {
                }
            }
            if (psmtUpdate2 != null) {
                try {
                    psmtUpdate2.close();
                } catch (SQLException ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return 0;
    }

}