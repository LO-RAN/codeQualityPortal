CREATE TABLE ARCHI_LINK ( 
  ID_LINK  varchar2 (32)  NOT NULL, 
  ID_FROM  varchar2 (32)  NOT NULL, 
  ID_TO    varchar2 (32)  NOT NULL, 
  ID_PROJ  varchar2 (64)  NOT NULL,
  TYPE     number   (12))
;

CREATE TABLE BASELINE ( 
  ID_BLINE            varchar2 (32)  NOT NULL, 
  TYPE_BLINE          varchar2 (32), 
  LIB_BLINE           varchar2 (32), 
  DESC_BLINE          varchar2 (128), 
  PRO_BLINRE          varchar2 (32)  NOT NULL, 
  DINST_BLINE         date, 
  DMAJ_BLINE          date, 
  DAPPLICATION_BLINE  date, 
  DPREMPTION_BLINE    date, 
  CUSER_BLINE         varchar2 (16))
;

CREATE TABLE CATEGORIE ( 
  ID_CAT            varchar2 (32)  NOT NULL, 
  LIB_CAT           varchar2 (32), 
  DESC_CAT          varchar2 (128), 
  CUSER_CAT         varchar2 (16), 
  DINST_CAT         date, 
  DMAJ_CAT          date, 
  DAPPLICATION_CAT  date, 
  DPEREMPTION_CAT   date)
;

CREATE TABLE CRITERE ( 
  ID_CRIT            varchar2 (64)  NOT NULL, 
  DINST_CRIT         date, 
  DMAJ_CRIT          date, 
  DAPPLICATION_CRIT  date, 
  DPEREMPTION_CRIT   date)
;

CREATE TABLE CRITERE_USAGE ( 
  ID_CRIT  varchar2 (64)  NOT NULL, 
  ID_USA   varchar2 (32)  NOT NULL, 
  ID_TELT  varchar2 (32)  NOT NULL)
; 

CREATE TABLE CRITERE_BLINE ( 
  ID_CRIT          varchar2 (64)  NOT NULL, 
  ID_BLINE         varchar2 (32)  NOT NULL, 
  ID_PRO           varchar2 (32)  NOT NULL, 
  ID_ELT           varchar2 (64)  NOT NULL, 
  DINST_CRIBL      date, 
  DMAJ_CRIBL       date, 
  NOTE_CRIBL       number (11,4), 
  ID_JUST          varchar2 (32), 
  JUST_NOTE_CRIBL  number (11,4), 
  TENDANCE         number (11,4) DEFAULT 0,
  COST             number (11,4),
  CRITERION_COMMENT varchar2 (4000))
;

CREATE TABLE DIALECTE ( 
  ID_DIALECTE    varchar2 (16)  NOT NULL, 
  ID_LANGAGE     varchar2 (16), 
  LIB_DIALECTE   varchar2 (32), 
  DESC_DIALECTE  varchar2 (128))
;

CREATE TABLE DROITS ( 
  ID_ELT          varchar2 (64)  NOT NULL, 
  TYPE_ACCES      varchar2 (1)  NOT NULL, 
  ID_PROFIL_USER  varchar2 (32)  NOT NULL, 
  DROITS          number (5))
;

CREATE TABLE ELEMENT ( 
  ID_ELT         varchar2 (64)  NOT NULL, 
  ID_PRO         varchar2 (32)  NOT NULL, 
  ID_USA         varchar2 (32), 
  ID_TELT        varchar2 (32),
  ID_MAIN_ELT    varchar2(64), 
  ID_PACK        varchar2 (32), 
  STREAM_ELT     varchar2 (128), 
  ID_DIALECTE    varchar2 (16), 
  LIB_ELT        varchar2 (128), 
  DESC_ELT       varchar2 (1024), 
  CUSER_ELT      varchar2 (16), 
  DINST_ELT      date, 
  DPEREMPTION    date, 
  DMAJ_ELT       date, 
  POIDS_ELT      INTEGER, 
  PVOBNAME       varchar2 (128), 
  VOBMOUNTPOINT  varchar2 (128), 
  MAKEFILE_DIR   varchar2 (128), 
  SOURCE_DIR     varchar2 (128), 
  BIN_DIR        varchar2 (128), 
  PERIODIC_DIR   varchar2 (128),
  LIB            varchar2 (3500),
  FILEPATH       varchar2(2048),
  LINEPOS        INTEGER,
  ID_STEREOTYPE  varchar2 (64),
  INFO1          varchar2 (512),
  INFO2          varchar2 (512),
  SCM_REPOSITORY varchar2(128),
  SCM_MODULE     varchar2(128),
  PROJECT_FILE_PATH varchar2(128))
;

CREATE TABLE ELEMENT_BASELINE_INFO ( 
  ID_ELT            varchar2 (64)  NOT NULL, 
  ID_BLINE            varchar2 (32)  NOT NULL, 
  ID_MAIN_ELT            varchar2 (32)  NOT NULL, 
  NOTE1        number (10)  NOT NULL, 
  NOTE2        number (10)  NOT NULL, 
  NOTE3        number (10)  NOT NULL, 
  NOTE4        number (10)  NOT NULL, 
  NBBETTER     number (10), 
  NBWORST      number (10), 
  NBSTABLE     number (10),
  START_LINE number DEFAULT 0)
;

CREATE TABLE ELT_LINKS ( 
  ELT_PERE            varchar2 (64)  NOT NULL, 
  ELT_FILS            varchar2 (64)  NOT NULL, 
  DINST_LINKS         date, 
  DMAJ_LINKS          date, 
  DAPPLICATION_LINKS  date, 
  DPEREMPTION_LINKS   date,
  TYPE                CHAR(1)  DEFAULT 'L')
;

CREATE TABLE FACTEUR ( 
  ID_FACT            varchar2 (32)  NOT NULL, 
  DINST_FACT         date, 
  DMAJ_FACT          date, 
  DAPPLICATION_FACT  date, 
  DPEREMPTION_FACT   date)
;

CREATE TABLE FACTEUR_BLINE ( 
  ID_FAC            varchar2 (32)  NOT NULL, 
  ID_BLINE          varchar2 (32)  NOT NULL, 
  ID_PRO            varchar2 (32)  NOT NULL, 
  ID_ELT            varchar2 (64)  NOT NULL, 
  DINST_FACBL       date, 
  DMAJ_FACBL        date, 
  NOTE_FACBL        number (11,4), 
  ID_LABEL          varchar2 (32), 
  LABEL_NOTE_FACBL  number (11,4), 
  TENDANCE          number (11,4) DEFAULT 0,
  FACTEUR_COMMENT   varchar2 (4000))
;

CREATE TABLE FACTEUR_CRITERE ( 
  ID_USA     varchar2 (32)  NOT NULL,
  ID_FACT    varchar2 (32)  NOT NULL, 
  ID_CRIT    varchar2 (64)  NOT NULL, 
  POIDS      number (11,4),
  DINST_REG  date, 
  DMAJ_REG   date)
;

CREATE TABLE I18N ( 
  TABLE_NAME           varchar2 (32)  NOT NULL, 
  COLUMN_NAME          varchar2 (32)  NOT NULL, 
  ID_TABLE             varchar2 (128)  NOT NULL, 
  ID_LANGUE            varchar2 (32)  NOT NULL, 
  TEXT                 varchar2 (4000)  NOT NULL)
;

CREATE TABLE JUSTIFICATION ( 
  ID_JUST           varchar2 (32)  NOT NULL, 
  LIB_JUST          varchar2 (32), 
  DESC_JUST         varchar2 (128), 
  DINST_JUST        date, 
  DMAJ_JUST         date, 
  STATUT_JUST       varchar2 (32), 
  PROPAGATION_JUST  varchar2 (1), 
  CUSER_JUST        varchar2 (16), 
  JUST_LINK         varchar2 (32))
;

CREATE TABLE LABELLISATION ( 
  ID_LABEL      varchar2 (32)  NOT NULL, 
  CUSER_LABEL   varchar2 (16), 
  ID_PRO        varchar2 (32)  NOT NULL, 
  ID_BLINE      varchar2 (32)  NOT NULL, 
  ID_TYPLAB     varchar2 (32), 
  LIB_LABEL     varchar2 (32), 
  DESC_LABEL    varchar2 (128), 
  DINST_LABEL   date, 
  DMAJ_LABEL    date, 
  STATUT_LABEL  varchar2 (16), 
  LABEL_LINK    varchar2 (32))
;

CREATE TABLE LANGAGE ( 
  ID_LANGAGE  varchar2 (16)  NOT NULL, 
  LIB_LANG    varchar2 (32), 
  DESC_LANG   varchar2 (128))
;

CREATE TABLE LANGUE ( 
  ID  varchar2 (32)  NOT NULL, 
  LIB    varchar2 (32), 
  DESCRIPTION   varchar2 (128))
;

CREATE TABLE LINK_ELT_BLINE ( 
  LINK_ID       varchar2 (32)  NOT NULL, 
  ELT_FROM_ID   varchar2 (32)  NOT NULL, 
  ELT_TO_ID     varchar2 (32)  NOT NULL, 
  ID_BLINE      varchar2 (32)  NOT NULL, 
  REAL_LINK_ID  varchar2 (32),
  TYPE          varchar2 (32))
;

CREATE TABLE LINK_REAL ( 
  ID_LINK   varchar2 (32)  NOT NULL, 
  ID_FROM   varchar2 (32), 
  ID_TO     varchar2 (32), 
  ID_PROJ   varchar2 (64)  NOT NULL, 
  ID_BLINE  varchar2 (32)  NOT NULL, 
  STATE     number (2)    NOT NULL)
;

CREATE TABLE METRIQUE ( 
  ID_MET            varchar2 (64)  NOT NULL, 
  TYPE_MET          varchar2 (16), 
  CAT_MET           varchar2 (32), 
  OUTIL_MET         varchar2 (40), 
  TECHNO_MET        varchar2 (32), 
  DINST_MET         date, 
  DMAJ_MET          date, 
  DAPPLICATION_MET  date, 
  DPEREMPTION_MET   date,
  COST_CORRECTION   varchar2(512))
;

CREATE TABLE NIVEAU ( 
  ID_NIV            varchar2 (32)  NOT NULL, 
  LIB_NIV           varchar2 (32), 
  IDESC_NIV         varchar2 (128), 
  DINST_NIV         date, 
  DMAJ_NIV          date, 
  CUSER_NIV         varchar2 (16), 
  DAPPLICATION_NIV  date, 
  DPEREMPTION_NIV   date)
;

CREATE TABLE PACKAGE ( 
  ID_PACK            varchar2 (32)  NOT NULL, 
  ID_NIV             varchar2 (32), 
  LIB_PACK           varchar2 (1024), 
  DESC_PACK          varchar2 (128), 
  DINST_PACK         date, 
  DMAJ_PACK          date, 
  DAPPLICATION_PACK  date, 
  DPEREMPTION_PACK   date, 
  COEFF_PACK         number (7,2), 
  ID_PROJ            varchar2 (64), 
  X                  number (12), 
  Y                  number (12), 
  WIDTH              number (12), 
  HEIGHT             number (12),
  ZORDER             number (12),
  TYPE               number (12),
  PARENT_PACKAGE     varchar2(32))
;

CREATE TABLE POIDS_FACT_CRIT ( 
  ID_FACT             varchar2 (32)  NOT NULL, 
  ID_CRIT             varchar2 (64)  NOT NULL, 
  BLINE_POIDS         varchar2 (32)  NOT NULL, 
  ID_PRO              varchar2 (32)  NOT NULL, 
  ID_ELT              varchar2 (64)  NOT NULL, 
  DINST_POIDS         date, 
  DMAJ_POIDS          date, 
  CUSER_POIDS         varchar2 (16), 
  DAPPLICATION_POIDS  date          NOT NULL, 
  DPEREMPTION_POIDS   date, 
  VALEUR_POIDS        number (11,4))
;

CREATE TABLE PROJET ( 
  ID_PRO     varchar2 (32)  NOT NULL, 
  LIB_PRO    varchar2 (128), 
  DESC_PRO   varchar2 (128), 
  DINST_PRO  date, 
  DMAJ_PRO   date, 
  DCLOS_PRO  date, 
  TYPE       INTEGER       DEFAULT 0)
;

CREATE TABLE QAMETRIQUE ( 
  ID_ELT               varchar2 (64)  NOT NULL, 
  ID_MET               varchar2 (64)  NOT NULL, 
  ID_BLINE             varchar2 (32)  NOT NULL, 
  DINST_QAMET          date, 
  DMAJ_QAMET           date, 
  CUSER_QAMET          varchar2 (16), 
  DAPPLICATION_QAMET   date, 
  DPEREMPTION_QAMET    date, 
  VALBRUTE_QAMET       number (15,4), 
  NOTECALC_QAMET       number (11,4), 
  JUST_MET             varchar2 (32), 
  JUST_VALBRUT_QAMET   number (11,4), 
  JUST_NOTECALC_QAMET  number (11,4), 
  TENDANCE             number (15,4) DEFAULT 0,
  LIGNES               varchar2 (2048))
;

CREATE TABLE REGLE ( 
  ID_CRIT    varchar2 (64)  NOT NULL, 
  ID_MET     varchar2 (64)  NOT NULL,
  ID_USA     varchar2 (32)  NOT NULL,
  DINST_REG  date, 
  DMAJ_REG   date)
;

CREATE TABLE SEUIL ( 
  ID_SEUIL            varchar2 (16)  NOT NULL, 
  ID_MET              varchar2 (64)  NOT NULL, 
  ID_TEC              varchar2 (32), 
  USAGE_SEUIL         varchar2 (32), 
  NOTE_SEUIL          number (9,2), 
  OPERATEUR_SEUIL     varchar2 (8), 
  HBORNE_SEUIL        number (8), 
  LBORNE_SEUIL        number (8), 
  USA_SEUIL           varchar2 (32), 
  DINST_SEUIL         date, 
  DMAJ_SEUIL          date, 
  DAPPLICATION_SEUIL  date, 
  DPEREMPTION_SEUIL   date)
;

CREATE TABLE SEVERITE ( 
  ID_SEV    varchar2 (3)  NOT NULL, 
  LIB_SEV   varchar2 (32), 
  DESC_SEV  varchar2 (128))
;

CREATE TABLE STEREOTYPE (
   ID_STEREOTYPE       varchar2 (64) NOT NULL,
   LIB_STEREOTYPE      varchar2 (64),
   DESC_STEREOTYPE     varchar2 (256))
;

CREATE TABLE TECHNOLOGIE ( 
  ID_TEC    varchar2 (32)  NOT NULL, 
  LIB_TEC   varchar2 (64), 
  DESC_TEC  varchar2 (255))
;

CREATE TABLE TYPE_ELEMENT ( 
  ID_TELT            varchar2 (32)  NOT NULL, 
  DINST_TELT         date, 
  DMAJ_TELT          date, 
  DAPPLICATION_TELT  date, 
  DPEREMPTION_TELT   date, 
  CUSER_TELT         varchar2 (16),
  HAS_SOURCE         number(1) DEFAULT 0,
  IS_FILE            number(1) DEFAULT 0)
;

CREATE TABLE TYPE_FICHE ( 
  ID_TFICHE     varchar2 (64)  NOT NULL, 
  LIB_TFICHE    varchar2 (32), 
  DESC_TFICHE   varchar2 (128), 
  DINST_TFICHE  date, 
  DMAJ_TFICHE   date)
;

CREATE TABLE TYPE_LABELLISATION ( 
  ID_TYPLAB            varchar2 (32)  NOT NULL, 
  LIB_TYPLAB           varchar2 (32), 
  DESC_TYPLAB          varchar2 (128), 
  DINST_TYPLAB         date, 
  DMAJ_TYPLAB          date, 
  DAPPLICATION_TYPLAB  date, 
  DPEREMPTION_TYPLAB   date)
;

CREATE TABLE TYPE_METRIQUE ( 
  ID_TMET         varchar2 (16)  NOT NULL, 
  LIB_TMET        varchar2 (32), 
  DESC_TMET       varchar2 (128), 
  OPERATEUR_TMET  varchar2 (20))
;

CREATE TABLE MODELE ( 
  ID_USA            varchar2 (32)  NOT NULL, 
  DINTS_USA         date, 
  DMAJ_USA          date, 
  DAPPLICATION_USA  date, 
  DPEREMPTION_USA   date,
  APU_LIMIT_MEDIUM  DECIMAL DEFAULT 10,
  APU_LIMIT_LONG    DECIMAL DEFAULT 20
  )
;

CREATE TABLE OUTILS (
  ID_OUTILS       varchar2 (40) NOT NULL
  )
;

CREATE TABLE CRITERENOTEREPARTITION ( 
  ID_ELT    varchar2 (64)  NOT NULL, 
  ID_BLINE  varchar2 (32)  NOT NULL, 
  ID_CRIT   varchar2 (64)  NOT NULL, 
  SEUIL     INTEGER       NOT NULL, 
  TOTAL     INTEGER       NOT NULL)
;

CREATE TABLE VOLUMETRY ( 
  ID_ELT    varchar2 (64)  NOT NULL, 
  ID_TELT   varchar2 (32)  NOT NULL, 
  ID_BLINE  varchar2 (32)  NOT NULL, 
  TOTAL     INTEGER       NOT NULL, 
  CREATED   INTEGER       NOT NULL, 
  DELETED   INTEGER       NOT NULL)
;

CREATE TABLE OUTILS_MODELE (
  ID_USA    varchar2 (40) NOT NULL,
  ID_OUTILS varchar2 (40) NOT NULL)
;
   
CREATE TABLE BASELINE_LINKS ( 
  PARENT_ID_BLINE     varchar2 (32)  NOT NULL, 
  CHILD_ID_BLINE      varchar2 (32)  NOT NULL,
  CHILD_ID_ELT        varchar2 (64)  NOT NULL)
;

CREATE TABLE CAQS_ROLE ( 
  ID_ROLE       varchar2 (64)  NOT NULL, 
  PORTAL_ROLE   varchar2 (64)  NOT NULL)
;

CREATE TABLE CAQS_ACCESS_DEFINITION ( 
  ID_ACCESS     varchar2 (64)  NOT NULL, 
  LIB_ACCESS    varchar2 (128)  NOT NULL)
;

CREATE TABLE CAQS_ACCESS_RIGHTS ( 
  ID_ACCESS    varchar2 (64)  NOT NULL, 
  ID_ROLE      varchar2 (64)  NOT NULL)
;

CREATE TABLE CAQS_TASKS (
	ID_TASK		varchar2(32) NOT NULL,
	TYPE_TASK	varchar2(32) NOT NULL,
	SHOW_USER	varchar2(32)
)
;

CREATE TABLE CAQS_MESSAGES (
	ID_MESSAGE 	varchar2(32) NOT NULL,
	ID_ELT		varchar2(64) NOT NULL,
	ID_BLINE	varchar2(32) NOT NULL,
	ID_USER		varchar2(32) NOT NULL,
	ID_TASK		varchar2(32) NOT NULL,
	STATUS		varchar2(32),
	BEGIN_DATE	date,
	END_DATE	date,
	COMPLETION_PCT		DECIMAL,
	SEEN		number(1),
	INFO1		varchar2(256),
	OTHERID		varchar2(32)
)
;

CREATE TABLE USER_SETTINGS ( 
  USER_ID         varchar2 (64)   NOT NULL, 
  SETTING_ID      varchar2 (64)   NOT NULL,
  SETTING_VALUE      varchar2 (256)   NOT NULL)
;

CREATE TABLE SETTINGS ( 
  SETTING_ID      varchar2 (64)   NOT NULL, 
  SETTING_DESC      varchar2 (256)   NOT NULL,
  SETTING_DEFAULT   varchar2 (256))
;

CREATE TABLE SETTINGS_VALUES ( 
  SETTING_ID      varchar2 (64)   NOT NULL, 
  SETTING_VALUE      varchar2 (256)   NOT NULL)
;

CREATE TABLE ACTION_PLAN (
  ID_ACTION_PLAN      varchar2 (64)   NOT NULL, 
  ID_ELT              varchar2 (64)   NOT NULL, 
  ID_BLINE            varchar2 (32)   NOT NULL,
  ACTION_PLAN_COMMENT varchar2 (1024),
  COMMENT_USER        varchar2 (64))
;

CREATE TABLE ACTION_PLAN_UNIT ( 
  UNIT_ID             VARCHAR2 (64)   NOT NULL, 
  UNIT_DESC           VARCHAR2 (256),
  NB_ACTION_PLAN_UNIT INTEGER default 1
  )
;

CREATE TABLE COPY_PASTE_REF (
  ID_COPY        varchar2 (64)  NOT NULL, 
  ID_BLINE       varchar2 (32)  NOT NULL,
  ID_ELT         varchar2 (64)  NOT NULL, 
  LINE           number  NOT NULL,
  LENGTH         number  NOT NULL)
;

CREATE TABLE ACTION_PLAN_CRITERION ( 
  ID_ACTION_PLAN    varchar2 (64)   NOT NULL,
  ID_CRIT           varchar2 (64)   NOT NULL,
  PRIORITY          varchar2 (32),
  SELECTED          number   (1)    NOT NULL,
  CRITERION_COMMENT       VARCHAR2(4000),
  COMMENT_USER      varchar2 (64),
  MASTER_ELEMENT    VARCHAR2 (64));

CREATE TABLE STATS (
  PERIOD  DATE NOT NULL,
  SUMCONN NUMBER NOT NULL,
  MAXCONN NUMBER NOT NULL)
;

CREATE TABLE TMP_ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL,
  ID_CRIT VARCHAR(64) NOT NULL,
  NOTE_CRIBL    NUMBER (11,4),
  PREV_NOTE_CRIBL NUMBER (11,4))
;

CREATE TABLE ELEMENT_EVOLUTION_INFO ( 
  ID_ELT            VARCHAR2 (64)  NOT NULL, 
  ID_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_PREV_BLINE            VARCHAR2 (32)  NOT NULL, 
  ID_MAIN_ELT            VARCHAR2 (32)  NOT NULL, 
  NBBETTER     NUMBER (10) DEFAULT 0, 
  NBWORST      NUMBER (10) DEFAULT 0, 
  NBSTABLE     NUMBER (10) DEFAULT 0)
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
  ID_ROLE varchar2 (64) NOT NULL)
;

