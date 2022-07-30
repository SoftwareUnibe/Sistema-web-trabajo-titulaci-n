package com.unibe.titulation.services;

import com.unibe.titulation.dtos.TopicTableDto;
import com.unibe.titulation.entities.Topic;
import com.unibe.titulation.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TopicService {

    private TopicRepository topicRepository;
    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }
    public List<TopicTableDto> findTopicsByCareer(String career) {
        List<Topic> topicList = this.topicRepository.findTopicsByTopicStatusAndCareer_Id("Disponible", career );
        List<TopicTableDto> topicDtoList = new ArrayList<>();
        for (Topic topic :
                topicList) {
            TopicTableDto topicDto = new TopicTableDto(topic.getId(), topic.getName(), topic.getArticulation(),topic.getCareer().getName());
            topicDtoList.add(topicDto);
        }
        return topicDtoList;
    }

    public List<TopicTableDto> findTopicsByTopicStatus() {
        List<Topic> topicList = this.topicRepository.findTopicsByTopicStatus("Disponible");;
        List<TopicTableDto> topicDtoList = new ArrayList<>();
        for (Topic topic :
                topicList) {
            TopicTableDto topicDto = new TopicTableDto(topic.getId(), topic.getName(), topic.getArticulation(),topic.getCareer().getName());
            topicDtoList.add(topicDto);
        }
        return topicDtoList;
    }

    public Optional<Topic> findTopicById(String id) {
        return this.topicRepository.findById(id);
    }

    public void addTopic(Topic topic) {
        this.topicRepository.save(topic);
    }

    public boolean updateTopicById(String id, Topic topic) {
        Optional<Topic> topicOptional = this.findTopicById(id);
        if (!topicOptional.isPresent())
            return false;
        Topic topic1 = topicOptional.get();
        topic1.setName(topic.getName());
        topic1.setArticulation(topic.getArticulation());
        topic1.setDescription(topic.getDescription());
        topic1.setTwoStudents(topic.isTwoStudents());
        topic1.setTopicStatus(topic.getTopicStatus());
        topicRepository.save(topic1);
        return true;
    }
    public long getSizeTopicsByCareer(String careerId) {
        return topicRepository.countByCareer_Id(careerId);
    }
    public boolean updateTopicStatus(String id, String topicStatus) {
        Optional<Topic> topicOptional = this.topicRepository.findById(id);
        if (!topicOptional.isPresent())
            return false;
        Topic topic = topicOptional.get();
        topic.setTopicStatus(topicStatus);
        topicRepository.save(topic);
        return true;
    }

    public void setTopicReader(String id, boolean hasReader) {
        Topic topic = this.topicRepository.getOne(id);
        topic.setHasReader(hasReader);
        topicRepository.save(topic);
    }
    public boolean deleteTopicById(String id) {
        Optional<Topic> topicOptional = this.findTopicById(id);
        if (!topicOptional.isPresent())
            return false;
        this.topicRepository.deleteById(id);
        return true;
    }
}
