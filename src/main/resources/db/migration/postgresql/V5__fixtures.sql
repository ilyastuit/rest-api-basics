INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (1, 'Android Development', 'Android Development', 200.21, 1000, '2021-06-24T11:48:23', '2021-06-25T23:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (2, 'IOS Development', 'IOS Development', 400.21, 1000, '2021-01-24T11:48:23', '2021-03-10T10:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (3, 'Java Backend Development', 'Java Backend Development', 300.21, 340, '2020-07-24T16:48:23', '2021-05-10T14:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (4, 'Python Backend Development', 'Python Backend Development', 300.21, 400, '2021-02-09T16:48:23', '2021-04-01T08:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (5, 'English courses', 'English courses', 1500.00, 2000, '2020-10-09T10:48:23', '2021-02-01T08:48:23');

INSERT INTO gifts.tag (id, name) VALUES (1, 'android');
INSERT INTO gifts.tag (id, name) VALUES (2, 'ios');
INSERT INTO gifts.tag (id, name) VALUES (3, 'mobile');
INSERT INTO gifts.tag (id, name) VALUES (4, 'backend');
INSERT INTO gifts.tag (id, name) VALUES (5, 'programming');
INSERT INTO gifts.tag (id, name) VALUES (6, 'java');
INSERT INTO gifts.tag (id, name) VALUES (7, 'python');
INSERT INTO gifts.tag (id, name) VALUES (8, 'language');
INSERT INTO gifts.tag (id, name) VALUES (9, 'english');

INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (1, 1, 1);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (2, 1, 3);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (3, 1, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (4, 2, 2);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (5, 2, 3);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (6, 2, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (7, 3, 4);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (8, 3, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (9, 3, 6);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (10, 4, 4);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (11, 4, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (12, 4, 7);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (13, 5, 8);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (14, 5, 9);