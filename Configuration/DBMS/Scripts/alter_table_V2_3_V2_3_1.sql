ALTER TABLE POIDS_FACT_CRIT DROP  CONSTRAINT CRI_BLINE_POIDS;
ALTER TABLE POIDS_FACT_CRIT DROP  CONSTRAINT FAC_BLINE_POIDS;

ALTER TABLE CRITERE_BLINE
 DROP CONSTRAINT CRITERE_BLINEP1;

ALTER TABLE FACTEUR_BLINE
 DROP CONSTRAINT FACTEUR_BLINEP1;

ALTER TABLE POIDS_FACT_CRIT
 DROP CONSTRAINT POIDS_FACT_CRITP1;

ALTER TABLE CRITERE_BLINE
 ADD CONSTRAINT CRITERE_BLINEP1
  PRIMARY KEY (ID_BLINE, ID_CRIT, ID_ELT) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
; 

ALTER TABLE FACTEUR_BLINE
 ADD CONSTRAINT FACTEUR_BLINEP1
  PRIMARY KEY (ID_BLINE, ID_FAC, ID_ELT) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
; 

ALTER TABLE POIDS_FACT_CRIT
 ADD CONSTRAINT POIDS_FACT_CRITP1
  PRIMARY KEY (BLINE_POIDS, ID_FACT, ID_CRIT, ID_ELT) 
  USING INDEX 
  TABLESPACE %INDEX_TBS%
; 

ALTER TABLE POIDS_FACT_CRIT ADD  CONSTRAINT CRI_BLINE_POIDS
 FOREIGN KEY (BLINE_POIDS, ID_CRIT, ID_ELT) 
  REFERENCES CRITERE_BLINE (ID_BLINE, ID_CRIT, ID_ELT) ;

ALTER TABLE POIDS_FACT_CRIT ADD  CONSTRAINT FAC_BLINE_POIDS
 FOREIGN KEY (BLINE_POIDS, ID_FACT, ID_ELT) 
  REFERENCES FACTEUR_BLINE (ID_BLINE, ID_FAC, ID_ELT) ;

CREATE TABLE CRITERENOTEREPARTITION ( 
  ID_ELT    VARCHAR2 (32)  NOT NULL, 
  ID_BLINE  VARCHAR2 (32)  NOT NULL, 
  ID_CRIT   VARCHAR2 (32)  NOT NULL, 
  SEUIL     INTEGER       NOT NULL, 
  TOTAL     INTEGER       NOT NULL)
   TABLESPACE %TABLE_TBS%
;

ALTER TABLE CRITERENOTEREPARTITION
 ADD CONSTRAINT PK_CRITERENOTEREPARTITION
  PRIMARY KEY (ID_BLINE, ID_CRIT, ID_ELT, SEUIL ) 
  USING INDEX 
  TABLESPACE %INDEX_TBS% 
;

ALTER TABLE CRITERENOTEREPARTITION ADD CONSTRAINT FK_CRITERE_BLINE
 FOREIGN KEY (ID_BLINE, ID_CRIT, ID_ELT)
  REFERENCES CRITERE_BLINE (ID_BLINE, ID_CRIT, ID_ELT);
