package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.util.*;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ClientImpl implements Client{
    private Long id;
    private Integer units;
    private Map<String, List<List<Time>>> schedule;

    public ClientImpl() {
        this.id = 0L;
        this.units = 0;
        this.schedule = new HashMap<>();
    }
    @Override
    public void addCourse(Course course) {
        this.units += course.getUnits();
        //java multithreading add time into schedule
        course.getCourseTime().parallelStream().forEach(info -> {
            String day = info.getDay();
            Time courseStart = info.getStartTime();
            Time courseEnd = info.getEndTime();
            List<Time> newTime = Arrays.asList(courseStart, courseEnd);

            synchronized (this.schedule) {
                this.schedule.computeIfAbsent(day, k -> new ArrayList<>()).add(newTime);
            }
        });
    }

}
