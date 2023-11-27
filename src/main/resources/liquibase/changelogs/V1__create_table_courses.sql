CREATE TABLE courses(
    id INT AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    category VARCHAR(45) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO courses(name, category) VALUES
('Kotlin', 'Linguagens de programação'),
('Flutter', 'Mobile'),
('KMP', 'Mobile'),
('Angular', 'Front-end'),
('Spring Boot', 'Back-end');
