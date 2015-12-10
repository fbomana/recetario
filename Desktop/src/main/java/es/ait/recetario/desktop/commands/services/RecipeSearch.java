/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.Command;
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
import org.eclipse.jetty.util.ajax.JSON;

/**
 *
 * @author aitkiar
 */
public class RecipeSearch extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        List<String> tags = Utils.string2tags( request.getParameter("tags"));
        try
            ( Connection connection = BBDDManager.getInstance("").getConnection())
        {
            List<Recipe> recipes = new RecipeDAO().search( connection, tags );
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
    
    @Override
    public String getContentType( String source )
    {
        return "application/json;charset=utf-8";
    }
    
}
