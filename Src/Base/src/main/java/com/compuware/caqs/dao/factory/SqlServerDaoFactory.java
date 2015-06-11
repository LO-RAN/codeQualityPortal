package com.compuware.caqs.dao.factory;

import com.compuware.caqs.dao.dbms.sqlserver.TendanceSqlServerDao;
import com.compuware.caqs.dao.interfaces.TendanceDao;

public class SqlServerDaoFactory extends DbmsDaoFactory {

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getTendanceDao()
	 */
	public TendanceDao getTendanceDao() {
		return TendanceSqlServerDao.getInstance();
	}

	
}
