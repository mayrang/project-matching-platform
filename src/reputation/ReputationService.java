package reputation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReputationService {

    private static ReputationService setReputationService(ResultSet rs) throws SQLException {
        String student_id = rs.getString("student_id");
        Integer valuation_score = rs.getInt("valuation_score");
        String review = rs.getString("review");
        Integer reputation_id = rs.getInt("reputation_id");

        return new Reputation(student_id, valuation_score, review, reputation_id);
    }

    public static List<Reputation> selectAll(){

    }

}
