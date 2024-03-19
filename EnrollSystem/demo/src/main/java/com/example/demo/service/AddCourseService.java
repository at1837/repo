package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseDb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddCourseService {

    @Autowired
    private DbService dbService;

    private void load(String name, Integer unit, Integer openSeat, Integer reserveSeat, List<Course.courseTimeInfo> courseTime) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(courseTime);
            CourseDb courseDb = new CourseDb()
                    .setName(name)
                    .setUnits(unit)
                    .setOpenSeat(openSeat)
                    .setCourseTime(json)
                    .setReserveSeat(reserveSeat)
                    .setVersion(0);
            dbService.courseInsert(courseDb);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }
    }
    public void addCourse() {
        List<Course.courseTimeInfo> courseTime = new ArrayList<>();
        Course.courseTimeInfo c = new Course.courseTimeInfo()
                .setDay("Mon")
                .setStartTime(Time.valueOf("16:30:00"))
                .setEndTime(Time.valueOf("17:20:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("16:30:00"))
                .setEndTime(Time.valueOf("17:20:00"));
        courseTime.add(c);
        load("CS 12SI", 1, 50, 6, courseTime);


        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("17:30:00"))
                .setEndTime(Time.valueOf("19:20:00"));
        courseTime.add(c);
        load("CS 21SI", 2, 40, 10, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Tue")
                .setStartTime(Time.valueOf("16:30:00"))
                .setEndTime(Time.valueOf("17:50:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Thu")
                .setStartTime(Time.valueOf("16:30:00"))
                .setEndTime(Time.valueOf("17:50:00"));
        courseTime.add(c);
        load("CS 52", 2, 30, 5, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Tue")
                .setStartTime(Time.valueOf("10:30:00"))
                .setEndTime(Time.valueOf("11:50:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Thu")
                .setStartTime(Time.valueOf("10:30:00"))
                .setEndTime(Time.valueOf("11:50:00"));
        courseTime.add(c);
        load("CS 53N", 3, 20, 7, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("15:30:00"))
                .setEndTime(Time.valueOf("17:20:00"));
        courseTime.add(c);
        load("CS 100ACE", 1, 40, 9, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Mon")
                .setStartTime(Time.valueOf("16:30:00"))
                .setEndTime(Time.valueOf("18:20:00"));
        courseTime.add(c);
        load("CS 100BACE", 1, 40, 3, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Mon")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:50:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:50:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Fri")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:50:00"));
        courseTime.add(c);
        load("CS 103", 5, 100, 1, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Tue")
                .setStartTime(Time.valueOf("15:00:00"))
                .setEndTime(Time.valueOf("16:50:00"));
        courseTime.add(c);
        load("CS 103ACE", 1, 40, 5, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Mon")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Fri")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        load("CS 105", 5, 100, 7, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Mon")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        c = new Course.courseTimeInfo()
                .setDay("Fri")
                .setStartTime(Time.valueOf("13:30:00"))
                .setEndTime(Time.valueOf("14:20:00"));
        courseTime.add(c);
        load("CS 106A", 5, 100, 20, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("00:00:00"))
                .setEndTime(Time.valueOf("01:00:00"));
        courseTime.add(c);
        load("Test Class", 18, 10, 10, courseTime);

        //-----------------
        courseTime = new ArrayList<>();
        c = new Course.courseTimeInfo()
                .setDay("Wed")
                .setStartTime(Time.valueOf("00:00:00"))
                .setEndTime(Time.valueOf("01:00:00"));
        courseTime.add(c);
        load("Test Class2", 18, 0, 0, courseTime);

    }
}
