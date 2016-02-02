/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.recipe;

import es.ait.recetario.Util;
import es.ait.recetario.model.Recipe;
import es.ait.recetario.model.RecipeDAO;
import es.ait.recetario.model.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for rest services that works with recipes.
 */
@RestController
@RequestMapping("/services/recipe/")
public class RecipeControllerRest
{
    @Autowired
    private RecipeDAO recipeDAO = new RecipeDAO();
    
    /**
     * Saves the recipe in session
     *
     * @param request
     * @param session 
     */
    @RequestMapping( path="/session/save", method = RequestMethod.POST )
    public void save( HttpServletRequest request, HttpSession session )
    {
        Recipe recipe = new Recipe();
        recipe.setRecipeTitle( request.getParameter("title") );
        recipe.setRecipe( request.getParameter("recipe") );
        recipe.setTagsList( Util.String2Tags(request.getParameter("tags")));
        session.setAttribute("recipeDraft", recipe );
    }
    
    /**
     * Returns the recipe stored in session or an empty recipe if none it's present
     * @param session
     * @return
     */
    @RequestMapping( path="/session/load", method = RequestMethod.GET, produces = "application/json" )
    public Recipe load( HttpSession session )
    {
        if ( session.getAttribute("recipeDraft") != null )
        {
            return ( Recipe )session.getAttribute("recipeDraft");
        }
        else
        {
            Recipe recipe = new Recipe();
            recipe.setRecipeTitle("");
            recipe.setTagsList( new ArrayList<>() );
            recipe.setRecipe("");
            return recipe;
        }
    }
    
    @RequestMapping( path="/search", method = RequestMethod.POST, produces = "application/json")
    public List<Recipe> search( HttpServletRequest request )
    {
        List<Tag> tags = Util.String2Tags(request.getParameter("tags"));
        return recipeDAO.searchByTags(tags, !"true".equals( request.getParameter("searchType")) );
    }
}
