create schema coursera;


CREATE TABLE coursera.courses
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    instructor_id INT          NOT NULL,
    total_time    SMALLINT     NOT NULL,
    credit        SMALLINT     NOT NULL,
    time_created  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE coursera.instructors
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    time_created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE coursera.students
(
    pin          CHAR(10) PRIMARY KEY,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    time_created TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE coursera.students_courses_xref
(
    student_pin     CHAR(10) NOT NULL,
    course_id       INT      NOT NULL,
    completion_date DATE,
    PRIMARY KEY (student_pin, course_id),
    FOREIGN KEY (student_pin) REFERENCES coursera.students (pin),
    FOREIGN KEY (course_id) REFERENCES coursera.courses (id)
);