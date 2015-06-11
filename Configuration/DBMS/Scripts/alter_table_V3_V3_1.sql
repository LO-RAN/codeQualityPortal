update droits set id_profil_user=trim(id_profil_user);
commit;

-- Suppression des règles OptimalAdvisor inutilisées
Delete from critere_usage where id_crit in (select distinct id_crit from regle where id_met in ('R0010','R0006','R0017','R0018','R0023','R0024')) and id_crit like 'R%';
Delete from facteur_critere where id_crit in (select distinct id_crit from regle where id_met in ('R0010','R0006','R0017','R0018','R0023','R0024')) and id_crit like 'R%';
Delete from regle where id_met in ('R0010','R0006','R0017','R0018','R0023','R0024');
commit;

INSERT INTO METRIQUE (ID_MET,LIB_MET,DESC_MET,TYPE_MET,CAT_MET,OUTIL_MET,TECHNO_MET,DINST_MET,DMAJ_MET,DAPPLICATION_MET,DPEREMPTION_MET) VALUES ('UNUSEDPRIVATEFIELD','UnusedPrivateField','Unused private field',null,null,'OPTIMALADVISOR',null,null,null,null,null);
commit;

Update regle set id_met='UNUSEDLOCALVARIABLE' where id_met='UNUSEDLOCALVARIABLECHECK';
Update regle set id_met='UNUSEDFORMALPARAMETER' where id_met='UNUSEDPARAMETERCHECK';
Update regle set id_met='UNUSEDPRIVATEMETHOD' where id_met='UNUSEDPRIVATEMETHODCHECK';
Update regle set id_met='UNUSEDPRIVATEFIELD' where id_met='UNUSEDPRIVATEFIELDCHECK';
commit;

INSERT INTO regle (ID_CRIT,ID_MET,ID_USA,LIB_REG,DESC_REG,DINST_REG,DMAJ_REG) VALUES ('LOOSECOUPLING','LOOSECOUPLING','JAVA-RULES',null,null,null,null);
commit;

-- UnifaceView pages and components titles
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','about','en','About');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','about','fr','A propos');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ap','en','About');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ap','fr','A propos');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_do','en','Documentation');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_do','fr','Documentation');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_em','en','Model export');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_em','fr','Export modèle');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ep','en','Project export');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ep','fr','Export projet');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_et','en','Task execution');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_et','fr','Exécution des tâches');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_hi','en','History');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_hi','fr','Historique');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_im','en','Import');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_im','fr','Import');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ju','en','Justification');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_ju','fr','Justification');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_lt','en','Task list');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_lt','fr','Liste des tâches');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_na','en','Browser');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_na','fr','Navigateur');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_tr','en','Translations');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','c_tr','fr','Traductions');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','connect','en','Connect');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','connect','fr','Connecter');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','home','en','Disconnect');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','home','fr','Déconnecter');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','id','en','Id');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','id','fr','Identifiant');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','login','en','Login');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','login','fr','Connexion');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','loginTitle','en','Portal login');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','loginTitle','fr','Connexion au portail');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_adm','en','Administration');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_adm','fr','Administration');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_arc','en','Architecture');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_arc','fr','Architecture');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_bdc','en','Knowledge base');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_bdc','fr','Base de connaissances');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_doc','en','Documentation');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_doc','fr','Documentation');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_jus','en','Justification');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_jus','fr','Justification');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_lab','en','Labelling');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_lab','fr','Labelisation');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_pil','en','Analysis control');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_pil','fr','Pilotage');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_qua','en','Quality management');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_qua','fr','Gestion qualité');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_vie','en','_ Admin View _');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','p_vie','fr','_ Admin View _');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','password','en','Password');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','password','fr','Mot de passe');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','dashboard','en','Process management dashboard');
INSERT INTO i18n (TABLE_NAME,COLUMN_NAME,ID_TABLE,ID_LANGUE,TEXT) VALUES ('unifaceview','lib','dashboard','fr','Console de suivi des processus');
commit;

-- missing labels/descriptions for element types
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'PKG', 'fr', 'Package');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'PKG', 'en', 'Package');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'CLS', 'fr', 'Classe');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'CLS', 'en', 'Class');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'MET', 'fr', 'Méthode');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'MET', 'en', 'Method');

insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'PRJ', 'fr', 'Projet');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'PRJ', 'en', 'Project');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'SSP', 'fr', 'Sous-projet');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'SSP', 'en', 'Sub-project');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'EA', 'fr', 'Entité applicative');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'desc', 'EA', 'en', 'Application entity');

insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'PRJ', 'fr', 'Projet');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'PRJ', 'en', 'Project');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'SSP', 'fr', 'Sous-projet');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'SSP', 'en', 'Sub-project');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'EA', 'fr', 'Entité applicative');
insert into I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES ('type_element', 'lib', 'EA', 'en', 'Application entity');
commit;
