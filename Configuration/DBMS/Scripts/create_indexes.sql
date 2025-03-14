CREATE INDEX BASELINEI2 ON 
  BASELINE(PRO_BLINRE) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX CRITERE_BLINEI1 ON 
  CRITERE_BLINE(ID_BLINE, ID_ELT) 
   STORAGE (INITIAL 40M NEXT 40M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX CRITERE_BLINEI6 ON 
  CRITERE_BLINE(ID_JUST) 
   STORAGE (INITIAL 128K NEXT 128K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX CRITERE_BLINEI8 ON 
  CRITERE_BLINE(ID_BLINE, ID_CRIT, NOTE_CRIBL) 
   STORAGE (INITIAL 25M NEXT 25M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX DIALECTEI2 ON 
  DIALECTE(ID_LANGAGE) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX DROITSI1 ON 
  DROITS(ID_PROFIL_USER, TYPE_ACCES) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX DROITSI2 ON 
  DROITS(TYPE_ACCES, ID_PROFIL_USER) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX DROITSI3 ON 
  DROITS(ID_ELT) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELEMENTI2 ON 
  ELEMENT(ID_PRO) 
   STORAGE (INITIAL 3M NEXT 3M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELEMENTI3 ON 
  ELEMENT(ID_PACK) 
   STORAGE (INITIAL 192K NEXT 192K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELEMENTI4 ON 
  ELEMENT(ID_TELT) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELTMAINELTIDX ON 
  ELEMENT(ID_MAIN_ELT) 
   STORAGE (INITIAL 3M NEXT 3M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELTLIBIDX ON 
  ELEMENT(LIB_ELT) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELTDESCIDX ON 
  ELEMENT(ID_MAIN_ELT, DESC_ELT, ID_ELT) 
   STORAGE (INITIAL 10M NEXT 10M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELTPACKTYPEIDX ON 
  ELEMENT(ID_PACK, ID_TELT) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELT_LINKSI2 ON 
  ELT_LINKS(ELT_FILS) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELT_LINKSI3 ON 
  ELT_LINKS(ELT_PERE) 
   STORAGE (INITIAL 3M NEXT 3M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELT_LINKS_TYPE ON 
  ELT_LINKS(TYPE) 
   STORAGE (INITIAL 1M NEXT 1M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX ELEMENT_BASELINE_INFO_I1 ON 
  ELEMENT_BASELINE_INFO(ID_BLINE, ID_MAIN_ELT, ID_ELT) 
   STORAGE (INITIAL 6M NEXT 6M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX FACTEUR_BLINEI2 ON 
  FACTEUR_BLINE(ID_FAC) 
   STORAGE (INITIAL 6M NEXT 6M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX FACTEUR_BLINEI3 ON 
  FACTEUR_BLINE(ID_PRO) 
   STORAGE (INITIAL 15M NEXT 15M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX FACTEUR_BLINEI4 ON 
  FACTEUR_BLINE(ID_BLINE) 
   STORAGE (INITIAL 6M NEXT 6M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX FACTEUR_BLINEI5 ON 
  FACTEUR_BLINE(ID_ELT) 
   STORAGE (INITIAL 12M NEXT 12M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX FACTEUR_BLINEI7 ON 
  FACTEUR_BLINE(ID_LABEL, ID_PRO, ID_BLINE) 
   STORAGE (INITIAL 15M NEXT 15M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX JUSTIFICATIONSTATUT ON 
  JUSTIFICATION(STATUT_JUST) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LABELLISATIONI2 ON 
  LABELLISATION(ID_TYPLAB) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LINK_ELT_BLINEI1 ON 
  LINK_ELT_BLINE(REAL_LINK_ID) 
   STORAGE (INITIAL 192K NEXT 192K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LINK_ELT_BLINE_FROM ON 
  LINK_ELT_BLINE(ELT_FROM_ID) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LINK_ELT_BLINE_TO ON 
  LINK_ELT_BLINE(ELT_TO_ID) 
   STORAGE (INITIAL 2M NEXT 2M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX LINK_REALI1 ON 
  LINK_REAL(ID_PROJ, ID_BLINE) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX METRIQUEI2 ON 
  METRIQUE(CAT_MET) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX METRIQUEI4 ON 
  METRIQUE(TECHNO_MET) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX METRIQUEI5 ON 
  METRIQUE(TYPE_MET) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX PACKAGEI2 ON 
  PACKAGE(ID_NIV) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX PACKAGEI3 ON 
  PACKAGE(ID_PROJ) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX POIDS_FACT_CRITI1 ON 
  POIDS_FACT_CRIT(BLINE_POIDS, ID_ELT, ID_CRIT) 
   STORAGE (INITIAL 1M NEXT 1M PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX POIDS_FACT_CRITI2 ON 
  POIDS_FACT_CRIT(ID_FACT) 
   STORAGE (INITIAL 384K NEXT 384K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX POIDS_FACT_CRITI3 ON 
  POIDS_FACT_CRIT(ID_CRIT) 
   STORAGE (INITIAL 384K NEXT 384K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX POIDS_FACT_CRITI4 ON 
  POIDS_FACT_CRIT(BLINE_POIDS) 
   STORAGE (INITIAL 384K NEXT 384K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX POIDS_FACT_CRITI5 ON 
  POIDS_FACT_CRIT(BLINE_POIDS, ID_CRIT, ID_ELT) 
   STORAGE (INITIAL 768K NEXT 768K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX POIDS_FACT_CRITI6 ON 
  POIDS_FACT_CRIT(BLINE_POIDS, ID_FACT, ID_ELT) 
   STORAGE (INITIAL 960K NEXT 960K PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX QAMETRIQUEI2 ON 
  QAMETRIQUE(ID_BLINE) 
   STORAGE (INITIAL 15M NEXT 15M PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX QAMETRIQUEI3 ON 
  QAMETRIQUE(ID_ELT) 
   STORAGE (INITIAL 20M NEXT 20M PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX QAMETRIQUEI4 ON 
  QAMETRIQUE(JUST_MET) 
   STORAGE (INITIAL 192K NEXT 192K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX QAMETRIQUEI5 ON 
  QAMETRIQUE(ID_BLINE, ID_MET) 
   STORAGE (INITIAL 20M NEXT 20M PCTINCREASE 0) TABLESPACE %INDEX_TBS%
; 

CREATE INDEX REGLEI2 ON 
  REGLE(ID_MET) 
   STORAGE (INITIAL 128K NEXT 128K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX SEUILI2 ON 
  SEUIL(ID_MET) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

CREATE INDEX SEUILI3 ON 
  SEUIL(USAGE_SEUIL) 
   STORAGE (INITIAL 64K NEXT 64K PCTINCREASE 0) TABLESPACE %INDEX_TBS% 
; 

