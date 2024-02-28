DROP TABLE IF EXISTS COMMENTS;
DROP TABLE IF EXISTS BOOKINGS;
DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS USERS;

DROP TYPE IF EXISTS STATUS_TYPE;

CREATE TYPE STATUS_TYPE AS ENUM('WAITING', 'APPROVED', 'REJECTED');

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    NAME CHARACTER VARYING(255) NOT NULL,
    EMAIL CHARACTER VARYING(255) UNIQUE NOT NULL 
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ITEM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    AVAILABLE BOOLEAN NOT NULL DEFAULT TRUE,
    OWNER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    BOOKING_ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    ITEM_ID INTEGER NOT NULL REFERENCES ITEMS(ITEM_ID) ON DELETE CASCADE,
    DATE_FROM TIMESTAMP NOT NULL,
    DATE_TO TIMESTAMP NOT NULL,
    STATUS STATUS_TYPE NOT NULL DEFAULT 'WAITING',
    USER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    COMMENT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    ITEM_ID INTEGER NOT NULL REFERENCES ITEMS(ITEM_ID) ON DELETE CASCADE,
    USER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID) ON DELETE CASCADE,
    CONTENT CHARACTER VARYING(512) NOT NULL,
    CREATION_DATE TIMESTAMP NOT NULL
);
