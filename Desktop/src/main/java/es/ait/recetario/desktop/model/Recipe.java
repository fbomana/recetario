/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.util.List;
import java.util.Date;

/**
 *
 * @author aitkiar
 */
public class Recipe
{
    private int recipeId;
    private String recipeTitle;
    private String recipe;
    private Date recipeDate;
    private Date recipeUpdate;
    private List<String> tags;

    /**
     * @return the recipeId
     */
    public int getRecipeId()
    {
        return recipeId;
    }

    /**
     * @param recipeId the recipeId to set
     */
    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }

    /**
     * @return the recipeTitle
     */
    public String getRecipeTitle()
    {
        return recipeTitle;
    }

    /**
     * @param recipeTitle the recipeTitle to set
     */
    public void setRecipeTitle(String recipeTitle)
    {
        this.recipeTitle = recipeTitle;
    }

    /**
     * @return the recipe
     */
    public String getRecipe()
    {
        return recipe;
    }

    /**
     * @param recipe the recipe to set
     */
    public void setRecipe(String recipe)
    {
        this.recipe = recipe;
    }

    /**
     * @return the recipeDate
     */
    public Date getRecipeDate()
    {
        return recipeDate;
    }

    /**
     * @param recipeDate the recipeDate to set
     */
    public void setRecipeDate(Date recipeDate)
    {
        this.recipeDate = recipeDate;
    }

    /**
     * @return the recipeUpdate
     */
    public Date getRecipeUpdate()
    {
        return recipeUpdate;
    }

    /**
     * @param recipeUpdate the recipeUpdate to set
     */
    public void setRecipeUpdate(Date recipeUpdate)
    {
        this.recipeUpdate = recipeUpdate;
    }

    /**
     * @return the tags
     */
    public List<String> getTags()
    {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }
}
