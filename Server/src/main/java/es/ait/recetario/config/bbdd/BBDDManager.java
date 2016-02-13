/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.config.bbdd;

		
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * Controls the BBDD lifecycle.
 * 
 * @author aitkiar
 */	
public class BBDDManager
{
    private static BBDDManager instance;
    private DataSource datasource;
    private static final String[] versions = {"0.1.0", "0.2.0", "0.4.0"};
    
    private BBDDManager( )
    {
    }
    
    public static BBDDManager getInstance() throws NamingException
    {
        if ( instance == null )
        {
            instance = new BBDDManager();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            instance.datasource = ( javax.sql.DataSource ) envContext.lookup("jdbc/recetarioDatasource");
        }
        return instance;
    }
    
    public DataSource getDatasource()
    {
        return this.datasource;
    }
    /**
     * Returns a connection from the pool.
     * @return
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException
    {
        return datasource.getConnection();
    }   
    
    /**
     * Chexks the current version and runs the patches necesary to get to the last version.
     * If saome patch fails, the execution stops leaving the BBDD with the las 
     * succesfull patch applied.
     * @throws SQLException
     */
    public void versionControl() throws SQLException
    {
        try ( Connection connection = getConnection() )
        {
            int actualVersion = getVersion( connection );
            connection.setAutoCommit( false );
            for ( int i = actualVersion + 1; i < versions.length; i ++ )
            {
                try
                {
                    BBDDPatch patch = (BBDDPatch)Class.forName("es.ait.recetario.config.bbdd.patches.Patch_" + versions[i].replaceAll(Pattern.quote("."), "_")).newInstance();
                    patch.run( connection );
                    connection.commit(); // we commit after each sucsseful patch
                }
                catch ( SQLException e )
                {
                    connection.rollback();
                    throw e;
                }
                catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e )
                {
                    throw new SQLException("Can't instantiate the patch: " + versions[i] +
                             "\n" + e.getMessage());
                }
            }
            connection.commit();
        }
    }
    
    /**
     * Returns the index in the array of versions of the last version applied to
     * the BBDD.
     * 
     * @param connection the connection to the BBDD we want to check.
     * @return the index in the array of versions of the last version applied to
     * the BBDD. If the BBDD it's empty return -1.
     * @throws SQLException
     */
    private int getVersion( Connection connection ) throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet rs = metadata.getTables( null, null, null, new String[]{"TABLE"});
        if ( !rs.next() )
        {
            rs.close();
            return -1;
        }
        rs.close();
        try ( PreparedStatement ps = connection.prepareStatement("select app_version from preferences");
            ResultSet rset = ps.executeQuery())
        {
            if ( rset.next() )
            {
                String version = rset.getString(1);
                for ( int i = 0; i < versions.length; i ++ )
                {
                    if ( versions[i].equals( version ))
                    {
                        return i;
                    }
                }
                throw new SQLException("Current BBDD version it's higher than app version. Can't run.");
            }
            else
            {
                return -1;
            }
        }
    }
}
