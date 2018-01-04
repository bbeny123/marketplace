--------------------------------------------------------
--  DDL for Table SUBCATEGORIES
--------------------------------------------------------

CREATE TABLE SUBCATEGORIES
(
  SCT_ID     NUMBER(10) CONSTRAINT PK_SUBCATEGORIES PRIMARY KEY,
  SCT_CAT_ID NUMBER(10)    CONSTRAINT NN_SCT_CAT_ID NOT NULL,
  SCT_NAME   NVARCHAR2(20) CONSTRAINT NN_SCT_NAME NOT NULL,
  CONSTRAINT FK_SCT_CAT_ID FOREIGN KEY (SCT_CAT_ID) REFERENCES CATEGORIES (CAT_ID),
  CONSTRAINT UN_SCT_NAME_CAT_ID UNIQUE (SCT_CAT_ID, SCT_NAME)
);

--------------------------------------------------------
--  Sequence for Table SUBCATEGORIES
--------------------------------------------------------

CREATE SEQUENCE SEQ_SCT_ID
  START WITH 1
  INCREMENT BY 1;
