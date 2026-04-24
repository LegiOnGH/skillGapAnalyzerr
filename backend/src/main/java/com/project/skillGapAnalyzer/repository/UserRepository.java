package com.project.skillGapAnalyzer.repository;

import com.project.skillGapAnalyzer.enums.UserRole;
import com.project.skillGapAnalyzer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserNameIgnoreCase(String userName);

    boolean existsByUserNameIgnoreCase(String userName);
    boolean existsByEmailIgnoreCase(String email);

    long countByRole(UserRole userRole);

    Page<User> findByUserNameContainingIgnoreCaseAndRole(
            String search, UserRole userRole, Pageable pageable);
    Page<User> findByUserNameContainingIgnoreCase(
            String search, Pageable pageable);
    Page<User> findByRole(UserRole userRole, Pageable pageable);


}
