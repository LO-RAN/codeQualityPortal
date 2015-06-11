alter table PROJET modify DESC_PRO VARCHAR2(128);
commit;

accept startpos prompt 'Enter the starting position for source dir substr (first char means 1, end is / position): ';
update ELEMENT set SOURCE_DIR=substr(SOURCE_DIR, &startpos + 1) where SOURCE_DIR IS NOT NULL;
commit;

update ELEMENT set SOURCE_DIR=replace(SOURCE_DIR, '\', '/') where SOURCE_DIR IS NOT NULL;
commit;
