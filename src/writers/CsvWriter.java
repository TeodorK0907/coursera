package writers;

import models.CourseReport;
import models.StudentReport;
import writers.contracts.Writer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvWriter implements Writer {
    private static final String CSV_SEPARATOR = ",";

    public void write(String fileDir,
                           List<StudentReport> studentReports,
                           List<CourseReport> courseReports,
                           List<String> studentPins) {
        try {
            File dir = new File(fileDir);
            dir.mkdir();
            File file = new File(dir, "report.csv");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            StringBuilder headers = new StringBuilder();
            populateHeaders(headers, bw);
            if (!studentPins.isEmpty()) {
                for (String pin : studentPins) {
                    StringBuilder content = new StringBuilder();
                    for (StudentReport studentReport : studentReports) {
                        if (pin.equals(studentReport.getPin())) {
                            content.append(studentReport.getStudentNames());
                            content.append(CSV_SEPARATOR);
                            content.append(studentReport.getTotalCredits());
                            content.append(CSV_SEPARATOR);
                            bw.write(content.toString());
                            bw.newLine();
                        }
                        for (CourseReport courseReport : courseReports) {
                            if (pin.equals(courseReport.getPin())) {
                                String courseContent = "" +
                                        CSV_SEPARATOR +
                                        courseReport.getCourseName() +
                                        CSV_SEPARATOR +
                                        courseReport.getTotalTime() +
                                        CSV_SEPARATOR +
                                        courseReport.getCredit() +
                                        CSV_SEPARATOR +
                                        courseReport.getInstructorNames();
                                bw.write(courseContent);
                                bw.newLine();
                            }
                        }
                    }
                }
            } else {
                for (StudentReport studentReport : studentReports) {
                    StringBuilder content = new StringBuilder();
                    content.append(studentReport.getStudentNames());
                    content.append(CSV_SEPARATOR);
                    content.append(studentReport.getTotalCredits());
                    content.append(CSV_SEPARATOR);
                    bw.write(content.toString());
                    bw.newLine();
                    for (CourseReport courseReport : courseReports) {
                        if (courseReport.getPin().equals(studentReport.getPin())) {
                            String courseContent = "" +
                                    CSV_SEPARATOR +
                                    courseReport.getCourseName() +
                                    CSV_SEPARATOR +
                                    courseReport.getTotalTime() +
                                    CSV_SEPARATOR +
                                    courseReport.getCredit() +
                                    CSV_SEPARATOR +
                                    courseReport.getInstructorNames();
                            bw.write(courseContent);
                            bw.newLine();
                        }
                    }
                }
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private static void populateHeaders(StringBuilder csv, BufferedWriter bw) throws IOException {
        csv.append("Student Name");
        csv.append(CSV_SEPARATOR);
        csv.append("Total Credit");
        csv.append(CSV_SEPARATOR);
        csv.append(System.lineSeparator());
        csv.append("");
        csv.append(CSV_SEPARATOR);
        csv.append("Course Name");
        csv.append(CSV_SEPARATOR);
        csv.append("Total Time");
        csv.append(CSV_SEPARATOR);
        csv.append("Credit");
        csv.append(CSV_SEPARATOR);
        csv.append("Instructor Name");
        csv.append(CSV_SEPARATOR);
        bw.write(csv.toString());
        bw.newLine();
    }
}
