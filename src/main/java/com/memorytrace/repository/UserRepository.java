package com.memorytrace.repository;


import com.memorytrace.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(Long uid);

    Optional<User> findBySnsKey(String snsKey);
}
