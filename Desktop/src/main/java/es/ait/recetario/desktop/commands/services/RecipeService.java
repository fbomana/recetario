/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.model.Recipe;
import es.ait.recetario.desktop.model.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest service for the recipe object. Because the recipe list cant be too big, the list of all recipes it's forbidden
 * 
 * @author aitkiar
 */
@CommandPath(path="/services/recipe")
public class RecipeService extends JSONServiceCommand
{
    protected void get(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try ( Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            RecipeDAO dao = RecipeDAO.getInstance();
            String id = getUrlParam( 1 );
            if ( id == null)
            {
                // All recipes forbiden
                response.sendError( 501, "501 - Not Implemented.\nThe search for the entire recipe list it's forbidden due to it's huge size. Use RecipeSearch service instead.");
            }
            else
            {
                JsonWriter writer = Json.createWriter( out );
                Recipe recipe = dao.search(connection, Integer.parseInt( id ));
                if ( recipe != null )
                {
                    writer.write( recipe.toJSON());
                }
                else
                {
                    response.sendError(404, "Recipe with id:" + id + " not found");
                }
            }
        }
        catch ( SQLException e )
        {
            throw new ServletException( e );
        }
    }
    
    @Override
    protected void put(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try ( Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            RecipeDAO dao = RecipeDAO.getInstance();
            JsonReader reader = Json.createReader( request.getInputStream());
            try
            {
                Recipe recipe = new Recipe( reader.readObject());
                if ( recipe.getRecipeId() != -1 )
                {
                    dao.update( connection, recipe );
                }
                else
                {
                    dao.create( connection, recipe );
                }
                Json.createWriter( out ).write( recipe.toJSON() );
            }
            catch ( ParseException e )
            {
                response.sendError(400, "400 Bad Request: the json format of the recipe it's invalid.");
            }
        }
        catch ( SQLException e )
        {
            throw new ServletException( e );
        }
    }
    
    /**
     * redirect to put method
     * @param request
     * @param response
     * @param out
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    protected void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        put ( request, response, out );
    }
    
    /**
     * Deletes a recipe.
     * @param request
     * @param response
     * @param out
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    protected void delete(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try ( Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            RecipeDAO dao = RecipeDAO.getInstance();

            String id = getUrlParam( 1 );
            if ( id == null)
            {
                // All recipes forbiden
                response.sendError( 501, "501 - Not Implemented.\nDeletitng the entire tag collection it's not supported");
            }
            dao.delete( connection, Integer.parseInt( id ));
        }
        catch ( SQLException e )
        {
            throw new ServletException ( e );
        }
    }    
    
    
}
