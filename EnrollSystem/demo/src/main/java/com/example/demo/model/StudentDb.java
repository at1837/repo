package com.example.demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StudentDb {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer units;
    private Integer totalUnits;
    private String enrolledCourse;
    private String schedule;
    private boolean hasPriority;

    public StudentDb(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.units = student.getUnits();
        this.totalUnits = student.getTotalUnits();
        this.hasPriority = student.isHasPriority();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.enrolledCourse = objectMapper.writeValueAsString(student.getEnrolledCourse());
            this.schedule = objectMapper.writeValueAsString(student.getSchedule());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
