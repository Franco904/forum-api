CREATE TABLE courses(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    category VARCHAR(45) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO courses(id, name, category) VALUES
(1, 'Kotlin', 'Linguagens de programação'),
(2, 'Flutter', 'Mobile'),
(3, 'KMP', 'Mobile'),
(4, 'Angular', 'Front-end'),
(5, 'Spring Boot', 'Back-end');
