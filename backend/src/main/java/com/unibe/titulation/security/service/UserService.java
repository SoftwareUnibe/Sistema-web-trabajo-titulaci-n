package com.unibe.titulation.security.service;

import com.unibe.titulation.repositories.DesignationTTRepository;
import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.enums.RolName;
import com.unibe.titulation.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    UserRepository userRepository;
    DesignationTTRepository designationTTRepository;

    @Autowired
    public UserService(UserRepository userRepository, DesignationTTRepository designationTTRepository) {
        this.userRepository = userRepository;
        this.designationTTRepository = designationTTRepository;
    }

    public List<User> getByRoleNameAndCareerId(String roleName, String careerId) {
        RolName rolName = RolName.valueOf(roleName);
        return userRepository.findUserByRoles_RolNameAndCareer_Id(rolName, careerId);
    }

    public User getOneUserByRoles_RolNameAndCareer_Id(String roleName, String careerId) {
        RolName rolName = RolName.valueOf(roleName);
        return userRepository.findOneUserByRoles_RolNameAndCareer_Id(rolName, careerId);
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> getByRoleName(String roleName) {
        RolName rolName = RolName.valueOf(roleName);
        return userRepository.findUserByRoles_RolName(rolName);
    }

    public List<User> findUserByRoleNames(List<String> rolNames) {
        List<User> usersFound = new ArrayList<>();
        List<RolName> rolNames1 = new ArrayList<>();
        for (int i = 0; i < rolNames.size(); i++) {
            rolNames1.add(RolName.valueOf(rolNames.get(i)));
            usersFound = userRepository.findUserByRoles_RolNameIn(rolNames1);
        }
        return usersFound;
    }

    public List<User> findUsersByTutorIdsAndRoleNames() {
        List<String> teachersAsTutorsIds = this.designationTTRepository.getAllTeachersAsTutorsIds();
        List<RolName> rolNames = new ArrayList<>();
        rolNames.add(RolName.valueOf("ROLE_CAREER_DIRECTOR"));
        rolNames.add(RolName.valueOf("ROLE_TEACHER"));
        List<User> userList = userRepository.findUserByIdNotInAndRoles_RolNameIn(teachersAsTutorsIds, rolNames);
        return userList;
    }

    public Optional<User> getByUserName(String nombreUsuario) {
        return userRepository.findByUserName(nombreUsuario);
    }

    public Optional<User> getByUserNameOrEmail(String nombreOrEmail) {
        return userRepository.findByUserNameOrEmail(nombreOrEmail, nombreOrEmail);
    }

    public Optional<User> getByCi(String ci) {
        return userRepository.findByCi(ci);
    }

    public Optional<User> getByCiAndCareerId(String ci, String careerId) {
        return this.userRepository.findByCiAndCareer_Id(ci, careerId);
    }

    public Optional<User> getByTokenPassword(String tokenPassword) {
        return userRepository.findByTokenPassword(tokenPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByUserName(String nombreUsuario) {
        return userRepository.existsByUserName(nombreUsuario);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existByCi(String ci) {
        return userRepository.existsByCi(ci);
    }

    public void save(User user) {
        userRepository.save(user);
    }


}
