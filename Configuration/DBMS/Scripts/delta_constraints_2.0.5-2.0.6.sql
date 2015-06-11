alter table CRITERE_USAGE drop constraint CRITERE_USAGE_CRIT;                   
alter table CRITERE_USAGE drop constraint CRITERE_USAGE_USAGE;                    
alter table FACTEUR_CRITERE drop constraint CRITERE_FACTEUR_CRITERE;            
alter table FACTEUR_CRITERE drop constraint FACTEUR_FACTEUR_CRITERE;            
alter table FACTEUR_CRITERE drop constraint FACTEUR_CRITERE_USAGE;
alter table REGLE drop constraint CRITERE_REGLE;                                
alter table REGLE drop constraint METRIQUE_REGLE;                               
alter table REGLE drop constraint REGLE_USAGE;                                  
commit;
ALTER  TABLE Critere_usage ADD CONSTRAINT critere_usage_usage FOREIGN KEY (id_usa) REFERENCES USAGE (Id_usa) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
ALTER  TABLE Critere_usage ADD CONSTRAINT Critere_usage_Critere FOREIGN KEY (id_crit) REFERENCES Critere (Id_crit) DEFERRABLE INITIALLY DEFERRED;
ALTER  TABLE Regle ADD CONSTRAINT regle_critere_usage FOREIGN KEY (id_usa, id_crit) REFERENCES Critere_usage (id_usa, id_crit) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
ALTER  TABLE Regle ADD CONSTRAINT Regle_Metrique FOREIGN KEY (id_met) REFERENCES Metrique (Id_met) DEFERRABLE INITIALLY DEFERRED;
ALTER  TABLE Facteur_critere ADD CONSTRAINT facteur_critere_critere_usage FOREIGN KEY (id_usa, id_crit) REFERENCES Critere_usage (id_usa, id_crit) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
ALTER  TABLE Facteur_critere ADD CONSTRAINT Facteur_critere_Facteur FOREIGN KEY (id_fact) REFERENCES Facteur (Id_fact) DEFERRABLE INITIALLY DEFERRED;
commit;

