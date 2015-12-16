/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.handlers.RecetarioHandler;
import es.ait.recetario.desktop.preferences.Preferences;
import java.sql.SQLException;
import org.eclipse.jetty.server.Server;

/**
 *
 * @author aitkiar
 */
public class Recetario
{
    private Server server;
    private int port = 8080;
    

    public String getPort()
    {
        return "" + port;
    }
    
    /**
     * Supports systemtray icon in all windows and in all linux except gnome and kde plasma 5
     * @return 
     */
    public boolean isIconAvailable()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if ( os.contains("windows") )
        {
            return true;
        }
        if ( !System.getenv("XDG_CURRENT_DESKTOP").toLowerCase().contains("GNOME"))
        {
            return !"kde".equals( System.getenv("XDG_CURRENT_DESKTOP").toLowerCase()) || !"5".equals( System.getenv("KDE_SESSION_VERSION"));
        }
        return false;
        
    }
    
    public void starServer() throws Exception
    {
        server = new Server( port );
        server.setHandler( new RecetarioHandler());
        server.start();
        server.dumpStdErr();
        server.join();
    }
    
    private void startBBDD() throws SQLException
    {
        Preferences preferences = Preferences.getInstance();
        if ( !preferences.isFirstRun() && preferences.getDerbyFolder() != null )
        {
            BBDDManager.getInstance( preferences.getDerbyFolder() ).startUp();
        }
    }
    
    public void exit()
    {
        if ( server != null )
        {
            try
            {
                System.out.println("------------------------------------------------------------------");
                try
                {
                    BBDDManager.getInstance(Preferences.getInstance().getDerbyFolder()).shutDown();
                }
                catch ( Exception e )
                {
                }
                server.stop();
                System.exit(0);
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }
        
    public static void main( String args[])
    {
        Recetario recetario = null;
        try
        {
            recetario = new Recetario();
            recetario.startBBDD();
            if ( recetario.isIconAvailable() )
            {
                RecetarioIcon icon = new RecetarioIcon(recetario);
                icon.enableSystemTray();
            }
            else
            {
                RecetarioDesktop.showRecetario( recetario );
            }
            recetario.starServer();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            recetario.exit();
        }
    }
}
