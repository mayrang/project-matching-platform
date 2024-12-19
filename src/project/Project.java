package project;
import java.sql.Date;

public class Project {
    private String projectId;
    private String projectName;
    private String topic;
    private int recruitment;
    private Date startDate;
    private Date endDate;
    private String goal;
    private String status;

    // Constructor
    public Project(String projectId, String projectName, String topic, int recruitment, Date startDate, Date endDate, String goal, String status) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.topic = topic;
        this.recruitment = recruitment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
        this.status = status;
    }

    public Project() {

    }

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(int recruitment) {
        this.recruitment = recruitment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}