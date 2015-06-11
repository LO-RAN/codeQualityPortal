ALTER TABLE ARCHI_LINK
 ADD CONSTRAINT ARCHI_LINKP1
  PRIMARY KEY (ID_LINK) 
; 

ALTER TABLE BASELINE
 ADD CONSTRAINT BASELINEP1
  PRIMARY KEY (ID_BLINE) 
; 

ALTER TABLE BASELINE_LINKS
 ADD CONSTRAINT BASELINE_LINKSP1
  PRIMARY KEY (PARENT_ID_BLINE, CHILD_ID_BLINE, CHILD_ID_ELT) 
; 

ALTER TABLE CATEGORIE
 ADD CONSTRAINT CATEGORIEP1
  PRIMARY KEY (ID_CAT) 
; 

ALTER TABLE CRITERE
 ADD CONSTRAINT CRITEREP1
  PRIMARY KEY (ID_CRIT) 
; 

ALTER TABLE CRITERE_USAGE
 ADD CONSTRAINT CRITERE_USAGEP1
  PRIMARY KEY (ID_CRIT, ID_USA) 
; 

ALTER TABLE CRITERE_BLINE
 ADD CONSTRAINT CRITERE_BLINEP1
  PRIMARY KEY (ID_BLINE, ID_CRIT, ID_ELT) 
; 

ALTER TABLE DIALECTE
 ADD CONSTRAINT DIALECTEP1
  PRIMARY KEY (ID_DIALECTE) 
; 

ALTER TABLE DROITS
 ADD CONSTRAINT DROITSP1
  PRIMARY KEY (ID_ELT, TYPE_ACCES, ID_PROFIL_USER) 
; 

ALTER TABLE ELEMENT
 ADD CONSTRAINT ELEMENTP1
  PRIMARY KEY (ID_ELT) 
; 

ALTER TABLE ELEMENT_BASELINE_INFO
 ADD CONSTRAINT ELEMENT_BASELINE_INFOP1
  PRIMARY KEY (ID_ELT, ID_BLINE) 
; 

ALTER TABLE ELT_LINKS
 ADD CONSTRAINT ELT_LINKSP1
  PRIMARY KEY (ELT_PERE, ELT_FILS) 
; 

ALTER TABLE FACTEUR
 ADD CONSTRAINT FACTEURP1
  PRIMARY KEY (ID_FACT) 
; 

ALTER TABLE FACTEUR_BLINE
 ADD CONSTRAINT FACTEUR_BLINEP1
  PRIMARY KEY (ID_BLINE, ID_FAC, ID_ELT) 
; 

ALTER TABLE FACTEUR_CRITERE
 ADD CONSTRAINT FACTEUR_CRITEREP1
  PRIMARY KEY (ID_USA, ID_FACT, ID_CRIT) 
; 

ALTER TABLE I18N
 ADD CONSTRAINT I18NP1
  PRIMARY KEY (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE) 
; 

ALTER TABLE JUSTIFICATION
 ADD CONSTRAINT JUSTIFICATIONP1
  PRIMARY KEY (ID_JUST) 
; 

ALTER TABLE LABELLISATION
 ADD CONSTRAINT LABELLISATIONP1
  PRIMARY KEY (ID_LABEL, ID_PRO, ID_BLINE) 
; 

ALTER TABLE LANGAGE
 ADD CONSTRAINT LANGAGEP1
  PRIMARY KEY (ID_LANGAGE) 
; 

ALTER TABLE LANGUE
 ADD CONSTRAINT LANGUEP1
  PRIMARY KEY (ID) 
; 

ALTER TABLE LINK_ELT_BLINE
 ADD CONSTRAINT LINK_ELT_BLIN_PRIMARY
  PRIMARY KEY (LINK_ID) 
; 

ALTER TABLE LINK_REAL
 ADD CONSTRAINT LINK_REAL_PRIMARY
  PRIMARY KEY (ID_LINK) 
; 

ALTER TABLE METRIQUE
 ADD CONSTRAINT METRIQUEP1
  PRIMARY KEY (ID_MET) 
; 

ALTER TABLE NIVEAU
 ADD CONSTRAINT NIVEAUP1
  PRIMARY KEY (ID_NIV) 
; 

ALTER TABLE PACKAGE
 ADD CONSTRAINT PACKAGEP1
  PRIMARY KEY (ID_PACK) 
; 

ALTER TABLE POIDS_FACT_CRIT
 ADD CONSTRAINT POIDS_FACT_CRITP1
  PRIMARY KEY (BLINE_POIDS, ID_FACT, ID_CRIT, ID_ELT) 
; 

ALTER TABLE PROJET
 ADD CONSTRAINT PROJETP1
  PRIMARY KEY (ID_PRO) 
; 

ALTER TABLE QAMETRIQUE
 ADD CONSTRAINT QAMETRIQUEP1
  PRIMARY KEY (ID_ELT, ID_BLINE, ID_MET) 
; 

ALTER TABLE REGLE
 ADD CONSTRAINT REGLEP1
  PRIMARY KEY (ID_USA, ID_CRIT, ID_MET) 
; 

ALTER TABLE SEUIL
 ADD CONSTRAINT SEUILP1
  PRIMARY KEY (ID_SEUIL, ID_MET) 
; 

ALTER TABLE SEVERITE
 ADD CONSTRAINT SEVERITEP1
  PRIMARY KEY (ID_SEV) 
; 

ALTER TABLE STEREOTYPE
  ADD CONSTRAINT STEREOTYPEP1
  PRIMARY KEY (ID_STEREOTYPE) 
;

ALTER TABLE TECHNOLOGIE
 ADD CONSTRAINT TECHNOLOGIEP1
  PRIMARY KEY (ID_TEC) 
; 

ALTER TABLE TYPE_ELEMENT
 ADD CONSTRAINT TYPE_ELEMENTP1
  PRIMARY KEY (ID_TELT) 
; 

ALTER TABLE TYPE_FICHE
 ADD CONSTRAINT TYPE_FICHEP1
  PRIMARY KEY (ID_TFICHE) 
; 

ALTER TABLE TYPE_LABELLISATION
 ADD CONSTRAINT TYPE_LABELLISATIONP1
  PRIMARY KEY (ID_TYPLAB) 
; 

ALTER TABLE TYPE_METRIQUE
 ADD CONSTRAINT TYPE_METRIQUEP1
  PRIMARY KEY (ID_TMET) 
; 

ALTER TABLE MODELE
 ADD CONSTRAINT MODELEP1
  PRIMARY KEY (ID_USA) 
; 

ALTER TABLE OUTILS
 ADD CONSTRAINT OUTILSP1
  PRIMARY KEY (ID_OUTILS) 
; 

ALTER TABLE CRITERENOTEREPARTITION
 ADD CONSTRAINT PK_CRITERENOTEREPARTITION
  PRIMARY KEY (ID_BLINE, ID_CRIT, ID_ELT, SEUIL ) 
;

ALTER TABLE VOLUMETRY
 ADD CONSTRAINT VOLUMETRYP1
  PRIMARY KEY (ID_ELT, ID_TELT, ID_BLINE) 
;

ALTER TABLE OUTILS_MODELE
 ADD CONSTRAINT OUTILS_MODELEP1
  PRIMARY KEY (ID_OUTILS, ID_USA) 
;

ALTER TABLE CAQS_ROLE
 ADD CONSTRAINT CAQS_ROLEP1
  PRIMARY KEY (ID_ROLE) 
; 

ALTER TABLE CAQS_ACCESS_DEFINITION
 ADD CONSTRAINT CAQS_ACCESS_DEFINITIONP1
  PRIMARY KEY (ID_ACCESS) 
; 

ALTER TABLE CAQS_ACCESS_RIGHTS
 ADD CONSTRAINT CAQS_ACCESS_RIGHTSP1
  PRIMARY KEY (ID_ACCESS, ID_ROLE) 
; 

ALTER TABLE CAQS_TASKS
 ADD CONSTRAINT TASKSP1
  PRIMARY KEY (ID_TASK) 
; 

ALTER TABLE CAQS_MESSAGES
 ADD CONSTRAINT CAQS_MESSAGESP1
  PRIMARY KEY (ID_MESSAGE) 
; 

ALTER TABLE USER_SETTINGS
 ADD CONSTRAINT USER_SETTINGSP1
  PRIMARY KEY (USER_ID, SETTING_ID, SETTING_VALUE) 
; 

ALTER TABLE SETTINGS
 ADD CONSTRAINT SETTINGSP1
  PRIMARY KEY (SETTING_ID) 
; 

ALTER TABLE SETTINGS_VALUES
 ADD CONSTRAINT SETTINGS_VALUESP1
  PRIMARY KEY (SETTING_ID, SETTING_VALUE) 
; 

ALTER TABLE ACTION_PLAN
 ADD CONSTRAINT ACTION_PLANP1
  PRIMARY KEY (ID_ACTION_PLAN) 
; 

ALTER TABLE ACTION_PLAN_UNIT
 ADD CONSTRAINT ACTION_PLAN_UNITP1
  PRIMARY KEY (UNIT_ID) 
; 

ALTER TABLE COPY_PASTE_REF
 ADD CONSTRAINT COPY_PASTE_REFP1
  PRIMARY KEY (ID_COPY, ID_BLINE, ID_ELT, LINE) 
; 

ALTER TABLE ACTION_PLAN_CRITERION
 ADD CONSTRAINT ACTION_PLAN_CRITERIONP1
  PRIMARY KEY (ID_ACTION_PLAN, ID_CRIT) 
;

ALTER TABLE STATS
 ADD CONSTRAINT STATSP1
  PRIMARY KEY (PERIOD) 
; 

ALTER TABLE CAQS_USER
 ADD CONSTRAINT CAQS_USERP1
  PRIMARY KEY (ID_USER) 
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

ALTER TABLE ARCHI_LINK ADD  CONSTRAINT DU_PROJET_ARCHILINK
 FOREIGN KEY (ID_PROJ) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE ARCHI_LINK ADD  CONSTRAINT FROM_LINK
 FOREIGN KEY (ID_FROM) 
  REFERENCES PACKAGE (ID_PACK) ;

ALTER TABLE ARCHI_LINK ADD  CONSTRAINT TO_LINK
 FOREIGN KEY (ID_TO) 
  REFERENCES PACKAGE (ID_PACK) ;

ALTER TABLE BASELINE ADD  CONSTRAINT PROJET_BASELINE
 FOREIGN KEY (PRO_BLINRE) 
  REFERENCES PROJET (ID_PRO) ;

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_CHILD
 FOREIGN KEY (CHILD_ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_PARENT
 FOREIGN KEY (PARENT_ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE BASELINE_LINKS ADD  CONSTRAINT BASELINE_LINKS_CHILD_ELT
 FOREIGN KEY (CHILD_ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE CRITERE_BLINE ADD  CONSTRAINT BL_CRI_BLINE
 FOREIGN KEY (ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE CRITERE_BLINE ADD  CONSTRAINT CRI_CRI_BLINE
 FOREIGN KEY (ID_CRIT) 
  REFERENCES CRITERE (ID_CRIT) ;

ALTER TABLE CRITERE_BLINE ADD  CONSTRAINT ELT_CRITERE_BLINE
 FOREIGN KEY (ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE CRITERE_BLINE ADD  CONSTRAINT JUS_CRI_BLINE
 FOREIGN KEY (ID_JUST) 
  REFERENCES JUSTIFICATION (ID_JUST) ;

ALTER TABLE DIALECTE ADD  CONSTRAINT LANGAGE_DIALECTE
 FOREIGN KEY (ID_LANGAGE) 
  REFERENCES LANGAGE (ID_LANGAGE) ;

ALTER TABLE DROITS ADD  CONSTRAINT ELEMENT_DROITS
 FOREIGN KEY (ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE ELEMENT ADD  CONSTRAINT PACKAGE_ELT
 FOREIGN KEY (ID_PACK) 
  REFERENCES PACKAGE (ID_PACK) ;

ALTER TABLE ELEMENT ADD  CONSTRAINT PROJET_ELEMENT
 FOREIGN KEY (ID_PRO) 
  REFERENCES PROJET (ID_PRO) ;

ALTER TABLE ELEMENT ADD  CONSTRAINT MODELE_ELEMENT
 FOREIGN KEY (ID_USA) 
  REFERENCES MODELE (ID_USA) ;

ALTER TABLE ELEMENT ADD  CONSTRAINT TYPE_ELT_ELT
 FOREIGN KEY (ID_TELT) 
  REFERENCES TYPE_ELEMENT (ID_TELT) ;

ALTER TABLE ELEMENT ADD  CONSTRAINT STEREOTYPE_ELEMENT
 FOREIGN KEY (ID_STEREOTYPE)
  REFERENCES STEREOTYPE (ID_STEREOTYPE) ;

ALTER TABLE ELT_LINKS ADD  CONSTRAINT ELT_FILS
 FOREIGN KEY (ELT_FILS) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE ELT_LINKS ADD  CONSTRAINT ELT_PERE
 FOREIGN KEY (ELT_PERE) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE FACTEUR_BLINE ADD  CONSTRAINT BLINE_FAC_BLINE
 FOREIGN KEY (ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE FACTEUR_BLINE ADD  CONSTRAINT ELT_FACTEUR_BLINE
 FOREIGN KEY (ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE FACTEUR_BLINE ADD  CONSTRAINT FAC_FAC_BLINE
 FOREIGN KEY (ID_FAC) 
  REFERENCES FACTEUR (ID_FACT) ;

ALTER TABLE FACTEUR_BLINE ADD  CONSTRAINT LAB_FAC_BLINE
 FOREIGN KEY (ID_LABEL, ID_PRO, ID_BLINE) 
  REFERENCES LABELLISATION (ID_LABEL, ID_PRO, ID_BLINE) ;

ALTER  TABLE Facteur_critere ADD CONSTRAINT Facteur_critere_Facteur 
 FOREIGN KEY (id_fact) 
 REFERENCES Facteur (Id_fact);

ALTER TABLE JUSTIFICATION ADD  CONSTRAINT JUSTIFICATION_FK_JUSTLINK
 FOREIGN KEY (JUST_LINK) 
  REFERENCES JUSTIFICATION (ID_JUST) ;

ALTER TABLE I18N ADD  CONSTRAINT I18N_FK_LANGUE
 FOREIGN KEY (ID_LANGUE) 
  REFERENCES LANGUE (ID) ;

ALTER TABLE LABELLISATION ADD  CONSTRAINT TYPLAB_LABEL
 FOREIGN KEY (ID_TYPLAB) 
  REFERENCES TYPE_LABELLISATION (ID_TYPLAB) ;

ALTER TABLE LINK_REAL ADD  CONSTRAINT LINK_REAL_FK_BLINE
 FOREIGN KEY (ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE LINK_REAL ADD  CONSTRAINT LINK_REAL_FK_ELT
 FOREIGN KEY (ID_PROJ) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE METRIQUE ADD  CONSTRAINT CAT_MET
 FOREIGN KEY (CAT_MET) 
  REFERENCES CATEGORIE (ID_CAT) ;

ALTER TABLE METRIQUE ADD  CONSTRAINT TEC_METRIQUE
 FOREIGN KEY (TECHNO_MET) 
  REFERENCES TECHNOLOGIE (ID_TEC) ;

ALTER TABLE METRIQUE ADD  CONSTRAINT TYPE_MET_METRIQUE
 FOREIGN KEY (TYPE_MET) 
  REFERENCES TYPE_METRIQUE (ID_TMET) ;

ALTER TABLE METRIQUE ADD  CONSTRAINT OUTILS_METRIQUE
 FOREIGN KEY (OUTIL_MET) 
  REFERENCES OUTILS (ID_OUTILS) ;

ALTER TABLE PACKAGE ADD  CONSTRAINT DU_PROJET
 FOREIGN KEY (ID_PROJ) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE PACKAGE ADD  CONSTRAINT NIV_PACKAGE
 FOREIGN KEY (ID_NIV) 
  REFERENCES NIVEAU (ID_NIV) ;

ALTER TABLE POIDS_FACT_CRIT ADD  CONSTRAINT BLINE_POIDS
 FOREIGN KEY (BLINE_POIDS) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER TABLE POIDS_FACT_CRIT ADD  CONSTRAINT CRI_BLINE_POIDS
 FOREIGN KEY (BLINE_POIDS, ID_CRIT, ID_ELT) 
  REFERENCES CRITERE_BLINE (ID_BLINE, ID_CRIT, ID_ELT) ;

ALTER TABLE POIDS_FACT_CRIT ADD  CONSTRAINT FAC_BLINE_POIDS
 FOREIGN KEY (BLINE_POIDS, ID_FACT, ID_ELT) 
  REFERENCES FACTEUR_BLINE (ID_BLINE, ID_FAC, ID_ELT) ;

ALTER TABLE QAMETRIQUE ADD  CONSTRAINT ELT_QAMETRIQUE
 FOREIGN KEY (ID_ELT) 
  REFERENCES ELEMENT (ID_ELT) ;

ALTER TABLE QAMETRIQUE ADD  CONSTRAINT JUS_QAMETRIQUE
 FOREIGN KEY (JUST_MET) 
  REFERENCES JUSTIFICATION (ID_JUST) ;

ALTER TABLE QAMETRIQUE ADD  CONSTRAINT MET_QAMETRIQUE
 FOREIGN KEY (ID_MET) 
  REFERENCES METRIQUE (ID_MET) ;

ALTER TABLE QAMETRIQUE ADD  CONSTRAINT QAMETRIQUE_BLINE
 FOREIGN KEY (ID_BLINE) 
  REFERENCES BASELINE (ID_BLINE) ;

ALTER  TABLE Regle ADD CONSTRAINT Regle_Metrique
 FOREIGN KEY (id_met) 
 REFERENCES Metrique (Id_met);

ALTER  TABLE Regle ADD CONSTRAINT regle_critere_usage 
 FOREIGN KEY (id_crit, id_usa) 
 REFERENCES Critere_usage (id_crit, id_usa) ON DELETE CASCADE;

ALTER  TABLE Facteur_critere ADD CONSTRAINT facteur_critere_critere_usage 
 FOREIGN KEY (id_crit, id_usa) 
 REFERENCES Critere_usage (id_crit, id_usa) ON DELETE CASCADE;

ALTER TABLE SEUIL ADD  CONSTRAINT METRIQUE_SEUIL
 FOREIGN KEY (ID_MET) 
  REFERENCES METRIQUE (ID_MET) ;

ALTER  TABLE Critere_usage ADD CONSTRAINT Critere_usage_Critere 
 FOREIGN KEY (id_crit) 
 REFERENCES Critere (Id_crit);

ALTER TABLE CRITERE_USAGE ADD  CONSTRAINT CRITERE_USAGE_TELT
 FOREIGN KEY (ID_TELT) 
  REFERENCES TYPE_ELEMENT (ID_TELT);

ALTER TABLE CRITERE_USAGE ADD CONSTRAINT CRITERE_USAGE_USAGE
 FOREIGN KEY (ID_USA) 
  REFERENCES MODELE (ID_USA) ON DELETE CASCADE;

ALTER TABLE CRITERENOTEREPARTITION ADD CONSTRAINT FK_CRITERE_BLINE
 FOREIGN KEY (ID_BLINE, ID_CRIT, ID_ELT)
  REFERENCES CRITERE_BLINE (ID_BLINE, ID_CRIT, ID_ELT);

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_ELEMENT
 FOREIGN KEY (ID_ELT)
  REFERENCES ELEMENT (ID_ELT);

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_TYPE_ELEMENT
 FOREIGN KEY (ID_TELT)
  REFERENCES TYPE_ELEMENT (ID_TELT);

ALTER TABLE VOLUMETRY ADD CONSTRAINT FK_VOLUMETRY_BASELINE
 FOREIGN KEY (ID_BLINE)
  REFERENCES BASELINE (ID_BLINE);

ALTER TABLE CAQS_ACCESS_RIGHTS ADD  CONSTRAINT CAQS_ACCESS_RIGHTS_ACCESS
 FOREIGN KEY (ID_ACCESS) 
  REFERENCES CAQS_ACCESS_DEFINITION (ID_ACCESS) ;

ALTER TABLE CAQS_ACCESS_RIGHTS ADD  CONSTRAINT CAQS_ACCESS_RIGHTS_ROLE
 FOREIGN KEY (ID_ROLE) 
  REFERENCES CAQS_ROLE (ID_ROLE) ;

ALTER TABLE SETTINGS_VALUES ADD  CONSTRAINT SETTINGS_VALUES_ID
 FOREIGN KEY (SETTING_ID) 
  REFERENCES SETTINGS (SETTING_ID) ;

  ALTER TABLE USER_SETTINGS ADD  CONSTRAINT USER_SETTINGS_ID
 FOREIGN KEY (SETTING_ID) 
  REFERENCES SETTINGS (SETTING_ID) ;

ALTER TABLE COPY_PASTE_REF ADD CONSTRAINT FK_COPY_PASTE_REF_ELEMENT
 FOREIGN KEY (ID_ELT)
  REFERENCES ELEMENT (ID_ELT);

ALTER TABLE COPY_PASTE_REF ADD CONSTRAINT FK_COPY_PASTE_REF_BASELINE
 FOREIGN KEY (ID_BLINE)
  REFERENCES BASELINE (ID_BLINE);
  
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
  
