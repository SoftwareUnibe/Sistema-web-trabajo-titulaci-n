package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.FinalDegreeCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinalDegreeCalendarRepository extends JpaRepository<FinalDegreeCalendar, String> {
    List<FinalDegreeCalendar> findByCareer_id(String careerId);
}
