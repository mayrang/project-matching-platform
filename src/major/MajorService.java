package major;

import conf.Conf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MajorService {

    private static Major setMajor(ResultSet rs) throws SQLException {
        String major_id = rs.getString("major_id");
        String major_name = rs.getString("major_name");
        String classification = rs.getString("classification");

        return new Major(major_id, major_name, classification);
    }

    public static List<Major> selectAll() {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Major> majorList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {

            String query = "SELECT * FROM major";
            psmtQuery = conn.prepareStatement(query);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
               Major major = setMajor(rs);
                majorList.add(major);
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

        return majorList;
    }
    public static List<Major> getMajorById(final String majorId) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Major> majorList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
        ) {
             psmtQuery = conn.prepareStatement(
                    "SELECT *" +
                            "FROM major " +

                            "WHERE major_id = ?");
             psmtQuery.setString(1, majorId);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Major major = setMajor(rs);

                majorList.add(major);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return majorList;
    }

    public static String insert(final String major_id, final String major_name, final String classification) {


        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);
            String insertStatement = "INSERT INTO major VALUES (?,?,?)";

            psmtUpdate = conn.prepareStatement(insertStatement);

            psmtUpdate.setString(1, major_id);
            psmtUpdate.setString(2, major_name);

            psmtUpdate.setString(3, classification);
            if (psmtUpdate.executeUpdate() > 0) {
               conn.commit();
                return major_id;



            } else {
                conn.rollback();
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static int updateById(final String major_id, final String major_name, final String classification) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT * FROM major WHERE major_id = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, major_id);
            rs = psmtQuery.executeQuery();
            if (rs.next()) {
                String updateStatement = "UPDATE major SET major_name = ?, classification = ?  WHERE major_id = ?";
                psmtUpdate = conn.prepareStatement(updateStatement);

                psmtUpdate.setString(1, major_name);
                psmtUpdate.setString(2, classification);

                psmtUpdate.setString(3, major_id);
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
    public static int deleteById(final String major_id) {
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtDeleteMajor = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String checkQuery = "SELECT * FROM major WHERE major_id = ?";
            psmtQuery = conn.prepareStatement(checkQuery);
            psmtQuery.setString(1, major_id);

            try (ResultSet rs = psmtQuery.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
            }


            String deleteStudentQuery = "DELETE FROM major WHERE major_id = ?";
            psmtDeleteMajor = conn.prepareStatement(deleteStudentQuery);
            psmtDeleteMajor.setString(1, major_id);
            int majorDeletedCount = psmtDeleteMajor.executeUpdate();

            if (majorDeletedCount > 0) {
                conn.commit();
                return majorDeletedCount;
            } else {
                conn.rollback();
                return -1;
            }

        } catch (SQLException e) {
            // 예외 발생 시 롤백
            e.printStackTrace();
            return -1;
        } finally {
            // 리소스 정리
            try {
                if (psmtQuery != null) psmtQuery.close();

                if (psmtDeleteMajor != null) psmtDeleteMajor.close();
            } catch (SQLException ignored) {
            }
        }
    }

}