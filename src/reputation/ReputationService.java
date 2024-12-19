package reputation;

import conf.Conf;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReputationService {

    private static Reputation setReputation(ResultSet rs) throws SQLException {
        String student_id = rs.getString("student_id");
        Integer valuation_score = rs.getInt("valuation_score");
        String review = rs.getString("review");
        Integer reputation_id = rs.getInt("reputation_id");



        return new Reputation(student_id, valuation_score, review, reputation_id);
    }

    public static List<Reputation> getReputationByStudentId(final String student_id, final String order) throws SQLException {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Reputation> reputationList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String orderClause = "DESC".equalsIgnoreCase(order) ? "DESC" : "ASC";
            String query = "SELECT * FROM reputation WHERE student_id = ? ORDER BY valuation_score " + orderClause;

            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, student_id);

            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Reputation reputation = setReputation(rs);
                reputationList.add(reputation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reputationList;
    }

    public static int insert(final String student_id, final int valuation_score, final String review) {
        PreparedStatement psmtInsert = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);
            String insertStatement = "INSERT INTO reputation (student_id, valuation_score, review) VALUES (?, ?, ?)";

            psmtInsert = conn.prepareStatement(insertStatement,  Statement.RETURN_GENERATED_KEYS);

            psmtInsert.setString(1, student_id);
            psmtInsert.setInt(2, valuation_score);
            psmtInsert.setString(3, review);

            if (psmtInsert.executeUpdate() > 0) {
                try (ResultSet generatedKeys = psmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        conn.commit();

                        return generatedKeys.getInt("reputation_id");
                    }
                }
            }
             else {
                conn.rollback();
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (psmtInsert != null) {
                try {
                    psmtInsert.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return 0;
    }

    public static int update(final int reputation_id, final int valuation_score, final String review) {
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String updateStatement = "UPDATE reputation SET valuation_score = ?, review = ? WHERE reputation_id = ?";
            psmtUpdate = conn.prepareStatement(updateStatement);

            psmtUpdate.setInt(1, valuation_score);
            psmtUpdate.setString(2, review);
            psmtUpdate.setInt(3, reputation_id);

            return psmtUpdate.executeUpdate(); // 업데이트된 row 수 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // 실패 시 0 반환
        } finally {
            if (psmtUpdate != null) {
                try {
                    psmtUpdate.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static int delete(final int reputation_id) {
        PreparedStatement psmtDelete = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String deleteStatement = "DELETE FROM reputation WHERE reputation_id = ?";
            psmtDelete = conn.prepareStatement(deleteStatement);
            psmtDelete.setInt(1, reputation_id);

            int deletedCount = psmtDelete.executeUpdate();

            if (deletedCount > 0) {
                conn.commit();
                return deletedCount; // 삭제된 row 수 반환
            } else {
                conn.rollback();
                return 0; // 삭제 실패
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (psmtDelete != null) {
                try {
                    psmtDelete.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }



}
