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
    public static final String version = "0.3.0";
    
    private boolean firstRun = false;
    private String derbyFolder;
    private File file;
    private String recetarioName;
    private int recipeBackupInterval = 120000;
    private ReadOnlyMode mode;
    
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
            recetarioName = prop.getProperty("recetarioName", null );
            recipeBackupInterval = Integer.parseInt( prop.getProperty("recipeBackupInterval", "120000" ));
            mode = ReadOnlyMode.getMode( prop.getProperty("mode", "1"));
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
            writer.write("# Recetario Name\n");
            writer.write("recetarioName=" + ( recetarioName != null ? recetarioName : "")+ "\n");
            writer.write("\n");
            writer.write("# BBDD folder\n");
            writer.write("derbyFolder=" + ( derbyFolder != null ? derbyFolder : "")+ "\n");
            writer.write("# Backup interval\n");
            writer.write("recipeBackupInterval=" + recipeBackupInterval + "\n");
            writer.write("mode=" + mode.getMode() + "\n");
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
    
    public String getRecetarioName()
    {
        return recetarioName;
    }
    
    public void setRecetarioName( String recetarioName )
    {
        this.recetarioName = recetarioName;
    }
    
    public int getRecipeBackupInterval()
    {
        return recipeBackupInterval;
    }
    
    public void setRecipeBackupInterval( int recipeBackupInterval )
    {
        this.recipeBackupInterval = recipeBackupInterval;
    }

    /**
     * @return the mode
     */
    public ReadOnlyMode getMode()
    {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(ReadOnlyMode mode)
    {
        this.mode = mode;
    }
}
