accept ID_USA char prompt 'Enter the id of the model to delete: '

DELETE FROM FACTEUR_CRITERE where id_usa = '&ID_USA';
commit;
DELETE FROM REGLE where id_usa = '&ID_USA';
commit;
DELETE FROM CRITERE_USAGE where id_usa = '&ID_USA';
commit;
DELETE FROM USAGE where id_usa = '&ID_USA';
commit;
