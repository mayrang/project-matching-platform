package major;


import java.util.List;

public class StudentMain {
    public static void main(String[] args) {
        // 1. 학생 추가 테스트
        System.out.println("=== 학생 추가 테스트 ===");
        String newStudentId = MajorService.insert("S2024001", "김철수", 1, 2, "010-1234-5678");
        if (newStudentId != null) {
            System.out.println("새 학생 추가 성공: " + newStudentId);
        } else {
            System.out.println("학생 추가 실패");
        }

        // 2. 전체 학생 조회 테스트
        System.out.println("\n=== 전체 학생 조회 ===");
        List<Major> allMajors = MajorService.selectAll();
        for (Major major : allMajors) {
            System.out.println(major);
        }

        // 3. 특정 프로젝트의 학생 조회 테스트
        System.out.println("\n=== 특정 프로젝트 학생 조회 ===");
        String projectId = "PROJ001"; // 실제 존재하는 프로젝트 ID로 대체
        List<Major> projectMajors = MajorService.getStudentsByProjectId(projectId);
        for (Major major : projectMajors) {
            System.out.println(major);
        }

        // 4. 학생 정보 업데이트 테스트
        System.out.println("\n=== 학생 정보 업데이트 ===");
        int updateResult = MajorService.updateById("S2024001", "김철수", "2", 3, "010-8765-4321");
        if (updateResult > 0) {
            System.out.println("학생 정보 업데이트 성공");
        } else {
            System.out.println("학생 정보 업데이트 실패");
        }

        // 5. 학생 삭제 테스트
        System.out.println("\n=== 학생 삭제 테스트 ===");
        int deleteResult = MajorService.deleteById("S2024001");
        if (deleteResult > 0) {
            System.out.println("학생 삭제 성공: " + deleteResult + "명 삭제");
        } else if (deleteResult == 0) {
            System.out.println("삭제할 학생 없음");
        } else {
            System.out.println("학생 삭제 실패");
        }
    }
}