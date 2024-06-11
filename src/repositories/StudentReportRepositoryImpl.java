package repositories;

import helpers.CourseQuery;
import helpers.StudentQuery;
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
    private static final String SEPARATOR = ",";
    private final DbConnectionManager dbManager;
    private InputManager inputManager;

    public StudentReportRepositoryImpl(InputManager inputManager) {
        this.dbManager = new DbConnectionManager();
        this.inputManager = inputManager;
    }

    @Override
    public List<StudentReport> getStudentReport() {
        StringBuilder query = new StringBuilder();
        Map<Integer, String> statementMap = new HashMap<>();
        query.append(StudentQuery.SELECT);
        query.append(StudentQuery.JOIN);
        query.append(StudentQuery.WHERE);
        getStudentPinList(inputManager.getStudentPins(), query, statementMap);
        query.append(StudentQuery.WHERE_ARGS);
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
        query.append(CourseQuery.SELECT);
        query.append(CourseQuery.JOIN);
        query.append(CourseQuery.WHERE_PINS);
        query.append(CourseQuery.INNER_SELECT);
        query.append(StudentQuery.JOIN);
        query.append(CourseQuery.WHERE);
        getStudentPinList(inputManager.getStudentPins(), query, statementMap);
        query.append(CourseQuery.INNER_WHERE_ARGS);
        query.append(CourseQuery.WHERE_ARGS);
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

        query.append(StudentQuery.IN_PREFIX);

        for (int i = 1; i <= studentPins.size(); i++) {
            if (i == studentPins.size()) {
                query.append(StudentQuery.WILDCARD);
                statementMap.put(i, studentPins.get(i - 1));
                break;
            }
            query.append(studentPins.get(i));
            query.append(SEPARATOR);
            statementMap.put(i, studentPins.get(i - 1));
        }

        query.append(StudentQuery.IN_SUFFIX);
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
                    courseData.getString("instructor"),
                    courseData.getString("pin")

            );
            reports.add(report);
        }
        return reports;
    }
}