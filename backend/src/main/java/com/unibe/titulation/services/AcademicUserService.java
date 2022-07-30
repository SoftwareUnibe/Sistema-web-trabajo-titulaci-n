package com.unibe.titulation.services;

import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.enums.RolName;
import com.unibe.titulation.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class AcademicUserService {
    private final UserRepository academicUserRepository;
    @Autowired
    public AcademicUserService(UserRepository academicUserRepository) {
        this.academicUserRepository = academicUserRepository;
    }
    public List<User> findUserByCareerDirector(String career) {
        return this.academicUserRepository.findUserByRoles_RolNameAndCareer_Id(RolName.ROLE_CAREER_DIRECTOR, career);
    }
}
