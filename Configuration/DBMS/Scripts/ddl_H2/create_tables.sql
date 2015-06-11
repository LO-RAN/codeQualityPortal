CREATE TABLE ARCHI_LINK ( 
  ID_LINK  varchar (32)  NOT NULL, 
  ID_FROM  varchar (32)  NOT NULL, 
  ID_TO    varchar (32)  NOT NULL, 
  ID_PROJ  varchar (64)  NOT NULL,
  TYPE     numeric   (12))
;

CREATE TABLE BASELINE ( 
  ID_BLINE            varchar (32)  NOT NULL, 
  TYPE_BLINE          varchar (32), 
  LIB_BLINE           varchar (32), 
  DESC_BLINE          varchar (128), 
  PRO_BLINRE          varchar (32)  NOT NULL, 
  DINST_BLINE         TIMESTAMP, 
  DMAJ_BLINE          TIMESTAMP, 
  DAPPLICATION_BLINE  TIMESTAMP, 
  DPREMPTION_BLINE    TIMESTAMP, 
  CUSER_BLINE         varchar (16))
;

CREATE TABLE CATEGORIE ( 
  ID_CAT            varchar (32)  NOT NULL, 
  LIB_CAT           varchar (32), 
  DESC_CAT          varchar (128), 
  CUSER_CAT         varchar (16), 
  DINST_CAT         TIMESTAMP, 
  DMAJ_CAT          TIMESTAMP, 
  DAPPLICATION_CAT  TIMESTAMP, 
  DPEREMPTION_CAT   TIMESTAMP)
;

CREATE TABLE CRITERE ( 
  ID_CRIT            varchar (64)  NOT NULL, 
  DINST_CRIT         TIMESTAMP, 
  DMAJ_CRIT          TIMESTAMP, 
  DAPPLICATION_CRIT  TIMESTAMP, 
  DPEREMPTION_CRIT   TIMESTAMP)
;

CREATE TABLE CRITERE_USAGE ( 
  ID_CRIT  varchar (64)  NOT NULL, 
  ID_USA   varchar (32)  NOT NULL, 
  ID_TELT  varchar (32)  NOT NULL)
; 

CREATE TABLE CRITERE_BLINE ( 
  ID_CRIT          varchar (64)  NOT NULL, 
  ID_BLINE         varchar (32)  NOT NULL, 
  ID_PRO           varchar (32)  NOT NULL, 
  ID_ELT           varchar (64)  NOT NULL, 
  DINST_CRIBL      TIMESTAMP, 
  DMAJ_CRIBL       TIMESTAMP, 
  NOTE_CRIBL       numeric (11,4), 
  ID_JUST          varchar (32), 
  JUST_NOTE_CRIBL  numeric (11,4), 
  TENDANCE         numeric (11,4) DEFAULT 0,
  COST             numeric (11,4),
  CRITERION_COMMENT varchar (4000))
;

CREATE TABLE DIALECTE ( 
  ID_DIALECTE    varchar (16)  NOT NULL, 
  ID_LANGAGE     varchar (16), 
  LIB_DIALECTE   varchar (32), 
  DESC_DIALECTE  varchar (128))
;

CREATE TABLE DROITS ( 
  ID_ELT          varchar (64)  NOT NULL, 
  TYPE_ACCES      varchar (1)  NOT NULL, 
  ID_PROFIL_USER  varchar (32)  NOT NULL, 
  DROITS          numeric (5))
;

CREATE TABLE ELEMENT ( 
  ID_ELT         varchar (64)  NOT NULL, 
  ID_PRO         varchar (32)  NOT NULL, 
  ID_USA         varchar (32), 
  ID_TELT        varchar (32),
  ID_MAIN_ELT    varchar(64), 
  ID_PACK        varchar (32), 
  STREAM_ELT     varchar (128), 
  ID_DIALECTE    varchar (16), 
  LIB_ELT        varchar (128), 
  DESC_ELT       varchar (1024), 
  CUSER_ELT      varchar (16), 
  DINST_ELT      TIMESTAMP, 
  DPEREMPTION    TIMESTAMP, 
  DMAJ_ELT       TIMESTAMP, 
  POIDS_ELT      INTEGER, 
  PVOBNAME       varchar (128), 
  VOBMOUNTPOINT  varchar (128), 
  MAKEFILE_DIR   varchar (128), 
  SOURCE_DIR     varchar (128), 
  BIN_DIR        varchar (128), 
  PERIODIC_DIR   varchar (128),
  LIB            varchar (3500),
  FILEPATH       varchar(2048),
  LINEPOS        INTEGER,
  ID_STEREOTYPE  varchar (64),
  INFO1          varchar (512),
  INFO2          varchar (512),
  SCM_REPOSITORY varchar(128),
  SCM_MODULE     varchar(128),
  PROJECT_FILE_PATH varchar(128))
;

CREATE TABLE ELEMENT_BASELINE_INFO ( 
  ID_ELT            varchar (64)  NOT NULL, 
  ID_BLINE            varchar (32)  NOT NULL, 
  ID_MAIN_ELT            varchar (32)  NOT NULL, 
  NOTE1        numeric (10)  NOT NULL, 
  NOTE2        numeric (10)  NOT NULL, 
  NOTE3        numeric (10)  NOT NULL, 
  NOTE4        numeric (10)  NOT NULL, 
  NBBETTER     numeric (10), 
  NBWORST      numeric (10), 
  NBSTABLE     numeric (10),
  START_LINE numeric DEFAULT 0)
;

CREATE TABLE ELT_LINKS ( 
  ELT_PERE            varchar (64)  NOT NULL, 
  ELT_FILS            varchar (64)  NOT NULL, 
  DINST_LINKS         TIMESTAMP, 
  DMAJ_LINKS          TIMESTAMP, 
  DAPPLICATION_LINKS  TIMESTAMP, 
  DPEREMPTION_LINKS   TIMESTAMP,
  TYPE                CHAR(1)  DEFAULT 'L')
;

CREATE TABLE FACTEUR ( 
  ID_FACT            varchar (32)  NOT NULL, 
  DINST_FACT         TIMESTAMP, 
  DMAJ_FACT          TIMESTAMP, 
  DAPPLICATION_FACT  TIMESTAMP, 
  DPEREMPTION_FACT   TIMESTAMP)
;

CREATE TABLE FACTEUR_BLINE ( 
  ID_FAC            varchar (32)  NOT NULL, 
  ID_BLINE          varchar (32)  NOT NULL, 
  ID_PRO            varchar (32)  NOT NULL, 
  ID_ELT            varchar (64)  NOT NULL, 
  DINST_FACBL       TIMESTAMP, 
  DMAJ_FACBL        TIMESTAMP, 
  NOTE_FACBL        numeric (11,4), 
  ID_LABEL          varchar (32), 
  LABEL_NOTE_FACBL  numeric (11,4), 
  TENDANCE          numeric (11,4) DEFAULT 0,
  FACTEUR_COMMENT   varchar (4000))
;

CREATE TABLE FACTEUR_CRITERE ( 
  ID_USA     varchar (32)  NOT NULL,
  ID_FACT    varchar (32)  NOT NULL, 
  ID_CRIT    varchar (64)  NOT NULL, 
  POIDS      numeric (11,4),
  DINST_REG  TIMESTAMP, 
  DMAJ_REG   TIMESTAMP)
;

CREATE TABLE I18N ( 
  TABLE_NAME           varchar (32)  NOT NULL, 
  COLUMN_NAME          varchar (32)  NOT NULL, 
  ID_TABLE             varchar (128)  NOT NULL, 
  ID_LANGUE            varchar (32)  NOT NULL, 
  TEXT                 varchar (4000)  NOT NULL)
;

CREATE TABLE JUSTIFICATION ( 
  ID_JUST           varchar (32)  NOT NULL, 
  LIB_JUST          varchar (32), 
  DESC_JUST         varchar (128), 
  DINST_JUST        TIMESTAMP, 
  DMAJ_JUST         TIMESTAMP, 
  STATUT_JUST       varchar (32), 
  PROPAGATION_JUST  varchar (1), 
  CUSER_JUST        varchar (16), 
  JUST_LINK         varchar (32))
;

CREATE TABLE LABELLISATION ( 
  ID_LABEL      varchar (32)  NOT NULL, 
  CUSER_LABEL   varchar (16), 
  ID_PRO        varchar (32)  NOT NULL, 
  ID_BLINE      varchar (32)  NOT NULL, 
  ID_TYPLAB     varchar (32), 
  LIB_LABEL     varchar (32), 
  DESC_LABEL    varchar (128), 
  DINST_LABEL   TIMESTAMP, 
  DMAJ_LABEL    TIMESTAMP, 
  STATUT_LABEL  varchar (16), 
  LABEL_LINK    varchar (32))
;

CREATE TABLE LANGAGE ( 
  ID_LANGAGE  varchar (16)  NOT NULL, 
  LIB_LANG    varchar (32), 
  DESC_LANG   varchar (128))
;

CREATE TABLE LANGUE ( 
  ID  varchar (32)  NOT NULL, 
  LIB    varchar (32), 
  DESCRIPTION   varchar (128))
;

CREATE TABLE LINK_ELT_BLINE ( 
  LINK_ID       varchar (32)  NOT NULL, 
  ELT_FROM_ID   varchar (32)  NOT NULL, 
  ELT_TO_ID     varchar (32)  NOT NULL, 
  ID_BLINE      varchar (32)  NOT NULL, 
  REAL_LINK_ID  varchar (32),
  TYPE          varchar (32))
;

CREATE TABLE LINK_REAL ( 
  ID_LINK   varchar (32)  NOT NULL, 
  ID_FROM   varchar (32), 
  ID_TO     varchar (32), 
  ID_PROJ   varchar (64)  NOT NULL, 
  ID_BLINE  varchar (32)  NOT NULL, 
  STATE     numeric (2)    NOT NULL)
;

CREATE TABLE METRIQUE ( 
  ID_MET            varchar (64)  NOT NULL, 
  TYPE_MET          varchar (16), 
  CAT_MET           varchar (32), 
  OUTIL_MET         varchar (40), 
  TECHNO_MET        varchar (32), 
  DINST_MET         TIMESTAMP, 
  DMAJ_MET          TIMESTAMP, 
  DAPPLICATION_MET  TIMESTAMP, 
  DPEREMPTION_MET   TIMESTAMP,
  COST_CORRECTION   varchar(512))
;

CREATE TABLE NIVEAU ( 
  ID_NIV            varchar (32)  NOT NULL, 
  LIB_NIV           varchar (32), 
  IDESC_NIV         varchar (128), 
  DINST_NIV         TIMESTAMP, 
  DMAJ_NIV          TIMESTAMP, 
  CUSER_NIV         varchar (16), 
  DAPPLICATION_NIV  TIMESTAMP, 
  DPEREMPTION_NIV   TIMESTAMP)
;

CREATE TABLE PACKAGE ( 
  ID_PACK            varchar (32)  NOT NULL, 
  ID_NIV             varchar (32), 
  LIB_PACK           varchar (1024), 
  DESC_PACK          varchar (128), 
  DINST_PACK         TIMESTAMP, 
  DMAJ_PACK          TIMESTAMP, 
  DAPPLICATION_PACK  TIMESTAMP, 
  DPEREMPTION_PACK   TIMESTAMP, 
  COEFF_PACK         numeric (7,2), 
  ID_PROJ            varchar (64), 
  X                  numeric (12), 
  Y                  numeric (12), 
  WIDTH              numeric (12), 
  HEIGHT             numeric (12),
  ZORDER             numeric (12),
  TYPE               numeric (12),
  PARENT_PACKAGE     varchar(32))
;

CREATE TABLE POIDS_FACT_CRIT ( 
  ID_FACT             varchar (32)  NOT NULL, 
  ID_CRIT             varchar (64)  NOT NULL, 
  BLINE_POIDS         varchar (32)  NOT NULL, 
  ID_PRO              varchar (32)  NOT NULL, 
  ID_ELT              varchar (64)  NOT NULL, 
  DINST_POIDS         TIMESTAMP, 
  DMAJ_POIDS          TIMESTAMP, 
  CUSER_POIDS         varchar (16), 
  DAPPLICATION_POIDS  TIMESTAMP          NOT NULL, 
  DPEREMPTION_POIDS   TIMESTAMP, 
  VALEUR_POIDS        numeric (11,4))
;

CREATE TABLE PROJET ( 
  ID_PRO     varchar (32)  NOT NULL, 
  LIB_PRO    varchar (128), 
  DESC_PRO   varchar (128), 
  DINST_PRO  TIMESTAMP, 
  DMAJ_PRO   TIMESTAMP, 
  DCLOS_PRO  TIMESTAMP, 
  TYPE       INTEGER       DEFAULT 0)
;

CREATE TABLE QAMETRIQUE ( 
  ID_ELT               varchar (64)  NOT NULL, 
  ID_MET               varchar (64)  NOT NULL, 
  ID_BLINE             varchar (32)  NOT NULL, 
  DINST_QAMET          TIMESTAMP, 
  DMAJ_QAMET           TIMESTAMP, 
  CUSER_QAMET          varchar (16), 
  DAPPLICATION_QAMET   TIMESTAMP, 
  DPEREMPTION_QAMET    TIMESTAMP, 
  VALBRUTE_QAMET       numeric (15,4), 
  NOTECALC_QAMET       numeric (11,4), 
  JUST_MET             varchar (32), 
  JUST_VALBRUT_QAMET   numeric (11,4), 
  JUST_NOTECALC_QAMET  numeric (11,4), 
  TENDANCE             numeric (15,4) DEFAULT 0,
  LIGNES               varchar (2048))
;

CREATE TABLE REGLE ( 
  ID_CRIT    varchar (64)  NOT NULL, 
  ID_MET     varchar (64)  NOT NULL,
  ID_USA     varchar (32)  NOT NULL,
  DINST_REG  TIMESTAMP, 
  DMAJ_REG   TIMESTAMP)
;

CREATE TABLE SEUIL ( 
  ID_SEUIL            varchar (16)  NOT NULL, 
  ID_MET              varchar (64)  NOT NULL, 
  ID_TEC              varchar (32), 
  USAGE_SEUIL         varchar (32), 
  NOTE_SEUIL          numeric (9,2), 
  OPERATEUR_SEUIL     varchar (8), 
  HBORNE_SEUIL        numeric (8), 
  LBORNE_SEUIL        numeric (8), 
  USA_SEUIL           varchar (32), 
  DINST_SEUIL         TIMESTAMP, 
  DMAJ_SEUIL          TIMESTAMP, 
  DAPPLICATION_SEUIL  TIMESTAMP, 
  DPEREMPTION_SEUIL   TIMESTAMP)
;

CREATE TABLE SEVERITE ( 
  ID_SEV    varchar (3)  NOT NULL, 
  LIB_SEV   varchar (32), 
  DESC_SEV  varchar (128))
;

CREATE TABLE STEREOTYPE (
   ID_STEREOTYPE       varchar (64) NOT NULL,
   LIB_STEREOTYPE      varchar (64),
   DESC_STEREOTYPE     varchar (256))
;

CREATE TABLE TECHNOLOGIE ( 
  ID_TEC    varchar (32)  NOT NULL, 
  LIB_TEC   varchar (64), 
  DESC_TEC  varchar (255))
;

CREATE TABLE TYPE_ELEMENT ( 
  ID_TELT            varchar (32)  NOT NULL, 
  DINST_TELT         TIMESTAMP, 
  DMAJ_TELT          TIMESTAMP, 
  DAPPLICATION_TELT  TIMESTAMP, 
  DPEREMPTION_TELT   TIMESTAMP, 
  CUSER_TELT         varchar (16),
  HAS_SOURCE         numeric(1) DEFAULT 0,
  IS_FILE            numeric(1) DEFAULT 0)
;

CREATE TABLE TYPE_FICHE ( 
  ID_TFICHE     varchar (64)  NOT NULL, 
  LIB_TFICHE    varchar (32), 
  DESC_TFICHE   varchar (128), 
  DINST_TFICHE  TIMESTAMP, 
  DMAJ_TFICHE   TIMESTAMP)
;

CREATE TABLE TYPE_LABELLISATION ( 
  ID_TYPLAB            varchar (32)  NOT NULL, 
  LIB_TYPLAB           varchar (32), 
  DESC_TYPLAB          varchar (128), 
  DINST_TYPLAB         TIMESTAMP, 
  DMAJ_TYPLAB          TIMESTAMP, 
  DAPPLICATION_TYPLAB  TIMESTAMP, 
  DPEREMPTION_TYPLAB   TIMESTAMP)
;

CREATE TABLE TYPE_METRIQUE ( 
  ID_TMET         varchar (16)  NOT NULL, 
  LIB_TMET        varchar (32), 
  DESC_TMET       varchar (128), 
  OPERATEUR_TMET  varchar (20))
;

CREATE TABLE MODELE ( 
  ID_USA            varchar (32)  NOT NULL, 
  DINTS_USA         TIMESTAMP, 
  DMAJ_USA          TIMESTAMP, 
  DAPPLICATION_USA  TIMESTAMP, 
  DPEREMPTION_USA   TIMESTAMP,
  APU_LIMIT_MEDIUM  DECIMAL DEFAULT 10,
  APU_LIMIT_LONG    DECIMAL DEFAULT 20
  )
;

CREATE TABLE OUTILS (
  ID_OUTILS       varchar (40) NOT NULL
  )
;

CREATE TABLE CRITERENOTEREPARTITION ( 
  ID_ELT    varchar (64)  NOT NULL, 
  ID_BLINE  varchar (32)  NOT NULL, 
  ID_CRIT   varchar (64)  NOT NULL, 
  SEUIL     INTEGER       NOT NULL, 
  TOTAL     INTEGER       NOT NULL)
;

CREATE TABLE VOLUMETRY ( 
  ID_ELT    varchar (64)  NOT NULL, 
  ID_TELT   varchar (32)  NOT NULL, 
  ID_BLINE  varchar (32)  NOT NULL, 
  TOTAL     INTEGER       NOT NULL, 
  CREATED   INTEGER       NOT NULL, 
  DELETED   INTEGER       NOT NULL)
;

CREATE TABLE OUTILS_MODELE (
  ID_USA    varchar (40) NOT NULL,
  ID_OUTILS varchar (40) NOT NULL)
;
   
CREATE TABLE BASELINE_LINKS ( 
  PARENT_ID_BLINE     varchar (32)  NOT NULL, 
  CHILD_ID_BLINE      varchar (32)  NOT NULL,
  CHILD_ID_ELT        varchar (64)  NOT NULL)
;

CREATE TABLE CAQS_ROLE ( 
  ID_ROLE       varchar (64)  NOT NULL, 
  PORTAL_ROLE   varchar (64)  NOT NULL)
;

CREATE TABLE CAQS_ACCESS_DEFINITION ( 
  ID_ACCESS     varchar (64)  NOT NULL, 
  LIB_ACCESS    varchar (128)  NOT NULL)
;

CREATE TABLE CAQS_ACCESS_RIGHTS ( 
  ID_ACCESS    varchar (64)  NOT NULL, 
  ID_ROLE      varchar (64)  NOT NULL)
;

CREATE TABLE CAQS_TASKS (
	ID_TASK		VARCHAR(32) NOT NULL,
	TYPE_TASK	VARCHAR(32) NOT NULL,
	SHOW_USER	VARCHAR(32)
)
;

CREATE TABLE CAQS_MESSAGES (
	ID_MESSAGE 	VARCHAR(32) NOT NULL,
	ID_ELT		VARCHAR(64) NOT NULL,
	ID_BLINE	VARCHAR(32) NOT NULL,
	ID_USER		VARCHAR(32) NOT NULL,
	ID_TASK		VARCHAR(32) NOT NULL,
	STATUS		VARCHAR(32),
	BEGIN_DATE	TIMESTAMP,
	END_DATE	TIMESTAMP,
	COMPLETION_PCT		DECIMAL,
	SEEN		numeric(1),
	INFO1		VARCHAR(256),
	OTHERID		VARCHAR(32)
)
;

CREATE TABLE USER_SETTINGS ( 
  USER_ID         varchar (64)   NOT NULL, 
  SETTING_ID      varchar (64)   NOT NULL,
  SETTING_VALUE      varchar (256)   NOT NULL)
;

CREATE TABLE SETTINGS ( 
  SETTING_ID      varchar (64)   NOT NULL, 
  SETTING_DESC      varchar (256)   NOT NULL,
  SETTING_DEFAULT   varchar (256))
;

CREATE TABLE SETTINGS_VALUES ( 
  SETTING_ID      varchar (64)   NOT NULL, 
  SETTING_VALUE      varchar (256)   NOT NULL)
;

CREATE TABLE ACTION_PLAN (
  ID_ACTION_PLAN      varchar (64)   NOT NULL, 
  ID_ELT              varchar (64)   NOT NULL, 
  ID_BLINE            varchar (32)   NOT NULL,
  ACTION_PLAN_COMMENT varchar (1024),
  COMMENT_USER        varchar (64))
;

CREATE TABLE ACTION_PLAN_UNIT ( 
  UNIT_ID         varchar (64)   NOT NULL, 
  UNIT_DESC       varchar (256),
  NB_ACTION_PLAN_UNIT INTEGER)
;

CREATE TABLE COPY_PASTE_REF (
  ID_COPY        varchar (64)  NOT NULL, 
  ID_BLINE       varchar (32)  NOT NULL,
  ID_ELT         varchar (64)  NOT NULL, 
  LINE           numeric  NOT NULL,
  LENGTH         numeric  NOT NULL)
;

CREATE TABLE ACTION_PLAN_CRITERION ( 
  ID_ACTION_PLAN    varchar (64)   NOT NULL,
  ID_CRIT           varchar (64)   NOT NULL,
  PRIORITY          varchar(32),
  SELECTED          numeric(1) NOT NULL,
  CRITERION_COMMENT       VARCHAR(4000),
  COMMENT_USER      varchar (64),
  MASTER_ELEMENT    VARCHAR (64))
;

CREATE TABLE STATS (
  PERIOD  DATE NOT NULL,
  SUMCONN numeric NOT NULL,
  MAXCONN numeric NOT NULL)
;

CREATE TABLE TMP_ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR (64)  NOT NULL, 
  ID_BLINE            VARCHAR (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR (32)  NOT NULL,
  ID_CRIT VARCHAR(64) NOT NULL,
  NOTE_CRIBL    numeric (11,4),
  PREV_NOTE_CRIBL numeric (11,4))
;

CREATE TABLE ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR (64)  NOT NULL, 
  ID_BLINE            VARCHAR (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR (32)  NOT NULL, 
  NBBETTER     numeric (10) DEFAULT 0, 
  NBWORST      numeric (10) DEFAULT 0, 
  NBSTABLE     numeric (10) DEFAULT 0)
;

CREATE TABLE CAQS_USER
(
   ID_USER varchar(255) NOT NULL,
   PASSWORD varchar(255),
   FIRSTNAME varchar(255),
   LASTNAME varchar(255),
   EMAIL varchar(255),
   CREATEDDATE timestamp,
   LASTLOGINTIME timestamp
)
;

CREATE TABLE USER_ROLE (
  ID_USER   varchar (255) NOT NULL,
  ID_ROLE varchar (64) NOT NULL)
;

