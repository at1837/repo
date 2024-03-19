package com.example.demo.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseDb {
    private String name;
    private Integer units;
    private String courseTime;
    private Integer openSeat;
    private Integer reserveSeat;

    @Version
    private Integer version;

    public CourseDb(Course course) {
        this.name = course.getName();
        this.units = course.getUnits();
        this.openSeat = course.getOpenSeat();
        this.reserveSeat = course.getReserveSeat();
        this.version = course.getVersion();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.courseTime = objectMapper.writeValueAsString(course.getCourseTime());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
