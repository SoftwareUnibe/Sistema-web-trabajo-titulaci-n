package com.unibe.titulation.services;

import com.unibe.titulation.entities.Career;
import com.unibe.titulation.repositories.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CareerService {

    private final CareerRepository careerRepository;
    @Autowired
    public CareerService(CareerRepository careerRepository) {

        this.careerRepository = careerRepository;
    }
    public List<Career> findAllCareers() {

        return this.careerRepository.findAll();
    }

    public Optional<Career> findCareerById(String id) {

        return this.careerRepository.findById(id);
    }

    public List<Career> findCareersByFaculty(String faculty) {
        return this.careerRepository.findCareersByFaculty_Name(faculty);
    }

    public void addCareer(Career career) {
        this.careerRepository.save(career);
    }

    public boolean deleteCareerById(String id) {
        Optional<Career> careerOptional = this.findCareerById(id);
        if (!careerOptional.isPresent())
            return false;
        this.careerRepository.deleteById(id);
        return true;
    }
}
