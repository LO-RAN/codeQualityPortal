alter table ELEMENT add SCM_REPOSITORY VARCHAR2(128);
alter table ELEMENT add SCM_MODULE VARCHAR2(128);
alter table ELEMENT add PROJECT_FILE_PATH VARCHAR2(128);
commit;

Update ELEMENT set SCM_REPOSITORY=INFO1 Where INFO1 IS NOT NULL And ID_DIALECTE not like 'vb%';
Update ELEMENT set SCM_MODULE=INFO2 Where INFO2 IS NOT NULL And ID_DIALECTE not like 'vb%';
Update ELEMENT set INFO1=NULL Where INFO1 IS NOT NULL And ID_DIALECTE not like 'vb%';
Update ELEMENT set INFO2=NULL Where INFO2 IS NOT NULL And ID_DIALECTE not like 'vb%';

/*
Update ELEMENT set INFO1=NULL Where INFO1 IS NOT NULL And ID_DIALECTE not like 'vb%';
*/

Update ELEMENT set PROJECT_FILE_PATH=INFO1 Where INFO1 IS NOT NULL And ID_DIALECTE like 'vb%';
Update ELEMENT set INFO1=NULL Where INFO1 IS NOT NULL And ID_DIALECTE like 'vb%';

commit;