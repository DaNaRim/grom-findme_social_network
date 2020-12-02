CREATE TABLE USERS
(
    ID                  INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FIRST_NAME          VARCHAR(30)                         NOT NULL,
    LAST_NAME           VARCHAR(30)                         NOT NULL,
    PHONE               VARCHAR(30) UNIQUE                  NOT NULL,
    MAIL                VARCHAR(30) UNIQUE                  NOT NULL,
    PASSWORD            VARCHAR(30)                         NOT NULL,
    COUNTRY             VARCHAR(30),
    CITY                VARCHAR(30),
    AGE                 INT,
    DATE_REGISTERED     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    DATE_LAST_ACTIVE    TIMESTAMP                           NOT NULL,
    RELIGION            VARCHAR(30),
    SCHOOL              VARCHAR(30),
    UNIVERSITY          VARCHAR(30)
);