package com.notbooking.userms.repository;

import com.notbooking.userms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findById(@Param("id") long id);

    @Query("select res from UserRole res left join fetch res.users w where w.email =?1")
    Optional<UserRole> findByEmail(String email);

    @Query("select res from UserRole res left join fetch res.users w where w.username =?1")
    Optional<UserRole> findByUsername(String username);

    Optional<UserRole> findByName(@Param("name") String name);
}
