package net.sourceforge.jdbcimporter.columntranslator;

/*
 * JDBC Importer - database import utility/framework.
 * Copyright (C) 2002 Chris Nagy
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Chris Nagy
 * Email:  cnagyxyz@hotmail.com
 */

import java.sql.Types;

import net.sourceforge.jdbcimporter.ColumnDef;
import net.sourceforge.jdbcimporter.ColumnTranslator;
import net.sourceforge.jdbcimporter.ColumnValue;

public class ReverseColumnTranslator implements ColumnTranslator {

	/* (non-Javadoc)
	 * @see net.sourceforge.jdbcimporter.ColumnTranslator#getValue(net.sourceforge.jdbcimporter.ColumnDef, net.sourceforge.jdbcimporter.ColumnValue)
	 */
	public ColumnValue getValue(ColumnDef column, ColumnValue columnValue) 
	{
		if ( Types.VARCHAR == column.getType() && columnValue.getString() != null )
		{
			String oldValue = columnValue.getString();
			StringBuffer newValue = new StringBuffer("");
			for ( int i = oldValue.length() - 1; i >= 0; i-- )
			{
				newValue.append( oldValue.charAt(i) );
			}
			ColumnValue newColumnValue = new ColumnValue();
			newColumnValue.setString( newValue.toString() );
			return newColumnValue;
		}
		return columnValue;
	}

}
