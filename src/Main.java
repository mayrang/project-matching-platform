




import major.MajorService;
import project.ParticipationStudent;
import project.ProjectService;
import reputation.Reputation;
import student.StudentService;
import reputation.ReputationService;
import student.Student;
import project.Project;
import major.Major;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            insertSampleData();
            while (true) {
                System.out.println("\n메뉴:");
                System.out.println("1. 학생 가입자 플로우");
                System.out.println("2. 프로젝트 관리자 플로우");
                System.out.println("3. 평판 관리 플로우");
                System.out.println("0. 종료");

                System.out.print("원하는 메뉴 번호를 입력하세요: ");
                int menuChoice = Integer.parseInt(scanner.nextLine());

                if (menuChoice == 0) break;

                switch (menuChoice) {
                    case 1:
                        studentFlow(scanner);
                        break;
                    case 2:
                        projectManagerFlow(scanner);
                        break;
                    case 3:
                        reputationFlow(scanner);
                        break;
                    default:
                        System.out.println("잘못된 메뉴 번호입니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 학생 가입자 플로우
    private static void studentFlow(Scanner scanner) {
        System.out.println("\n-- 학생 가입자 플로우 --");
        createStudent(scanner); // 학생 가입
        showStudentList(); // 학생 리스트 조회
        showProjectListAndApply(scanner); // 프로젝트 리스트 조회 및 자동 신청
        showParticipatedProjects(scanner); // 참여한 프로젝트 확인
    }

    // 프로젝트 관리자 플로우
    private static void projectManagerFlow(Scanner scanner) {
        System.out.println("\n-- 프로젝트 관리자 플로우 --");
        createProject(scanner); // 새 프로젝트 생성
        showProjectApplicants(scanner); // 신청한 학생 자동 추가
        manageProjectParticipants(scanner); // 학생 수락/거절 및 학생 보기
        updateDeleteProject(scanner); // 프로젝트 업데이트 및 삭제
    }

    // 평판 관리 플로우
    private static void reputationFlow(Scanner scanner) {
        System.out.println("\n-- 평판 관리 플로우 --");
        showStudentList(); // 학생 리스트 조회
        showStudentReputation(scanner); // 평판 상세 보기
        manageReputation(scanner); // 평판 작성, 수정, 삭제
    }

    // 학생 가입
    private static void createStudent(Scanner scanner) {
        System.out.println("\n학생 정보를 입력하세요.");
        System.out.print("학생 ID: ");
        String studentId = scanner.nextLine();
        System.out.print("학생 이름: ");
        String studentName = scanner.nextLine();
        System.out.print("전공 코드: ");
        String majorCode = scanner.nextLine();
        System.out.print("학년: ");
        int grade = Integer.parseInt(scanner.nextLine());
        System.out.print("전화번호: ");
        String phone = scanner.nextLine();

        try {
            StudentService.insert(studentId, studentName, majorCode, grade, phone);
            System.out.println("학생 정보가 저장되었습니다.");
        } catch (Exception e) {
            System.out.println("학생 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 학생 리스트 조회
    private static void showStudentList() {
        System.out.println("\n학생 리스트:");
        try {
            List<Student> students = StudentService.selectAll();
            for (Student student : students) {
                System.out.println("학생 ID: " + student.getStudent_id() + ", 이름: " + student.getName());
            }
        } catch (Exception e) {
            System.out.println("학생 리스트 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 프로젝트 리스트 조회 및 신청
    private static void showProjectListAndApply(Scanner scanner) {
        System.out.println("\n프로젝트 리스트:");
        try {
            List<Project> projects = ProjectService.getProjects(null);
            for (Project project : projects) {
                System.out.println("프로젝트 ID: " + project.getProjectId() + ", 프로젝트명: " + project.getProjectName());
            }

            System.out.print("\n자동으로 신청할 프로젝트 ID를 입력하세요: ");
            String projectId = scanner.nextLine();
            System.out.print("\n 내 ID를 입력하세요: ");
            String studentId = scanner.nextLine();

            int id = ProjectService.applyForProject(studentId, projectId, "member", "신청중");
            System.out.println("프로젝트 신청이 완료되었습니다.");
            ProjectService.updateApplicationStatus(id, "참여");

        } catch (Exception e) {
            System.out.println("프로젝트 신청 중 오류 발생: " + e.getMessage());
        }
    }

    // 학생이 참여한 프로젝트 목록 확인
    private static void showParticipatedProjects(Scanner scanner) {
        System.out.println("\n참여한 프로젝트 목록:");
        try {
            System.out.print("\n 내 ID를 입력하세요: ");
            String studentId = scanner.nextLine();
            List<Project> projects = ProjectService.getProjectsByStudentId(studentId);
            for (Project project : projects) {
                System.out.println("프로젝트 ID: " + project.getProjectId() + ", 프로젝트명: " + project.getProjectName());
            }
        } catch (Exception e) {
            System.out.println("참여 프로젝트 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 새 프로젝트 생성
    private static void createProject(Scanner scanner) {
        System.out.print("\n새 프로젝트를 생성하려면 프로젝트명을 입력하세요: ");
        String newProjectName = scanner.nextLine();
        System.out.print("프로젝트 상태 (시작예정/진행중/완료): ");
        String newProjectStatus = scanner.nextLine();
        System.out.print("프로젝트 주제 : ");
        String topic = scanner.nextLine();
        System.out.print("시작일 (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("종료일 (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        System.out.print("모집인원: ");
        int recruitment = Integer.parseInt(scanner.nextLine());
        System.out.print("프로젝트 목표 : ");
        String goal = scanner.nextLine();

        try {
            ProjectService.createProject("P012", newProjectName, newProjectStatus, topic,recruitment, startDate, endDate, goal);
            System.out.println("새 프로젝트가 생성되었습니다.");
        } catch (Exception e) {
            System.out.println("프로젝트 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // 프로젝트 신청자 자동 추가
    private static void showProjectApplicants(Scanner scanner) {
        System.out.println("\n프로젝트 신청자 자동 추가:");
        try {

            String projectId = "P012"; // 예시 프로젝트 ID
            ProjectService.applyForProject("2023001", projectId, "member", "신청중");
            List<ParticipationStudent> applicants = ProjectService.getMasterApplications(projectId);
            for (ParticipationStudent participationStudent : applicants) {
                System.out.println("학생 ID: " + participationStudent.getStudentId() + "가 신청했습니다.");
                ProjectService.addStudentToProject(projectId, participationStudent.getStudentId(), "member", "신청중");
            }
            System.out.println("신청자들이 자동으로 프로젝트에 추가되었습니다.");
        } catch (Exception e) {
            System.out.println("신청자 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 프로젝트 학생 수락/거절
    private static void manageProjectParticipants(Scanner scanner) {

        System.out.print("\n프로젝트에 참여할 학생을 수락/거절하세요. (학생 ID와 수락/거절 입력): ");
        String studentId = scanner.nextLine();
        String decision = scanner.nextLine();

        if ("수락".equals(decision)) {
            System.out.println(studentId + " 학생을 수락했습니다.");
        } else if ("거절".equals(decision)) {
            System.out.println(studentId + " 학생을 거절했습니다.");
        }
    }

    // 프로젝트 업데이트 및 삭제
    private static void updateDeleteProject(Scanner scanner) {
        // 1. 프로젝트 ID 입력
        System.out.print("\n업데이트할 프로젝트 ID 입력: ");
        String projectId = scanner.nextLine();

        // 2. 프로젝트 정보를 업데이트할 새로운 데이터 입력
        System.out.print("새 프로젝트명 입력: ");
        String projectName = scanner.nextLine();
        System.out.print("프로젝트 주제 입력: ");
        String topic = scanner.nextLine();
        System.out.print("프로젝트 모집인원 입력: ");
        int recruitment = Integer.parseInt(scanner.nextLine());
        System.out.print("시작일 (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("종료일 (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();
        System.out.print("프로젝트 목표 입력: ");
        String goal = scanner.nextLine();
        System.out.print("프로젝트 상태 (시작예정/진행중/완료): ");
        String status = scanner.nextLine();

        // 3. 프로젝트 객체 생성
        Project updatedProject = new Project();
        updatedProject.setProjectId(projectId);  // 기존 프로젝트 ID
        updatedProject.setProjectName(projectName);
        updatedProject.setTopic(topic);
        updatedProject.setRecruitment(recruitment);
        updatedProject.setStartDate(Date.valueOf(startDate));
        updatedProject.setEndDate(Date.valueOf(endDate));
        updatedProject.setGoal(goal);
        updatedProject.setStatus(status);

        try {
            // 4. 프로젝트 업데이트 호출
            ProjectService.updateProject(updatedProject);
            System.out.println("프로젝트가 업데이트되었습니다.");
            List<Project> projects = ProjectService.getProjects(null);
            for (Project project : projects) {
                System.out.println("프로젝트 ID: " + project.getProjectId() + ", 프로젝트명: " + project.getProjectName());
            }
        } catch (Exception e) {
            System.out.println("프로젝트 업데이트 중 오류 발생: " + e.getMessage());
        }

        // 5. 프로젝트 삭제
        System.out.print("삭제할 프로젝트 ID 입력: ");
        String deleteProjectId = scanner.nextLine();

        try {
            ProjectService.deleteProject(deleteProjectId);
            System.out.println("프로젝트가 삭제되었습니다.");
            List<Project> projects = ProjectService.getProjects(null);
            for (Project project : projects) {
                System.out.println("프로젝트 ID: " + project.getProjectId() + ", 프로젝트명: " + project.getProjectName());
            }
        } catch (Exception e) {
            System.out.println("프로젝트 삭제 중 오류 발생: " + e.getMessage());
        }
    }


    // 학생 평판 상세 보기
    private static void showStudentReputation(Scanner scanner) {
        System.out.print("\n평판을 조회할 학생 ID를 입력하세요: ");
        String studentId = scanner.nextLine();

        try {
            List<Reputation> reputations = ReputationService.getReputationByStudentId(studentId, "DESC");
            for (Reputation reputation : reputations) {
                System.out.println(reputation.toString());
            }
        } catch (Exception e) {
            System.out.println("평판 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 평판 작성, 수정, 삭제
    private static void manageReputation(Scanner scanner) {
        System.out.println("\n평판을 작성, 수정, 삭제할 수 있습니다.");
        System.out.print("평판 내용: ");
        String reputationText = scanner.nextLine();
        System.out.print("평판 점수: ");
        int valuation_score = Integer.parseInt(scanner.nextLine());
        System.out.print("해당 사용자 ID: ");
        String student_id = scanner.nextLine();

        System.out.print("평판 수정/삭제 여부 (수정/삭제): ");


        String action = scanner.nextLine();


        try {
            if ("수정".equals(action)) {
                System.out.print("평판  ID: ");
                int reviewerId = Integer.parseInt(scanner.nextLine());

                ReputationService.update(reviewerId, valuation_score, reputationText);
                System.out.println("평판이 수정되었습니다.");
            } else if ("삭제".equals(action)) {
                System.out.print("평판  ID: ");
                int reviewerId = Integer.parseInt(scanner.nextLine());

                ReputationService.delete(reviewerId);
                System.out.println("평판이 삭제되었습니다.");
            } else {
                ReputationService.insert(student_id, valuation_score, reputationText);
                System.out.println("평판이 작성되었습니다.");
            }
            List<Reputation> reputations = ReputationService.getReputationByStudentId(student_id, "DESC");
            for (Reputation reputation : reputations) {
                System.out.println(reputation.toString());
            }

        } catch (Exception e) {
            System.out.println("평판 처리 중 오류 발생: " + e.getMessage());
        }
    }

        // 샘플 데이터 삽입
    private static void insertSampleData() throws Exception {
        // 전공 데이터 삽입
           MajorService.insert("CS001", "Computer Science", "Engineering");
            MajorService.insert("EE001", "Electrical Engineering", "Engineering");
           MajorService.insert("ME001", "Mechanical Engineering", "Engineering");

            // 학생 데이터 삽입
           StudentService.insert("2023001", "Alice", "CS001", 3, "010-1111-2222");
            StudentService.insert("2023002", "Bob", "EE001", 2, "010-3333-4444");
            StudentService.insert("2023003", "Charlie", "ME001", 1, "010-5555-6666");
            StudentService.insert("2023004", "David", "CS001", 4, "010-7777-8888");

        // 프로젝트 데이터 삽입
        ProjectService.createProject("P001", "AI Research", "진행중", "Artificial Intelligence project", 4, "2024-12-01", "2025-12-01", "Research on AI");
        ProjectService.createProject("P002", "IoT Development", "진행중", "Internet of Things project", 6, "2024-12-01", "2025-06-01", "Development of IoT");
        ProjectService.createProject("P003", "Autonomous Vehicles", "진행중", "Self-driving cars", 3, "2024-11-01", "2025-05-01", "Research on autonomous vehicles");
        ProjectService.createProject("P004", "Blockchain Security", "진행중", "Blockchain technology research",  3, "2024-12-01", "2025-12-31", "Research on blockchain security");
        ProjectService.createProject("P005", "Cloud Computing", "진행중", "Cloud infrastructure development", 6, "2024-10-01", "2025-10-01", "Development of cloud computing infrastructure");

        // 프로젝트에 속한 학생들 추가 (role: master, member)
        ProjectService.addStudentToProject("P001", "2023001", "master", "참여");
        ProjectService.addStudentToProject("P001", "2023002", "member", "참여");
        ProjectService.addStudentToProject("P001", "2023003", "member", "참여");
        ProjectService.addStudentToProject("P001", "2023004", "member", "참여");

        ProjectService.addStudentToProject("P002", "2023002", "master", "참여");
        ProjectService.addStudentToProject("P002", "2023003", "member", "참여");
        ProjectService.addStudentToProject("P002", "2023004", "member", "참여");
        ProjectService.addStudentToProject("P002", "2023001", "member", "참여");

        ProjectService.addStudentToProject("P003", "2023003", "master", "참여");
        ProjectService.addStudentToProject("P003", "2023004", "member", "참여");

        ProjectService.addStudentToProject("P004", "2023004", "master", "참여");
        ProjectService.addStudentToProject("P004", "2023001", "member", "참여");

        ProjectService.addStudentToProject("P005", "2023001", "master", "참여");
        ProjectService.addStudentToProject("P005", "2023002", "member", "참여");

        // 학생의 평판 추가
        ReputationService.insert("2023001", 4, "Excellent teamwork and problem-solving skills");
        ReputationService.insert("2023002", 3, "Great contribution but needs to improve communication");
        ReputationService.insert("2023003", 5, "Outstanding technical skills and leadership");
        ReputationService.insert("2023004", 4, "Good work ethic, but should be more proactive");
    }

}
