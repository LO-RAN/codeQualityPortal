ALTER TABLE TYPE_ELEMENT ADD HAS_SOURCE NUMBER(1) DEFAULT 0;
insert into TYPE_ELEMENT (ID_TELT, LIB_TELT, DESC_TELT) VALUES ('DOMAIN', 'Domain', 'Domain');

--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','PRJ','fr','Projet');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','PRJ','en','Project');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','PRJ','fr','Projet');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','PRJ','en','Project');

--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','SSP','fr','Sous-projet');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','SSP','en','Sub-project');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','SSP','fr','Sous-projet');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','SSP','en','Sub-project');

--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','EA','fr','Entit� applicative');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','EA','en','Application entity');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','EA','fr','Entit� applicative');
--INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','EA','en','Application entity');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','DOMAIN','fr','Domaine');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','lib','DOMAIN','en','Domain');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','DOMAIN','fr','Domaine');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('type_element','desc','DOMAIN','en','Domain');
update type_element set has_source=1 where id_telt='CLS';
update type_element set has_source=1 where id_telt='MET';

commit;

CREATE TABLE BASELINE_LINKS ( 
  PARENT_ID_BLINE     VARCHAR2 (64)  NOT NULL, 
  CHILD_ID_BLINE      VARCHAR2 (64)  NOT NULL,
  CHILD_ID_ELT        VARCHAR2 (64)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE BASELINE_LINKS
 ADD CONSTRAINT BASELINE_LINKSP1
  PRIMARY KEY (PARENT_ID_BLINE, CHILD_ID_BLINE, CHILD_ID_ELT) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_CHILD
 FOREIGN KEY (CHILD_ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_PARENT
 FOREIGN KEY (PARENT_ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_CHILD_ELT
 FOREIGN KEY (CHILD_ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

CREATE TABLE CAQS_TASKS (
	ID_TASK		VARCHAR(32) NOT NULL,
	TYPE_TASK	VARCHAR(32) NOT NULL,
	SHOW_USER	VARCHAR(32)
)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CAQS_TASKS
 ADD CONSTRAINT TASKSP1
  PRIMARY KEY (ID_TASK) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE CAQS_MESSAGES (
	ID_MESSAGE 	VARCHAR(32) NOT NULL,
	ID_ELT		VARCHAR(32) NOT NULL,
	ID_BLINE	VARCHAR(32) NOT NULL,
	ID_USER		VARCHAR(32) NOT NULL,
	ID_TASK		VARCHAR(32) NOT NULL,
	STATUS		VARCHAR(32),
	BEGIN_DATE	DATE,
	END_DATE	DATE,
	PERCENT		DECIMAL,
	SEEN		NUMBER(1),
	INFO1		VARCHAR(256),
	OTHERID		VARCHAR(32)
)
  STORAGE
	( 	INITIAL 	1 M
  		NEXT 	  	1 M
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CAQS_MESSAGES
 ADD CONSTRAINT CAQS_MESSAGESP1
  PRIMARY KEY (ID_MESSAGE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('GENERATING_REPORT','INFO','WHEN_FINISHED');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','GENERATING_REPORT','fr','G�n�ration du rapport termin�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','GENERATING_REPORT','fr','G�n�ration du rapport termin�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','GENERATING_REPORT','en','Report generated');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','GENERATING_REPORT','en','Report generated');
INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('COMPUTING','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','COMPUTING','fr','Calcul');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','COMPUTING','fr','Calcul');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','COMPUTING','en','Compute');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','COMPUTING','en','Compute');

CREATE TABLE CAQS_ROLE ( 
  ID_ROLE       VARCHAR2 (64)  NOT NULL, 
  PORTAL_ROLE   VARCHAR2 (64)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CAQS_ROLE
 ADD CONSTRAINT CAQS_ROLEP1
  PRIMARY KEY (ID_ROLE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE CAQS_ACCESS_DEFINITION ( 
  ID_ACCESS     VARCHAR2 (64)  NOT NULL, 
  LIB_ACCESS    VARCHAR2 (128)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CAQS_ACCESS_DEFINITION
 ADD CONSTRAINT CAQS_ACCESS_DEFINITIONP1
  PRIMARY KEY (ID_ACCESS) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE TABLE CAQS_ACCESS_RIGHTS ( 
  ID_ACCESS    VARCHAR2 (64)  NOT NULL, 
  ID_ROLE      VARCHAR2 (128)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CAQS_ACCESS_RIGHTS
 ADD CONSTRAINT CAQS_ACCESS_RIGHTSP1
  PRIMARY KEY (ID_ACCESS, ID_ROLE) 
  USING INDEX 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

ALTER TABLE CAQS_ACCESS_RIGHTS ADD  CONSTRAINT CAQS_ACCESS_RIGHTS_ACCESS
 FOREIGN KEY (ID_ACCESS) 
  REFERENCES CAQS_ACCESS_DEFINITION (ID_ACCESS) ;

ALTER TABLE CAQS_ACCESS_RIGHTS ADD  CONSTRAINT CAQS_ACCESS_RIGHTS_ROLE
 FOREIGN KEY (ID_ROLE) 
  REFERENCES CAQS_ROLE (ID_ROLE) ;

INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('ADMINISTRATOR', 'Administrateur');
INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('ARCHITECT', 'Architecte');
INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('QUALITYEXPERT', 'Qualiticien');
INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('MANAGER', 'Manager');
INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('PROJECTMANAGER', 'Chef de Projet');
INSERT INTO CAQS_ROLE (ID_ROLE, PORTAL_ROLE) VALUES ('DEVELOPER', 'Developpeur');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('CRITERION_DETAIL', 'Access criterion detail (element with quality violation)');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('JUSTIFICATION', 'Access justification information');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('JUSTIFICATION_VALIDATION', 'Access justification validation');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('LABEL', 'Acces label information');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('LABEL_VALIDATION', 'Access label validation');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('DATA', 'Access metric data');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('CALCULATION', 'Access re-calculation');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('REPORT', 'Access report');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('UPLOAD', 'Access upload for metrics');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('MODEL_EDITOR', 'Access model editor');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('PROJECT_ADMIN', 'Access project administration');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('ALL_PROJECT_ADMIN', 'Access project administration for all projects');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('EDIT_ARCHITECTURE', 'Access architecture in edit mode');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CRITERION_DETAIL', 'DEVELOPER');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CRITERION_DETAIL', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CRITERION_DETAIL', 'ARCHITECT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CRITERION_DETAIL', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION', 'DEVELOPER');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION', 'ARCHITECT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION_VALIDATION', 'QUALITYEXPERT');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('LABEL', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('LABEL', 'MANAGER');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('LABEL', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('LABEL_VALIDATION', 'QUALITYEXPERT');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('DATA', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('DATA', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CALCULATION', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CALCULATION', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('REPORT', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('REPORT', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('UPLOAD', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('UPLOAD', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('MODEL_EDITOR', 'QUALITYEXPERT');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ALL_PROJECT_ADMIN', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ALL_PROJECT_ADMIN', 'ADMINISTRATOR');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('PROJECT_ADMIN', 'PROJECTMANAGER');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('EDIT_ARCHITECTURE', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('EDIT_ARCHITECTURE', 'ARCHITECT');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('JUSTIFICATION_CREATION', 'Access justification creation');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION_CREATION', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('JUSTIFICATION_CREATION', 'PROJECTMANAGER');
INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('ADMIN_DRAGDROP', 'Drag and drop possibility in administration');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ADMIN_DRAGDROP', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('ADMIN_DRAGDROP', 'ADMINISTRATOR');

ALTER TABLE ELEMENT_BASELINE_INFO ADD START_LINE NUMBER DEFAULT 0;

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('CANCEL_JUSTIF', 'Access justification cancelling');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CANCEL_JUSTIF', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('CANCEL_JUSTIF', 'ADMINISTRATOR');

INSERT INTO CAQS_TASKS (ID_TASK,TYPE_TASK,SHOW_USER) VALUES ('ANALYSING','PROGRESS','ALWAYS');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','ANALYSING','fr','{0} : Analyse');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','ANALYSING','fr','{0} : Analyse');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','lib','ANALYSING','en','{0} : Analysis');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('task','desc','ANALYSING','en','{0} : Analysis');

INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('PROJECT_ADMIN', 'QUALITYEXPERT');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('MANUAL_ANALYSIS', 'Launch manual analysis access');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('IMPORT_EXPORT', 'Access import/export');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('IMPORT_EXPORT', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('IMPORT_EXPORT', 'ADMINISTRATOR');

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('TRANSLATION', 'Access translation');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('TRANSLATION', 'QUALITYEXPERT');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('TRANSLATION', 'ADMINISTRATOR');


INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_NOT_STARTED','fr','non commenc�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_NOT_STARTED','en','not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_IN_PROGRESS','fr','en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_IN_PROGRESS','en','in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_COMPLETED','fr','termin�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_COMPLETED','en','completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_FAILED','fr','�chou�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','ANALYSING_FAILED','en','failed');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_NOT_STARTED','fr','non commenc�');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_NOT_STARTED','en','not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_IN_PROGRESS','fr','en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_IN_PROGRESS','en','in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_COMPLETED','fr','termin�');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_COMPLETED','en','completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_FAILED','fr','�chou�');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','COMPUTING_FAILED','en','failed');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_NOT_STARTED','fr','non commenc�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_NOT_STARTED','en','not started');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_IN_PROGRESS','fr','en cours');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_IN_PROGRESS','en','in progress');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_COMPLETED','fr','termin�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_COMPLETED','en','completed');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_FAILED','fr','�chou�e');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('message_status','lib','GENERATING_REPORT_FAILED','en','failed');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_wlc','fr','Bienvenue');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_wlc','en','Welcome');

INSERT INTO LANGAGE(ID_LANGAGE, LIB_LANG, DESC_LANG) VALUES ('php', 'php', 'php');
INSERT INTO DIALECTE(ID_DIALECTE, ID_LANGAGE, LIB_DIALECTE, DESC_DIALECTE) VALUES ('PHP','php', 'PHP 3/4/5', 'PHP');

-- synchronize project names between tables PROJET and ELEMENT
update projet set lib_pro = (select lib_elt from element where id_telt = 'PRJ' and projet.id_pro = element.id_pro);