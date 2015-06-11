package net.sourceforge.jdbcexporter.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import net.sourceforge.jdbcexporter.ExportColumnDef;
import net.sourceforge.jdbcexporter.ExportEngine;
import net.sourceforge.jdbcexporter.ExportEntityDef;
import net.sourceforge.jdbcimporter.ColumnValue;
import net.sourceforge.jdbcimporter.MalformedDataException;
import net.sourceforge.jdbcimporter.util.JDBCParameterHelper;

public class SQLStmtEngine implements ExportEngine 
{
	public static String SELECT_STMT_KEY = "selectStmt";
	
	Connection connection;
	ExportEntityDef  entityDef;

	Statement selectStatement;
	ResultSet resultSet;

	JDBCParameterHelper engineHelper = new JDBCParameterHelper();
	
	public void setConnection(Connection con) 
	{
		this.connection = con;
	}

	public Connection getConnection() 
	{
		return this.connection;
	}

	public void setEntityDef(ExportEntityDef entityDef) 
	{
		this.entityDef = entityDef;
	}

	public void init() throws SQLException 
	{
		String selectStmt = entityDef.getProperty( SELECT_STMT_KEY );
		if ( selectStmt == null )
		{
			throw new SQLException("Property '"+SELECT_STMT_KEY+"' not defined in EntityDef '"+entityDef.getTable()+"'");
		}
		this.selectStatement = connection.createStatement();
		this.resultSet = selectStatement.executeQuery( selectStmt );
	}

	public void cleanup() throws SQLException 
	{
		this.resultSet.close();
		this.selectStatement.close();
	}

	public ColumnValue[] exportRow() throws SQLException 
	{
		if (!resultSet.next())
		{
			return null;
		}
		ExportColumnDef[] columnDefs = entityDef.getColumns();
		
		ColumnValue[] values = new ColumnValue[columnDefs.length];
		for ( int i = 0; i < values.length; i++ )
		{
			values[i] = engineHelper.getColumn( resultSet, i+1, columnDefs[i] );
		}
		return values;
	}

}
