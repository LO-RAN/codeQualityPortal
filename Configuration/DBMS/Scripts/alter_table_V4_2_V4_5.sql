alter table  ACTION_PLAN_CRITERION add MASTER_ELEMENT VARCHAR(64);
UPDATE ACTION_PLAN_CRITERION apc SET MASTER_ELEMENT = (select id_elt from action_plan ap where ap.id_action_plan=apc.id_action_plan);

INSERT INTO METRIQUE(ID_MET, OUTIL_MET) VALUES('IFPUG', 'CAQS');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'lib', 'IFPUG', 'fr', 'Points de fonctions');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'desc', 'IFPUG', 'fr', 'Métrique de points de fonctions');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'compl', 'IFPUG', 'fr', 'Son implémentation dépend du modèle qualimétrique. Se référer à l''annexe pour plus d''informations quant à sa formule de calcul.');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'lib', 'IFPUG', 'en', 'Functions points');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'desc', 'IFPUG', 'en', 'Functions points metric');
INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES('metrique', 'compl', 'IFPUG', 'en', 'Its implementation differs from one quality model to another. Refer to the appendice for more information regarding its computation formula.');

alter table  MODELE add APU_LIMIT_MEDIUM DECIMAL;
alter table  MODELE add APU_LIMIT_LONG DECIMAL;
UPDATE MODELE SET APU_LIMIT_MEDIUM = 10;
UPDATE MODELE SET APU_LIMIT_LONG = 20;

alter table ACTION_PLAN_CRITERION modify criterion_comment VARCHAR(4000);

-- DAZ 03-12-2009

alter table  ACTION_PLAN_UNIT add NB_ACTION_PLAN_UNIT INTEGER;
UPDATE ACTION_PLAN_UNIT SET NB_ACTION_PLAN_UNIT = 1;
alter table  MODELE drop column NB_ACTION_PLAN_UNIT;
ALTER TABLE MODELE drop CONSTRAINT MODELE_ACTION_PLAN;
alter table  MODELE drop column ACTION_PLAN_UNIT_ID;

-- DAZ 18/02/2010
INSERT INTO SETTINGS(SETTING_ID,SETTING_DESC,SETTING_DEFAULT) VALUES ('DASHBOARD_DEFAULT_DOMAIN','Default domain displayed in dashboard','DASHBOARD_ALL_DOMAINS');

-- DAZ 23/02/2010

CREATE TABLE TMP_ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL,
  ID_CRIT VARCHAR(64) NOT NULL,
  NOTE_CRIBL    NUMBER (11,4),
  PREV_NOTE_CRIBL NUMBER (11,4))
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	5 M
  		PCTINCREASE 10 
	)
  TABLESPACE %TABLE_TBS%;

CREATE TABLE ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL, 
  NBBETTER     NUMBER (10) DEFAULT 0, 
  NBWORST      NUMBER (10) DEFAULT 0, 
  NBSTABLE     NUMBER (10) DEFAULT 0)
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	5 M
  		PCTINCREASE 10 
	)
  TABLESPACE %TABLE_TBS%;

  -- DAZ 23/07/2010 --
INSERT INTO LANGAGE (ID_LANGAGE,LIB_LANG,DESC_LANG) VALUES ('flex','flex','Flex');
INSERT INTO DIALECTE (ID_DIALECTE,ID_LANGAGE,LIB_DIALECTE,DESC_DIALECTE) VALUES ('flex','flex','Flex',null);

 -- LIZ 10/13/2010 --
 
alter table modele drop column lib_usa;
alter table modele drop column desc_usa;

alter table facteur drop column lib_fact;
alter table facteur drop column desc_fact;

alter table outils drop column lib_outils;

alter table type_element drop column lib_telt;
alter table type_element drop column desc_telt;

alter table metrique drop column lib_met;
alter table metrique drop column desc_met;

alter table critere drop column lib_crit;
alter table critere drop column desc_crit;

alter table regle drop column lib_reg;
alter table regle drop column desc_reg;

alter table facteur_critere drop column lib_rel;
alter table facteur_critere drop column desc_rel;


alter table facteur_bline modify tendance number (11,4) DEFAULT 0;

-- LIZ 04/01/2011 --
CREATE TABLE CAQS_USER
(
   ID_USER varchar(255) NOT NULL,
   PASSWORD varchar(255),
   FIRSTNAME varchar(255),
   LASTNAME varchar(255),
   EMAIL varchar(255),
   CREATEDDATE timestamp,
   LASTLOGINTIME timestamp)
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	5 M
  		PCTINCREASE 10 
	)
  TABLESPACE %TABLE_TBS%;

  ALTER TABLE CAQS_USER
 ADD CONSTRAINT CAQS_USERP1
  PRIMARY KEY (ID_USER) 
; 

INSERT INTO CAQS_ACCESS_DEFINITION (ID_ACCESS, LIB_ACCESS) VALUES ('USER_ADMIN_ACCESS', 'Access users administration');
INSERT INTO CAQS_ACCESS_RIGHTS (ID_ACCESS, ID_ROLE) VALUES ('USER_ADMIN_ACCESS', 'ADMINISTRATOR');

-- LI Fri Jan 14 11:41:56 CET 2011 @487 /Internet Time/
CREATE TABLE USER_ROLE (
  ID_USER   varchar (255) NOT NULL,
  ID_ROLE varchar2 (64) NOT NULL)
;

ALTER TABLE USER_ROLE
 ADD CONSTRAINT USER_ROLEP1
  PRIMARY KEY (ID_USER, ID_ROLE) 
;

ALTER TABLE USER_ROLE ADD  CONSTRAINT USER_ROLE_ROLE
 FOREIGN KEY (ID_ROLE) 
  REFERENCES CAQS_ROLE (ID_ROLE) ;
  
ALTER TABLE USER_ROLE ADD  CONSTRAINT USER_ROLE_USER
 FOREIGN KEY (ID_USER) 
  REFERENCES CAQS_USER (ID_USER) ;

