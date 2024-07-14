CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    content VARCHAR(300) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES article(id)
);
