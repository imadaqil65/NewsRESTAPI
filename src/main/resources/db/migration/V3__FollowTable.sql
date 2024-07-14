CREATE TABLE follow (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     user_id BIGINT NOT NULL,
     topic_id BIGINT NOT NULL,
     CONSTRAINT fk_user_topic FOREIGN KEY (user_id) REFERENCES user(id),
     CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES topic(id)
);