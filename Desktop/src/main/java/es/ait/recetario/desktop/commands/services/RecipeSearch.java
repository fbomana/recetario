/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.model.Recipe;
import es.ait.recetario.desktop.model.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class RecipeSearch extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        
        try
            ( Connection connection = BBDDManager.getInstance("").getConnection())
        {
            List<Recipe> recipes;
            if ( request.getParameter("importList") != null && !"".equals( request.getParameter("importList")))
            {
                List<String> shareIds = Utils.splitString(request.getParameter("importList"), false );
                recipes = new RecipeDAO().shareIdSearch( connection, shareIds );
            }
            else
            {
                List<String> tags = Utils.string2tags( request.getParameter("tags"));
                recipes = new RecipeDAO().search(connection, tags, !"true".equals( request.getParameter("searchType")) );
            }
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for ( Recipe recipe : recipes )
            {
                builder = builder.add( recipe.toJSON());
            }
            Json.createWriter(out).write( builder.build() );
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }   
}
