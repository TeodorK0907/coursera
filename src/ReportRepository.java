import managers.DbConnectionManager;
import managers.InputManager;
import models.CourseReport;
import models.StudentReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {
    private final DbConnectionManager dbManager;
    private InputManager inputManager;

    public ReportRepository(InputManager inputManager) {
        this.dbManager = new DbConnectionManager();
        this.inputManager = inputManager;
    }

    public List<StudentReport> getStudentReport() {
        try (
                Connection connection = dbManager.connectDB();
                PreparedStatement statement = connection.prepareStatement("select sum(coursera.courses.credit) as totalCredits, " +
                        "       coursera.students.pin as pins, " +
                        "       concat(coursera.students.first_name, ' ', coursera.students.last_name) as fullName " +
                        "from coursera.students " +
                        "         join coursera.students_courses_xref on coursera.students_courses_xref.student_pin = coursera.students.pin " +
                        "         join coursera.courses on students_courses_xref.course_id = courses.id " +
                        "where coursera.students_courses_xref.completion_date >= ? " +
                        "  and coursera.students_courses_xref.completion_date <= ? " +
                        "and coursera.courses.credit >= ? " +
                        "group by coursera.students.pin")
        ) {
            statement.setDate(1, Date.valueOf(inputManager.getStartDate()));
            statement.setDate(2, Date.valueOf(inputManager.getEndDate()));
            statement.setShort(3, inputManager.getMinCredit());

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                List<StudentReport> reports = getStudentReports(resultSet);
//                if (reports.size() == 0) {
//                    sout
//                }
                return reports;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CourseReport> getCourseCreditReport() {
        try (
                Connection connection = dbManager.connectDB();
                PreparedStatement statement = connection.prepareStatement("select coursera.courses.name as courseName," +
                        "       coursera.courses.total_time as totalTime, " +
                        "       coursera.courses.credit as credit, " +
                        "       students.pin, " +
                        "       concat(coursera.instructors.first_name, ' ', coursera.instructors.last_name) as instructor " +
                        "from coursera.courses " +
                        "         join coursera.students_courses_xref as studentCourses on studentCourses.course_id = coursera.courses.id " +
                        "         join coursera.students as students on studentCourses.student_pin = students.pin " +
                        "         join coursera.instructors on coursera.instructors.id = coursera.courses.instructor_id " +
                        "where students.pin in (select coursera.students.pin " +
                        "                       from coursera.students " +
                        "                                join coursera.students_courses_xref " +
                        "                                     on coursera.students_courses_xref.student_pin = coursera.students.pin " +
                        "                                join coursera.courses on students_courses_xref.course_id = courses.id " +
                        "                       where completion_date >= ? " +
                        "                         and completion_date <= ? " +
                        "group by coursera.students.pin)")
        ) {
            statement.setDate(1, Date.valueOf(inputManager.getStartDate()));
            statement.setDate(2, Date.valueOf(inputManager.getEndDate()));

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                List<CourseReport> reports = getCourseReports(resultSet);
//                if (reports.size() == 0) {
//                    sout
//                }
                return reports;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<StudentReport> getStudentReports(ResultSet studentData) throws SQLException {
        List<StudentReport> reports = new ArrayList<>();
        while (studentData.next()) {
            StudentReport report = new StudentReport(
                    studentData.getShort("totalCredits"),
                    studentData.getString("pins"),
                    studentData.getString("fullName")
            );
            reports.add(report);
        }
        return reports;
    }

    private List<CourseReport> getCourseReports(ResultSet courseData) throws SQLException {
        List<CourseReport> reports = new ArrayList<>();
        while (courseData.next()) {
            CourseReport report = new CourseReport(
                    courseData.getString("courseName"),
                    courseData.getShort("totalTime"),
                    courseData.getShort("credit"),
                    courseData.getString("instructor"),
                    courseData.getString("pin")
            );
            reports.add(report);
        }
        return reports;
    }
}