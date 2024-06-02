package models;

import java.util.Objects;

public class StudentReport {
    private String pin;
    private String studentNames;
    private short totalCredits;

    public StudentReport(short totalCredits,
                         String pin,
                         String studentNames) {
        this.totalCredits = totalCredits;
        this.pin = pin;
        this.studentNames = studentNames;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(String studentNames) {
        this.studentNames = studentNames;
    }

    public short getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(short totalCredits) {
        this.totalCredits = totalCredits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentReport that)) return false;
        return totalCredits == that.totalCredits
                && Objects.equals(pin, that.pin)
                && Objects.equals(studentNames, that.studentNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pin, studentNames, totalCredits);
    }
}
