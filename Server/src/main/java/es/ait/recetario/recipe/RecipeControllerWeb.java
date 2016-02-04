/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.recipe;

import es.ait.recetario.Util;
import es.ait.recetario.config.Preferences;
import es.ait.recetario.model.Recipe;
import es.ait.recetario.model.RecipeDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author aitkiar
 */
@Controller
@RequestMapping("/recipe")
public class RecipeControllerWeb
{
    @Autowired
    private RecipeDAO recipeDao;
    
    /**
     * Return the new recipe screen
     * @param model
     * @return 
     */
    @RequestMapping(path = "/new", method = RequestMethod.GET )
    public String newRecipe( Model model )
    {
        model.addAttribute("action", "/recetario/recipe/new");
        model.addAttribute("backupInterval", Preferences.getInstance().getAutoSaveInterval());
        return "/recipe/new.jsp";
    }
    
    /**
     * Saves a new recipe into the BBDD.
     * 
     * @param request
     * @param session
     * @param model
     * @return 
     */
    @RequestMapping(path = "/new", method = RequestMethod.POST )
    public String newRecipe( HttpServletRequest request, HttpSession session, Model model )
    {
        Recipe recipe = new Recipe();
        recipe.setRecipe( request.getParameter("contentEditor"));
        recipe.setRecipeTitle( request.getParameter("title"));
        recipe.setTagsList( Util.String2Tags(request.getParameter("tags")));
        recipe.setRecipeDate( new Date());
        recipe.setRecipeUpdate( recipe.getRecipeDate());
        recipe.setRecipeOrigin( Preferences.getInstance().getName());
        
        recipeDao.save(recipe);
        
        session.removeAttribute("recipeDraft");
        
        model.addAttribute("url", "/recetario/recipe/search");
        return "/redirect.jsp";
    }
    
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search( Model model )
    {
        return "/recipe/search.jsp";
    }
}
