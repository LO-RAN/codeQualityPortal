accept IDBASELINE char prompt 'Enter the id of the baseline to delete: ';

DELETE FROM Qametrique where id_bline = '&IDBASELINE';
commit;
DELETE FROM poids_fact_crit where bline_poids = '&IDBASELINE';
commit;
delete from criterenoterepartition where id_bline = '&IDBASELINE';
commit;
DELETE FROM critere_bline where id_bline = '&IDBASELINE';
commit;
DELETE FROM facteur_bline where id_bline = '&IDBASELINE';
commit;
DELETE FROM link_elt_bline where id_bline = '&IDBASELINE';
commit;
DELETE FROM link_real where id_bline = '&IDBASELINE';
commit;
DELETE FROM CritereNoteRepartition where id_bline = '&IDBASELINE';
commit;
DELETE FROM ELEMENT_BASELINE_INFO where id_bline = '&IDBASELINE';
commit;
DELETE FROM Baseline where id_bline = '&IDBASELINE';
commit;
