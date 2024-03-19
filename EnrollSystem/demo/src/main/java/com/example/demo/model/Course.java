package com.example.demo.model;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.util.*;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Course {
    private String name;
    private Integer units;
    private List<courseTimeInfo> courseTime;
    private Integer openSeat;
    private Integer reserveSeat;

    @Version
    private Integer version;

    public Course(CourseDb courseDb) {
        this.name = courseDb.getName();
        this.units = courseDb.getUnits();
        this.openSeat = courseDb.getOpenSeat();
        this.reserveSeat = courseDb.getReserveSeat();
        this.version = courseDb.getVersion();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.courseTime = Arrays.asList(objectMapper.readValue(courseDb.getCourseTime(), courseTimeInfo[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class courseTimeInfo {
        private String day;
        private Time startTime;
        private Time endTime;
    }

}
