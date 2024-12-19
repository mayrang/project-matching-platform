package reputation;

import java.sql.SQLException;
import java.util.List;

public class ReputationMain {
    public static void main(String[] args) {
        try {
            // 1. Insert 테스트
            System.out.println("=== Insert Test ===");
            int insertedReputationId = ReputationService.insert("20221018", 4, "Excellent performance");
            if (insertedReputationId > 0) {
                System.out.println("Inserted successfully! New Reputation ID: " + insertedReputationId);
            } else {
                System.out.println("Insert failed.");
            }

            // 2. GetReputationByStudentId 테스트
            System.out.println("\n=== GetReputationByStudentId Test ===");
            List<Reputation> reputations = ReputationService.getReputationByStudentId("20221017", "DESC");
            for (Reputation reputation : reputations) {
                System.out.println("Reputation ID: " + reputation.getReputation_id() +
                        ", Student ID: " + reputation.getStudent_id() +
                        ", Valuation Score: " + reputation.getValuation_score() +
                        ", Review: " + reputation.getReview());
            }

            // 3. Update 테스트
            System.out.println("\n=== Update Test ===");
            int updatedRows = ReputationService.update(insertedReputationId, 3, "Outstanding performance!");
            if (updatedRows > 0) {
                System.out.println("Update successful! Updated rows: " + updatedRows);
            } else {
                System.out.println("Update failed.");
            }

            // 4. Delete 테스트
            System.out.println("\n=== Delete Test ===");
            int deletedRows = ReputationService.delete(insertedReputationId);
            if (deletedRows > 0) {
                System.out.println("Delete successful! Deleted rows: " + deletedRows);
            } else {
                System.out.println("Delete failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
