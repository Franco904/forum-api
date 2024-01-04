CREATE TABLE answers(
    id INT NOT NULL AUTO_INCREMENT,
    message VARCHAR(300) NOT NULL,
    creation_date DATETIME NOT NULL,
    user_id INT NOT NULL,
    topic_id INT NOT NULL,
    has_solved_topic BOOLEAN NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(topic_id) REFERENCES topics(id) ON DELETE CASCADE
);
