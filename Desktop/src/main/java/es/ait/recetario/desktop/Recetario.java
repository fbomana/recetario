/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.handlers.RecetarioBaseServlet;
import es.ait.recetario.desktop.preferences.Preferences;

import  es.ait.recetario.desktop.commands.JSonServiceFilter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author aitkiar
 */
public class Recetario
{
    private Server server;
    private int port = 8080;
    private final int MAX_PORT_NUMBER = 65535;
    private final int MIN_PORT_NUMBER = 8080;
    
    

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
        if ( !System.getenv("XDG_CURRENT_DESKTOP").toUpperCase().contains("GNOME"))
        {
            return !"kde".equals( System.getenv("XDG_CURRENT_DESKTOP").toLowerCase()) || !"5".equals( System.getenv("KDE_SESSION_VERSION"));
        }
        return false;
        
    }
    
    private void findPort()
    {
        while ( !available( port ) )
        {
            port ++;
        }
    }
    
    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    private boolean available(int port) 
    {
        if ( port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER ) 
        {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try 
        {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } 
        catch (IOException e) 
        {
        } 
        finally 
        {
            if (ds != null) 
            {
                ds.close();
            }

            if (ss != null) 
            {
                try 
                {
                    ss.close();
                } 
                catch (IOException e) 
                {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
    
    public void starServer() throws Exception
    {
        findPort();

        server = new Server( port );

        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder holder = new ServletHolder(new RecetarioBaseServlet());
        context.addServlet(holder, "/*");
        context.addFilter( JSonServiceFilter.class, "/*", EnumSet.of( DispatcherType.REQUEST ));

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
            Preferences preferences = Preferences.getInstance();
            if ( preferences.getRecetarioName() == null )
            {
                java.awt.EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        new AskForName().setVisible(true);
                    }
                });
            }
            else
            {
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
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            recetario.exit();
        }
    }
}
