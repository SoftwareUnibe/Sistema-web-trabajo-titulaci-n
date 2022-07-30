package com.unibe.titulation.security.repository;

import com.unibe.titulation.security.entity.Rol;
import com.unibe.titulation.security.enums.RolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolName(RolName rolName);
    boolean existsByRolName(RolName rolName);
}
