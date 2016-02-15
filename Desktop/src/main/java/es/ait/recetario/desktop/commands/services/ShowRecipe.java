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
import java.util.Properties;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
@CommandPath(path="/services/recipe")
public class ShowRecipe extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        Properties properties = new Properties();
        try
            (Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            Recipe recipe = new RecipeDAO().search(connection, Integer.parseInt( request.getParameter("id") ));
            Json.createWriter(out).write( recipe.toJSON() );
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }
    
}
