/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.preferences.ReadOnlyMode;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest service to interact with the preferences object
 * 
 * @author aitkiar
 */
@CommandPath(path="/services/preferences")
public class Preferences extends JSONServiceCommand
{
   
    @Override
    protected void get(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        es.ait.recetario.desktop.preferences.Preferences pref = es.ait.recetario.desktop.preferences.Preferences.getInstance();
        Json.createWriter(out).write( pref.toJSon() );
    }
    
    @Override
    protected void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try
        {
            JsonReader reader = Json.createReader( request.getInputStream() );
            JsonObject jsonPreferences = reader.readObject();
            preferences.setRecetarioName( jsonPreferences.getString( "recetarioName" ));
            preferences.setRecipeBackupInterval( jsonPreferences.getInt( "recipeBackupInterval"));
            preferences.setMode( ReadOnlyMode.getMode( "" + jsonPreferences.getInt("mode")));
            preferences.setRecipesPerPage( jsonPreferences.getInt("recipesPerPage"));
            preferences.save();
        }
        catch ( Exception e )
        {
            response.sendError(500, e.getMessage());
        }
    }
    
    @Override
    protected void put(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        post( request, response, out );
    }
}
