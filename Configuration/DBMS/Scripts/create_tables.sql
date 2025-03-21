CREATE TABLE ARCHI_LINK ( 
  ID_LINK  VARCHAR2 (32)  NOT NULL, 
  ID_FROM  VARCHAR2 (32)  NOT NULL, 
  ID_TO    VARCHAR2 (32)  NOT NULL, 
  ID_PROJ  VARCHAR2 (64)  NOT NULL,
  TYPE     NUMBER   (12))
  STORAGE
	( 	INITIAL 	512 K
  		NEXT 	  	512 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE BASELINE ( 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  TYPE_BLINE          VARCHAR2 (32), 
  LIB_BLINE           VARCHAR2 (32), 
  DESC_BLINE          VARCHAR2 (128), 
  PRO_BLINRE          VARCHAR2 (32)  NOT NULL, 
  DINST_BLINE         DATE, 
  DMAJ_BLINE          DATE, 
  DAPPLICATION_BLINE  DATE, 
  DPREMPTION_BLINE    DATE, 
  CUSER_BLINE         VARCHAR2 (16))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE CATEGORIE ( 
  ID_CAT            VARCHAR2 (32)  NOT NULL, 
  LIB_CAT           VARCHAR2 (32), 
  DESC_CAT          VARCHAR2 (128), 
  CUSER_CAT         VARCHAR2 (16), 
  DINST_CAT         DATE, 
  DMAJ_CAT          DATE, 
  DAPPLICATION_CAT  DATE, 
  DPEREMPTION_CAT   DATE)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE CRITERE ( 
  ID_CRIT            VARCHAR2 (64)  NOT NULL, 
  LIB_CRIT           VARCHAR2 (96), 
  DESC_CRIT          VARCHAR2 (768), 
  DINST_CRIT         DATE, 
  DMAJ_CRIT          DATE, 
  DAPPLICATION_CRIT  DATE, 
  DPEREMPTION_CRIT   DATE)
  STORAGE
	( 	INITIAL 	128 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE CRITERE_USAGE ( 
  ID_CRIT  VARCHAR2 (64)  NOT NULL, 
  ID_USA   VARCHAR2 (32)  NOT NULL, 
  ID_TELT  VARCHAR2 (32)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
; 

CREATE TABLE CRITERE_BLINE ( 
  ID_CRIT          VARCHAR2 (64)  NOT NULL, 
  ID_BLINE         VARCHAR2 (32)  NOT NULL, 
  ID_PRO           VARCHAR2 (32)  NOT NULL, 
  ID_ELT           VARCHAR2 (64)  NOT NULL, 
  DINST_CRIBL      DATE, 
  DMAJ_CRIBL       DATE, 
  NOTE_CRIBL       NUMBER (11,4), 
  ID_JUST          VARCHAR2 (32), 
  JUST_NOTE_CRIBL  NUMBER (11,4), 
  TENDANCE         NUMBER (11,4) DEFAULT 0,
  COST             NUMBER (11,4),
  CRITERION_COMMENT VARCHAR2 (4000))
  STORAGE
	( 	INITIAL 	50 M
  		NEXT 	  	50 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE DIALECTE ( 
  ID_DIALECTE    VARCHAR2 (16)  NOT NULL, 
  ID_LANGAGE     VARCHAR2 (16), 
  LIB_DIALECTE   VARCHAR2 (32), 
  DESC_DIALECTE  VARCHAR2 (128))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE DROITS ( 
  ID_ELT          VARCHAR2 (64)  NOT NULL, 
  TYPE_ACCES      VARCHAR2 (1)  NOT NULL, 
  ID_PROFIL_USER  VARCHAR2 (32)  NOT NULL, 
  DROITS          NUMBER (5))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE ELEMENT ( 
  ID_ELT         VARCHAR2 (64)  NOT NULL, 
  ID_PRO         VARCHAR2 (32)  NOT NULL, 
  ID_USA         VARCHAR2 (32), 
  ID_TELT        VARCHAR2 (32),
  ID_MAIN_ELT    VARCHAR2(64), 
  ID_PACK        VARCHAR2 (32), 
  STREAM_ELT     VARCHAR2 (128), 
  ID_DIALECTE    VARCHAR2 (16), 
  LIB_ELT        VARCHAR2 (128), 
  DESC_ELT       VARCHAR2 (1024), 
  CUSER_ELT      VARCHAR2 (16), 
  DINST_ELT      DATE, 
  DPEREMPTION    DATE, 
  DMAJ_ELT       DATE, 
  POIDS_ELT      INTEGER, 
  PVOBNAME       VARCHAR2 (128), 
  VOBMOUNTPOINT  VARCHAR2 (128), 
  MAKEFILE_DIR   VARCHAR2 (128), 
  SOURCE_DIR     VARCHAR2 (128), 
  BIN_DIR        VARCHAR2 (128), 
  PERIODIC_DIR   VARCHAR2 (128),
  LIB            VARCHAR2 (3500),
  FILEPATH       VARCHAR2(2048),
  LINEPOS        INTEGER,
  ID_STEREOTYPE  VARCHAR2 (64),
  INFO1          VARCHAR2 (512),
  INFO2          VARCHAR2 (512),
  SCM_REPOSITORY VARCHAR2(128),
  SCM_MODULE     VARCHAR2(128),
  PROJECT_FILE_PATH VARCHAR2(128))
  STORAGE
	( 	INITIAL 	10 M
  		NEXT 	  	10 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE ELEMENT_BASELINE_INFO ( 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL, 
  NOTE1        NUMBER (10)  NOT NULL, 
  NOTE2        NUMBER (10)  NOT NULL, 
  NOTE3        NUMBER (10)  NOT NULL, 
  NOTE4        NUMBER (10)  NOT NULL, 
  NBBETTER     NUMBER (10), 
  NBWORST      NUMBER (10), 
  NBSTABLE     NUMBER (10),
  START_LINE NUMBER DEFAULT 0)
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	5 M
  		PCTINCREASE 10 
	)
  TABLESPACE %TABLE_TBS%;

CREATE TABLE ELT_LINKS ( 
  ELT_PERE            VARCHAR2 (64)  NOT NULL, 
  ELT_FILS            VARCHAR2 (64)  NOT NULL, 
  DINST_LINKS         DATE, 
  DMAJ_LINKS          DATE, 
  DAPPLICATION_LINKS  DATE, 
  DPEREMPTION_LINKS   DATE,
  TYPE                CHAR(1)  DEFAULT 'L')
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	5 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE FACTEUR ( 
  ID_FACT            VARCHAR2 (32)  NOT NULL, 
  LIB_FACT           VARCHAR2 (32), 
  DESC_FACT          VARCHAR2 (128), 
  DINST_FACT         DATE, 
  DMAJ_FACT          DATE, 
  DAPPLICATION_FACT  DATE, 
  DPEREMPTION_FACT   DATE)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE FACTEUR_BLINE ( 
  ID_FAC            VARCHAR2 (32)  NOT NULL, 
  ID_BLINE          VARCHAR2 (32)  NOT NULL, 
  ID_PRO            VARCHAR2 (32)  NOT NULL, 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  DINST_FACBL       DATE, 
  DMAJ_FACBL        DATE, 
  NOTE_FACBL        NUMBER (11,4), 
  ID_LABEL          VARCHAR2 (32), 
  LABEL_NOTE_FACBL  NUMBER (11,4), 
  TENDANCE          NUMBER (11,4),
  FACTEUR_COMMENT   VARCHAR2(4000))
  STORAGE
	( 	INITIAL 	20 M
  		NEXT 	  	20 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE FACTEUR_CRITERE ( 
  ID_USA     VARCHAR2 (32)  NOT NULL,
  ID_FACT    VARCHAR2 (32)  NOT NULL, 
  ID_CRIT    VARCHAR2 (64)  NOT NULL, 
  LIB_REL    VARCHAR2 (32), 
  DESC_REL   VARCHAR2 (128), 
  POIDS      NUMBER (11,4),
  DINST_REG  DATE, 
  DMAJ_REG   DATE)
  STORAGE
	( 	INITIAL 	128 K
  		NEXT 	  	128 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE I18N ( 
  TABLE_NAME           VARCHAR2 (32)  NOT NULL, 
  COLUMN_NAME          VARCHAR2 (32)  NOT NULL, 
  ID_TABLE             VARCHAR2 (128)  NOT NULL, 
  ID_LANGUE            VARCHAR2 (32)  NOT NULL, 
  TEXT                 VARCHAR2 (4000)  NOT NULL)
  STORAGE
	( 	INITIAL 	5 M
  		NEXT 	  	1 M
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE JUSTIFICATION ( 
  ID_JUST           VARCHAR2 (32)  NOT NULL, 
  LIB_JUST          VARCHAR2 (32), 
  DESC_JUST         VARCHAR2 (128), 
  DINST_JUST        DATE, 
  DMAJ_JUST         DATE, 
  STATUT_JUST       VARCHAR2 (32), 
  PROPAGATION_JUST  VARCHAR2 (1), 
  CUSER_JUST        VARCHAR2 (16), 
  JUST_LINK         VARCHAR2 (32))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE LABELLISATION ( 
  ID_LABEL      VARCHAR2 (32)  NOT NULL, 
  CUSER_LABEL   VARCHAR2 (16), 
  ID_PRO        VARCHAR2 (32)  NOT NULL, 
  ID_BLINE      VARCHAR2 (32)  NOT NULL, 
  ID_TYPLAB     VARCHAR2 (32), 
  LIB_LABEL     VARCHAR2 (32), 
  DESC_LABEL    VARCHAR2 (128), 
  DINST_LABEL   DATE, 
  DMAJ_LABEL    DATE, 
  STATUT_LABEL  VARCHAR2 (16), 
  LABEL_LINK    VARCHAR2 (32))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE LANGAGE ( 
  ID_LANGAGE  VARCHAR2 (16)  NOT NULL, 
  LIB_LANG    VARCHAR2 (32), 
  DESC_LANG   VARCHAR2 (128))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE LANGUE ( 
  ID  VARCHAR2 (32)  NOT NULL, 
  LIB    VARCHAR2 (32), 
  DESCRIPTION   VARCHAR2 (128))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE LINK_ELT_BLINE ( 
  LINK_ID       VARCHAR2 (32)  NOT NULL, 
  ELT_FROM_ID   VARCHAR2 (32)  NOT NULL, 
  ELT_TO_ID     VARCHAR2 (32)  NOT NULL, 
  ID_BLINE      VARCHAR2 (32)  NOT NULL, 
  REAL_LINK_ID  VARCHAR2 (32),
  TYPE          VARCHAR2 (32))
  STORAGE
	( 	INITIAL 	2 M
  		NEXT 	  	2 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE LINK_REAL ( 
  ID_LINK   VARCHAR2 (32)  NOT NULL, 
  ID_FROM   VARCHAR2 (32), 
  ID_TO     VARCHAR2 (32), 
  ID_PROJ   VARCHAR2 (64)  NOT NULL, 
  ID_BLINE  VARCHAR2 (32)  NOT NULL, 
  STATE     NUMBER (2)    NOT NULL)
  STORAGE
	( 	INITIAL 	2 M
  		NEXT 	  	2 M
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE METRIQUE ( 
  ID_MET            VARCHAR2 (64)  NOT NULL, 
  LIB_MET           VARCHAR2 (64), 
  DESC_MET          VARCHAR2 (768), 
  TYPE_MET          VARCHAR2 (16), 
  CAT_MET           VARCHAR2 (32), 
  OUTIL_MET         VARCHAR2 (40), 
  TECHNO_MET        VARCHAR2 (32), 
  DINST_MET         DATE, 
  DMAJ_MET          DATE, 
  DAPPLICATION_MET  DATE, 
  DPEREMPTION_MET   DATE,
  COST_CORRECTION   varchar(512))
  STORAGE
	( 	INITIAL 	100 K
  		NEXT 	  	50 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE NIVEAU ( 
  ID_NIV            VARCHAR2 (32)  NOT NULL, 
  LIB_NIV           VARCHAR2 (32), 
  IDESC_NIV         VARCHAR2 (128), 
  DINST_NIV         DATE, 
  DMAJ_NIV          DATE, 
  CUSER_NIV         VARCHAR2 (16), 
  DAPPLICATION_NIV  DATE, 
  DPEREMPTION_NIV   DATE)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE PACKAGE ( 
  ID_PACK            VARCHAR2 (32)  NOT NULL, 
  ID_NIV             VARCHAR2 (32), 
  LIB_PACK           VARCHAR2 (1024), 
  DESC_PACK          VARCHAR2 (128), 
  DINST_PACK         DATE, 
  DMAJ_PACK          DATE, 
  DAPPLICATION_PACK  DATE, 
  DPEREMPTION_PACK   DATE, 
  COEFF_PACK         NUMBER (7,2), 
  ID_PROJ            VARCHAR2 (64), 
  X                  NUMBER (12), 
  Y                  NUMBER (12), 
  WIDTH              NUMBER (12), 
  HEIGHT             NUMBER (12),
  ZORDER             NUMBER (12),
  TYPE               NUMBER (12),
  PARENT_PACKAGE     varchar2(32))
  STORAGE
	( 	INITIAL 	10 K
  		NEXT 	  	10 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE POIDS_FACT_CRIT ( 
  ID_FACT             VARCHAR2 (32)  NOT NULL, 
  ID_CRIT             VARCHAR2 (64)  NOT NULL, 
  BLINE_POIDS         VARCHAR2 (32)  NOT NULL, 
  ID_PRO              VARCHAR2 (32)  NOT NULL, 
  ID_ELT              VARCHAR2 (64)  NOT NULL, 
  DINST_POIDS         DATE, 
  DMAJ_POIDS          DATE, 
  CUSER_POIDS         VARCHAR2 (16), 
  DAPPLICATION_POIDS  DATE          NOT NULL, 
  DPEREMPTION_POIDS   DATE, 
  VALEUR_POIDS        NUMBER (11,4))
  STORAGE
	( 	INITIAL 	100 K
  		NEXT 	  	100 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE PROJET ( 
  ID_PRO     VARCHAR2 (32)  NOT NULL, 
  LIB_PRO    VARCHAR2 (128), 
  DESC_PRO   VARCHAR2 (128), 
  DINST_PRO  DATE, 
  DMAJ_PRO   DATE, 
  DCLOS_PRO  DATE, 
  TYPE       INTEGER       DEFAULT 0)
  STORAGE
	( 	INITIAL 	5 K
  		NEXT 	  	5 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE QAMETRIQUE ( 
  ID_ELT               VARCHAR2 (64)  NOT NULL, 
  ID_MET               VARCHAR2 (64)  NOT NULL, 
  ID_BLINE             VARCHAR2 (32)  NOT NULL, 
  DINST_QAMET          DATE, 
  DMAJ_QAMET           DATE, 
  CUSER_QAMET          VARCHAR2 (16), 
  DAPPLICATION_QAMET   DATE, 
  DPEREMPTION_QAMET    DATE, 
  VALBRUTE_QAMET       NUMBER (15,4), 
  NOTECALC_QAMET       NUMBER (11,4), 
  JUST_MET             VARCHAR2 (32), 
  JUST_VALBRUT_QAMET   NUMBER (11,4), 
  JUST_NOTECALC_QAMET  NUMBER (11,4), 
  TENDANCE             NUMBER (15,4) DEFAULT 0,
  LIGNES               VARCHAR2(2048))
  STORAGE
	( 	INITIAL 	20 M
  		NEXT 	  	20 M
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE REGLE ( 
  ID_CRIT    VARCHAR2 (64)  NOT NULL, 
  ID_MET     VARCHAR2 (64)  NOT NULL,
  ID_USA     VARCHAR2 (32)  NOT NULL,
  LIB_REG    VARCHAR2 (32), 
  DESC_REG   VARCHAR2 (128), 
  DINST_REG  DATE, 
  DMAJ_REG   DATE)
  STORAGE
	( 	INITIAL 	20 K
  		NEXT 	  	20 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE SEUIL ( 
  ID_SEUIL            VARCHAR2 (16)  NOT NULL, 
  ID_MET              VARCHAR2 (64)  NOT NULL, 
  ID_TEC              VARCHAR2 (32), 
  USAGE_SEUIL         VARCHAR2 (32), 
  NOTE_SEUIL          NUMBER (9,2), 
  OPERATEUR_SEUIL     VARCHAR2 (8), 
  HBORNE_SEUIL        NUMBER (8), 
  LBORNE_SEUIL        NUMBER (8), 
  USA_SEUIL           VARCHAR2 (32), 
  DINST_SEUIL         DATE, 
  DMAJ_SEUIL          DATE, 
  DAPPLICATION_SEUIL  DATE, 
  DPEREMPTION_SEUIL   DATE)
  STORAGE
	( 	INITIAL 	2 K
  		NEXT 	  	2 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE SEVERITE ( 
  ID_SEV    VARCHAR2 (3)  NOT NULL, 
  LIB_SEV   VARCHAR2 (32), 
  DESC_SEV  VARCHAR2 (128))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE STEREOTYPE (
   ID_STEREOTYPE       VARCHAR2 (64) NOT NULL,
   LIB_STEREOTYPE      VARCHAR2 (64),
   DESC_STEREOTYPE     VARCHAR2 (256))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE TECHNOLOGIE ( 
  ID_TEC    VARCHAR2 (32)  NOT NULL, 
  LIB_TEC   VARCHAR2 (64), 
  DESC_TEC  VARCHAR2 (255))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE TYPE_ELEMENT ( 
  ID_TELT            VARCHAR2 (32)  NOT NULL, 
  LIB_TELT           VARCHAR2 (32), 
  DESC_TELT          VARCHAR2 (128), 
  DINST_TELT         DATE, 
  DMAJ_TELT          DATE, 
  DAPPLICATION_TELT  DATE, 
  DPEREMPTION_TELT   DATE, 
  CUSER_TELT         VARCHAR2 (16),
  HAS_SOURCE         NUMBER(1) DEFAULT 0,
  IS_FILE            NUMBER(1) DEFAULT 0)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE TYPE_FICHE ( 
  ID_TFICHE     VARCHAR2 (64)  NOT NULL, 
  LIB_TFICHE    VARCHAR2 (32), 
  DESC_TFICHE   VARCHAR2 (128), 
  DINST_TFICHE  DATE, 
  DMAJ_TFICHE   DATE)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE TYPE_LABELLISATION ( 
  ID_TYPLAB            VARCHAR2 (32)  NOT NULL, 
  LIB_TYPLAB           VARCHAR2 (32), 
  DESC_TYPLAB          VARCHAR2 (128), 
  DINST_TYPLAB         DATE, 
  DMAJ_TYPLAB          DATE, 
  DAPPLICATION_TYPLAB  DATE, 
  DPEREMPTION_TYPLAB   DATE)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE TYPE_METRIQUE ( 
  ID_TMET         VARCHAR2 (16)  NOT NULL, 
  LIB_TMET        VARCHAR2 (32), 
  DESC_TMET       VARCHAR2 (128), 
  OPERATEUR_TMET  VARCHAR2 (20))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE MODELE ( 
  ID_USA            VARCHAR2 (32)  NOT NULL, 
  LIB_USA           VARCHAR2 (32), 
  DESC_USA          VARCHAR2 (128), 
  DINTS_USA         DATE, 
  DMAJ_USA          DATE, 
  DAPPLICATION_USA  DATE, 
  DPEREMPTION_USA   DATE,
  APU_LIMIT_MEDIUM  DECIMAL DEFAULT 10,
  APU_LIMIT_LONG    DECIMAL DEFAULT 20
  )
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE OUTILS (
  ID_OUTILS       VARCHAR2 (40) NOT NULL,
  LIB_OUTILS      VARCHAR2 (40))
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE CRITERENOTEREPARTITION ( 
  ID_ELT    VARCHAR2 (64)  NOT NULL, 
  ID_BLINE  VARCHAR2 (32)  NOT NULL, 
  ID_CRIT   VARCHAR2 (64)  NOT NULL, 
  SEUIL     INTEGER       NOT NULL, 
  TOTAL     INTEGER       NOT NULL)
  STORAGE
	( 	INITIAL 	1 M
  		NEXT 	  	500 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE VOLUMETRY ( 
  ID_ELT    VARCHAR2 (64)  NOT NULL, 
  ID_TELT   VARCHAR2 (32)  NOT NULL, 
  ID_BLINE  VARCHAR2 (32)  NOT NULL, 
  TOTAL     INTEGER       NOT NULL, 
  CREATED   INTEGER       NOT NULL, 
  DELETED   INTEGER       NOT NULL)
  STORAGE
	( 	INITIAL 	20 K
  		NEXT 	  	20 K
  		PCTINCREASE 0 
	)
   TABLESPACE %TABLE_TBS%
;
CREATE TABLE OUTILS_MODELE (
  ID_USA    VARCHAR2 (40) NOT NULL,
  ID_OUTILS VARCHAR2 (40) NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 0 
	)
  TABLESPACE %TABLE_TBS%
;
   
CREATE TABLE BASELINE_LINKS ( 
  PARENT_ID_BLINE     VARCHAR2 (32)  NOT NULL, 
  CHILD_ID_BLINE      VARCHAR2 (32)  NOT NULL,
  CHILD_ID_ELT        VARCHAR2 (64)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

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

CREATE TABLE CAQS_ACCESS_RIGHTS ( 
  ID_ACCESS    VARCHAR2 (64)  NOT NULL, 
  ID_ROLE      VARCHAR2 (64)  NOT NULL)
  STORAGE
	( 	INITIAL 	64 K
  		NEXT 	  	64 K
  		PCTINCREASE 10 
	)
   TABLESPACE %TABLE_TBS%
;

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

CREATE TABLE CAQS_MESSAGES (
	ID_MESSAGE 	VARCHAR(32) NOT NULL,
	ID_ELT		VARCHAR(64) NOT NULL,
	ID_BLINE	VARCHAR(32) NOT NULL,
	ID_USER		VARCHAR(32) NOT NULL,
	ID_TASK		VARCHAR(32) NOT NULL,
	STATUS		VARCHAR(32),
	BEGIN_DATE	DATE,
	END_DATE	DATE,
	COMPLETION_PCT		DECIMAL,
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

CREATE TABLE USER_SETTINGS ( 
  USER_ID         VARCHAR2 (64)   NOT NULL, 
  SETTING_ID      VARCHAR2 (64)   NOT NULL,
  SETTING_VALUE      VARCHAR2 (256)   NOT NULL)
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE SETTINGS ( 
  SETTING_ID      VARCHAR2 (64)   NOT NULL, 
  SETTING_DESC      VARCHAR2 (256)   NOT NULL,
  SETTING_DEFAULT   VARCHAR2 (256))
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE SETTINGS_VALUES ( 
  SETTING_ID      VARCHAR2 (64)   NOT NULL, 
  SETTING_VALUE      VARCHAR2 (256)   NOT NULL)
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE ACTION_PLAN ( 
  ID_ACTION_PLAN    VARCHAR2 (64)   NOT NULL,
  ID_ELT               VARCHAR2 (64)   NOT NULL, 
  ID_BLINE         VARCHAR2 (32)   NOT NULL,
  ACTION_PLAN_COMMENT       VARCHAR2(1024),
  COMMENT_USER        VARCHAR(64))
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE ACTION_PLAN_UNIT ( 
  UNIT_ID             VARCHAR2 (64)   NOT NULL, 
  UNIT_DESC           VARCHAR2 (256),
  NB_ACTION_PLAN_UNIT INTEGER default 1
  )
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE COPY_PASTE_REF (
  ID_COPY        VARCHAR2 (64)  NOT NULL, 
  ID_BLINE       VARCHAR2 (32)  NOT NULL,
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

CREATE TABLE ACTION_PLAN_CRITERION ( 
  ID_ACTION_PLAN    VARCHAR2 (64)   NOT NULL,
  ID_CRIT         VARCHAR2 (64)   NOT NULL,
  PRIORITY         VARCHAR2(32),
  SELECTED      NUMBER(1) NOT NULL,
  CRITERION_COMMENT       VARCHAR2(4000),
  COMMENT_USER        VARCHAR(64),
  MASTER_ELEMENT VARCHAR(64))
  STORAGE
   (    INITIAL    64 K
        NEXT         64 K
        PCTINCREASE 10 
   )
   TABLESPACE %TABLE_TBS%
;

CREATE TABLE STATS (
  PERIOD  DATE NOT NULL,
  SUMCONN NUMBER NOT NULL,
  MAXCONN NUMBER NOT NULL)
  STORAGE (
   	INITIAL 	64 K
  	NEXT 	  	64 K
  	PCTINCREASE 10 
  )
  TABLESPACE %TABLE_TBS%
;

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