package com.example.demo.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Student extends ClientImpl{
    private String firstName;
    private String lastName;
    private Integer totalUnits;
    private Map<String, List<String>> enrolledCourse;
    private boolean hasPriority;

    public Student(StudentDb studentDb) {
        this.setId(studentDb.getId());
        this.firstName = studentDb.getFirstName();
        this.lastName = studentDb.getLastName();
        this.setUnits(studentDb.getUnits());
        this.totalUnits = studentDb.getTotalUnits();
        this.hasPriority = studentDb.isHasPriority();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.enrolledCourse = objectMapper.readValue(studentDb.getEnrolledCourse(), HashMap.class);
            this.setSchedule(objectMapper.readValue(studentDb.getSchedule(), Map.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println();
    }

    public boolean hasPriority() {
        return this.hasPriority;
    }

    public void addCourse(Course course) {
        super.addCourse(course);
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        String key = month + "/" + year;
        if (!this.enrolledCourse.containsKey(key)) {
            this.enrolledCourse.put(key, Arrays.asList(course.getName()));
        } else {
            this.enrolledCourse.get(key).add(course.getName());
        }
    }


}
