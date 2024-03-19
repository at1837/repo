package com.example.demo.service;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EnrollService {

    @Autowired
    private DbService dbService;

    private boolean hasOpenSeat(Course course, Student student) {
        if (student.hasPriority()) {
            if (course.getReserveSeat() > 0) {
                return true;
            }
        }
        return course.getOpenSeat() > 0;
    }

    private boolean courseExist(CourseDb courseDb) {
        return courseDb != null;
    }
    private boolean studentExist(StudentDb studentDb) {
        return studentDb != null;
    }

    private boolean withinCapUnits(Course course, ClientImpl client) {
        return client.getUnits() + course.getUnits() <= 20;
    }

    private boolean hasTimeConflict(Course course, ClientImpl client) {
        //only check conflict when there are class enrolled
        if (client.getUnits() > 0) {
            Map<String, List<List<Time>>> curSchedule = client.getSchedule();
            for (Course.courseTimeInfo info : course.getCourseTime()) {
                String day = info.getDay();
                Time courseStart = info.getStartTime();
                if (curSchedule.containsKey(day)) {
                    for (List<Time> t : curSchedule.get(day)) {
                        Time start = Time.valueOf(String.valueOf(t.get(0)));
                        Time end = Time.valueOf(String.valueOf(t.get(1)));
                        // if course start time within any enrolled class time
                        if (start.compareTo(courseStart) <= 0 && courseStart.before(end)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    //ObjectOptimisticLockingFailureException need to config JPA, i just comment out for now. It can prevent over enroll right now.
    //but someone dont get enroll at the moment need to manually click the enroll again.
    @Transactional
    public Integer updateDatabase(Student student, Course course, EnrollDb enrollDb, Long id, String courseName) {
        int count = 0;
//        try {
            count += dbService.enrollInsert(enrollDb);
            count += dbService.updateStudent(new StudentDb(student));
            count += dbService.updateCourse(new CourseDb(course));
//        } catch (ObjectOptimisticLockingFailureException e) {
//            enroll(id, courseName);
//        }
        return count;
    }

    public Integer enroll(Long id, String courseName) {
        CourseDb courseDb = dbService.getCourse(courseName);
        StudentDb studentDb = dbService.getStudent(id);

        if (!courseExist(courseDb)) {
            throw new RuntimeException("course does not exist.");
        }
        if (!studentExist(studentDb)) {
            throw new RuntimeException("student id cant find.");
        }

        Course course = new Course(courseDb);
        Student student = new Student(studentDb);


        if (!withinCapUnits(course, student)) {
            throw new RuntimeException("Cant add more than 20 units.");
        }
        if (hasTimeConflict(course, student)) {
            throw new RuntimeException("has time conflict.");
        }

        if (!hasOpenSeat(course, student)) {
            throw new RuntimeException("do not have enough seat.");
        }

        student.addCourse(course);
        EnrollDb enrollDb = new EnrollDb()
                .setId(student.getId())
                .setTime(System.currentTimeMillis())
                .setCourseName(courseName);
        if (student.hasPriority()) {
            course.setReserveSeat(course.getReserveSeat() - 1);
        } else {
            course.setOpenSeat(course.getOpenSeat() - 1);
        }
        return updateDatabase(student, course, enrollDb, id, courseName);
    }

    public boolean hasMinUnits(Long id) {
        StudentDb studentDb = dbService.getStudent(id);
        if (!studentExist(studentDb)) {
            throw new RuntimeException("student id cant find.");
        }
        Student student = new Student(studentDb);
        return student.getUnits() >= 12;
    }

    public List<String> planner(Long id, String courseName) {
        StudentDb studentDb = dbService.getStudent(id);
        CourseDb courseDb = dbService.getCourse(courseName);
        if (!courseExist(courseDb)) {
            throw new RuntimeException("course does not exist.");
        }
        if (!studentExist(studentDb)) {
            throw new RuntimeException("student id cant find.");
        }

        Course course = new Course(courseDb);
        PlannerDb plannerDb = dbService.getPlanner(id);
        Planner planner = null;
        if (plannerDb != null) {
            planner = new Planner(plannerDb);
            if (!withinCapUnits(course, planner)) {
                throw new RuntimeException("Cant add more than 20 units.");
            }
            if (hasTimeConflict(course, planner)) {
                throw new RuntimeException("has time conflict.");
            }
            planner.addCourse(course);
            dbService.updatePlanner(new PlannerDb(planner));
        } else {
            planner = new Planner(studentDb.getId());
            planner.addCourse(course);
            dbService.plannerInsert(new PlannerDb(planner));
        }
        return planner.getCourse();
    }

    public List<String> batchEnroll(Long id, List<String> addList, Queue<String> backup) {
        boolean needBackup = false;
        for (String s : addList) {
            try {
                enroll(id, s);
            } catch (Exception e) {
                needBackup = true;
            }
        }

        List<String> replaceClass = new ArrayList<>();
        if (needBackup) {
            while (!backup.isEmpty()) {
                String cur = backup.poll();
                if (!addList.contains(cur)) {
                    try {
                        enroll(id, cur);
                        replaceClass.add(cur);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return replaceClass;
    }
}
