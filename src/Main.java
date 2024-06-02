import managers.InputManager;
import models.CourseReport;
import models.StudentReport;
import writers.CsvWriter;
import writers.HtmlWriter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        InputManager inputManager = new InputManager();

        while (!input.equalsIgnoreCase("end")) {

            int separatorIndex = input.indexOf(":");
            String command = input.substring(0, separatorIndex);
            switch (command) {
                case "studentPins":
                    inputManager.setStudentPins(
                            Arrays.asList
                                    (input.substring(separatorIndex + 1).split(","))
                    );
                    break;
                case "minimumCredits":
                    inputManager.setMinCredit(Short.parseShort(input.substring(separatorIndex + 1)));
                    break;
                case "startDate":
                    inputManager.setStartDate(LocalDate.parse(input.substring(separatorIndex + 1)));
                    break;
                case "endDate":
                    inputManager.setEndDate(LocalDate.parse(input.substring(separatorIndex + 1)));
                    break;
                case "outputFormat":
                    inputManager.setOutputFormat(input.substring(separatorIndex + 1));
                    break;
                case "saveTo":
                    inputManager.setDirPath(input.substring(separatorIndex + 1));
                    break;
                default:
                    System.out.println("please provide a valid input from the below list once" +
                            " or type \"end\" to restart the program.");
            }
            if (isInputManagerFull(inputManager)) {
               break;
            }
            input = scanner.nextLine();
        }
        if (input.equalsIgnoreCase("end")) {
            return;
        }
        ReportRepository repo = new ReportRepository(inputManager);
        List<StudentReport> studentReports = repo.getStudentReport();
        List<CourseReport> courseReports = repo.getCourseCreditReport();
//        List<OutputReport> outputReport = populateStudentReportCourses(
//                inputManager.getStudentPins(),
//                studentReports,
//                courseReports);
        CsvWriter csvWriter = new CsvWriter();
        HtmlWriter htmlWriter = new HtmlWriter();
        switch (inputManager.getOutputFormat()) {
            case "csv":
                csvWriter.writeToCSV(inputManager.getDirPath(),
                        studentReports, courseReports, inputManager.getStudentPins());
                break;
            case "html":
              //  htmlWriter.render(inputManager.getDirPath(), studentReports, courseReports);
                break;
            default:
                csvWriter.writeToCSV(inputManager.getDirPath(),
                        studentReports, courseReports, inputManager.getStudentPins());
              //  htmlWriter.render(inputManager.getDirPath(), studentReports, courseReports);
        }
    }

    private static boolean isInputManagerFull(InputManager inputManager) {
        return (inputManager.getMinCredit() != 0)
                && (inputManager.getStartDate() != null)
                && (inputManager.getEndDate() != null)
                && (inputManager.getDirPath() != null);
    }

//    private static List<OutputReport> populateStudentReportCourses(List<String> studentPins,
//                                                                   List<StudentReport> studentReports,
//                                                                   List<CourseReport> courseReports) {
//        List<OutputReport> outputReports = new ArrayList<>();
//        if (!studentPins.isEmpty()) {
//            for (String pin : studentPins) {
//                OutputReport outputReport = new OutputReport();
//                for (StudentReport studentReport : studentReports) {
//                    if (pin.equals(studentReport.getPin())) {
//                        outputReport.setStudentNames(studentReport.getStudentNames());
//                        outputReport.setTotalCredit(studentReport.getTotalCredits());
//                    }
//                    for (CourseReport courseReport : courseReports) {
//                        if (pin.equals(courseReport.getPin())) {
//                            outputReport.getCouseReports().add(courseReport);
//                        }
//                    }
//                    outputReports.add(outputReport);
//                }
//            }
//        } else {
//            for (StudentReport studentReport : studentReports) {
//                OutputReport outputReport = new OutputReport();
//                outputReport.setStudentNames(studentReport.getStudentNames());
//                outputReport.setTotalCredit(studentReport.getTotalCredits());
//                for (CourseReport courseReport : courseReports) {
//                    if (courseReport.getPin().equals(studentReport.getPin()))
//                        outputReport.getCouseReports().add(courseReport);
//                }
//                outputReports.add(outputReport);
//            }
//        }
//        return outputReports;
//    }
}