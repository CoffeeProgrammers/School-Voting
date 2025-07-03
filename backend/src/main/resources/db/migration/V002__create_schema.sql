CREATE TABLE "users"
(
    "id"               BIGSERIAL PRIMARY KEY,
    "first_name"       VARCHAR(255) NOT NULL,
    "last_name"        VARCHAR(255) NOT NULL,
    "email"            VARCHAR(255) NOT NULL UNIQUE,
    "keycloak_user_id" VARCHAR(255) NOT NULL UNIQUE,
    "role"             VARCHAR(255) NOT NULL
);

CREATE TABLE "schools"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "director_id" BIGINT,

    CONSTRAINT fk_school_director
        FOREIGN KEY ("director_id") REFERENCES "users" ("id") ON DELETE SET NULL
);

ALTER TABLE "users"
    ADD COLUMN "school_id" BIGINT;

ALTER TABLE "users"
    ADD CONSTRAINT fk_user_school
        FOREIGN KEY ("school_id") REFERENCES "schools" ("id") ON DELETE SET NULL;


CREATE TABLE "classes"
(
    "id"        BIGSERIAL PRIMARY KEY,
    "school_id" BIGINT       NOT NULL,
    "name"      VARCHAR(255) NOT NULL,
    FOREIGN KEY ("school_id") REFERENCES "schools" ("id") ON DELETE CASCADE
);


ALTER TABLE "users"
    ADD COLUMN "class_id" BIGINT;

CREATE TABLE "students_class"
(
    "class_id" BIGINT NOT NULL,
    "user_id"  BIGINT NOT NULL UNIQUE,
    PRIMARY KEY ("class_id", "user_id"),
    FOREIGN KEY ("class_id") REFERENCES "classes" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE
);

CREATE TABLE "votings"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "description" TEXT,
    "level_type"  BIGINT       NOT NULL,
    "start_time"  TIMESTAMP    NOT NULL,
    "end_time"    TIMESTAMP    NOT NULL,
    "creator_id"  BIGINT       NOT NULL,
    FOREIGN KEY ("creator_id") REFERENCES "users" ("id") ON DELETE SET NULL
);

CREATE TABLE "answers"
(
    "id"        BIGSERIAL PRIMARY KEY,
    "name"      VARCHAR(255) NOT NULL,
    "voting_id" BIGINT       NOT NULL,
    "count"     BIGINT       NOT NULL,
    FOREIGN KEY ("voting_id") REFERENCES "votings" ("id") ON DELETE CASCADE
);

CREATE TABLE "voting_user"
(
    "user_id"   BIGINT NOT NULL,
    "voting_id" BIGINT NOT NULL,
    "answer_id" BIGINT,
    PRIMARY KEY ("user_id", "voting_id"),
    FOREIGN KEY ("voting_id") REFERENCES "votings" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("answer_id") REFERENCES "answers" ("id") ON DELETE SET NULL
);

CREATE TABLE "petitions"
(
    "id"          BIGSERIAL PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "description" TEXT,
    "end_time"    TIMESTAMP    NOT NULL,
    "level_type"  BIGINT       NOT NULL,
    "creator_id"  BIGINT       NOT NULL,
    "status"      BIGINT       NOT NULL,
    "count"       BIGINT       NOT NULL,
    "school_id"   BIGINT,
    "class_id"    BIGINT,
    FOREIGN KEY ("creator_id") REFERENCES "users" ("id") ON DELETE SET NULL,
    FOREIGN KEY ("school_id") REFERENCES "schools" ("id") ON DELETE SET NULL,
    FOREIGN KEY ("class_id") REFERENCES "classes" ("id") ON DELETE SET NULL
);

CREATE TABLE "petition_user"
(
    "user_id"     BIGINT NOT NULL,
    "petition_id" BIGINT NOT NULL,
    PRIMARY KEY ("user_id", "petition_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("petition_id") REFERENCES "petitions" ("id") ON DELETE CASCADE
);

CREATE TABLE "comments"
(
    "id"           BIGSERIAL PRIMARY KEY,
    "user_id"      BIGINT NOT NULL,
    "petition_id"  BIGINT NOT NULL,
    "text"         TEXT   NOT NULL,
    "created_time" TIMESTAMP,
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("petition_id") REFERENCES "petitions" ("id") ON DELETE CASCADE
);

CREATE TABLE "files"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "user_id"        BIGINT,
    "path"           VARCHAR(255),
    "file_name"      VARCHAR(255),
    "file_real_name" VARCHAR(255),
    "file_size"      BIGINT,
    "file_type"      VARCHAR(100),
    "file_hash"      VARCHAR(255),
    "upload_date"    TIMESTAMP,
    "file_type_enum" SMALLINT,

    CONSTRAINT fk_files_user FOREIGN KEY (user_id) REFERENCES "users" (id) ON DELETE SET NULL
);

