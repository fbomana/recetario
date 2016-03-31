/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.recetario;

import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.preferences.Preferences;
import es.ait.recetario.desktop.preferences.ReadOnlyMode;
import es.ait.recetario.desktop.templates.TemplateFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command that displays de configuration template and process the data to save the changes.
 */
@CommandPath(path="/recetario/Configuration")
public class Configuration extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        Properties properties = new Properties();
        properties.setProperty("message", "");
        Preferences preferences = Preferences.getInstance();
        if ( "/recetario/Configuration".equals( request.getParameter("post")))
        {
            preferences.setRecetarioName( request.getParameter("recetarioName"));
            preferences.setRecipeBackupInterval( Integer.parseInt( request.getParameter("recipeBackupInterval")));
            preferences.setMode( ReadOnlyMode.getMode( request.getParameter("readOnlyMode")));
            preferences.setRecipesPerPage(Integer.parseInt( request.getParameter("recipesPerPage")));
            preferences.save();
            properties.setProperty("message", "Configuration saved");
        }
        properties.setProperty("recetarioName", preferences.getRecetarioName());
        properties.setProperty("recipeBackupInterval", preferences.getRecipeBackupInterval()+ "");
        properties.setProperty("readOnlyMode", preferences.getMode().getMode() + "");
        properties.setProperty("recipesPerPage", preferences.getRecipesPerPage() + "");
        out.print( TemplateFactory.getTemplate( request, "configuration.html", properties ));
    }
    
}
