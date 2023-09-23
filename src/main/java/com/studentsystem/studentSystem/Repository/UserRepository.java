package com.studentsystem.studentSystem.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.studentsystem.studentSystem.Entity.User;
public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByUserEmailAndUserPassword(String email,String password);
}
