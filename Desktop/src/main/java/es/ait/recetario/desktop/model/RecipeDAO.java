/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO that encapsulates all BBDD operations over the table "RECIPE".
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
    
    /**
     * Searchs for the recipes that have any of the tags passed as parameter. If no 
     * tags passed shows all recipes.
     * @param connection
     * @param tags
     * @return return a list with all the recipes but whihtout the recipe.
     * @throws SQLException 
     */
    public List<Recipe> search( Connection connection, List<String> tags ) throws SQLException
    {
        String sql = "select a.recipe_id, a.recipe_title, a.recipe_date, "
            + "a.recipe_update, b.tag from recipe a, recipe_tags b where "
            + "a.recipe_id = b.recipe_id ";
        List<Recipe> result = new ArrayList<>();
        if ( tags != null && !tags.isEmpty())
        {
            sql+= "and tag in ( ";
            for( int i = 0; i < tags.size(); i ++ )
            {
                if ( i > 0 )
                {
                    sql+= ", ";
                }
                sql += "?";
            }
            sql += ")";
        }
        sql += " order by recipe_update desc";
        try
            (PreparedStatement ps = connection.prepareStatement(sql))
        {
            for ( int i = 0; i < tags.size(); i ++ )
            {
                ps.setString(i+1, tags.get( i ));
            }
            ResultSet rs = ps.executeQuery();
            Recipe recipe = null;
            while ( rs.next())
            {
                if ( recipe == null || rs.getInt( "recipe_id") != recipe.getRecipeId() )
                {
                    recipe = readRecipe( rs, false );
                    result.add( recipe );
                }
                recipe.addTag( rs.getString( "tag"));
            }
            rs.close();
        }
        return result;
    }
    
    /**
     * Searchs for a specific recipe and get all the information about it.
     * @param connection
     * @param recipeId
     * @return
     * @throws SQLException 
     */
    public Recipe search( Connection connection, int recipeId ) throws SQLException
    {
        String sql = "select a.*, b.tag from recipe a, recipe_tags b where a.recipe_id = b.recipe_id and a.recipe_id = ?";
        Recipe result = null;
        try
            (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt( 1, recipeId );
            ResultSet rs = ps.executeQuery();
            while ( rs.next() )
            {
                if ( result == null )
                {
                    result = readRecipe( rs, true );
                }
                result.addTag( rs.getString("tag"));
            }
            rs.close();
        }
        return result;
    }
    
    /**
     * Reads a recipe from the resultset
     * @param rs
     * @param readClob
     * @return 
     */
    private Recipe readRecipe( ResultSet rs, boolean readClob ) throws SQLException
    {
        Recipe recipe = new Recipe();
        recipe.setRecipeId( rs.getInt("recipe_id"));
        recipe.setRecipeTitle( rs.getString("recipe_title"));
        recipe.setRecipeDate( rs.getTimestamp("recipe_date"));
        recipe.setRecipeUpdate( rs.getTimestamp("recipe_update"));
        if ( readClob )
        {
            Clob clob = rs.getClob("recipe");
            try
                ( BufferedReader reader = new BufferedReader( clob.getCharacterStream() ))
            {
                String line;
                String recipeText = "";
                while ( (line = reader.readLine()) != null )
                {
                    recipeText += line + "\n";
                }
                recipe.setRecipe( recipeText );
            }
            catch ( IOException e )
            {
                throw new SQLException ( e );
            }
        }
        return recipe;
    }
}
