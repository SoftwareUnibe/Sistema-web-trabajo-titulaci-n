package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.Career;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CareerRepository extends JpaRepository<Career, String>{

	List<Career> findCareersByFaculty_Name(String faculty);
}
