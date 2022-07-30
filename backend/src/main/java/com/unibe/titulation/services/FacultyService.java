package com.unibe.titulation.services;

import com.unibe.titulation.entities.Faculty;
import com.unibe.titulation.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacultyService {

    private final FacultyRepository facultyRepository;
    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    public List<Faculty> findAllFacultys(){
        return this.facultyRepository.findAll();
    }

    public Optional<Faculty> findFacultyById(String id){
        return this.facultyRepository.findById(id);
    }

    public void addfaculty(Faculty faculty) {
        this.facultyRepository.save(faculty);
    }

    public boolean deleteFacultyById(String id) {
        Optional<Faculty> facultyOptional = this.findFacultyById(id);
        if (!facultyOptional.isPresent()) return false;
        facultyRepository.deleteById(id);
        return true;
    }
}
