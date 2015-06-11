-- modifications suite à l'intégration du parseur Phoenix de Metaware
alter table METRIQUE modify ID_MET VARCHAR2(64);
alter table METRIQUE modify LIB_MET VARCHAR2(64);

alter table QAMETRIQUE modify ID_MET VARCHAR2(64);

alter table FACTEUR_CRITERE modify ID_CRIT VARCHAR2(64);

alter table CRITERE_USAGE modify ID_CRIT VARCHAR2(64);

alter table CRITERE_BLINE modify ID_CRIT VARCHAR2(64);

alter table POIDS_FACT_CRIT modify ID_CRIT VARCHAR2(64);

alter table REGLE modify ID_CRIT VARCHAR2(64);

alter table CRITERE modify ID_CRIT VARCHAR2(64);
alter table CRITERE modify DESC_CRIT VARCHAR2(768);
