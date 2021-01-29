CREATE TABLE MESSAGE
(
    ID        INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TEXT      TEXT                                NOT NULL,
    DATE_SENT TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    DATE_READ TIMESTAMP,
    USER_FROM INT REFERENCES USERS                NOT NULL,
    USER_TO   INT REFERENCES USERS                NOT NULL
);