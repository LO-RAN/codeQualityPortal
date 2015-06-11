CREATE TABLE USER_SETTINGS ( 
  USER_ID			VARCHAR2 (64)	NOT NULL, 
  SETTING_ID		VARCHAR2 (64)	NOT NULL,
  SETTING_VALUE		VARCHAR2 (256)	NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE USER_SETTINGS
 ADD CONSTRAINT USER_SETTINGSP1
  PRIMARY KEY (USER_ID, SETTING_ID, SETTING_VALUE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE SETTINGS ( 
  SETTING_ID		VARCHAR2 (64)	NOT NULL, 
  SETTING_DESC		VARCHAR2 (256)	NOT NULL,
  SETTING_DEFAULT	VARCHAR2 (256))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE SETTINGS
 ADD CONSTRAINT SETTINGSP1
  PRIMARY KEY (SETTING_ID) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE SETTINGS_VALUES ( 
  SETTING_ID		VARCHAR2 (64)	NOT NULL, 
  SETTING_VALUE		VARCHAR2 (256)	NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE SETTINGS_VALUES
 ADD CONSTRAINT SETTINGS_VALUESP1
  PRIMARY KEY (SETTING_ID, SETTING_VALUE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

ALTER TABLE SETTINGS_VALUES ADD  CONSTRAINT SETTINGS_VALUES_ID
 FOREIGN KEY (SETTING_ID) 
  REFERENCES SETTINGS (SETTING_ID) ;

  ALTER TABLE USER_SETTINGS ADD  CONSTRAINT USER_SETTINGS_ID
 FOREIGN KEY (SETTING_ID) 
  REFERENCES SETTINGS (SETTING_ID) ;

INSERT INTO SETTINGS(SETTING_ID,SETTING_DESC,SETTING_DEFAULT) VALUES ('COLOR_THEME','ExtJS color theme','theme-slate');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-default');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-black');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-gray');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-olive');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-purple');
INSERT INTO SETTINGS_VALUES(SETTING_ID,SETTING_VALUE) VALUES ('COLOR_THEME','theme-slate');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-default','fr','Bleu');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-black','fr','Noir');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-gray','fr','Gris');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-olive','fr','Olive');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-purple','fr','Pourpre');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-slate','fr','Ardoise');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-default','en','Blue');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-black','en','Black');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-gray','en','Gray');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-olive','en','Olive');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-purple','en','Purple');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('setting_value','lib','theme-slate','en','Slate');

-- already added in 4.0.1
--INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('ACTION_PLAN', 'Action plan definition');
--INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ACTION_PLAN', 'PROJECTMANAGER');

CREATE TABLE ACTION_PLAN_UNIT ( 
  UNIT_ID			VARCHAR2 (64)	NOT NULL, 
  UNIT_DESC			VARCHAR2 (256))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE ACTION_PLAN_UNIT
 ADD CONSTRAINT ACTION_PLAN_UNITP1
  PRIMARY KEY (UNIT_ID) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

INSERT INTO ACTION_PLAN_UNIT(UNIT_ID,UNIT_DESC) VALUES ('DAY_PER_MAN','Unit Day/Man');
INSERT INTO ACTION_PLAN_UNIT(UNIT_ID,UNIT_DESC) VALUES ('K_EURO','K-Euros');
INSERT INTO ACTION_PLAN_UNIT(UNIT_ID,UNIT_DESC) VALUES ('K_DOLLAR','K-Dollar');

INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','DAY_PER_MAN','fr','Jours/Homme');
INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','DAY_PER_MAN','en','Days/Man');
INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','K_EURO','fr','K-Euros');
INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','K_EURO','en','K-Euros');
INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','K_DOLLAR','fr','K-Dollars');
INSERT INTO I18N(TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('action_plan_unit','lib','K_DOLLAR','en','K-Dollars');

alter table  MODELE add NB_ACTION_PLAN_UNIT INTEGER;
UPDATE MODELE SET NB_ACTION_PLAN_UNIT = 1;
alter table  MODELE add ACTION_PLAN_UNIT_ID VARCHAR(64);
UPDATE MODELE SET ACTION_PLAN_UNIT_ID = 'DAY_PER_MAN';

ALTER TABLE MODELE
 ADD CONSTRAINT MODELE_ACTION_PLAN
  FOREIGN KEY (ACTION_PLAN_UNIT_ID)
  REFERENCES ACTION_PLAN_UNIT(UNIT_ID)
; 

-- in CAQS_MESSAGES table, 
-- change column name from PERCENT to COMPLETION_PCT 
-- because PERCENT is a reserved keyword for MS-SQL Server 
alter table  caqs_messages add COMPLETION_PCT decimal;
update caqs_messages set COMPLETION_PCT=PERCENT;
alter table caqs_messages drop column PERCENT;
commit;

-- in BASELINE_LINKS table,
-- change size from 64 to 32 for columns PARENT_ID_BLINE and CHILD_ID_BLINE
-- to fit size of referenced column ID_BLINE in BASELINE table
-- (enforced by MS-SQL Server)  
alter table baseline_links modify PARENT_ID_BLINE varchar2(32);
alter table baseline_links modify CHILD_ID_BLINE varchar2(32);

-- in CAQS_ACCESS_RIGHTS table,
-- change size from 128 to 64 for column ID_ROLE
-- to fit size of referenced column ID_ROLE in CAQS_ROLE table
-- (enforced by MS-SQL Server)
alter table caqs_access_rights modify id_role varchar2(64);

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('ADMIN_DOMAIN_MODIFICATION', 'Possibility to modify domains information');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ADMIN_DOMAIN_MODIFICATION', 'ADMINISTRATOR');


-- Modify 2 architecture tables to allow type for archilinks and archi packages
alter table ARCHI_LINK add TYPE NUMBER(12);
alter table PACKAGE add TYPE NUMBER(12);
-- Allow package into package
alter table PACKAGE add PARENT_PACKAGE varchar2(32);

UPDATE CAQS_TASKS SET SHOW_USER='ALWAYS', TYPE_TASK='PROGRESS' WHERE ID_TASK='GENERATING_REPORT';
UPDATE I18N SET text='Analyse non commencée' WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_NOT_STARTED' and id_langue='fr';
UPDATE I18N SET text='Analysis not started'   WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_NOT_STARTED' and id_langue='en';
UPDATE I18N SET text='Analyse en cours'      WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_IN_PROGRESS' and id_langue='fr';
UPDATE I18N SET text='Analysis in progress'  WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_IN_PROGRESS' and id_langue='en';
UPDATE I18N SET text='Analyse terminée'      WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_COMPLETED'   and id_langue='fr';
UPDATE I18N SET text='Analysis completed'    WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_COMPLETED'   and id_langue='en';
UPDATE I18N SET text='Analyse échouée'       WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_FAILED'      and id_langue='fr';
UPDATE I18N SET text='Analysis failed'       WHERE table_name='message_status' and column_name='lib' and id_table='ANALYSING_FAILED'      and id_langue='en';

UPDATE I18N SET text='Calcul non commencé' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_NOT_STARTED' and id_langue='fr';
UPDATE I18N SET text='Computing not started' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_NOT_STARTED' and id_langue='en';
UPDATE I18N SET text='Calcul en cours' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_IN_PROGRESS' and id_langue='fr';
UPDATE I18N SET text='Computing in progress' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_IN_PROGRESS' and id_langue='en';
UPDATE I18N SET text='Calcul terminé' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_COMPLETED' and id_langue='fr';
UPDATE I18N SET text='Computing completed' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_COMPLETED' and id_langue='en';
UPDATE I18N SET text='Calcul échoué' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_FAILED' and id_langue='fr';
UPDATE I18N SET text='Computing failed' WHERE table_name='message_status' and column_name='lib' and id_table='COMPUTING_FAILED' and id_langue='en';

UPDATE I18N SET text='Génération du rapport non commencée' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_NOT_STARTED' and id_langue='fr';
UPDATE I18N SET text='Report generation not started' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_NOT_STARTED' and id_langue='en';
UPDATE I18N SET text='Génération du rapport en cours' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_IN_PROGRESS' and id_langue='fr';
UPDATE I18N SET text='Report generation in progress' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_IN_PROGRESS' and id_langue='en';
UPDATE I18N SET text='Génération du rapport terminée' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_COMPLETED' and id_langue='fr';
UPDATE I18N SET text='Report generation completed' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_COMPLETED' and id_langue='en';
UPDATE I18N SET text='Génération du rapport échouée' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_FAILED' and id_langue='fr';
UPDATE I18N SET text='Report generation failed' WHERE table_name='message_status' and column_name='lib' and id_table='GENERATING_REPORT_FAILED' and id_langue='en';

alter table  METRIQUE add COST_CORRECTION varchar(512);

INSERT INTO SETTINGS(SETTING_ID,SETTING_DESC,SETTING_DEFAULT) VALUES ('FAVORITES','Favorites','');

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('EXPORT_MODEL','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_NOT_STARTED','fr','{0} : Export du modèle non commencé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_NOT_STARTED','en','{0} : Model export not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_IN_PROGRESS','fr','{0} : Export du modèle en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_IN_PROGRESS','en','{0} :Model export in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_COMPLETED','fr','{0} : Export du modèle terminé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_COMPLETED','en','{0} : Model export completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_FAILED','fr','{0} : Export du modèle échoué');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_MODEL_FAILED','en','{0} : Model export failed');

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('EXPORT_PROJECT','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_NOT_STARTED','fr','Export non commencé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_NOT_STARTED','en','Export not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_IN_PROGRESS','fr','Export en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_IN_PROGRESS','en','Export in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_COMPLETED','fr','Export terminé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_COMPLETED','en', 'Export completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_FAILED','fr','Export échoué');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','EXPORT_PROJECT_FAILED','en','Export failed');

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('IMPORT_PROJECT','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_NOT_STARTED','fr','{0} : Import du projet non commencé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_NOT_STARTED','en','{0} : Project import not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_IN_PROGRESS','fr','{0} : Import du projet en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_IN_PROGRESS','en','{0} : Project import in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_COMPLETED','fr','{0} : Import du projet terminé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_COMPLETED','en', '{0} : Project import completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_FAILED','fr','{0} : Import du projet échoué');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_PROJECT_FAILED','en','{0} : Project import failed');

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('IMPORT_MODEL','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_NOT_STARTED','fr','{0} : Import du modèle non commencé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_NOT_STARTED','en','{0} : Model import not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_IN_PROGRESS','fr','{0} : Import du modèle en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_IN_PROGRESS','en','{0} : Model import in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_COMPLETED','fr','{0} : Import du modèle terminé');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_COMPLETED','en', '{0} : Model import completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_FAILED','fr','{0} : Import du modèle échoué');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','IMPORT_MODEL_FAILED','en','{0} : Model import failed');


-- change fields to NOT NULL as they are part of the primary key
-- (Apache DERBY database enforces this constraints)
alter table BASELINE_LINKS modify CHILD_ID_ELT not null;
alter table FICHE_LINKS modify FICHE_PERE not null;
alter table FICHE_LINKS modify FICHE_FILS not null;


CREATE TABLE COPY_PASTE_REF (
  ID_COPY        VARCHAR2 (64)  NOT NULL, 
  ID_BLINE       VARCHAR2 (64)  NOT NULL,
  ID_ELT         VARCHAR2 (64)  NOT NULL, 
  LINE           NUMBER  NOT NULL,
  LENGTH         NUMBER  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE COPY_PASTE_REF
 ADD CONSTRAINT COPY_PASTE_REFP1
  PRIMARY KEY (ID_COPY, ID_BLINE, ID_ELT, LINE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

ALTER TABLE COPY_PASTE_REF ADD CONSTRAINT FK_COPY_PASTE_REF_ELEMENT
 FOREIGN KEY (ID_ELT)
  REFERENCES ELEMENT (ID_ELT);

ALTER TABLE COPY_PASTE_REF ADD CONSTRAINT FK_COPY_PASTE_REF_BASELINE
 FOREIGN KEY (ID_BLINE)
  REFERENCES BASELINE (ID_BLINE);

alter table CRITERE_BLINE add COST NUMBER (11,4);

alter table QAMETRIQUE modify LIGNES VARCHAR2 (2048);

CREATE TABLE ACTION_PLAN ( 
  ID_ACTION_PLAN    VARCHAR2 (64)	NOT NULL,
  ID_ELT			      VARCHAR2 (64)	NOT NULL, 
  ID_BLINE			VARCHAR2 (32)	NOT NULL,
  ACTION_PLAN_COMMENT       VARCHAR2(1024),
  COMMENT_USER        VARCHAR(64))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE ACTION_PLAN
 ADD CONSTRAINT ACTION_PLANP1
  PRIMARY KEY (ID_ACTION_PLAN) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE ACTION_PLAN_CRITERION ( 
  ID_ACTION_PLAN    VARCHAR2 (64)	NOT NULL,
  ID_CRIT			VARCHAR2 (64)	NOT NULL,
  PRIORITY			VARCHAR2(32),
  SELECTED      NUMBER(1) NOT NULL,
  CRITERION_COMMENT       VARCHAR2(1024),
  COMMENT_USER        VARCHAR(64))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE ACTION_PLAN_CRITERION
 ADD CONSTRAINT ACTION_PLAN_CRITERIONP1
  PRIMARY KEY (ID_ACTION_PLAN, ID_CRIT) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
;

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('ACTION_PLAN_EDITION', 'Action plan edition');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ACTION_PLAN_EDITION', 'PROJECTMANAGER');

ALTER TABLE FACTEUR_BLINE DROP COLUMN just_note_facbl;
ALTER TABLE FACTEUR_BLINE DROP COLUMN id_just;

alter table  TYPE_ELEMENT add IS_FILE NUMBER DEFAULT 0;
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'FILE';
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'JSP';
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'JS';
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'DOC';
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'PROGRAM';
UPDATE TYPE_ELEMENT SET IS_FILE = 1 WHERE ID_TELT = 'CLS';

alter table CRITERE_BLINE add CRITERION_COMMENT VARCHAR2(4000);
alter table FACTEUR_BLINE add FACTEUR_COMMENT VARCHAR2(4000);

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('TOP_DOWN_FACTOR_COMMENT', 'Possibility to comment goals and criterions in the Top-Down interface');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('TOP_DOWN_FACTOR_COMMENT', 'PROJECTMANAGER');

ALTER TABLE ACTION_PLAN
 ADD CONSTRAINT ACTION_PLAN_ELEMENT_ID_FK
  FOREIGN KEY (ID_ELT)
  REFERENCES ELEMENT(ID_ELT); 

ALTER TABLE ACTION_PLAN
 ADD CONSTRAINT ACTION_PLAN_ID_BLINE_FK
  FOREIGN KEY (ID_BLINE)
  REFERENCES BASELINE(ID_BLINE); 

ALTER TABLE ACTION_PLAN_CRITERION
 ADD CONSTRAINT ID_ACTION_PLAN_FK
  FOREIGN KEY (ID_ACTION_PLAN)
  REFERENCES ACTION_PLAN(ID_ACTION_PLAN);

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('DELETE_ELEMENTS','INFO','NEVER');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('DELETE_FROM_TRASH', 'Possibility to delete elements located into the trash');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('DELETE_FROM_TRASH', 'ADMINISTRATOR');
