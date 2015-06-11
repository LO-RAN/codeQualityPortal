select INDEX_NAME, USED from v$object_usage where USED='NO' AND MONITORING='YES';
