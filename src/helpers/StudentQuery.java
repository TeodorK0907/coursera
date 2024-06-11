package helpers;

public enum StudentQuery {
    SELECT("select sum(coursera.courses.credit) as totalCredits, " +
            "coursera.students.pin as pins, " +
            "concat(coursera.students.first_name, ' ', coursera.students.last_name) as fullName " +
            "from coursera.students "),
    JOIN("join coursera.students_courses_xref on coursera.students_courses_xref.student_pin = coursera.students.pin " +
            "         join coursera.courses on students_courses_xref.course_id = courses.id "),
    WHERE("where "),
    WHERE_ARGS("coursera.students_courses_xref.completion_date >= ? " +
            "  and coursera.students_courses_xref.completion_date <= ? " +
            "and coursera.courses.credit >= ? " +
            "group by coursera.students.pin"),
    IN_PREFIX("coursera.students.pin in ("),
    IN_SUFFIX(") and "),
    WILDCARD("?");

    private final String description;

    StudentQuery(String description) {
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
