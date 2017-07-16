CREATE TABLE "USERS" (
  "USER_ID"    BIGSERIAL NOT NULL, 
  "USERNAME"   varchar(255) NOT NULL, 
  "EMAIL"      varchar(255) NOT NULL, 
  "PASSWORD"   varchar(255) NOT NULL, 
  "ROLE"       varchar(40) NOT NULL, 
  "FIRST_NAME" varchar(255), 
  "LAST_NAME"  varchar(255), 
  "BIRTH_DATE" date, 
  "COUNTRY"    varchar(255), 
  "CITY"       varchar(255), 
  "GENDER"     char(6), 
  "LEVEL"      varchar(40), 
  "PHOTO"      bytea, 
  PRIMARY KEY ("USER_ID"));
CREATE TABLE "TRIPS" (
  "TRIP_ID"      BIGSERIAL NOT NULL, 
  "DATE_TIME"    timestamp NOT NULL, 
  "PLACE"        varchar(255) NOT NULL, 
  "COORDINATE_X" float8 NOT NULL, 
  "COORDINATE_Y" float8 NOT NULL, 
  "RADIUS"       float8 NOT NULL, 
  PRIMARY KEY ("TRIP_ID"));
CREATE TABLE "DISCOVERIES" (
  "DISCOVERY_ID"    BIGSERIAL NOT NULL, 
  "TRIP_ID"         int8 NOT NULL, 
  "MUSH_SPECIES_ID" int8 NOT NULL, 
  "USER_ID"         int8 NOT NULL, 
  "COORDINATE_X"    float8 NOT NULL, 
  "COORDINATE_Y"    float8 NOT NULL, 
  "PHOTO"           bytea NOT NULL, 
  "DATE_TIME"       timestamp NOT NULL, 
  "IS_PUBLIC"       bool NOT NULL, 
  PRIMARY KEY ("DISCOVERY_ID"));
CREATE TABLE "TAGS" (
  "TAG_ID"       BIGSERIAL NOT NULL, 
  "DISCOVERY_ID" int8 NOT NULL, 
  "NAME"         char(50) NOT NULL, 
  PRIMARY KEY ("TAG_ID"));
CREATE TABLE "SCORES" (
  "SCORE_ID"     BIGSERIAL NOT NULL, 
  "DISCOVERY_ID" int8 NOT NULL, 
  "USER_ID"      int8 NOT NULL, 
  "VALUE"        int4 NOT NULL, 
  PRIMARY KEY ("SCORE_ID"));
CREATE TABLE "COMMENTS" (
  "COMMENT_ID"        BIGSERIAL NOT NULL, 
  "DISCOVERY_ID"      int8 NOT NULL, 
  "USER_ID"           int8 NOT NULL, 
  "TARGET_COMMENT_ID" int8, 
  "CONTENT"           varchar(255) NOT NULL, 
  "DATE_TIME"         timestamp NOT NULL, 
  PRIMARY KEY ("COMMENT_ID"));
CREATE TABLE "MUSHROOMS_SPECIES" (
  "MUSH_SPECIES_ID" BIGSERIAL NOT NULL, 
  "MUSH_GENUS_ID"   int8 NOT NULL, 
  "NAME"            varchar(255) NOT NULL, 
  "EXAMPLE_PHOTO"   bytea, 
  PRIMARY KEY ("MUSH_SPECIES_ID"));
CREATE TABLE "MUSHROOMS_GENUSES" (
  "MUSH_GENUS_ID"  BIGSERIAL NOT NULL, 
  "MUSH_FAMILY_ID" int8 NOT NULL, 
  "NAME"           varchar(255) NOT NULL, 
  PRIMARY KEY ("MUSH_GENUS_ID"));
CREATE TABLE "MUSHROOMS_FAMILIES" (
  "MUSH_FAMILY_ID" BIGSERIAL NOT NULL, 
  "MUSH_ORDER_ID"  int8 NOT NULL, 
  "NAME"           varchar(255) NOT NULL, 
  PRIMARY KEY ("MUSH_FAMILY_ID"));
CREATE TABLE "MUSHROOMS_ORDERS" (
  "MUSH_ORDER_ID" BIGSERIAL NOT NULL, 
  "MUSH_CLASS_ID" int8 NOT NULL, 
  "NAME"          varchar(255) NOT NULL, 
  PRIMARY KEY ("MUSH_ORDER_ID"));
CREATE TABLE "MUSHROOMS_CLASSES" (
  "MUSH_CLASS_ID" BIGSERIAL NOT NULL, 
  "NAME"          varchar(255) NOT NULL, 
  PRIMARY KEY ("MUSH_CLASS_ID"));
CREATE TABLE "USERS_TRIPS" (
  "USER_ID"   int8 NOT NULL, 
  "TRIP_ID"   int8 NOT NULL, 
  "DATE_TIME" timestamp, 
  PRIMARY KEY ("USER_ID", 
  "TRIP_ID"));
CREATE TABLE "USERS_USERS" (
  "USER_ID"   int8 NOT NULL, 
  "FRIEND_ID" int8 NOT NULL, 
  "DATE_TIME" timestamp, 
  PRIMARY KEY ("USER_ID", 
  "FRIEND_ID"));
CREATE TABLE "NOTIFICATIONS" (
  "NOTIFICATION_ID" BIGSERIAL NOT NULL, 
  "USER_ID"         int8 NOT NULL, 
  "RELATED_ID"      int8 NOT NULL, 
  "CONTENT"         varchar(255) NOT NULL, 
  "TYPE"            char(80) NOT NULL, 
  "DATE_TIME"       timestamp, 
  PRIMARY KEY ("NOTIFICATION_ID"));
ALTER TABLE "DISCOVERIES" ADD CONSTRAINT "FKDISCOVERIE634287" FOREIGN KEY ("TRIP_ID") REFERENCES "TRIPS" ("TRIP_ID");
ALTER TABLE "DISCOVERIES" ADD CONSTRAINT "FKDISCOVERIE993930" FOREIGN KEY ("MUSH_SPECIES_ID") REFERENCES "MUSHROOMS_SPECIES" ("MUSH_SPECIES_ID");
ALTER TABLE "TAGS" ADD CONSTRAINT "FKTAGS338564" FOREIGN KEY ("DISCOVERY_ID") REFERENCES "DISCOVERIES" ("DISCOVERY_ID");
ALTER TABLE "SCORES" ADD CONSTRAINT "FKSCORES667828" FOREIGN KEY ("DISCOVERY_ID") REFERENCES "DISCOVERIES" ("DISCOVERY_ID");
ALTER TABLE "COMMENTS" ADD CONSTRAINT "FKCOMMENTS982736" FOREIGN KEY ("DISCOVERY_ID") REFERENCES "DISCOVERIES" ("DISCOVERY_ID");
ALTER TABLE "COMMENTS" ADD CONSTRAINT "FKCOMMENTS455011" FOREIGN KEY ("TARGET_COMMENT_ID") REFERENCES "COMMENTS" ("COMMENT_ID");
ALTER TABLE "COMMENTS" ADD CONSTRAINT "FKCOMMENTS14517" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "SCORES" ADD CONSTRAINT "FKSCORES636048" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "MUSHROOMS_SPECIES" ADD CONSTRAINT "FKMUSHROOMS_907368" FOREIGN KEY ("MUSH_GENUS_ID") REFERENCES "MUSHROOMS_GENUSES" ("MUSH_GENUS_ID");
ALTER TABLE "MUSHROOMS_GENUSES" ADD CONSTRAINT "FKMUSHROOMS_273923" FOREIGN KEY ("MUSH_FAMILY_ID") REFERENCES "MUSHROOMS_FAMILIES" ("MUSH_FAMILY_ID");
ALTER TABLE "MUSHROOMS_FAMILIES" ADD CONSTRAINT "FKMUSHROOMS_739473" FOREIGN KEY ("MUSH_ORDER_ID") REFERENCES "MUSHROOMS_ORDERS" ("MUSH_ORDER_ID");
ALTER TABLE "MUSHROOMS_ORDERS" ADD CONSTRAINT "FKMUSHROOMS_766555" FOREIGN KEY ("MUSH_CLASS_ID") REFERENCES "MUSHROOMS_CLASSES" ("MUSH_CLASS_ID");
ALTER TABLE "USERS_TRIPS" ADD CONSTRAINT "FKUSERS_TRIP484894" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "USERS_TRIPS" ADD CONSTRAINT "FKUSERS_TRIP432910" FOREIGN KEY ("TRIP_ID") REFERENCES "TRIPS" ("TRIP_ID");
ALTER TABLE "DISCOVERIES" ADD CONSTRAINT "FKDISCOVERIE389287" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "USERS_USERS" ADD CONSTRAINT "FKUSERS_USER535363" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "USERS_USERS" ADD CONSTRAINT "FKUSERS_USER175780" FOREIGN KEY ("FRIEND_ID") REFERENCES "USERS" ("USER_ID");
ALTER TABLE "NOTIFICATIONS" ADD CONSTRAINT "FKNOTIFICATI432882" FOREIGN KEY ("USER_ID") REFERENCES "USERS" ("USER_ID");
