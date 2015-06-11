CREATE USER "OVIEW" PROFILE "DEFAULT" IDENTIFIED BY "oview" 
    DEFAULT 
    TABLESPACE "USERS" TEMPORARY 
    TABLESPACE "TEMP" ACCOUNT UNLOCK;
GRANT UNLIMITED TABLESPACE TO oview;
GRANT CONNECT TO oview;
GRANT DBA TO oview;
GRANT RESOURCE TO oview;
connect oview/oview;
