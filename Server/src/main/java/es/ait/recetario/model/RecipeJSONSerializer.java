/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Serialize Recipe objetcs in the same way that the desktop version so both version
 * can synchronize recipes.
 */
public class RecipeJSONSerializer extends JsonSerializer<Recipe>
{
    @Override
    public void serialize(Recipe recipe, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        jg.writeStartObject();
        jg.writeNumberField("id",  recipe.getRecipeId() != null ? recipe.getRecipeId() : -1);
        jg.writeStringField("title", recipe.getRecipeTitle() != null ? recipe.getRecipeTitle() : "");
        jg.writeStringField("recipe", recipe.getRecipe() != null ? recipe.getRecipe() : "");
        jg.writeStringField("date", recipe.getRecipeDate() != null ? sdf.format( recipe.getRecipeDate()): "");
        jg.writeStringField("update", recipe.getRecipeUpdate() != null ? sdf.format( recipe.getRecipeUpdate()) : "");
        jg.writeStringField( "origin", recipe.getRecipeOrigin() != null ? recipe.getRecipeOrigin() : "");
        jg.writeStringField( "shareId", recipe.getRecipeShareId() != null ? recipe.getRecipeShareId() : "");
        jg.writeArrayFieldStart("tags");
        if ( recipe.getTagsList() != null )
        {
            for ( Tag tag : recipe.getTagsList())
            {
                jg.writeString( tag.getTag());
            }
        }
        jg.writeEndArray();
        jg.writeEndObject();
    }
}
