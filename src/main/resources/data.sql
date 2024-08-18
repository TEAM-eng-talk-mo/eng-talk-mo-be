INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('hui', 'hui@engtalk.com', NOW(), NOW());
INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('hyena', 'hyena@engtalk.com', NOW(), NOW());
INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('mina', 'mina@engtalk.com', NOW(), NOW());

INSERT INTO article (member_id, title, content, created_date, last_modified_date)
    VALUES (1, 'Spring', 'spring is good', NOW(), NOW());
INSERT INTO article (member_id, title, content, created_date, last_modified_date)
    VALUES (1, 'JPA/Hibernate', 'ORM is good', NOW(), NOW());
INSERT INTO article (member_id, title, content, created_date, last_modified_date)
    VALUES (1, 'MyBatis', '...', NOW(), NOW());