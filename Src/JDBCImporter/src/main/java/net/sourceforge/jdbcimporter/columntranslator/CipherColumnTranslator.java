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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.sourceforge.jdbcimporter.ColumnDef;
import net.sourceforge.jdbcimporter.ColumnTranslator;
import net.sourceforge.jdbcimporter.ColumnValue;

public class CipherColumnTranslator implements ColumnTranslator
{
	Cipher cipher;
	int cipherMode = Cipher.ENCRYPT_MODE;
	String desKeyFile = "deskey";
	
    public CipherColumnTranslator()
    {
    }

    public void setCipherMode( String mode )
    {
    	if ( "ENCRYPT".equals( mode ) )
    	{
    		cipherMode = Cipher.ENCRYPT_MODE;
    	}
    	else if ( "DECRYPT".equals( mode ) )
    	{
    		cipherMode = Cipher.DECRYPT_MODE;
    	}
    }
    
    public void initCipher()
    {
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			FileInputStream in = new FileInputStream( desKeyFile );
			byte[] buf = new byte[1024];
			int    bufLen = -1;
			while( ( bufLen = in.read( buf ) ) != -1 )
			{
				out.write( buf, 0, bufLen );
			}
			in.close();
			
			byte[] rawDesKey = out.toByteArray();
			
			SecretKey desKey = new SecretKeySpec( rawDesKey, "DES");
    		cipher = Cipher.getInstance( "DES" );
    		if ( cipherMode == Cipher.ENCRYPT_MODE )
    			cipher.init( cipherMode, desKey );
    		else
    			cipher.init( cipherMode, desKey );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
    }
    
	/* (non-Javadoc)
	 * @see net.sourceforge.jdbcimporter.ColumnTranslator#getValue(net.sourceforge.jdbcimporter.ColumnDef, net.sourceforge.jdbcimporter.ColumnValue)
	 */
	public ColumnValue getValue( ColumnDef column, ColumnValue columnValue )
	{
		if ( cipher == null )
		{
			initCipher();
		}
		
		ColumnValue returnVal = columnValue; 
		if ( cipher != null )
		{
			if ( cipherMode == Cipher.ENCRYPT_MODE && 
				 columnValue.isString() && 
				 columnValue.getString() != null ) 
			{
				returnVal = new ColumnValue();
				try
				{
					returnVal.setBytes( cipher.doFinal( columnValue.getString().getBytes() ) );
				}
				catch ( Exception e )
				{
					returnVal = columnValue;
					e.printStackTrace();
				}
			}
			else if ( cipherMode == Cipher.DECRYPT_MODE && 
					  columnValue.isBytes() && 
					  columnValue.getBytes() != null && 
					  columnValue.getBytes().length > 0 )
			{
				returnVal = new ColumnValue();
				try
				{
					returnVal.setString( new String( cipher.doFinal( columnValue.getBytes() ) ) );
				}
				catch ( Exception e )
				{
					returnVal = columnValue;
					e.printStackTrace();
				}
			}
		}
		return returnVal;
	}

	public static void main( String[] args ) throws Exception
	{
		KeyGenerator desGen = KeyGenerator.getInstance("DES");
		SecretKey desKey = desGen.generateKey();
		
		SecretKeyFactory desFactory = SecretKeyFactory.getInstance("DES");
		DESKeySpec desSpec = (DESKeySpec) desFactory.getKeySpec(desKey, javax.crypto.spec.DESKeySpec.class );
		byte[] rawDesKey = desSpec.getKey();
		
		String file = "deskey";
		
		FileOutputStream out = new FileOutputStream( file );
		out.write( rawDesKey );
		out.flush();
		out.close();
	}
}
