package writers;

import models.CourseReport;
import models.StudentReport;
import writers.contracts.Writer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlWriter implements Writer {

    public void write(String fileDir,
                            List<StudentReport> studentReports,
                            List<CourseReport> courseReports,
                            List<String> studentPins) throws IOException {
        File dir = new File(fileDir);
        dir.mkdir();
        File file = new File(dir, "report.html");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        StringBuilder head = new StringBuilder();
        head.append("<!Doctype html>\n");
        head.append("<html lang='en'>\n");
        head.append("<style>\n");
        head.append("table, th, td {\n");
        head.append(" border:3px solid black;\n");
        head.append("}\n");
        head.append("</style>\n");
        head.append("<head>\n");
        head.append("<meta charset='utf-8'>\n");
        head.append("<title>Coursera HTML report</title>\n");
        head.append("</head>\n\n");
        head.append("<body>\n");
        head.append("<h1>Eligible students</h1>\n");
        bw.write(head.toString());
        populateTableHeaders(bw);
        if (!studentPins.isEmpty()) {
            for (String pin : studentPins) {
                StringBuilder studentContent = new StringBuilder();
                for (StudentReport studentReport : studentReports) {
                    if (pin.equals(studentReport.getPin())) {
                        populateStudentRows(bw, studentReport, studentContent);
                        for (CourseReport courseReport : courseReports) {
                            StringBuilder courseContent = new StringBuilder();
                            if (pin.equals(courseReport.getPin())) {
                                populateCourseRows(courseReport, courseContent);
                            }
                            courseContent.append("</tr>\n");
                            bw.write(courseContent.toString());
                            bw.newLine();
                        }
                    }
                }
                StringBuilder tableCloure = new StringBuilder();
                tableCloure.append("</table>\n");
                tableCloure.append("</body>\n\n");
                tableCloure.append("</html>");
            }
        } else {
            for (StudentReport studentReport : studentReports) {
                StringBuilder studentContent = new StringBuilder();
                populateStudentRows(bw, studentReport, studentContent);
                for (CourseReport courseReport : courseReports) {
                    StringBuilder courseContent = new StringBuilder();
                    if (courseReport.getPin().equals(studentReport.getPin())) {
                        populateCourseRows(courseReport, courseContent);
                    }
                    courseContent.append("</tr>\n");
                    bw.write(courseContent.toString());
                    bw.newLine();
                }
                StringBuilder tableCloure = new StringBuilder();
                tableCloure.append("</table>\n");
                tableCloure.append("</body>\n\n");
                tableCloure.append("</html>");
            }
        }
        bw.flush();
        bw.close();
    }

    private void populateCourseRows(CourseReport courseReport,
                                    StringBuilder courseContent) {
        courseContent.append("<tr>\n");
        courseContent.append("<td>");
        courseContent.append("</td>\n");
        courseContent.append("<td>");
        courseContent.append(courseReport.getCourseName());
        courseContent.append("</td>\n");
        courseContent.append("<td>");
        courseContent.append(courseReport.getTotalTime());
        courseContent.append("</td>\n");
        courseContent.append("<td>");
        courseContent.append(courseReport.getCredit());
        courseContent.append("</td>\n");
        courseContent.append("<td>");
        courseContent.append(courseReport.getInstructorNames());
        courseContent.append("</td>\n");
    }

    private void populateStudentRows(BufferedWriter bw,
                                     StudentReport studentReport,
                                     StringBuilder studentContent) throws IOException {
        studentContent.append("<tr>\n");
        studentContent.append("<td>");
        studentContent.append(studentReport.getStudentNames());
        studentContent.append("</td>\n");
        studentContent.append("<td>");
        studentContent.append(studentReport.getTotalCredits());
        studentContent.append("</td>\n");
        studentContent.append("</tr>\n");
        bw.write(studentContent.toString());
        bw.newLine();
    }

    private void populateTableHeaders(BufferedWriter bw) throws IOException {
        StringBuilder headers = new StringBuilder();
        headers.append("<table>\n");
        headers.append("<tr>\n");
        headers.append("<th>Student</th>\n");
        headers.append("<th>Total Credit</th>\n");
        headers.append("</tr>\n");
        headers.append("<tr>\n");
        headers.append("<th></th>\n");
        headers.append("<th>Course Name</th>\n");
        headers.append("<th>Time</th>\n");
        headers.append("<th>Credit</th>\n");
        headers.append("<th>Instructor</th>\n");
        headers.append("</tr>\n");
        bw.write(headers.toString());
    }
}