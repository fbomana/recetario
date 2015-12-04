/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.handlers.RecetarioHandler;
import es.ait.recetario.desktop.preferences.Preferences;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import org.eclipse.jetty.server.Server;

/**
 *
 * @author aitkiar
 */
public class Recetario implements ActionListener
{
    private Server server;
    private int port = 8080;
    
    private Image readImage() throws IOException
    {
        return ImageIO.read( getClass().getResourceAsStream("/resources/img/icon.png"));
    }
    
    public void starServer() throws Exception
    {
        server = new Server( port );
        server.setHandler( new RecetarioHandler());
        server.start();
        server.dumpStdErr();
        server.join();
    }

    public void enableSystemTray() throws Exception
    {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) 
        {
            throw new Exception("SystemTray not supported");
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon( readImage (), "Recetario");
        trayIcon.setImageAutoSize( true );
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem recipesItem = new MenuItem("Ver recetas");
        recipesItem.setName("recipes");
        recipesItem.addActionListener( this );
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setName("exit");
        exitItem.addActionListener( this );
       
        //Add components to pop-up menu
        popup.add( aboutItem );
        popup.addSeparator();
        popup.add( recipesItem );
        popup.addSeparator();
        popup.add( exitItem );
       
        trayIcon.setPopupMenu( popup );
       
        try 
        {
            tray.add(trayIcon);
        } 
        catch (AWTException e) 
        {
            System.out.println("TrayIcon could not be added.");
            server.stop();
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed( ActionEvent event )
    {
        try
        {
            switch ( ((MenuItem)event.getSource()).getName())
            {
                case "exit":
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
                    break;
                case "recipes":
                    Utils.browse("http://localhost:" + port + "/recipes/SearchRecipes");
                    break;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    private void startBBDD() throws SQLException
    {
        Preferences preferences = Preferences.getInstance();
        if ( !preferences.isFirstRun() && preferences.getDerbyFolder() != null )
        {
            BBDDManager.getInstance( preferences.getDerbyFolder() ).startUp();
        }
    }
    
    public static void main( String args[])
    {
        Recetario recetario = null;
        try
        {
            recetario = new Recetario();
            recetario.startBBDD();
            recetario.enableSystemTray();
            recetario.starServer();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
