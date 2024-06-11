import managers.InputManager;
import models.CourseReport;
import models.StudentReport;
import repositories.contracts.ReportRepository;
import repositories.StudentReportRepositoryImpl;
import writers.CsvWriter;
import writers.HtmlWriter;
import writers.contracts.Writer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String INVALID_COMMAND = "Please provide a valid input from the below list:" +
            "studentPins:" +
            "minimumCredits:" +
            "startDate:" +
            "endDate:" +
            "outputFormat:" +
            "saveTo:" +
            " once or type \"end\" to restart the program.";
    private static final String STUDENT_PINS = "studentPins";
    private static final String MINIMUM_CREDITS = "minimumCredits";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OUTPUT_FORMAT = "outputFormat";
    private static final String SAVE_TO = "saveTo";
    private static final String END = "end";
    private static final String CSV = "csv";
    private static final String HTML = "html";
    private static final int ZERO = 0;
    private static final String ELEMENT_SEPARATOR = ",";
    private static final String COMMAND_SEPARATOR = ":";
    private static final String NO_RESULTS = "No results were found based on your search criteria. Please try again.";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        InputManager inputManager = new InputManager();

        while (!input.equalsIgnoreCase(END)) {

            int separatorIndex = input.indexOf(COMMAND_SEPARATOR);
            String command = input.substring(0, separatorIndex);
            switch (command) {
                case STUDENT_PINS:
                    inputManager.setStudentPins(
                            Arrays.asList
                                    (input.substring(separatorIndex + 1).split(ELEMENT_SEPARATOR))
                    );
                    break;
                case MINIMUM_CREDITS:
                    inputManager.setMinCredit(Short.parseShort(input.substring(separatorIndex + 1)));
                    break;
                case START_DATE:
                    inputManager.setStartDate(LocalDate.parse(input.substring(separatorIndex + 1)));
                    break;
                case END_DATE:
                    inputManager.setEndDate(LocalDate.parse(input.substring(separatorIndex + 1)));
                    break;
                case OUTPUT_FORMAT:
                    inputManager.setOutputFormat(input.substring(separatorIndex + 1));
                    break;
                case SAVE_TO:
                    inputManager.setDirPath(input.substring(separatorIndex + 1));
                    break;
                default:
                    System.out.println(INVALID_COMMAND);
            }
            if (isInputManagerFull(inputManager)) {
                break;
            }
            input = scanner.nextLine();
        }
        if (input.equalsIgnoreCase(END)) {
            return;
        }
        ReportRepository repo = new StudentReportRepositoryImpl(inputManager);
        List<StudentReport> studentReports = repo.getStudentReport();
        List<CourseReport> courseReports = repo.getCourseCreditReport();

        if (studentReports.isEmpty() || courseReports.isEmpty()) {
            System.out.println(NO_RESULTS);
            return;
        }

        Writer writer;
        switch (inputManager.getOutputFormat()) {
            case CSV:
                writer = new CsvWriter();
                writer.write(inputManager.getDirPath(), studentReports, courseReports);
                break;
            case HTML:
                writer = new HtmlWriter();
                writer.write(inputManager.getDirPath(), studentReports, courseReports);
                break;
            default:
                writer = new CsvWriter();
                writer.write(inputManager.getDirPath(), studentReports, courseReports);
                writer = new HtmlWriter();
                writer.write(inputManager.getDirPath(), studentReports, courseReports);
        }
    }

    private static boolean isInputManagerFull(InputManager inputManager) {
        return (inputManager.getMinCredit() != ZERO)
                && (inputManager.getStartDate() != null)
                && (inputManager.getEndDate() != null)
                && (inputManager.getDirPath() != null);
    }
}