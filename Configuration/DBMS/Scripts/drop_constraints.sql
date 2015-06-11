
alter table ARCHI_LINK drop constraint DU_PROJET_ARCHILINK;                     
alter table ARCHI_LINK drop constraint FROM_LINK;                               
alter table ARCHI_LINK drop constraint TO_LINK;                                 

alter table BASELINE drop constraint PROJET_BASELINE;                           

alter table BLOB_TABLE drop constraint FICHE_BLOB_TABLE;                        

alter table CRITERE_BLINE drop constraint BL_CRI_BLINE;                         
alter table CRITERE_BLINE drop constraint CRI_CRI_BLINE;                        
alter table CRITERE_BLINE drop constraint ELT_CRITERE_BLINE;                    
alter table CRITERE_BLINE drop constraint JUS_CRI_BLINE;                        

alter table CRITERE_USAGE drop constraint CRITERE_USAGE_CRIT;                   
alter table CRITERE_USAGE drop constraint CRITERE_USAGE_TELT;                   
alter table CRITERE_USAGE drop constraint CRITERE_USAGE_USAGE;
alter table CRITERE_USAGE drop constraint Critere_usage_Critere;

alter table DIALECTE drop constraint LANGAGE_DIALECTE;                          

alter table DROITS drop constraint ELEMENT_DROITS;                              

alter table ELEMENT drop constraint PACKAGE_ELT;                                
alter table ELEMENT drop constraint PROJET_ELEMENT;                             
alter table ELEMENT drop constraint TYPE_ELT_ELT;                               

alter table ELT_LINKS drop constraint ELT_FILS;                                 
alter table ELT_LINKS drop constraint ELT_PERE;                                 

alter table FACTEUR_BLINE drop constraint BLINE_FAC_BLINE;                      
alter table FACTEUR_BLINE drop constraint ELT_FACTEUR_BLINE;                    
alter table FACTEUR_BLINE drop constraint FAC_FAC_BLINE;                        
alter table FACTEUR_BLINE drop constraint JUS_FAC_BLINE;                        
alter table FACTEUR_BLINE drop constraint LAB_FAC_BLINE;                        

alter table FACTEUR_CRITERE drop constraint FACTEUR_FACTEUR_CRITERE;            
alter table Facteur_critere drop constraint facteur_critere_critere_usage;
alter table Facteur_critere drop constraint Facteur_critere_Facteur;

alter table JUSTIFICATION drop constraint JUSTIFICATION_FK21036491497145;       

alter table LABELLISATION drop constraint TYPLAB_LABEL;                         

alter table LINK_REAL drop constraint LINK_REAL_FK31037977673290;               
alter table LINK_REAL drop constraint LINK_REAL_FK51037977693018;               

alter table METRIQUE drop constraint CAT_MET;                                   
alter table METRIQUE drop constraint TEC_METRIQUE;                              
alter table METRIQUE drop constraint TYPE_MET_METRIQUE;                         
alter table METRIQUE drop constraint OUTILS_METRIQUE;                         

alter table PACKAGE drop constraint DU_PROJET;                                  
alter table PACKAGE drop constraint NIV_PACKAGE;                                

alter table POIDS_FACT_CRIT drop constraint BLINE_POIDS;                        
alter table POIDS_FACT_CRIT drop constraint CRI_BLINE_POIDS;                    
alter table POIDS_FACT_CRIT drop constraint FAC_BLINE_POIDS;                    

alter table PROJET drop constraint USAGE_PROJET;                                

alter table QAMETRIQUE drop constraint ELT_QAMETRIQUE;                          
alter table QAMETRIQUE drop constraint JUS_QAMETRIQUE;                          
alter table QAMETRIQUE drop constraint MET_QAMETRIQUE;                          
alter table QAMETRIQUE drop constraint QAMETRIQUE_BLINE;                        

alter table Regle drop constraint regle_critere_usage;
alter table Regle drop constraint Regle_Metrique;

alter table SEUIL drop constraint METRIQUE_SEUIL;                               
alter table SEUIL drop constraint USAGE_SEUIL;                                  

alter table CRITERENOTEREPARTITION drop constraint FK_CRITERE_BLINE;                                  

alter table VOLUMETRY drop constraint FK_VOLUMETRY_ELEMENT;                                  
alter table VOLUMETRY drop constraint FK_VOLUMETRY_TYPE_ELEMENT;                                  
alter table VOLUMETRY drop constraint FK_VOLUMETRY_BASELINE;                                  

