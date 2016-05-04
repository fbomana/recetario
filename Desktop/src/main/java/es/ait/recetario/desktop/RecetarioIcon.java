/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author aitkiar
 */
public class RecetarioIcon implements ActionListener
{
    Recetario recetario;
    
    public RecetarioIcon( Recetario recetario )
    {
        this.recetario = recetario;
    }
    
    private Image readImage() throws IOException
    {
        return ImageIO.read( getClass().getResourceAsStream("/resources/img/icon.png"));
    }
    
    public void enableSystemTray() throws Exception
    {
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon( readImage (), "Recetario");
        trayIcon.setImageAutoSize( true );
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem recipesItem = new MenuItem("Show Recipes");
        recipesItem.setName("recipes");
        recipesItem.addActionListener( this );

        MenuItem newRecipeItem = new MenuItem("New Recipe");
        newRecipeItem.setName("newRecipe");
        newRecipeItem.addActionListener( this );
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setName("exit");
        exitItem.addActionListener( this );
       
        //Add components to pop-up menu
        popup.add( aboutItem );
        popup.addSeparator();
        popup.add( recipesItem );
        popup.add( newRecipeItem );
        popup.addSeparator();
        popup.add( exitItem );
       
        trayIcon.setPopupMenu( popup );

        tray.add(trayIcon);
    }
    
    @Override
    public void actionPerformed( ActionEvent event )
    {
        try
        {
            switch ( ((MenuItem)event.getSource()).getName())
            {
                case "exit":
                    recetario.exit();
                    break;
                case "recipes":
                    Utils.browse("http://localhost:" + recetario.getPort() + "/html/index.html");
                    break;
                case "newRecipe":
                    Utils.browse("http://localhost:" + recetario.getPort() + "/html/index.html#/new");
                    break;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
