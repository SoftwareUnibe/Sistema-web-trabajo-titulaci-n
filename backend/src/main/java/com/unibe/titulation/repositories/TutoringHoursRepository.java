package com.unibe.titulation.repositories;
import com.unibe.titulation.entities.TutoringHours;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TutoringHoursRepository extends JpaRepository<TutoringHours, String> {
    
    List<TutoringHours> findByTopic_Id(String topic_Id);
}
