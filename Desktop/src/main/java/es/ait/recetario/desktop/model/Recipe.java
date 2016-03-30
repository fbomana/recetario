/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import es.ait.recetario.desktop.preferences.Preferences;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author aitkiar
 */
public class Recipe
{
    private int recipeId = -1;
    private String recipeTitle;
    private String recipe;
    private Date recipeDate;
    private Date recipeUpdate;
    private String recipeOrigin;
    private String recipeShareId;
    private List<String> tags;

    public Recipe()
    {   
    }
    
    public Recipe( JsonObject json ) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
        recipeId = json.getInt( "id" );
        recipeTitle = json.getString("title");
        recipe = json.getString("recipe");
        recipeDate = sdf.parse( json.getString("date"));
        recipeUpdate = sdf.parse( json.getString( "update"));
        recipeOrigin = json.getString( "origin" );
        recipeShareId = json.getString( "shareId" );
        JsonArray array = json.getJsonArray( "tags" );
        for ( int i = 0; array != null && i < array.size(); i++ )
        {
            addTag( array.getString( i ));
        }
    }
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
    
    /**
     * Adds a tag to a recipe.
     * @param tag 
     */
    public void addTag( String tag )
    {
        if ( tags == null )
        {
            tags = new ArrayList<String>();
        }
        if ( !tags.contains( tag ))
        {
            tags.add( tag );
        }
    }
    
    /**
     * Returns a JsonObject representing the object in BBDD.
     * @return 
     */
    public JsonObject toJSON()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
        JsonArrayBuilder builder = Json.createArrayBuilder();
        if ( tags != null )
        {
            for ( String tag : tags )
            {
                builder = builder.add( tag );
            }
        }
        return Json.createObjectBuilder()
            .add("id", recipeId )
            .add("title", recipeTitle )
            .add("recipe", recipe != null ? recipe : "")
            .add("date", recipeDate != null ? sdf.format( recipeDate ) : "" )
            .add("update", recipeUpdate != null ? sdf.format( recipeUpdate ) : "" )
            .add("origin", recipeOrigin != null ? recipeOrigin : "" )
            .add("shareId", getRecipeShareId() != null ? getRecipeShareId() : "" )
            .add("tags", builder.build())
            .build();
                
    }

    /**
     * @return the recipeOrigin
     */
    public String getRecipeOrigin()
    {
        if ( recipeOrigin == null )
        {
            recipeOrigin = Preferences.getInstance().getRecetarioName();
        }
        return recipeOrigin;
    }

    /**
     * @param recipeOrigin the recipeOrigin to set
     */
    public void setRecipeOrigin(String recipeOrigin)
    {
        this.recipeOrigin = recipeOrigin;
    }

    /**
     * @return the recipeShareId
     */
    public String getRecipeShareId()
    {
        if ( recipeShareId == null && recipeDate != null && recipeOrigin != null && recipeTitle != null)
        {
            try
            {
                String shareId = recipeOrigin + recipeTitle + new SimpleDateFormat("yyyyMMddHHmmss").format( recipeDate );
                recipeShareId = Base64.getEncoder().encodeToString( shareId.getBytes("UTF-8"));
            }
            catch ( UnsupportedEncodingException e )
            {
                // do nothing if utf-8 is not supported.
            }
        }
        return recipeShareId;
    }

    /**
     * @param recipeShareId the recipeShareId to set
     */
    public void setRecipeShareId(String recipeShareId)
    {
        this.recipeShareId = recipeShareId;
    }
}
