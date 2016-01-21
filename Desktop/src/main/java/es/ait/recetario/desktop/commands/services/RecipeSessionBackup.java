/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.commands.services;

import es.ait.recetario.desktop.Utils;
import es.ait.recetario.desktop.commands.JSONServiceCommand;
import es.ait.recetario.desktop.model.Recipe;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This service stores in session a copy of the actual recipe being editted so 
 * the user can recover from accidentally closing the browser tab.
 *
 * @author aitkiar
 */
public class RecipeSessionBackup extends JSONServiceCommand
{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException
    {
        if ( request.getMethod().equals("POST"))
        {
            Recipe recipe = new Recipe();
            recipe.setRecipeTitle( request.getParameter("title"));
            try
            {
                recipe.setRecipeId( Integer.parseInt( request.getParameter("id")));
            }
            catch ( Exception e )
            {
                recipe.setRecipeId( -1 );
            }
            recipe.setRecipe( request.getParameter("recipe"));
            recipe.setTags( Utils.string2tags( request.getParameter("tags")));
            session.setAttribute("backup_recipe", recipe );
        }
        else if ( session.getAttribute("backup_recipe") != null )
        {
            Json.createWriter(out).write( ((Recipe) session.getAttribute("backup_recipe")).toJSON());
        }
    }
    
}
