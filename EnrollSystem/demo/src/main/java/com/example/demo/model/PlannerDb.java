package com.example.demo.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PlannerDb {

    private Long id;
    private Integer units;
    private String schedule;
    private String course;
    public PlannerDb(Planner planner) {
        this.id = planner.getId();
        this.units = planner.getUnits();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.schedule = objectMapper.writeValueAsString(planner.getSchedule());
            this.course = objectMapper.writeValueAsString(planner.getCourse());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
