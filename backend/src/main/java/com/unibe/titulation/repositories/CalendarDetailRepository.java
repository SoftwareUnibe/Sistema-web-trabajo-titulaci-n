package com.unibe.titulation.repositories;

import com.unibe.titulation.entities.CalendarDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarDetailRepository extends JpaRepository<CalendarDetail, String> {
    List<CalendarDetail> findByFinalDegreeCalendar_Id(String id);
    CalendarDetail findByStudent_Email(String studentEmail);
    boolean existsByFinalDegreeCalendar_Id(String id);

}
