/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.CommandPath;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.model.Recipe;
import es.ait.recetario.desktop.model.RecipeDAO;
import es.ait.recetario.desktop.model.RecipeResult;
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
@CommandPath(path="/services/recipe/search")
public class RecipeSearch extends JSONServiceCommand
{

    @Override
    public void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        
        try
            ( Connection connection = BBDDManager.getInstance("").getConnection())
        {
            List<Recipe> recipes;
            RecipeResult result;
            if ( request.getParameter("importList") != null && !"".equals( request.getParameter("importList")))
            {
                List<String> shareIds = Utils.splitString(request.getParameter("importList"), false );
                recipes = new RecipeDAO().shareIdSearch( connection, shareIds );
                JsonArrayBuilder builder = Json.createArrayBuilder();
                for ( Recipe recipe : recipes )
                {
                    builder = builder.add( recipe.toJSON());
                }
                Json.createWriter(out).write( builder.build() );

            }
            else
            {
                List<String> tags = Utils.string2tags( request.getParameter("tags"));
                if ( request.getParameter("page") != null && request.getParameter("pageSize") != null )
                {
                    result = new RecipeDAO().search(connection, tags, !"1".equals( request.getParameter("searchType")), new Integer( request.getParameter("page")), new Integer ( request.getParameter("pageSize")) );
                }
                else
                {
                    result = new RecipeDAO().search(connection, tags, !"1".equals( request.getParameter("searchType")), null, null );
                }
                Json.createWriter( out ).write( result.toJSON());
            }
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }   
}
