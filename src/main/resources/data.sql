INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('hui', 'hui@engtalk.com', NOW(), NOW());
INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('hyena', 'hyena@engtalk.com', NOW(), NOW());
INSERT INTO member (nickname, email, created_date, last_modified_date)
    VALUES ('mina', 'mina@engtalk.com', NOW(), NOW());

INSERT INTO article (author, title, content, created_date, last_modified_date)
    VALUES (hui, 'Spring', 'spring is good', NOW(), NOW());
INSERT INTO article (author, title, content, created_date, last_modified_date)
    VALUES (hui, 'JPA/Hibernate', 'ORM is good', NOW(), NOW());
INSERT INTO article (author, title, content, created_date, last_modified_date)
    VALUES (hyena, 'MyBatis', '...', NOW(), NOW());