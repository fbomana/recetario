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
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aitkiar
 */
public class NewRecipe extends Command
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        if ( "NewRecipe".equals( request.getParameter("post")))
        {
            processPost( request, response, out );
            return;
        }
        
        Properties properties = new Properties();
        out.print( TemplateFactory.getTemplate( "newRecipe.html", properties ));
    }
    
    private void processPost( HttpServletRequest request, HttpServletResponse response, PrintWriter out ) throws IOException, ServletException
    {
        Recipe recipe = new Recipe();
        recipe.setRecipeTitle( request.getParameter("title"));
        recipe.setRecipe( request.getParameter("content"));
        recipe.setTags( Utils.string2tags( request.getParameter("tags")));
        
        Connection connection = null;
        try
        {
            connection = BBDDManager.getInstance( null ).getConnection();
            connection.setAutoCommit( false );
            
            new RecipeDAO().create( connection, recipe );
            
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
