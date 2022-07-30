package com.unibe.titulation.security.repository;

import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.enums.RolName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCi(String ci);
    User findOneUserByRoles_RolNameAndCareer_Id(RolName rol, String careerID);
    List<User> findUserByRoles_RolNameAndCareer_Id(RolName rol, String careerID);
    List<User> findUserByRoles_RolName(RolName rol);
    List<User> findUserByRoles_RolNameIn(Collection<RolName> roles);
    List<User> findUserByIdNotInAndRoles_RolNameIn(Collection<String> teacherIds, Collection<RolName> rolNames);
    Optional<User> findByUserName(String userName);
    Optional<User> findByUserNameOrEmail(String userName, String email);
    Optional<User> findByTokenPassword(String tokenPassword);
    Optional<User> findByCiAndCareer_Id(String ci, String career_id);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByCi(String ci);

}
