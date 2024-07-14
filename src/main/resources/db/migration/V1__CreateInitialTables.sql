CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255) NOT NULL CHECK (LENGTH(name) >= 2),
    password VARCHAR(255) NOT NULL,
    bio      VARCHAR(300),
    usertype INT
);

CREATE TABLE topic
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK ((LENGTH(name) >= 3))
);

CREATE TABLE article
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(150) NOT NULL CHECK (LENGTH(title) >= 3),
    user_id  BIGINT       NOT NULL,
    topic_id BIGINT       NOT NULL,
    date     DATE,
    content  LONGTEXT         NOT NULL CHECK (LENGTH(content) >= 10),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (topic_id) REFERENCES topic (id)
);