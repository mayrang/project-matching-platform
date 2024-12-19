package project;

public class ParticipationStudent {
    private int participationId;
    private String studentId;
    private String projectId;
    private String role;
    private String status;

    // Constructor
    public ParticipationStudent(int participationId, String studentId, String projectId, String role, String status) {
        this.participationId = participationId;
        this.studentId = studentId;
        this.projectId = projectId;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public int getParticipationId() {
        return participationId;
    }

    public void setParticipationId(int participationId) {
        this.participationId = participationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
