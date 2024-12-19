package project;
import conf.Conf;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ProjectService {

    public static List<Project> getProjects(String status) throws SQLException {
        String sql = "SELECT * FROM project";
        if (status != null && !status.isEmpty()) {
            sql += " WHERE status = ?";
        }

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            if (status != null && !status.isEmpty()) {
                psmt.setString(1, status);
            }

            try (ResultSet rs = psmt.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (rs.next()) {
                    projects.add(setProject(rs));
                }
                return projects;
            }
        }
    }

    // 프로젝트 생성
    public static boolean createProject(String projectId, String projectName, String status, String topic, int recruitment,  String startDate, String endDate, String goal) throws SQLException {
        String sql = "INSERT INTO project (project_id, project_name, topic, recruitment, start_date, end_date, goal, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, projectId);
            psmt.setString(2, projectName);
            psmt.setString(3, topic); //. 주제
            psmt.setInt(4, 10); // 모집인원 수 예시로 10명 지정
            psmt.setDate(5, Date.valueOf(startDate));
            psmt.setDate(6, Date.valueOf(endDate));
            psmt.setString(7, goal); // 목표 설명 추가
            psmt.setString(8, status);

            return psmt.executeUpdate() > 0;
        }
    }

    // 프로젝트 신청
    public static int applyForProject(String studentId, String projectId, String role, String status) throws SQLException {
        String sql = "INSERT INTO participation_student (student_id, project_id, role, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            psmt.setString(1, studentId);
            psmt.setString(2, projectId);
            psmt.setString(3, role);
            psmt.setString(4, status);

            if (psmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = psmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // 생성된 참여 ID 반환
                    }
                }
            }
            return -1;
        }
    }


    // 프로젝트 상세보기
    public static Project getProjectById(String projectId) throws SQLException {
        String sql = "SELECT * FROM project WHERE project_id = ?";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, projectId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return setProject(rs);
                }
                return null; // 해당 프로젝트가 없을 경우
            }
        }
    }




    // role이 master인 사람이 신청 온거 확인하기
    public static List<ParticipationStudent> getMasterApplications(String projectId) throws SQLException {
        String sql = "SELECT * FROM participation_student WHERE project_id = ? AND status = '신청중'";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, projectId);

            try (ResultSet rs = psmt.executeQuery()) {
                List<ParticipationStudent> applications = new ArrayList<>();
                while (rs.next()) {
                    applications.add(setParticipationStudent(rs));
                }
                return applications;
            }
        }
    }

    public static List<Project> getProjectsByStudentId(String studentId) throws SQLException {
        String sql = "SELECT * FROM project p " +
                "JOIN participation_student ps ON p.project_id = ps.project_id " +
                "WHERE ps.student_id = ?";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, studentId); // studentId 바인딩

            try (ResultSet rs = psmt.executeQuery()) {
                List<Project> projects = new ArrayList<>();
                while (rs.next()) {
                    projects.add(setProject(rs));
                }
                return projects; // 학생이 참여한 프로젝트 목록 반환
            }
        }
    }

    // 신청 수락 / 거절
    public static boolean updateApplicationStatus(int participationId, String status) throws SQLException {
        String sql = "UPDATE participation_student SET status = ? WHERE participation_id = ?";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, status);
            psmt.setInt(2, participationId);

            return psmt.executeUpdate() > 0;
        }
    }

    public static boolean addStudentToProject(String projectId,String studentId,  String role, String status) throws SQLException {
        // 이미 학생이 해당 프로젝트에 참여 중인지 확인
        String checkQuery = "SELECT COUNT(*) FROM participation_student WHERE student_id = ? AND project_id = ?";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(checkQuery)) {
            psmt.setString(1, studentId);
            psmt.setString(2, projectId);

            try (ResultSet rs = psmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                // 이미 참여 중이면 false 반환
                if (count > 0) {
                    return false; // 이미 참여 중인 학생은 추가하지 않음
                }
            }
        }

        // 학생을 프로젝트에 추가
        String insertQuery = "INSERT INTO participation_student (student_id, project_id, role, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setString(1, studentId);
            psmt.setString(2, projectId);
            psmt.setString(3, role);
            psmt.setString(4, status);

            return psmt.executeUpdate() > 0; // 성공적으로 삽입되면 true 반환
        }
    }

    // 프로젝트 수정
    public static boolean updateProject(Project project) throws SQLException {
        String sql = "UPDATE project SET project_name = ?, topic = ?, recruitment = ?, start_date = ?, end_date = ?, goal = ?, status = ? WHERE project_id = ?";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setString(1, project.getProjectName());
            psmt.setString(2, project.getTopic());
            psmt.setInt(3, project.getRecruitment());
            psmt.setDate(4, project.getStartDate());
            psmt.setDate(5, project.getEndDate());
            psmt.setString(6, project.getGoal());
            psmt.setString(7, project.getStatus());
            psmt.setString(8, project.getProjectId());

            return psmt.executeUpdate() > 0;
        }
    }

    // 프로젝트 삭제
    public static boolean deleteProject(String projectId) throws SQLException {
        String sql = "DELETE FROM project WHERE project_id = ?";

        try (Connection conn = DriverManager.getConnection(Conf.DB_URL, Conf.DB_USER, Conf.DB_PASSWORD);
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, projectId);

            return psmt.executeUpdate() > 0;
        }
    }

    // ResultSet을 이용해 Project 객체 설정
    private static Project setProject(ResultSet rs) throws SQLException {
        String projectId = rs.getString("project_id");
        String projectName = rs.getString("project_name");
        String topic = rs.getString("topic");
        int recruitment = rs.getInt("recruitment");
        Date startDate = rs.getDate("start_date");
        Date endDate = rs.getDate("end_date");
        String goal = rs.getString("goal");
        String status = rs.getString("status");

        return new Project(projectId, projectName, topic, recruitment, startDate, endDate, goal, status);
    }

    // ResultSet을 이용해 ParticipationStudent 객체 설정
    private static ParticipationStudent setParticipationStudent(ResultSet rs) throws SQLException {
        int participationId = rs.getInt("participation_id");
        String studentId = rs.getString("student_id");
        String projectId = rs.getString("project_id");
        String role = rs.getString("role");
        String status = rs.getString("status");

        return new ParticipationStudent(participationId, studentId, projectId, role, status);
    }
}
