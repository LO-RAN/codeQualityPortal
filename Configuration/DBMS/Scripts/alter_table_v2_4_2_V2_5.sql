ALTER TABLE CRITERENOTEREPARTITION MODIFY(ID_ELT VARCHAR2(64));
ALTER TABLE CRITERENOTEREPARTITION MODIFY(ID_CRIT VARCHAR2(64));

ALTER TABLE ELEMENT MODIFY(LIB_ELT VARCHAR2(64));

Alter Table Element Add ID_MAIN_ELT VARCHAR2(64);

Update Element fils
Set id_main_elt=(Select elt_pere
From Elt_links, Element pere
Where pere.id_telt='EA'
And pere.id_elt = elt_pere
And fils.id_elt = elt_fils);

commit;

CREATE INDEX ELTMAINELTIDX ON 
  ELEMENT(ID_MAIN_ELT) 
  TABLESPACE %INDEX_TBS% 
; 

CREATE TABLE LANGUE ( 
  ID  VARCHAR2 (16)  NOT NULL, 
  LIB    VARCHAR2 (32), 
  DESCRIPTION   VARCHAR2 (128))
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE I18N ( 
  TABLE_NAME           VARCHAR2 (64)  NOT NULL, 
  COLUMN_NAME          VARCHAR2 (64)  NOT NULL, 
  ID_TABLE             VARCHAR2 (64)  NOT NULL, 
  ID_LANGUE            VARCHAR2 (16)  NOT NULL, 
  TEXT                 VARCHAR2 (4000)  NOT NULL)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE LANGUE
 ADD CONSTRAINT LANGUEP1
  PRIMARY KEY (ID) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
; 

ALTER TABLE I18N
 ADD CONSTRAINT I18NP1
  PRIMARY KEY (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
; 

ALTER TABLE I18N ADD  CONSTRAINT I18N_FK_LANGUE
 FOREIGN KEY (ID_LANGUE) 
  REFERENCES LANGUE (ID) ;

INSERT INTO LANGUE (ID, LIB, DESCRIPTION) VALUES ('fr', 'Français', 'Français');
INSERT INTO LANGUE (ID, LIB, DESCRIPTION) VALUES ('en', 'English', 'English');
INSERT INTO LANGUE (ID, LIB, DESCRIPTION) VALUES ('de', 'Deutsch', 'Deutsch');
INSERT INTO LANGUE (ID, LIB, DESCRIPTION) VALUES ('es', 'Español', 'Español');
INSERT INTO LANGUE (ID, LIB, DESCRIPTION) VALUES ('it', 'Italiano', 'Italiano');

INSERT INTO OUTILS ( ID_OUTILS, LIB_OUTILS ) VALUES ( 'OPTIMALADVISOR', 'OptimalAdvisor');
INSERT INTO OUTILS ( ID_OUTILS, LIB_OUTILS ) VALUES ( 'CAQS', 'CAQS');

INSERT INTO METRIQUE (ID_MET, LIB_MET, DESC_MET, OUTIL_MET ) VALUES ( 'LOC', 'Nombre de lignes de code', 'Nombre de lignes de code', 'CAQS');
INSERT INTO METRIQUE (ID_MET, LIB_MET, DESC_MET, OUTIL_MET ) VALUES ( 'CLOC', 'Nombre de lignes de commentaire', 'Nombre de lignes de commentaire', 'CAQS');
INSERT INTO METRIQUE (ID_MET, LIB_MET, DESC_MET, OUTIL_MET ) VALUES ( 'FANOUT', 'Fanout', 'Fanout', 'CAQS');
INSERT INTO METRIQUE (ID_MET, LIB_MET, DESC_MET, OUTIL_MET ) VALUES ( 'MAXMCD', 'Max Mcd', 'max Mcd', 'CAQS');

Insert Into I18N Select 'metrique', 'lib', id_met, 'fr', lib_met From METRIQUE;
Insert Into I18N Select 'metrique', 'desc', id_met, 'fr', desc_met From METRIQUE where desc_met is not null;
Insert Into I18N Select 'metrique', 'desc', id_met, 'fr', lib_met From METRIQUE where desc_met is null;

Insert Into I18N Select 'critere', 'lib', id_crit, 'fr', lib_crit From CRITERE;
Insert Into I18N Select 'critere', 'desc', id_crit, 'fr', desc_crit From CRITERE where desc_crit is not null;
Insert Into I18N Select 'critere', 'desc', id_crit, 'fr', lib_crit From CRITERE where desc_crit is null;

Insert Into I18N Select 'facteur', 'lib', id_fact, 'fr', lib_fact From FACTEUR;
Insert Into I18N Select 'facteur', 'desc', id_fact, 'fr', desc_fact From FACTEUR where desc_fact is not null;
Insert Into I18N Select 'facteur', 'desc', id_fact, 'fr', lib_fact From FACTEUR where desc_fact is null;

Insert Into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('critere', 'lib', 'OTHER', 'fr', 'Autre');

Insert Into I18N Select 'outils', 'lib', id_outils, 'fr', lib_outils From OUTILS;

commit;

CREATE TABLE ELEMENT_BASELINE_INFO ( 
  ID_ELT            VARCHAR2 (32)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL, 
  NOTE1        NUMBER (10)  NOT NULL, 
  NOTE2        NUMBER (10)  NOT NULL, 
  NOTE3        NUMBER (10)  NOT NULL, 
  NOTE4        NUMBER (10)  NOT NULL, 
  NBBETTER     NUMBER (10), 
  NBWORST      NUMBER (10), 
  NBSTABLE     NUMBER (10)
)  TABLESPACE %TABLE_TBS%;

ALTER TABLE ELEMENT_BASELINE_INFO
 ADD CONSTRAINT ELEMENT_BASELINE_INFOP1
  PRIMARY KEY (ID_ELT, ID_BLINE) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
; 

Insert Into ELEMENT_BASELINE_INFO Select distinct elt.id_elt, id_bline, id_main_elt, 0, 0, 0, 0, NULL, NULL, NULL From Element elt, Critere_bline cb Where elt.id_elt=cb.id_elt and id_telt in ('CLS','MET');
commit;
Update ELEMENT_BASELINE_INFO e Set note1=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and note_cribl>=1 and note_cribl<2 and just_note_cribl is null);
commit;
Update ELEMENT_BASELINE_INFO e Set note2=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and note_cribl>=2 and note_cribl<3 and just_note_cribl is null);
commit;
Update ELEMENT_BASELINE_INFO e Set note3=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and ((note_cribl>=3 and note_cribl<4) or (just_note_cribl >=3 and just_note_cribl < 4)));
commit;
Update ELEMENT_BASELINE_INFO e Set note4=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and (note_cribl=4 or just_note_cribl = 4));
commit;
Update ELEMENT_BASELINE_INFO e Set nbbetter=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and note_cribl>tendance);
commit;
Update ELEMENT_BASELINE_INFO e Set nbworst=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and note_cribl<tendance);
commit;
Update ELEMENT_BASELINE_INFO e Set nbstable=(Select count(note_cribl) From Critere_bline c Where e.id_elt=c.id_elt and e.id_bline=c.id_bline and note_cribl=tendance);
commit;

Alter Table Qametrique Add LINES VARCHAR2(512);
commit;
