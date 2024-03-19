package com.example.demo.Mapper;


import com.example.demo.model.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface dbMapper {
    @Insert("INSERT INTO course (name, units, course_time, open_seat, reserve_seat, version) VALUES (#{name}, #{units}, #{courseTime}, #{openSeat}, #{reserveSeat}, #{version})")
    public Integer courseInsert(CourseDb courseDb);

    @Insert("INSERT INTO student (id, first_name, last_name, units, total_units, enrolled_course, schedule, has_priority) VALUES (#{id}, #{firstName}, #{lastName}, #{units}, #{totalUnits}, #{enrolledCourse}, #{schedule}, #{hasPriority})")
    public Integer studentInsert(StudentDb studentDb);

    @Insert("INSERT INTO enroll (id, course_name, time) VALUES (#{id}, #{courseName}, #{time})")
    public Integer enrollInsert(EnrollDb enrollDb);

    @Insert("INSERT INTO planner (id, units, schedule, course) VALUES (#{id}, #{units}, #{schedule}, #{course})")
    public Integer plannerInsert(PlannerDb plannerDb);

    @Select("SELECT name, units, course_time as courseTime, open_seat as openSeat, reserve_seat as reserveSeat, version FROM course WHERE name = #{name}")
    public CourseDb getCourse(String name);

    @Select("SELECT id, first_name as firstName, last_name as LastName, units, total_units as totalUnits, enrolled_course as enrolledCourse, schedule, has_priority as hasPriority FROM student WHERE id = #{id}")
    public StudentDb getStudent(Long id);

    @Select("SELECT * FROM planner WHERE id = #{id}")
    public PlannerDb getPlanner(Long id);

    @Update("UPDATE student SET units = #{units}, enrolled_course = #{enrolledCourse}, schedule = #{schedule} WHERE id = #{id}")
    public Integer updateStudent(StudentDb studentDb);

    @Update("UPDATE course SET open_seat = #{openSeat}, reserve_seat = #{reserveSeat}, version = version + 1 WHERE name = #{name} AND version = #{version}")
    public Integer updateCourse(CourseDb courseDb);

    @Update("UPDATE planner SET units = #{units}, schedule = #{schedule}, course = #{course} WHERE id = #{id}")
    public Integer updatePlanner(PlannerDb plannerDb);

    @Update("DELETE FROM enroll")
    public void deleteEnroll();

    @Update("DELETE FROM student")
    public void deleteStudent();

    @Update("DELETE FROM course")
    public void deleteCourse();

    @Update("DELETE FROM planner")
    public void deletePlanner();


}
