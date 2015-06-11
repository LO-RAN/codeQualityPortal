@echo off
set PRG_DIR=%~dp0

call %PRG_DIR%process_detail %PRG_DIR%S1_Paiement_des_rentes\paiement_des_rentes_IHM
call %PRG_DIR%process_detail %PRG_DIR%S1_Paiement_des_rentes\batch_2021
call %PRG_DIR%process_detail %PRG_DIR%S1_Paiement_des_rentes\batch_2023
call %PRG_DIR%process_detail %PRG_DIR%S2_Paiement_des_capitaux\paiement_des_capitaux_IHM
call %PRG_DIR%process_detail %PRG_DIR%S2_Paiement_des_capitaux\batch_2030
call %PRG_DIR%process_detail %PRG_DIR%S2_Paiement_des_capitaux\batch_2031
call %PRG_DIR%process_detail %PRG_DIR%S3_Listes_collectives\listes_collectives_IHM
call %PRG_DIR%process_detail %PRG_DIR%S3_Listes_collectives\batch_2020
call %PRG_DIR%process_detail %PRG_DIR%S3_Listes_collectives\batch_2022
call %PRG_DIR%process_detail %PRG_DIR%S4_Traitement_des_evenements\traitement_evenement_IHM
call %PRG_DIR%process_detail %PRG_DIR%S4_Traitement_des_evenements\batch_2025
call %PRG_DIR%process_detail %PRG_DIR%S4_Traitement_des_evenements\batch_2027
call %PRG_DIR%process_detail %PRG_DIR%S5_Edition_documents_individuels_en_masse\edition_document_IHM
call %PRG_DIR%process_detail %PRG_DIR%S5_Edition_documents_individuels_en_masse\batch_2032
call %PRG_DIR%process_detail %PRG_DIR%S6_Calcul_de_situation_de_masse\calcul_de_masse_IHM
call %PRG_DIR%process_detail %PRG_DIR%S6_Calcul_de_situation_de_masse\batch_2033
