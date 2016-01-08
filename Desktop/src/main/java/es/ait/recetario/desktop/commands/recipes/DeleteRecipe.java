/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.recipes;

import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.model.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class DeleteRecipe extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        try ( Connection connection = BBDDManager.getInstance(null).getConnection())
        {
            connection.setAutoCommit(false );
            new RecipeDAO().delete( connection, Integer.parseInt(request.getParameter("id")));
            connection.commit();
            forward("/recipes/SearchRecipes", request, response, out);
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
    }
    
}
