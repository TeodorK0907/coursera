package models;

import java.util.Objects;

public class CourseReport {
    private String courseName;
    private short totalTime;
    private short credit;
    private String instructorNames;
    private String pin;

    public CourseReport(String courseName,
                        short totalTime,
                        short credit,
                        String instructorNames,
                        String pin) {
        this.courseName = courseName;
        this.totalTime = totalTime;
        this.credit = credit;
        this.instructorNames = instructorNames;
        this.pin = pin;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public short getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(short totalTime) {
        this.totalTime = totalTime;
    }

    public short getCredit() {
        return credit;
    }

    public void setCredit(short credit) {
        this.credit = credit;
    }

    public String getInstructorNames() {
        return instructorNames;
    }

    public void setInstructorNames(String instructorNames) {
        this.instructorNames = instructorNames;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseReport that)) return false;
        return totalTime == that.totalTime
                && credit == that.credit
                && Objects.equals(courseName, that.courseName)
                && Objects.equals(instructorNames, that.instructorNames)
                && Objects.equals(pin, that.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, totalTime, credit,
                instructorNames, pin);
    }
}
