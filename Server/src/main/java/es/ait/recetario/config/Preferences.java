/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.config;

import es.ait.recetario.config.bbdd.BBDDManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 * this class keeps the recetario configuration in memory.
 * @author aitkiar
 */
public class Preferences
{

    /**************************************************************************/
    /* Singleton implementation                                               */
    /**************************************************************************/
    private static Preferences instance;
    
    public static Preferences getInstance()
    {
        if ( instance == null )
        {
            instance = new Preferences();
        }
        return instance;
    }
    
    private Preferences()
    {
    }

    /**************************************************************************/
    /* Load and save                                                          */
    /**************************************************************************/

    /**
     * Load preferences form BBDD
     * @throws java.sql.SQLException
     */
    public void load() throws SQLException
    {
        try ( Connection connection = BBDDManager.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from preferences");
            ResultSet rs = ps.executeQuery())
        {
            if ( rs.next())
            {
                version = rs.getString("app_version");
                name = rs.getString("name");
                autoSaveInterval = rs.getInt( "autosave_interval");
                loaded = true;
            }
        }
        catch ( NamingException e )
        {
            throw new SQLException( e );
        }
    }
    
    /**
     * Save preferences form BBDD.
     * @throws java.sql.SQLException
     */
    public void save() throws SQLException
    {
        try ( Connection connection = BBDDManager.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement("update preferences set name=?, autosave_interval=?");)
        {
            ps.setString( 1, name );
            ps.setInt( 2, autoSaveInterval );
            ps.executeUpdate();
        }
        catch ( NamingException e )
        {
            throw new SQLException( e );
        }
    }
    /**************************************************************************/
    /* Preferences variables                                                  */
    /**************************************************************************/
    
    /* BBDD */
    private String bbddHost;
    private String bbddPassword;
    private String bbddUser;

    private boolean loaded = false;
    
    private int autoSaveInterval;
    private String name;
    private String version;

    /**
     * @return the bbddHost
     */
    public String getBbddHost()
    {
        return bbddHost;
    }

    /**
     * @param bbddHost the bbddHost to set
     */
    public void setBbddHost(String bbddHost)
    {
        this.bbddHost = bbddHost;
    }

    /**
     * @return the bbddPassword
     */
    public String getBbddPassword()
    {
        return bbddPassword;
    }

    /**
     * @param bbddPassword the bbddPassword to set
     */
    public void setBbddPassword(String bbddPassword)
    {
        this.bbddPassword = bbddPassword;
    }

    /**
     * @return the bbddUser
     */
    public String getBbddUser()
    {
        return bbddUser;
    }

    /**
     * @param bbddUser the bbddUser to set
     */
    public void setBbddUser(String bbddUser)
    {
        this.bbddUser = bbddUser;
    }

    /**
     * @return the autoSaveInterval
     */
    public int getAutoSaveInterval()
    {
        return autoSaveInterval;
    }

    /**
     * @param autoSaveInterval the autoSaveInterval to set
     */
    public void setAutoSaveInterval(int autoSaveInterval)
    {
        this.autoSaveInterval = autoSaveInterval;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public boolean isLoaded()
    {
        return loaded;
    }
}
