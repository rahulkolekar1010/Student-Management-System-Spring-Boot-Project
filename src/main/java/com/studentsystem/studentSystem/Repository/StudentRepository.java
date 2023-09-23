package com.studentsystem.studentSystem.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import com.studentsystem.studentSystem.Entity.Student;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface StudentRepository extends CrudRepository<Student,Integer>, PagingAndSortingRepository<Student,Integer> {
    Page<Student> findStudentByStudentNameContainingIgnoreCase(String name,Pageable pageable);
    Page<Student> findStudentByStudentDepartmentContainingIgnoreCase(String department, Pageable pageable);
    Page <Student> findStudentByStudentClassContainingIgnoreCase(String className, Pageable pageable);
    Page<Student> findStudentByStudentGenderContainingIgnoreCase(String gender, Pageable pageable);
}
