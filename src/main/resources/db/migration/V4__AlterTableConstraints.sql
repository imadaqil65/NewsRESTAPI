ALTER TABLE article
DROP FOREIGN KEY article_ibfk_1,
ADD CONSTRAINT fk_article_user
FOREIGN KEY (user_id) REFERENCES user(id)
ON DELETE CASCADE;

ALTER TABLE article
DROP FOREIGN KEY article_ibfk_2,
ADD CONSTRAINT fk_article_topic
FOREIGN KEY (topic_id) REFERENCES topic(id)
ON DELETE CASCADE;

ALTER TABLE comment
DROP FOREIGN KEY fk_user,
ADD CONSTRAINT fk_comment_user
FOREIGN KEY (user_id) REFERENCES user(id)
ON DELETE CASCADE;

ALTER TABLE comment
DROP FOREIGN KEY fk_article,
ADD CONSTRAINT fk_comment_article
FOREIGN KEY (article_id) REFERENCES article(id)
ON DELETE CASCADE;
