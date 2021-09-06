--H2
CREATE TABLE task_type (
  id int(2) NOT NULL,
  type varchar(20) NOT NULL,
  comment varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE task (
  id int(5) NOT NULL AUTO_INCREMENT,
  user_id int(5) NOT NULL,
  type_id int(2) NOT NULL,
  title varchar(50) NOT NULL,
  detail text,
  deadline datetime NOT NULL,
  PRIMARY KEY (id)
) ;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  email varchar(70) NOT NULL,
  password varchar(60) NOT NULL,
  enabled tinyint(1) NOT NULL,
  authority varchar(50) NOT NULL,
  tempkey varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--PostgreSQL
create table public.task_type (
  id integer not null
  , type character varying(20) not null
  , comment character varying(50) default NULL
  , primary key (id)
);

create table public.task (
  id serial not null
  , user_id integer not null
  , type_id integer not null
  , title character varying(50) not null
  , detail text
  , deadline timestamp(6) without time zone not null
  , primary key (id)
);

create table public.users (
  id serial not null
  , username character varying(50) not null
  , email character varying(70) not null
  , password character varying(60) not null
  , enabled smallint not null
  , authority character varying(50) not null
  , tempkey character varying(255) default NULL
  , primary key (id)
);