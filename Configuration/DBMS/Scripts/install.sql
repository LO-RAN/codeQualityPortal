-- to be run as pqlapp/pqlapp
@create_tables.sql;
@add_constraints.sql;
@create_indexes.sql;
set define off
@insert_data.sql;
