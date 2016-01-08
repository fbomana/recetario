/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.recipes;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.BBDD.BBDDManager;
import es.ait.recetario.desktop.commands.Command;
import es.ait.recetario.desktop.model.Recipe;
import es.ait.recetario.desktop.model.RecipeDAO;
import es.ait.recetario.desktop.templates.TemplateFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class EditRecipe extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        if ( "/recipes/EditRecipe".equals(request.getParameter("post")))
        {
            processPost( request, response, out );
            return;
        }
        
        Properties properties = new Properties();
        properties.setProperty("id", request.getParameter("id"));
        properties.setProperty("action", "/recipes/EditRecipe");
        out.print( TemplateFactory.getTemplate( "newRecipe.html", properties ));
    }
    
    
    private void processPost( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) throws IOException, ServletException
    {

        
        Connection connection = null;
        try
        {
            connection = BBDDManager.getInstance( null ).getConnection();
            connection.setAutoCommit( false );
            
            Recipe recipe = new RecipeDAO().search(connection, new Integer( request.getParameter("id")) );
            recipe.setRecipeTitle( request.getParameter("title"));
            recipe.setRecipe( request.getParameter("contentEditor"));
            recipe.setTags( Utils.string2tags( request.getParameter("tags")));
            
            new RecipeDAO().update( connection, recipe );
            
            connection.commit();
            forward("/recipes/SearchRecipes", request, response, out);
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
        finally
        {
            try
            {
                connection.rollback();
                connection.close();
            }
            catch ( Exception e )
            {
                throw new ServletException( e );
            }
        }
        
    }
}
