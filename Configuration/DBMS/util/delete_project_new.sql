-- ${ID_PRO} is to be replaced with the actual project Id for the project that has to be deleted
delete FROM Baseline_links WHERE parent_id_bline in (select id_bline from baseline where PRO_BLINRE = '${ID_PRO}') or child_id_bline in (select id_bline from baseline where PRO_BLINRE = '${ID_PRO}');
delete FROM volumetry WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM archi_link WHERE ID_PROJ in (SELECT id_elt From Element Where ID_PRO='${ID_PRO}');
delete FROM link_elt_bline WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM link_real WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM poids_fact_crit WHERE BLINE_POIDS in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM facteur_bline WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM labellisation WHERE ID_PRO='${ID_PRO}';
delete FROM element_baseline_info WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM criterenoterepartition WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM justification WHERE ID_JUST in (Select distinct j.ID_JUST From Justification j, Critere_bline c Where c.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}') AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST);
delete FROM justification WHERE ID_JUST in (Select j.JUST_LINK From Justification j, Critere_bline c Where c.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}') AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST);
delete FROM critere_bline WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM qametrique WHERE ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='${ID_PRO}');
delete FROM elt_links WHERE ELT_FILS in (Select ID_ELT From Element Where ID_PRO='${ID_PRO}');
delete FROM Package where ID_PACK in (Select ID_PACK From Element Where ID_PRO='${ID_PRO}');
delete FROM Element WHERE ID_PRO='${ID_PRO}';
delete FROM Baseline WHERE PRO_BLINRE = '${ID_PRO}';
delete FROM Projet WHERE ID_PRO = '${ID_PRO}';
