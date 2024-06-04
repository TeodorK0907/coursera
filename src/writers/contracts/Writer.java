package writers.contracts;

import models.CourseReport;
import models.StudentReport;

import java.io.IOException;
import java.util.List;

public interface Writer {
    void write(String fileDir,
               List<StudentReport> studentReports,
               List<CourseReport> courseReports,
               List<String> studentPins) throws IOException;
}
