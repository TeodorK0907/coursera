package managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InputManager {
    private static final String EMPTY = "";
    private List<String> studentPins;
    private short minCredit;
    private LocalDate startDate;
    private LocalDate endDate;
    private String outputFormat;
    private String dirPath;

    public InputManager() {
        this.studentPins = new ArrayList<>();
        this.outputFormat = EMPTY;
    }

    public List<String> getStudentPins() {
        return studentPins;
    }

    public void setStudentPins(List<String> studentPins) {
        this.studentPins = studentPins;
    }

    public short getMinCredit() {
        return minCredit;
    }

    public void setMinCredit(short minCredit) {
        this.minCredit = minCredit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputManager that)) return false;
        return minCredit == that.minCredit
                && Objects.equals(studentPins, that.studentPins)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(outputFormat, that.outputFormat)
                && Objects.equals(dirPath, that.dirPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentPins, minCredit, startDate, endDate, outputFormat, dirPath);
    }
}
