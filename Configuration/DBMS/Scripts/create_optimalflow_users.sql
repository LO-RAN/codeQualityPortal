CREATE USER "OFLOW" PROFILE "DEFAULT" IDENTIFIED BY "oflow" 
    DEFAULT 
    TABLESPACE "USERS" TEMPORARY 
    TABLESPACE "TEMP" ACCOUNT UNLOCK;
GRANT UNLIMITED TABLESPACE TO oflow;
GRANT CONNECT TO oflow;
GRANT DBA TO oflow;
GRANT RESOURCE TO oflow;

CREATE USER "OFLOWDEV" PROFILE "DEFAULT" IDENTIFIED BY "oflowdev" 
    DEFAULT 
    TABLESPACE "USERS" TEMPORARY 
    TABLESPACE "TEMP" ACCOUNT UNLOCK;
GRANT UNLIMITED TABLESPACE TO oflowdev;
GRANT CONNECT TO oflowdev;
GRANT DBA TO oflowdev;
GRANT RESOURCE TO oflowdev;
