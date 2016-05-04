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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service for importing recipes. It return the imported recipe object with the destiny id.
 * @author aitkiar
 */
@CommandPath(path="/services/recipe/import")
public class RecipeImport extends JSONServiceCommand
{
    @Override
    protected void post(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try ( Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            connection.setAutoCommit( false );
            RecipeDAO dao = new RecipeDAO();
            JsonReader reader = Json.createReader( request.getInputStream());
            try
            {
                Recipe recipe = new Recipe( reader.readObject());
                dao.importRecipe(connection, recipe);
                connection.commit();
                Json.createWriter( out ).writeObject( recipe.toJSON());
            }
            catch ( ParseException e )
            {
                connection.rollback();
                response.sendError(400, "400 Bad Request: the json format of the recipe it's invalid.");
            }
        }
        catch ( SQLException e )
        {
            throw new ServletException( e );
        }
    }
}
