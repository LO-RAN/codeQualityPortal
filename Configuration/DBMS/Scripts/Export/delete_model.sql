DELETE FROM FACTEUR_CRITERE where id_usa = '&1';
commit;
DELETE FROM REGLE where id_usa = '&1';
commit;
DELETE FROM CRITERE_USAGE where id_usa = '&1';
commit;
DELETE FROM USAGE where id_usa = '&1';
commit;
quit
