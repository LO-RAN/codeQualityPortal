package net.sourceforge.jdbcimporter.ant;

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

import java.util.Map;

import org.apache.tools.ant.BuildException;

import net.sourceforge.jdbcimporter.*;

/**
 * The ConnectionDefElement provides an Ant wrapper for setting a
 * ConnectionDef object from Ant.
 * 
 * @version 	0.6
 * @author Chris Nagy
 */
public class ConnectionDefElement 
{
	/**
	 * The default connection definition type.
	 */
	protected static String DEFAULT_TYPE = "jdbc";
	
	/**
	 * The default connection definition implementation.
	 */
	protected static String DEFAULT_IMPL = "net.sourceforge.jdbcimporter.connection.JDBCConnectionDef";
	
	/**
	 * The prefix used in the properties to identify that this property
	 * is a connection definition mapping.
	 */
	public static String PREFIX = "connection.";
	
	/**
	 * The mapping between connection definition types
	 * and implementations.
	 */
	protected Map plugins = null;
	
	/**
	 * The wrapped connection definition.
	 */
	protected ConnectionDef connectionDef = null;
	
	/**
	 * The connection definition type.
	 */
	protected String type;
	
	/**
	 * Constructs an empty ConnectionDefElement with the given
	 * mapping between connection definition types and implementations.
	 * 
	 * @param plugins the plugin mappings
	 */
	public ConnectionDefElement( Map plugins )
	{
		this.plugins = plugins;	
	}
	
	/**
	 * Sets the connection definition type. The type should map to an implementation
	 * class.
	 * 
	 * @param type the connection definition type
	 */
	public void setType( String type )
	{
		this.type = type;
		String className;	
		if ( DEFAULT_TYPE.equals( type ) && !plugins.containsKey( PREFIX + DEFAULT_TYPE ) )
		{
			className = DEFAULT_IMPL;
		}
		else
		{
			className = (String) plugins.get( PREFIX+type );
			if (className == null)
			{
				throw new BuildException("Could not find connection implementation class for type '"+type+"'. No value specified in plugins with name '"+PREFIX+type+"'" );
			}
		}
		
		try
		{
			connectionDef = (ConnectionDef) Class.forName( className ).newInstance();	
		}
		catch ( ClassNotFoundException e )
		{
			throw new BuildException( "Class Not Found : '"+className+"'" );
		}
		catch ( IllegalAccessException e )
		{
			throw new BuildException( "Class Not Instantiated : '"+className+"'");	
		}
		catch ( InstantiationException e )
		{
			throw new BuildException( "Class Not Instantiated : '"+className+"'");	
		}
	}
	
	/**
	 * Add a property to the connection definition.
	 * 
	 * @param element the property element
	 */
	public void addConfiguredProperty( PropertyElement element )
	{
		if ( connectionDef == null )
		{
			throw new BuildException("ConnectionDef not initialized");
		}
		
		JDBCImporterTask.applyProperty( connectionDef, element.getName(), element.getValue() );
	}

	/**
	 * Returns the wrapped connection definition.
	 * 
	 * @return the connection definition
	 */
	public ConnectionDef getConnectionDef()
	{
		if ( connectionDef == null )
		{
			throw new BuildException("ConnectionDef not initialized");
		}

		return connectionDef;
	}
}
