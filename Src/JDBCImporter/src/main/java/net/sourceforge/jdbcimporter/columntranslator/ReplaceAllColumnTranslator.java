package net.sourceforge.jdbcimporter.columntranslator;

/* 
 * Laurent IZAC
 * Email:  laurent.izac@compuware.com
 */

import java.sql.Types;

import net.sourceforge.jdbcimporter.ColumnDef;
import net.sourceforge.jdbcimporter.ColumnTranslator;
import net.sourceforge.jdbcimporter.ColumnValue;

public class ReplaceAllColumnTranslator implements ColumnTranslator {
   
   protected String searchString = "";
   protected String replaceString = "";

   public void setSearchString( String newVal )
   {
      this.searchString = newVal;
   }
   
   public String getSearchString()
   {
      return this.searchString;
   }
   
   public void setReplaceString( String newVal )
   {
      this.replaceString = newVal;
   }
   
   public String getReplaceString()
   {
      return this.replaceString;
   }

	/* (non-Javadoc)
	 * @see net.sourceforge.jdbcimporter.ColumnTranslator#getValue(net.sourceforge.jdbcimporter.ColumnDef, net.sourceforge.jdbcimporter.ColumnValue)
	 */
	public ColumnValue getValue(ColumnDef column, ColumnValue columnValue) 
	{
    if ( Types.VARCHAR == column.getType() && columnValue.getString() != null )
    {
	    columnValue.setString(columnValue.getString().replaceAll(searchString,replaceString));
	  }
		return columnValue;
	}

}
