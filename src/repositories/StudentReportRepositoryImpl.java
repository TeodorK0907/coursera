package repositories;

import managers.DbConnectionManager;
import managers.InputManager;
import models.CourseReport;
import models.StudentReport;
import repositories.contracts.StudentCourseRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentReportRepositoryImpl implements StudentCourseRepository {
    private static final String STUDENT_COURSE_TOTAL_SELECT = "select sum(coursera.courses.credit) as totalCredits, " +
            "coursera.students.pin as pins, " +
            "concat(coursera.students.first_name, ' ', coursera.students.last_name) as fullName " +
            "from coursera.students ";
    private static final String STUDENT_COURSE_TOTAL_JOIN = "join coursera.students_courses_xref on coursera.students_courses_xref.student_pin = coursera.students.pin " +
            "         join coursera.courses on students_courses_xref.course_id = courses.id ";
    private static final String SEPARATOR = ",";
    private static final String STUDENT_COURSE_TOTAL_WHERE = "where ";
    private static final String STUDENT_COURSE_TOTAL_WHERE_ARGS = "coursera.students_courses_xref.completion_date >= ? " +
            "  and coursera.students_courses_xref.completion_date <= ? " +
            "and coursera.courses.credit >= ? " +
            "group by coursera.students.pin";
    private static final String WILDCARD = "?";
    private final DbConnectionManager dbManager;
    private InputManager inputManager;
    private static final String STUDENT_COURSE_CREDIT_SELECT =
            "select coursera.courses.name as courseName, " +
                    "       coursera.courses.total_time as totalTime, " +
                    "       coursera.courses.credit as credit, " +
                    "students.pin, " +
                    "       concat(coursera.instructors.first_name, ' ', coursera.instructors.last_name) as instructor " +
                    "from coursera.courses ";
    private static final String STUDENT_COURSE_CREDIT_JOIN =
            "join coursera.students_courses_xref as studentCourses on studentCourses.course_id = coursera.courses.id " +
                    "join coursera.students as students on studentCourses.student_pin = students.pin " +
                    "join coursera.instructors on coursera.instructors.id = coursera.courses.instructor_id ";
    public static final String STUDENT_COURSE_CREDIT_WHERE = "where students.pin in ";
    public static final String STUDENT_COURSE_CREDIT_INNER_SELECT =
            "(select coursera.students.pin " +
            "from coursera.students ";
    private static final String STUDENT_COURSE_CREDIT_INNER_WHERE_ARGS =
            "completion_date >= ? " +
            "and completion_date <= ? " +
                    "group by coursera.students.pin)";
    private static final String STUDENT_COURSE_CREDIT_WHERE_ARGS =
            "and coursera.courses.credit >= ? " +
                    "group by coursera.students.pin";


    public StudentReportRepositoryImpl(InputManager inputManager) {
        this.dbManager = new DbConnectionManager();
        this.inputManager = inputManager;
    }

    @Override
    public List<StudentReport> getStudentReport() {
        StringBuilder query = new StringBuilder();
        Map<Integer, String> statementMap = new HashMap<>();
        query.append(STUDENT_COURSE_TOTAL_SELECT);
        query.append(STUDENT_COURSE_TOTAL_JOIN);
        query.append(STUDENT_COURSE_TOTAL_WHERE);
        getStudentPinList(inputManager.getStudentPins(), query, statementMap);
        query.append(STUDENT_COURSE_TOTAL_WHERE_ARGS);
        try (
                Connection connection = dbManager.connectDB();
                PreparedStatement statement = connection.prepareStatement(query.toString())
        ) {
            if (!statementMap.isEmpty()) {
                setStudentPins(statement, inputManager.getStudentPins());
            }
            statement.setDate(statementMap.size() + 1, Date.valueOf(inputManager.getStartDate()));
            statement.setDate(statementMap.size() + 2, Date.valueOf(inputManager.getEndDate()));
            statement.setShort(statementMap.size() + 3, inputManager.getMinCredit());

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                return getStudentReports(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CourseReport> getCourseCreditReport() {
        StringBuilder query = new StringBuilder();
        Map<Integer, String> statementMap = new HashMap<>();
        query.append(STUDENT_COURSE_CREDIT_SELECT);
        query.append(STUDENT_COURSE_CREDIT_JOIN);
        query.append(STUDENT_COURSE_CREDIT_WHERE);
        query.append(STUDENT_COURSE_CREDIT_INNER_SELECT);
        query.append(STUDENT_COURSE_TOTAL_JOIN);
        getStudentPinList(inputManager.getStudentPins(), query, statementMap);
        query.append(STUDENT_COURSE_CREDIT_INNER_WHERE_ARGS);
        query.append(STUDENT_COURSE_CREDIT_WHERE_ARGS);
        try (
                Connection connection = dbManager.connectDB();
                PreparedStatement statement = connection.prepareStatement(query.toString())
        ) {

            if (!statementMap.isEmpty()) {
                setStudentPins(statement, inputManager.getStudentPins());
            }
            statement.setDate(statementMap.size() + 1, Date.valueOf(inputManager.getStartDate()));
            statement.setDate(statementMap.size() + 2, Date.valueOf(inputManager.getEndDate()));
            statement.setDate(statementMap.size() + 3, Date.valueOf(inputManager.getStartDate()));
            statement.setDate(statementMap.size() + 4, Date.valueOf(inputManager.getEndDate()));

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                return getCourseReports(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getStudentPinList(List<String> studentPins,
                                  StringBuilder query,
                                  Map<Integer, String> statementMap) {
        if (studentPins.isEmpty()) {
            return;
        }

        query.append("coursera.students.pin in (");

        for (int i = 1; i <= studentPins.size(); i++) {
            if (i == studentPins.size()) {
                query.append(WILDCARD);
                statementMap.put(i, studentPins.get(i - 1));
                break;
            }
            query.append(studentPins.get(i));
            query.append(SEPARATOR);
            statementMap.put(i, studentPins.get(i - 1));
        }

        query.append(") and ");
    }

    private void setStudentPins(PreparedStatement statement,
                                List<String> studentPins) throws SQLException {
        for (int i = 1; i <= studentPins.size(); i++) {
            statement.setString(i, studentPins.get(i - 1));
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
                    courseData.getString("pin"),
                    courseData.getString("instructor")

            );
            reports.add(report);
        }
        return reports;
    }
}