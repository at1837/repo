package com.example.demo.controller;


import com.example.demo.model.Course;
import com.example.demo.service.AddCourseService;
import com.example.demo.service.AddStudentService;
import com.example.demo.service.DbService;
import com.example.demo.service.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class EnrollController {

    @Autowired
    private AddCourseService addCourseService;

    @Autowired
    private AddStudentService addStudentService;

    @Autowired
    private EnrollService enrollService;

    @Autowired
    private DbService dbService;

    @PostMapping("/enroll")
    public Integer enroll(@RequestParam Long id, @RequestParam String course){
        return enrollService.enroll(id, course);
    }

    @GetMapping("/hasMinUnits")
    public boolean hasMinUnits(@RequestParam Long id){
        return enrollService.hasMinUnits(id);
    }

    @GetMapping("/planner")
    public List<String> planner(@RequestParam Long id, @RequestParam String course) {
        return enrollService.planner(id, course);
    }

    @GetMapping("/batchEnroll")
    public List<String> batchEnroll(@RequestParam Long id, @RequestParam List<String> addList, @RequestParam Queue<String> backup) {
        return enrollService.batchEnroll(id, addList, backup);
    }

    @GetMapping("/create")
    public void createDB() {
        addCourseService.addCourse();
        addStudentService.addStudent();
    }

    @GetMapping("/delete")
    public void cleanDB() {
        dbService.delete();
    }

    @GetMapping("/test1")
    public Integer testAddNoConflict() {
        cleanDB();
        createDB();
        Integer sum = 0;
        sum += enroll(123456789L, "CS 100ACE");
        sum += enroll(123456789L, "CS 105");
        sum += enroll(123456789L, "CS 21SI");
        sum += enroll(123456789L, "CS 53N");
        return sum;
    }

    @GetMapping("/test2")
    public Integer testTimeConflict() {
        cleanDB();
        createDB();
        enroll(123456789L, "CS 100ACE");
        enroll(123456789L, "CS 105");
        return enroll(123456789L, "CS 106A");
    }

    @GetMapping("/test3")
    public Integer testUnitCap() {
        cleanDB();
        createDB();
        enroll(123456789L, "Test Class");
        return enroll(123456789L, "CS 105");
    }

    @GetMapping("/test4")
    public Integer testNoOpenSeat() {
        cleanDB();
        createDB();
        return enroll(123456789L, "Test Class2");
    }

    @GetMapping("/test5")
    public List<String> testPlanner() {
        cleanDB();
        createDB();
        planner(123456789L, "CS 100ACE");
        planner(123456789L, "CS 105");
        planner(123456789L, "CS 21SI");
        return planner(123456789L, "CS 53N");
    }

    @GetMapping("/test6")
    public List<String> testPlannerTimeConflict() {
        cleanDB();
        createDB();
        planner(123456789L, "CS 100ACE");
        planner(123456789L, "CS 105");
        return planner(123456789L, "CS 106A");
    }

    @GetMapping("/test7")
    public List<String> testPlannerUnitCap() {
        cleanDB();
        createDB();
        planner(123456789L, "Test Class");
        return planner(123456789L, "CS 105");
    }

    @GetMapping("/test8")
    public List<String> testBatchEnroll() {
        cleanDB();
        createDB();
        List<String> addList = Arrays.asList("CS 100ACE", "CS 105", "CS 21SI", "CS 53N");
        Queue<String> backup = new LinkedList<>();
        for (String s : Arrays.asList("CS 100BACE", "CS 103", "CS 103ACE", "CS 106A", "CS 12SI", "CS 52")) {
            backup.offer(s);
        }
        return batchEnroll(123456789L, addList, backup);
    }

    @GetMapping("/test9")
    public List<String> testBatchEnrollUsingBackup() {
        cleanDB();
        createDB();
        List<String> addList = Arrays.asList("CS 100ACE", "CS 105", "CS 106A");
        Queue<String> backup = new LinkedList<>();
        for (String s : Arrays.asList("CS 100BACE", "CS 103", "CS 103ACE", "CS 106A", "CS 12SI", "CS 21SI", "CS 52","CS 53N")) {
            backup.offer(s);
        }
        return batchEnroll(123456789L, addList, backup);
    }

    @GetMapping("/test10")
    public boolean testRequireGraduate() {
        return requireGraduate(123456789L, "CS 103");
    }
}
