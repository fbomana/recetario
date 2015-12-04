/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.preferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Bean containing app preferences
 * @author aitkiar
 */
public class Preferences
{
    private static Preferences instance;
    public static final String version = "0.1.0";
    
    private boolean firstRun = false;
    private String derbyFolder;
    private File file;
    
    /**
     * Default constructor. It's private to force use the singleton pattern.
     */
    private Preferences()
    {
        file = new File("recetario.properties");
        if ( file.exists())
        {
            // Portable media execution
            loadProperties( file );
            return;
        }
        file = new File( System.getProperty("user.home") + "/.recetario/recetario.properties" );
        if ( file.exists() )
        {
            // Computer local version
            loadProperties( file );
            return;            
        }
        firstRun = true;
    }
    
    /**
     * Reads the configuration file an initialize internal state.
     * 
     * @param file file object pointing to the recetario.properties configuration file.
     */
    private void loadProperties( File file )
    {
        Properties prop = new Properties();
        FileReader in = null;
        try
        {
            in = new FileReader( file );
            prop.load( in );
            derbyFolder = prop.getProperty("derbyFolder", "data");
        }
        catch ( Exception e )
        {
        }
        finally
        {
            try
            {
                in.close();
            }
            catch ( Exception e )
            {
            }
        }
    }
    
    public void save()
    {
        if ( ! file.exists())
        {
            file.getParentFile().mkdirs();
        }
        try ( FileWriter writer = new FileWriter( file ))
        {
            writer.write("###\n");
            writer.write("# Recetario configuration file\n");
            writer.write("###\n");
            writer.write("\n");
            writer.write("# BBDD folder\n");
            writer.write("derbyFolder=" + ( derbyFolder != null ? derbyFolder : "")+ "\n");
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    /**
     * Returns an instance of the configuration bean.
     * 
     * @return 
     */
    public static Preferences getInstance()
    {
        if ( instance == null )
        {
            instance = new Preferences();
        }
        return instance;
    }

    /**
     * @return the firstRun
     */
    public boolean isFirstRun()
    {
        return firstRun;
    }

    /**
     * @param firstRun the firstRun to set
     */
    public void setFirstRun(boolean firstRun)
    {
        this.firstRun = firstRun;
    }

    /**
     * @return the derbyFolder
     */
    public String getDerbyFolder()
    {
        return derbyFolder;
    }

    /**
     * @param derbyFolder the derbyFolder to set
     */
    public void setDerbyFolder(String derbyFolder)
    {
        this.derbyFolder = derbyFolder;
    }
    
}