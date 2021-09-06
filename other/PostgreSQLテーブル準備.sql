CREATE TABLE task_type (
  id INTEGER PRIMARY KEY,
  type varchar(20) NOT NULL,
  comment varchar(50) DEFAULT NULL
);


CREATE TABLE task (
  id SERIAL NOT NULL PRIMARY KEY,
  user_id INTEGER NOT NULL,
  type_id INTEGER NOT NULL,
  title varchar(50) NOT NULL,
  detail text,
  deadline timestamp NOT NULL
) ;

CREATE TABLE users (
  id SERIAL NOT NULL PRIMARY KEY,
  username varchar(50) NOT NULL,
  email varchar(70) NOT NULL,
  password varchar(60) NOT NULL,
  enabled smallint NOT NULL,
  authority varchar(50) NOT NULL,
  tempkey varchar(255) DEFAULT NULL
);

INSERT INTO task_type VALUES 
(1,'緊急','最優先で取り掛かるべきタスク'),
(2,'重要','期限に間に合わせるべきタスク'),
(3,'できれば','今後やってみたいアイデア');

INSERT INTO task (user_id, type_id, title, detail, deadline) VALUES 
(1,1,'JUnitを学習','テストの仕方を学習する','2020-07-07 15:00:00'),
(1,3,'サービスの自作','マイクロサービスを作ってみる','2020-09-13 17:00:00'),
(1,2,'AWSへの移行','AWS RDSにデータベースを移行する','2021-08-25 18:00:00');

