package helpers;

public enum CourseQuery {
    SELECT("select coursera.courses.name as courseName, " +
            "       coursera.courses.total_time as totalTime, " +
            "       coursera.courses.credit as credit, " +
            "students.pin, " +
            "       concat(coursera.instructors.first_name, ' ', coursera.instructors.last_name) as instructor " +
            "from coursera.courses "),
    JOIN("join coursera.students_courses_xref as studentCourses on studentCourses.course_id = coursera.courses.id " +
            "join coursera.students as students on studentCourses.student_pin = students.pin " +
            "join coursera.instructors on coursera.instructors.id = coursera.courses.instructor_id "),
    WHERE("where "),
    WHERE_PINS("where students.pin in "),
    WHERE_ARGS("and completion_date >= ? " +
            "and completion_date <= ?"),
    INNER_SELECT("(select coursera.students.pin " +
            "from coursera.students "),
    INNER_WHERE_ARGS("completion_date >= ? " +
            "and completion_date <= ? " +
            "group by coursera.students.pin) "),
    WILDCARD("?");

    private final String description;

    CourseQuery(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
