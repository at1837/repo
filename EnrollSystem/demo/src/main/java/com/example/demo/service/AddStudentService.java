package com.example.demo.service;


import com.example.demo.model.StudentDb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AddStudentService {
    @Autowired
    private DbService dbService;
    public void addStudent() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String enrolledCourse = objectMapper.writeValueAsString(new HashMap<>());
            String curSchedule = objectMapper.writeValueAsString(new HashMap<>());
            StudentDb studentDb = new StudentDb()
                    .setId(123456789L)
                    .setFirstName("Alex")
                    .setLastName("Wu")
                    .setEnrolledCourse(enrolledCourse)
                    .setSchedule(curSchedule)
                    .setUnits(0)
                    .setTotalUnits(100)
                    .setHasPriority(true);
            dbService.studentInsert(studentDb);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
