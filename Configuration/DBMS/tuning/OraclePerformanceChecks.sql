-- Misc. Oracle performance checking and tuning
-- cf. http://www.cryer.co.uk/brian/oracle/tuning.htm

-- Tuning the cache hit ratio
-- The miss ratio should be less than 15%. If this is not the case, 
-- increase the initialisation parameter SHARED_POOL_SIZE.
Select sum(getmisses) / sum(gets) *100 "Miss ratio" From v$rowcache;

-- Tuning the library cache
-- The hit ratio should be 85% (i.e. 0.85). 
-- The reload percent should be very low, 2% (i.e. 0.02) or less. 
-- If this is not the case, increase the initialisation parameter SHARED_POOL_SIZE. 
-- Although less likely, the init.ora parameter OPEN_CURSORS may also need to be increased.
Select sum(pinhits) / sum(pins) * 100 "Hit Ratio", sum(reloads) / sum(pins) *100 "Reload percent" From v$librarycache Where namespace in ('SQL AREA', 'TABLE/PROCEDURE', 'BODY', 'TRIGGER');

-- Tuning the log buffer
-- To tune the value for LOG_BUFFER first determine the space request ratio, 
-- this is the ratio of redo log space requests to redo log requests:
-- If the ratio (redo log space requests / redo entries) is greater than 1:5000, 
-- then increase the size of the redo log buffer until the space request ratio stops falling.
-- Alternately, if memory is not a constraint then try to reduce the number of times 
-- that a process had to wait for the log cache to be flushed:
-- The number of waits should always be zero. If not, increase the size of LOG_BUFFER, 
-- until the number returns to zero. Typically, there is no advantage in setting this beyond 1M.
Select name, value from v$sysstat Where name in ('redo log space requests', 'redo entries');

-- Tuning the buffer cache hit ratio
-- A logical read occurs whenever a user requests data from the database. 
-- It occurs whenever the data is in the buffer cache or whether the user process 
-- must read it from disk. If the data must be read from disk then a physical read occurs. 
-- Oracle keeps track of logical and physical reads in the V$SYSSTAT table.
-- Use the following SQL statement to determine the values required for the hit radio calculation:
-- The cache-hit ratio can be calculated as follows:
-- Hit ratio = 1 - (physical reads / (db block gets + consistent gets))
-- If the cache-hit ratio goes below 90% then:
-- * For Oracle 8 and earlier: increase the initialisation parameter DB_BLOCK_BUFFERS.
-- * For Oracle 9 onwards: increate the initialisation parameter DB_CACHE_SIZE.
Select name, value From v$sysstat Where name in ('db block gets', 'consistent gets', 'physical reads');

-- Tuning sorts
-- Issue the following SQL statement to determine the amount of sorting in memory and on disk:
-- If a large number of sorts require I/O to disk, increase the initialisation parameter SORT_AREA_SIZE.
Select name, value from v$sysstat where name in ('sorts (memory)', 'sorts (disk)');

-- Tuning rollback segments
-- To identify contention for rollback segments first find out the number 
-- of times that processes had to wait for the rollback segment header and blocks. 
-- The V$WAITSTAT view contains this information:
select class, count from v$waitstat where class in ('system undo header', 'system undo block', 'undo header', 'undo block');
-- The number of waits for any class should be compared with the number of 
-- logical reads over the same period of time. This information can be found in V$SYSSTAT:
select sum(value) from v$sysstat where name in ('db block gets', 'consistent gets');
-- If the number of waits for any class of waits is greater than 1% of the total number 
-- of logical reads then add more rollback segments.
-- The following query gives the percentage of times that a request for data resulted 
-- in a wait for a rollback segment:
select round(sum(waits)/sum(gets),2) from v$rollstat;
-- If the percentage is greater than 1% then create more rollback segments.
-- Rollback segments should be isolated as much as possible by placing them 
-- in their own tablespace, preferably on a separate disk from other active tablespaces. 
-- The OPTIMAL parameter is used to cause rollback segments to shrink back to an optimal 
-- size after they have dynamically extended. The V$ROLLSTAT table can help in determining 
-- proper sizing of rollback segments:
Select segment_name, shrinks, aveshrink, aveactive "Avg.Active" from v$rollstat v, dba_rollback_segs d where v.usn = d.segment_id;
-- The following table shows how to interpret these results:
-- 
-- Cumulative  	            Average size  	Recommendation
-- number of shrinks        of shrink
-- --------------------------------------------------------
-- Low 	                    Low 	          If the value for “Avg.Active” is close to OPTIMAL, 
--                                          the settings are correct. If not, then OPTIMAL is too large.
--                                          (Note: Be aware that it is sometimes better to have a larger 
--                                          optimal value - depending on the nature of the applications 
--                                          running, reducing it towards “Avg.Active” may cause some 
--                                          applications to start experiencing ORA-01555.)
-- Low 	                    High 	          Excellent – few, large shrinks.
-- High 	                  Low 	          Too many shrinks – OPTIMAL is too small.
-- High 	                  High 	          Increase OPTIMAL until the number of shrinks is lower.

-- Identifying missing indexes
-- There is no guaranteed way of finding missing indexes. 
--The following is intended to help identify where beneficial indexes do not exist.
-- To find the top SQL statements that have caused most block buffer reads:
Select buffer_gets, sql_text from v$sqlarea where buffer_gets > 10000 order by buffer_gets desc;
-- If this returns a large number of rows then increase the number of ‘buffer_gets’ required, 
-- if it returns no rows then decrease this threshold.
-- Typically, most of these will be select statements of some sort. 
-- Considering each in turn, identify what indexes would help with the query 
-- and then check that those indexes exist. Create them if necessary.
-- To find the most frequently executed SQL:
Select executions, buffer_gets, sql_text from v$sqlarea where executions > 10000 order by executions desc;
-- If this returns a large number of rows then increase the number of ‘executions’ required. 
-- If it returns no rows then decrease the number of executions required.

-- Identify index fragmentation
-- To obtain information about an index: 
analyze index $$index_name$$ validate structure;
-- This populates the table ‘index_stats’. It should be noted that this table contains 
-- only one row and therefore only one index can be analysed at a time.
-- An index should be considered for rebuilding under any of the following conditions:
-- * The percentage of deleted rows exceeds 30% of the total, i.e. if del_lf_rows / lf_rows > 0.3.
-- * If the ‘HEIGHT’ is greater than 4.
-- * If the number of rows in the index (‘LF_ROWS’) is significantly smaller than ‘LF_BLKS’ 
--   this can indicate a large number of deletes, indicating that the index should be rebuilt.

-- Identifying free list contention
-- To identify the percentage of requests that resulted in a wait for a free block
--  run the following query:
select round( (sum(decode(w.class,'free list',count,0)) / (sum(decode(name,'db block gets', value, 0)) + sum(decode(name,'consistent gets', value, 0)))) * 100,2) from v$waitstat w, v$sysstat;
-- This should be less than 1%. To reduce contention for a table’s free list 
-- the table must be recreated with a larger value in the FREELISTS storage parameter.

-- Identify significant reparsing of SQL
-- The shared-pool contains (amongst other things) previously parsed SQL, 
-- and this allows Oracle to avoid re-parsing SQL unnecessarily.
-- The following SQL identifies those SQL statements that have needed to be re-parsed numerous times:
select executions, t.sql_text from v$sqlarea a, v$sqltext t where parse_calls >1 and parse_calls = executions and a.address=t.address and executions > 10000 order by executions desc;
-- If this returns a large number of rows then increase the number of ‘executions’ required. 
-- If it returns no rows then perhaps decrease the number of executions required.
-- If there is SQL that is being repeatedly reparsed then consider increasing the value of SHARED_POOL_SIZE.

-- Reducing database fragmentation
-- Excessively fragmented tables or indexes can adversely affect performance. 
-- Use the following SQL to identify those database objects that have over 10 extents allocated:
select * from dba_segments where extents > 10;
-- In general, if a table or index has more than 10 extents then rebuild it to fit into one extent.
-- A table can only be rebuilt by exporting and then importing it. 
-- The database will be unavailable for use by applications during this time. 
-- The steps to accomplish this are:
--   1. Export the table with COMPRESS=Y
--   2. Drop the table
--  3. Import the table.
-- An index can be rebuilt without preventing others from still using it. 
-- Firstly change the storage parameters to make the ‘next’ storage parameter larger 
-- (perhaps double it). The initial storage value cannot be changed. Then rebuild the index.

-- Rebuilding indexes
-- Periodically, and typically after large deletes or inserts, 
-- it is worth rebuilding indexes. The SQL for this is:
Alter index $$index_name$$ rebuild;
-- Alternatively, the following performs the same, but avoids writing to the redo logs 
-- and thus speeds up the index rebuild:
Alter index $$index_name$$ rebuild unrecoverable;
-- Note: If performing this under Oracle 7.3 then be sure to specify the destination tablespace, ie:
Alter index $$index_name$$ rebuild tablespace $$tablespace$$;
-- Otherwise the index will be moved to the temporary tablespace.

