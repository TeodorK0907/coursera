package repositories.contracts;

import models.CourseReport;
import models.StudentReport;

import java.util.List;

public interface ReportRepository {

    List<StudentReport> getStudentReport();

    List<CourseReport> getCourseCreditReport();
}
