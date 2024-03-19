package com.example.demo.service;

import com.example.demo.Mapper.dbMapper;
import com.example.demo.model.CourseDb;
import com.example.demo.model.EnrollDb;
import com.example.demo.model.PlannerDb;
import com.example.demo.model.StudentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {
    @Autowired
    private dbMapper dbMapper;

    public Integer courseInsert(CourseDb courseDb) {
        return dbMapper.courseInsert(courseDb);
    }

    public Integer studentInsert(StudentDb studentDb) {
        return dbMapper.studentInsert(studentDb);
    }

    public Integer enrollInsert(EnrollDb enrollDb) {
        return dbMapper.enrollInsert(enrollDb);
    }

    public Integer plannerInsert(PlannerDb plannerDb) {
        return dbMapper.plannerInsert(plannerDb);
    }

    public CourseDb getCourse(String name) {
        return dbMapper.getCourse(name);
    }

    public StudentDb getStudent(Long id) {
        return dbMapper.getStudent(id);
    }

    public PlannerDb getPlanner(Long id) {
        return dbMapper.getPlanner(id);
    }


    public Integer updateStudent(StudentDb studentDb) {
        return dbMapper.updateStudent(studentDb);
    }

    public Integer updateCourse(CourseDb courseDb) {
        return dbMapper.updateCourse(courseDb);
    }

    public Integer updatePlanner(PlannerDb plannerDb) {
        return dbMapper.updatePlanner(plannerDb);
    }
    public void delete() {
        dbMapper.deleteStudent();
        dbMapper.deleteEnroll();
        dbMapper.deleteCourse();
        dbMapper.deletePlanner();
    }
}
