Alter Table Elt_links Add TYPE CHAR(1)  DEFAULT 'L';

CREATE INDEX ELT_LINKS_TYPE ON 
  ELT_LINKS(TYPE) 
  TABLESPACE %INDEX_TBS% 
; 

Update Elt_links set type='T' Where elt_fils in (Select id_elt From Element Where id_telt in ('PRJ','SSP', 'EA'));

commit;

DROP INDEX ELEMENTI4;

CREATE INDEX ELEMENTI4 ON 
  ELEMENT(ID_TELT, ID_ELT) 
  TABLESPACE %INDEX_TBS% 
; 

commit;

Delete From Qametrique Where id_met in (Select id_met From Metrique Where outil_met='OPTIMALADVISOR') and valbrute_qamet=0;
commit;
Delete From Qametrique Where id_met in (Select id_met From Metrique Where outil_met='CHECKSTYLE') and valbrute_qamet=0;
commit;
