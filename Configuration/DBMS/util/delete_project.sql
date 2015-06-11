accept IDPROJECT char prompt 'Enter the id of the project to delete: '

DELETE FROM Baseline where pro_blinre = '&IDPROJECT';

DELETE FROM Archi_link where id_proj in (
	Select distinct id_elt From Element Where id_pro='&IDPROJECT'
);
commit;
DELETE FROM Elt_links where elt_fils in (
	Select id_elt From Element Where id_pro='&IDPROJECT'
);
commit;
DELETE FROM Droits where id_elt in (
	Select id_elt From Element Where id_pro='&IDPROJECT'
);
commit;

DELETE FROM Element where id_pro = '&IDPROJECT' And id_pack is not null;
commit;

DELETE FROM Package where id_proj in (
	Select distinct id_elt From Element Where id_pro='&IDPROJECT'
);
commit;

DELETE FROM Element where id_pro = '&IDPROJECT';
commit;

DELETE FROM projet where id_pro = '&IDPROJECT';
commit;
