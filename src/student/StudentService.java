package student;

import conf.Conf;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentService {

    private static Student setStudent(ResultSet rs) throws SQLException {
        String student_id = rs.getString("student_id");
        String student_name = rs.getString("student_name");
        String major_id = rs.getString("major_id");
        Integer grade = rs.getInt("grade");
        String contact = rs.getString("contact");

        return new Student(student_id, student_name, major_id, grade, contact);
    }

    public static List<Student> selectAll() {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Student> studentList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {

            String query = "SELECT S.student_id, S.student_name, S.major_id, S.grade, S.contact, M.major_name, M.classification " +
                    "FROM student S " +
                    "JOIN major M ON M.major_id = S.major_id";;
            psmtQuery = conn.prepareStatement(query);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
               Student  student = setStudent(rs);
                studentList.add(student);
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

        return studentList;
    }
    public static List<Student> getStudentsByProjectId(final String projectId) {
        ResultSet rs = null;
        PreparedStatement psmtQuery = null;

        List<Student> studentList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
        ) {
             psmtQuery = conn.prepareStatement(
                    "SELECT SP.*, S,*" +
                            "FROM partification_student SP " +
                            "JOIN student S ON SP.student_id = S.student_id " +
                            "WHERE SP.project_id = ?");
             psmtQuery.setString(1, projectId);
            rs = psmtQuery.executeQuery();
            while (rs.next()) {
                Student  student = setStudent(rs);

                studentList.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    public static String insert(final String student_id, final String student_name , final String major_id, final Integer grade, final String contact) {


        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);
            String insertStatement = "INSERT INTO student VALUES (?,?,?,?,?)";

            psmtUpdate = conn.prepareStatement(insertStatement);

            psmtUpdate.setString(1, student_id);
            psmtUpdate.setString(2, student_name);
            psmtUpdate.setString(3, major_id);
            psmtUpdate.setInt(4, grade);
            psmtUpdate.setString(5, contact);
            if (psmtUpdate.executeUpdate() > 0) {
               conn.commit();
                return student_id;



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

    public static int updateById(final String student_id, final String student_name , final String major_id, final Integer grade, final String contact) {

        ResultSet rs = null;
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtUpdate = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            String query = "SELECT * FROM student WHERE student_id = ?";
            psmtQuery = conn.prepareStatement(query);
            psmtQuery.setString(1, student_id);
            rs = psmtQuery.executeQuery();
            if (rs.next()) {
                String updateStatement = "UPDATE student SET student_name = ?, major_id = ?, grade = ?, contact = ?  WHERE student_id = ?";
                psmtUpdate = conn.prepareStatement(updateStatement);

                psmtUpdate.setString(1, student_name);
                psmtUpdate.setString(2, major_id);
                psmtUpdate.setInt(3, grade);
                psmtUpdate.setString(4, contact);
                psmtUpdate.setString(5, student_id);
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
    public static int deleteById(final String student_id) {
        PreparedStatement psmtQuery = null;
        PreparedStatement psmtDeleteParticipation = null;
        PreparedStatement psmtDeleteStudent = null;

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String checkQuery = "SELECT * FROM student WHERE student_id = ?";
            psmtQuery = conn.prepareStatement(checkQuery);
            psmtQuery.setString(1, student_id);

            try (ResultSet rs = psmtQuery.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
            }

            String deleteParticipationQuery = "DELETE FROM participation_student WHERE student_id = ?";
            psmtDeleteParticipation = conn.prepareStatement(deleteParticipationQuery);
            psmtDeleteParticipation.setString(1, student_id);
            int participationDeletedCount = psmtDeleteParticipation.executeUpdate();

            String deleteStudentQuery = "DELETE FROM student WHERE student_id = ?";
            psmtDeleteStudent = conn.prepareStatement(deleteStudentQuery);
            psmtDeleteStudent.setString(1, student_id);
            int studentDeletedCount = psmtDeleteStudent.executeUpdate();

            if (studentDeletedCount > 0) {
                conn.commit();
                return studentDeletedCount;
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
                if (psmtDeleteParticipation != null) psmtDeleteParticipation.close();
                if (psmtDeleteStudent != null) psmtDeleteStudent.close();
            } catch (SQLException ignored) {
            }
        }
    }

}