delete from elt_links where type='L' and elt_pere in (select id_elt from element where id_telt='EA') and elt_fils in (Select id_elt from element where id_telt='MET');
commit;

insert into TYPE_ELEMENT (ID_TELT, LIB_TELT, DESC_TELT) VALUES ('PKG', 'Package', 'Package');
commit;

insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'PKG', 'fr', 'Package');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'PKG', 'en', 'Package');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'CLS', 'fr', 'Classe');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'CLS', 'en', 'Class');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'MET', 'fr', 'Méthode');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'MET', 'en', 'Method');
commit;

CREATE TABLE VOLUMETRY ( 
  ID_ELT    VARCHAR2 (64)  NOT NULL, 
  ID_TELT   VARCHAR2 (32)  NOT NULL, 
  ID_BLINE  VARCHAR2 (32)  NOT NULL, 
  TOTAL     INTEGER       NOT NULL, 
  CREATED   INTEGER       NOT NULL, 
  DELETED   INTEGER       NOT NULL)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE VOLUMETRY
 ADD CONSTRAINT VOLUMETRYP1
  PRIMARY KEY (ID_ELT, ID_TELT, ID_BLINE) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
;

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_ELEMENT
 FOREIGN KEY (ID_ELT)
  REFERENCES ELEMENT (ID_ELT);

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_TYPE_ELEMENT
 FOREIGN KEY (ID_TELT)
  REFERENCES TYPE_ELEMENT (ID_TELT);

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_BASELINE
 FOREIGN KEY (ID_BLINE)
  REFERENCES BASELINE (ID_BLINE);

INSERT INTO VOLUMETRY (ID_ELT, ID_TELT, ID_BLINE, TOTAL, CREATED, DELETED)
SELECT elt.ID_ELT, 'MET', qa1.ID_BLINE, qa1.VALBRUTE_QAMET, qa2.VALBRUTE_QAMET, qa3.VALBRUTE_QAMET
FROM Element elt, Qametrique qa1, Qametrique qa2, Qametrique qa3
WHERE elt.ID_ELT = qa1.id_elt
AND elt.ID_ELT = qa2.ID_ELT
AND elt.ID_ELT = qa3.ID_ELT
AND elt.ID_TELT = 'EA'
AND qa1.ID_MET = 'NUMBEROFMETHODS'
AND qa2.ID_MET = 'NBCREATEDMET'
AND qa3.ID_MET = 'NBDELETEDMET'
AND qa1.ID_BLINE = qa2.ID_BLINE
AND qa1.ID_BLINE = qa3.ID_BLINE
;
commit;

INSERT INTO VOLUMETRY (ID_ELT, ID_TELT, ID_BLINE, TOTAL, CREATED, DELETED)
SELECT elt.ID_ELT, 'CLS', qa1.ID_BLINE, qa1.VALBRUTE_QAMET, qa2.VALBRUTE_QAMET, qa3.VALBRUTE_QAMET
FROM Element elt, Qametrique qa1, Qametrique qa2, Qametrique qa3
WHERE elt.ID_ELT = qa1.id_elt
AND elt.ID_ELT = qa2.ID_ELT
AND elt.ID_ELT = qa3.ID_ELT
AND elt.ID_TELT = 'EA'
AND qa1.ID_MET = 'NUMBEROFCLASSES'
AND qa2.ID_MET = 'NBCREATEDCLS'
AND qa3.ID_MET = 'NBDELETEDCLS'
AND qa1.ID_BLINE = qa2.ID_BLINE
AND qa1.ID_BLINE = qa3.ID_BLINE
;
commit;

INSERT INTO METRIQUE (ID_MET,LIB_MET,DESC_MET,TYPE_MET,CAT_MET,OUTIL_MET,TECHNO_MET,DINST_MET,DMAJ_MET,DAPPLICATION_MET,DPEREMPTION_MET) VALUES ('ADPR_CLASS_LEVEL','ADPR class level','ADPR class level',null,null,'OPTIMALADVISOR',null,null,null,null,null);
INSERT INTO METRIQUE (ID_MET,LIB_MET,DESC_MET,TYPE_MET,CAT_MET,OUTIL_MET,TECHNO_MET,DINST_MET,DMAJ_MET,DAPPLICATION_MET,DPEREMPTION_MET) VALUES ('SIGNATURE_INTERFACE_USE','Signature Interface Use','Signature Interface Use',null,null,'OPTIMALADVISOR',null,null,null,null,null);
INSERT INTO METRIQUE (ID_MET,LIB_MET,DESC_MET,TYPE_MET,CAT_MET,OUTIL_MET,TECHNO_MET,DINST_MET,DMAJ_MET,DAPPLICATION_MET,DPEREMPTION_MET) VALUES ('NORMALIZED_DISTANCE','Normalized Distance','Normalized Distance',null,null,'OPTIMALADVISOR',null,null,null,null,null);
commit;

INSERT INTO CRITERE (ID_CRIT,LIB_CRIT,DESC_CRIT,DINST_CRIT,DMAJ_CRIT,DAPPLICATION_CRIT,DPEREMPTION_CRIT) VALUES ('SIGNATURE_INTERFACE_USE','Signature Interface Use','The percentage of interfaces in non-primitive parameters of public methods, exluding parameters whose types are library classes.',null,null,null,null);
INSERT INTO CRITERE (ID_CRIT,LIB_CRIT,DESC_CRIT,DINST_CRIT,DMAJ_CRIT,DAPPLICATION_CRIT,DPEREMPTION_CRIT) VALUES ('NORMALIZED_DISTANCE','Normalized Distance','Packages with a high normalized distance (close to 1) are candidates for refactoring. They are either not abstract enough or they depend to much on other a classes.',null,null,null,null);
commit;

UPDATE critere_usage SET ID_TELT='PKG' WHERE ID_CRIT='CYCLICDEPENDENCY' AND ID_USA='JAVA-RULES';
DELETE FROM regle WHERE id_crit='CYCLICDEPENDENCY' AND ID_USA='JAVA-RULES';
INSERT INTO regle (ID_CRIT,ID_MET,ID_USA,LIB_REG,DESC_REG,DINST_REG,DMAJ_REG) VALUES ('CYCLICDEPENDENCY','ADPR_CLASS_LEVEL','JAVA-RULES',null,null,null,null);
commit;

INSERT INTO critere_usage (ID_CRIT,ID_USA,ID_TELT) VALUES ('SIGNATURE_INTERFACE_USE','JAVA-RULES','PKG');
INSERT INTO critere_usage (ID_CRIT,ID_USA,ID_TELT) VALUES ('NORMALIZED_DISTANCE','JAVA-RULES','PKG');
commit;
INSERT INTO regle (ID_CRIT,ID_MET,ID_USA,LIB_REG,DESC_REG,DINST_REG,DMAJ_REG) VALUES ('SIGNATURE_INTERFACE_USE','SIGNATURE_INTERFACE_USE','JAVA-RULES',null,null,null,null);
INSERT INTO regle (ID_CRIT,ID_MET,ID_USA,LIB_REG,DESC_REG,DINST_REG,DMAJ_REG) VALUES ('NORMALIZED_DISTANCE','NORMALIZED_DISTANCE','JAVA-RULES',null,null,null,null);
commit;
INSERT INTO facteur_critere (ID_USA,ID_FACT,ID_CRIT,LIB_REL,DESC_REL,POIDS,DINST_REG,DMAJ_REG) VALUES ('JAVA-RULES','PERT','SIGNATURE_INTERFACE_USE',null,null,1,null,null);
INSERT INTO facteur_critere (ID_USA,ID_FACT,ID_CRIT,LIB_REL,DESC_REL,POIDS,DINST_REG,DMAJ_REG) VALUES ('JAVA-RULES','PERT','NORMALIZED_DISTANCE',null,null,1,null,null);
INSERT INTO facteur_critere (ID_USA,ID_FACT,ID_CRIT,LIB_REL,DESC_REL,POIDS,DINST_REG,DMAJ_REG) VALUES ('JAVA-RULES','EVOL','SIGNATURE_INTERFACE_USE',null,null,1,null,null);
INSERT INTO facteur_critere (ID_USA,ID_FACT,ID_CRIT,LIB_REL,DESC_REL,POIDS,DINST_REG,DMAJ_REG) VALUES ('JAVA-RULES','EVOL','NORMALIZED_DISTANCE',null,null,1,null,null);
INSERT INTO facteur_critere (ID_USA,ID_FACT,ID_CRIT,LIB_REL,DESC_REL,POIDS,DINST_REG,DMAJ_REG) VALUES ('JAVA-RULES','REUT','NORMALIZED_DISTANCE',null,null,1,null,null);
commit;

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','ADPR_CLASS_LEVEL','fr','ADPR class level');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','ADPR_CLASS_LEVEL','fr','ADPR class level');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','ADPR_CLASS_LEVEL','en','ADPR class level');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','ADPR_CLASS_LEVEL','en','ADPR class level');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','SIGNATURE_INTERFACE_USE','fr','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','SIGNATURE_INTERFACE_USE','fr','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','SIGNATURE_INTERFACE_USE','en','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','SIGNATURE_INTERFACE_USE','en','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','NORMALIZED_DISTANCE','fr','Normalized Distance');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','NORMALIZED_DISTANCE','fr','Normalized Distance');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','lib','NORMALIZED_DISTANCE','en','Normalized Distance');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('metrique','desc','NORMALIZED_DISTANCE','en','Normalized Distance');

INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','lib','SIGNATURE_INTERFACE_USE','fr','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','desc','SIGNATURE_INTERFACE_USE','fr','The percentage of interfaces in non-primitive parameters of public methods, exluding parameters whose types are library classes.');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','lib','SIGNATURE_INTERFACE_USE','en','Signature Interface Use');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','desc','SIGNATURE_INTERFACE_USE','en','The percentage of interfaces in non-primitive parameters of public methods, exluding parameters whose types are library classes.');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','lib','NORMALIZED_DISTANCE','fr','Normalized Distance');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','desc','NORMALIZED_DISTANCE','fr','Packages with a high normalized distance (close to 1) are candidates for refactoring. They are either not abstract enough or they depend to much on other a classes.');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','lib','NORMALIZED_DISTANCE','en','Normalized Distance');
INSERT INTO I18N (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('critere','desc','NORMALIZED_DISTANCE','en','Packages with a high normalized distance (close to 1) are candidates for refactoring. They are either not abstract enough or they depend to much on other a classes.');
commit;

drop index CRITERE_BLINEI2;
drop index CRITERE_BLINEI3;
drop index CRITERE_BLINEI4;
drop index CRITERE_BLINEI5;
drop index CRITERE_BLINEI7;
commit;

ALTER TABLE ELEMENT MODIFY(LIB_ELT VARCHAR2(128));
commit; 

CREATE INDEX LINK_ELT_BLINE_FROM ON 
  LINK_ELT_BLINE(ELT_FROM_ID) 
  TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LINK_ELT_BLINE_TO ON 
  LINK_ELT_BLINE(ELT_TO_ID) 
  TABLESPACE %INDEX_TBS% 
;