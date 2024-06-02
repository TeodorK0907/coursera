INSERT INTO coursera.courses (id, name, instructor_id, total_time, credit, time_created)
VALUES (1, 'Analysis', 1, 20, 10, '2020-03-16 13:26:44.283'::timestamp),
       (2, 'Linear Algebra', 1, 30, 15, '2020-03-16 13:27:26.300'::timestamp),
       (3, 'Statistics', 2, 30, 15, '2020-03-16 13:27:38.417'::timestamp),
       (4, 'Geometry', 3, 35, 20, '2020-03-16 13:27:54.013'::timestamp);

-- Insert into instructors
INSERT INTO coursera.instructors (id, first_name, last_name, time_created)
VALUES (1, 'Neno', 'Dimitrov', '2020-03-16 13:25:34.973'::timestamp),
       (2, 'Petko', 'Valchev', '2020-03-16 13:26:00.143'::timestamp),
       (3, 'Petar', 'Penchev', '2020-03-16 13:26:12.613'::timestamp);

-- Insert into students
INSERT INTO coursera.students (pin, first_name, last_name, time_created)
VALUES ('9412011005', 'Krasimir', 'Petrov', '2020-03-16 13:23:58.777'::timestamp),
       ('9501011014', 'Elena', 'Foteva', '2020-03-16 13:24:29.853'::timestamp),
       ('9507141009', 'Ivan', 'Ivanov', '2020-03-16 13:23:39.220'::timestamp);

-- Insert into students_courses_xref
INSERT INTO coursera.students_courses_xref (student_pin, course_id, completion_date)
VALUES ('9412011005', 1, '2019-07-16'::date),
       ('9412011005', 2, '2019-08-20'::date),
       ('9501011014', 1, '2019-07-16'::date),
       ('9501011014', 2, '2019-08-01'::date),
       ('9501011014', 3, '2019-10-01'::date),
       ('9501011014', 4, '2019-12-05'::date),
       ('9507141009', 4, '2019-08-20'::date);

-- Add foreign key constraints
ALTER TABLE coursera.courses
    ADD CONSTRAINT FK_courses_instructors
        FOREIGN KEY (instructor_id) REFERENCES coursera.instructors (id);

ALTER TABLE coursera.students_courses_xref
    ADD CONSTRAINT FK_students_courses_xref_courses
        FOREIGN KEY (course_id) REFERENCES coursera.courses (id);

ALTER TABLE coursera.students_courses_xref
    ADD CONSTRAINT FK_students_courses_xref_students
        FOREIGN KEY (student_pin) REFERENCES coursera.students (pin);