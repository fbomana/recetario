/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * This class represents the result of a search operation over the recipes. It's used to implement pagination over the 
 * results. In searchs with no pagination, it will return only one page of results with the number of results as pageSize.
 * 
 */
public class RecipeResult
{
    private int page;
    private int pageSize;
    private int totalPages;
    private int totalResults;
    private int results;
    private List<Recipe> recipes;

    /**
     * @return the page
     */
    public int getPage()
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages()
    {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    /**
     * @return the totalResults
     */
    public int getTotalResults()
    {
        return totalResults;
    }

    /**
     * @param totalResults the totalResults to set
     */
    public void setTotalResults(int totalResults)
    {
        this.totalResults = totalResults;
    }

    /**
     * @return the results
     */
    public int getResults()
    {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(int results)
    {
        this.results = results;
    }

    /**
     * @return the recipes
     */
    public List<Recipe> getRecipes()
    {
        return recipes;
    }

    /**
     * @param recipes the recipes to set
     */
    public void setRecipes(List<Recipe> recipes)
    {
        this.recipes = recipes;
    }
    
    /**
     * Returns the JSON representation of the RecipeResult object.
     * @return 
     */
    public JsonObject toJSON()
    {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add( "page", page );
        objectBuilder.add( "pageSize", pageSize );
        objectBuilder.add( "totalPages", totalPages );
        objectBuilder.add( "totalResults", totalResults );
        objectBuilder.add( "results", results );
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for ( Recipe recipe : recipes )
        {
            arrayBuilder.add( recipe.toJSON() );
        }
        objectBuilder.add( "recipes", arrayBuilder.build());
        return objectBuilder.build();
    }
}
