/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import es.ait.recetario.desktop.Utils;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author aitkiar
 */
public class RecipeDAO
{

    /**
     * Stores a new recipe in the BBDD.
     * 
     * @param connection the connection that is used for the BBDD transaction.
     * @param recipe the recipe to be stored. The recipeId value it's calculated during insert and updated in the recipe object.
     * @throws SQLException 
     */
    public void create ( Connection connection, Recipe recipe ) throws SQLException
    {
        try
            ( PreparedStatement ps = connection.prepareStatement("insert into recipe ( recipe_title, recipe, recipe_date, recipe_update ) values ( ?, ?, ?, ? )", new String[]{"RECIPE_ID"}))
        {
            recipe.setRecipeDate( new Date());
            recipe.setRecipeUpdate( recipe.getRecipeDate());
            ps.setString( 1, recipe.getRecipeTitle() );
            ps.setAsciiStream( 2, new ByteArrayInputStream( recipe.getRecipe().getBytes("utf-8")));
            ps.setTimestamp(3, new Timestamp( recipe.getRecipeDate().getTime()));
            ps.setTimestamp(4, new Timestamp( recipe.getRecipeUpdate().getTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if ( rs == null || !rs.next())
            {
                throw new SQLException("Error retrieving generated key");
            }
            recipe.setRecipeId( rs.getInt(1));
            rs.close();
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new SQLException( e );
        }
        
        new TagDAO().updateTags( connection, recipe );
    }
}
