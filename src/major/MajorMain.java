package major;

import java.util.List;

public class MajorMain {
    public static void main(String[] args) {
        // 1. 전체 조회 테스트
        System.out.println("=== 전체 전공 조회 ===");
        List<Major> allMajors = MajorService.selectAll();
        allMajors.forEach(major ->
                System.out.println("전공 ID: " + major.getMajorId() +
                        ", 전공명: " + major.getMajorName() +
                        ", 분류: " + major.getClassification())
        );

        // 2. 삽입 테스트
        System.out.println("\n=== 새로운 전공 삽입 ===");
        String newMajorId = MajorService.insert("CS001", "컴퓨터 공학", "이공계");
        if (newMajorId != null) {
            System.out.println("새로운 전공 삽입 성공: " + newMajorId);
        } else {
            System.out.println("전공 삽입 실패");
        }

        // 3. ID로 전공 조회 테스트
        System.out.println("\n=== 특정 전공 조회 ===");
        List<Major> specificMajors = MajorService.getMajorById("CS001");
        specificMajors.forEach(major ->
                System.out.println("전공 ID: " + major.getMajorId() +
                        ", 전공명: " + major.getMajorName() +
                        ", 분류: " + major.getClassification())
        );

        // 4. 전공 업데이트 테스트
        System.out.println("\n=== 전공 정보 업데이트 ===");
        int updateResult = MajorService.updateById("CS001", "컴퓨터 과학", "이공계열");
        System.out.println("업데이트 결과: " + (updateResult > 0 ? "성공" : "실패"));

//        // 5. 전공 삭제 테스트
//        System.out.println("\n=== 전공 삭제 ===");
//        int deleteResult = MajorService.deleteById("CS001");
//        System.out.println("삭제 결과: " + deleteResult);

        // 6. 삭제 후 전체 조회 테스트
        System.out.println("\n=== 삭제 후 전체 전공 조회 ===");
        List<Major> remainingMajors = MajorService.selectAll();
        remainingMajors.forEach(major ->
                System.out.println("전공 ID: " + major.getMajorId() +
                        ", 전공명: " + major.getMajorName() +
                        ", 분류: " + major.getClassification())
        );
    }
}