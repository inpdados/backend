CREATE TABLE comments (
    id       SERIAL               ,
    at       TIMESTAMP    NOT NULL ,
    target   VARCHAR(64)  NOT NULL ,
    comment  TEXT         NOT NULL ,
    likes    INTEGER     DEFAULT 0 ,
    dislikes INTEGER     DEFAULT 0 ,
    name     VARCHAR(64)           ,
    email    VARCHAR(128)          ,
    PRIMARY KEY(id)
);
