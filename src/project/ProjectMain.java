package project;

import java.sql.*;

public class ProjectMain {
    public static void main(String[] args) throws SQLException {
        // 예시: 프로젝트 리스트 가져오기
        System.out.println("== 전체 프로젝트 리스트 조회 ==");
        for (Project project : ProjectService.getProjects(null)) {
            System.out.println("Project Name: " + project.getProjectName());
        }

        // 예시: 특정 상태에 따른 프로젝트 리스트 가져오기
        System.out.println("== '진행중' 상태의 프로젝트 조회 ==");
        for (Project project : ProjectService.getProjects("진행중")) {
            System.out.println("Project Name: " + project.getProjectName());
        }

        // 예시: 프로젝트 상세보기
        System.out.println("== 프로젝트 상세 조회 ==");
        Project project = ProjectService.getProjectById("P001");
        if (project != null) {
            System.out.println("Project Name: " + project.getProjectName());
        }

        // 예시: 프로젝트 신청하기
        int participationId = ProjectService.applyForProject("S001", "P001", "member", "신청중");
        System.out.println("참여 신청 ID: " + participationId);

        // 예시: 신청 수락 / 거절
        boolean result = ProjectService.updateApplicationStatus(participationId, "수락");
        System.out.println("신청 수락 결과: " + result);
    }
}
