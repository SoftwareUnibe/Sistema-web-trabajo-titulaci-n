package com.unibe.titulation.dtos;

public class TopicTableDto {
    String id, name, articulation, career;

    public TopicTableDto() {
    }

    public TopicTableDto(String id, String name, String articulation, String career) {
        this.id = id;
        this.name = name;
        this.articulation = articulation;
        this.career = career;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArticulation() {
        return articulation;
    }

    public void setArticulation(String articulation) {
        this.articulation = articulation;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
}
