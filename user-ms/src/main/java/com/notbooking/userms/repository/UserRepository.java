package com.notbooking.userms.repository;

import com.notbooking.userms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    @Query("select u from User u where " +
            "u.email = :email and u.isDeleted = false ")
    Optional<User> findByEmailAndNotDeleted(@Param("email") String email);

    @Query("select u from User u where " +
            "u.username = :username and u.isDeleted = false")
    Optional<User> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query("select u.email from User u where " +
            "u.username = :username and u.isDeleted = false")
    String findEmailByUsernameAndNotDeleted(@Param("username") String username);
}
