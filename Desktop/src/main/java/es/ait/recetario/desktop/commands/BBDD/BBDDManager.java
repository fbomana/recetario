/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.BBDD;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;

/**
 * Controls the BBDD lifecycle.
 * 
 * @author aitkiar
 */
public class BBDDManager
{
    private static BBDDManager instance;
    private EmbeddedConnectionPoolDataSource datasource;
    private String bbdd;
    private boolean startedtUp = false;
    private static final String[] versions = {"0.1.0"};
    
    private BBDDManager( String bbdd )
    {
        this.bbdd = bbdd;
    }
    
    public static BBDDManager getInstance( String bbdd )
    {
        if ( instance == null )
        {
            instance = new BBDDManager( bbdd );
        }
        return instance;
    }
    
    /**
     * If not startedup starts up the BBDD and initialize the onnection pool
     * datasource
     * 
     * @throws SQLException 
     */
    public synchronized void startUp() throws SQLException
    {
        if ( !startedtUp )
        {
            try
            {
                datasource = new EmbeddedConnectionPoolDataSource();
                datasource.setCreateDatabase( "create");
                datasource.setDatabaseName( bbdd );
                try ( Connection connection = datasource.getConnection() )
                {
                    versionControl( connection );
                }
                startedtUp = true;
            }
            catch ( SQLException e )
            {
                System.out.println( e.getSQLState() + " -- " + e.getErrorCode() );
                if ( "XJ041".equals(e.getSQLState()) ) 
                {
                    System.out.println("La BBDD ya existe");
                }
                else
                {
                    throw e;
                }
            }
        }      
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
    
    public void shutDown() throws SQLException
    {
        if ( startedtUp)
        {
            try
            {
                // the shutdown=true attribute shuts down Derby
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            }
            catch (SQLException se)
            {
                if (( (se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState()) ))) 
                {
                    // we got the expected exception
                    System.out.println("Derby shut down normally");
                    // Note that for single database shutdown, the expected
                    // SQL state is "08006", and the error code is 45000.
                } 
                else 
                {
                    throw se;
                }
            }
        }
    }
    
    
    /**
     * Chexks the current version and runs the patches necesary to get to the last version.
     * If saome patch fails, the execution stops leaving the BBDD with the las 
     * succesfull patch applied.
     * @param conexion
     * @throws SQLException
     */
    private void versionControl( Connection connection ) throws SQLException
    {
        int actualVersion = getVersion( connection );
        connection.setAutoCommit( false );
        for ( int i = actualVersion + 1; i < versions.length; i ++ )
        {
            try
            {
                BBDDPatch patch = (BBDDPatch)Class.forName("es.ait.recetario.desktop.commands.BBDD.patches.Patch_" + versions[i].replaceAll(Pattern.quote("."), "_")).newInstance();
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
        try ( PreparedStatement ps = connection.prepareStatement("select app_version from version");
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
