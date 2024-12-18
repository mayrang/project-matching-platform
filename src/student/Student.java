package student;

import java.util.Date;

public class Student {

    private String student_id;
    private String name;
    private String major_id;
    private Integer grade;
    private String contact;


    public Student(final String student_id, final String name, final String major_id, final Integer grade,
                 final String contact) {
        this.student_id = student_id;
        this.name = name;
        this.major_id = major_id;
        this.grade = grade;
        this.contact = contact;

    }

    public String getStudent_id() {
        return student_id;
    }

    public String getName() {
        return name;
    }

    public String getMajorId() {
        return major_id;
    }

    public String getContact() {
        return contact;
    }

    public Integer getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "student_id='" + student_id + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major_id + '\'' +
                ", grade=" + grade +
                ", contact='" + contact + '\'' +
                '}';
    }
}