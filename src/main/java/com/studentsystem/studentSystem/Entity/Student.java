package com.studentsystem.studentSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;
    private String studentName;
    private String studentAddress;
    private String studentGender;
    private String studentClass;
    private String studentDepartment;
    private String studentPhoto;

    public Student(Integer studentId, String studentName, String studentAddress, String studentGender,
            String studentClass, String studentDepartment, String studentPhoto) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.studentGender = studentGender;
        this.studentClass = studentClass;
        this.studentDepartment = studentDepartment;
        this.studentPhoto = studentPhoto;
    }
}
