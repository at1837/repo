package com.example.demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.util.*;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Planner extends ClientImpl {
    private List<String> course;
    public Planner(Long id) {
        this.setId(id);
        this.course = new ArrayList<>();

    }
    public Planner(PlannerDb plannerDb) {
        this.setId(plannerDb.getId());
        this.setUnits(plannerDb.getUnits());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.setSchedule(objectMapper.readValue(plannerDb.getSchedule(), HashMap.class));
            this.course = objectMapper.readValue(plannerDb.getCourse(), List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCourse(Course course) {
        super.addCourse(course);
        this.course.add(course.getName());
    }
}
