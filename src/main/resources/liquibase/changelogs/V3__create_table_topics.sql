CREATE TABLE topics(
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(60) NOT NULL,
    message VARCHAR(300) NOT NULL,
    creation_date DATETIME NOT NULL,
    status VARCHAR(25),
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(course_id) REFERENCES courses(id)
);
