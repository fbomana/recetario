/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
        create( connection, recipe, false );
    }
    
    /**
     * Stores a new recipe in the BBDD.
     * 
     * @param connection the connection that is used for the BBDD transaction.
     * @param recipe the recipe to be stored. The recipeId value it's calculated during insert and updated in the recipe object.
     * @throws SQLException 
     */
    private void create ( Connection connection, Recipe recipe, boolean importRecipe ) throws SQLException
    {
        try
            ( PreparedStatement ps = connection.prepareStatement("insert into recipe ( recipe_title, recipe, recipe_date, recipe_update, recipe_origin, recipe_share_id ) values ( ?, ?, ?, ?, ?, ? )", new String[]{"RECIPE_ID"}))
        {
            if ( ! importRecipe )
            {
                recipe.setRecipeDate( new Date());
                recipe.setRecipeUpdate( recipe.getRecipeDate());
            }
            ps.setString( 1, recipe.getRecipeTitle() );
            ps.setCharacterStream(2, new StringReader( recipe.getRecipe()));
            ps.setTimestamp(3, new Timestamp( recipe.getRecipeDate().getTime()));
            ps.setTimestamp(4, new Timestamp( recipe.getRecipeUpdate().getTime()));
            ps.setString( 5, recipe.getRecipeOrigin());
            ps.setString( 6, recipe.getRecipeShareId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if ( rs == null || !rs.next())
            {
                throw new SQLException("Error retrieving generated key");
            }
            recipe.setRecipeId( rs.getInt(1));
            rs.close();
        }
        
        new TagDAO().updateTags( connection, recipe );
    }
 
    /**
     * Updates all the information of a recipe in BBDD.
     * @param connection
     * @param recipe
     * @throws SQLException 
     */
    public void update ( Connection connection, Recipe recipe ) throws SQLException
    {
        update( connection, recipe, false );
    }
    
    /**
     * Updates all the information of a recipe in BBDD.
     * @param connection
     * @param recipe
     * @throws SQLException 
     */
    private void update ( Connection connection, Recipe recipe, boolean importRecipe ) throws SQLException
    {
        try
            ( PreparedStatement ps = connection.prepareStatement("update recipe set recipe_title=?, recipe=?,"
                + " recipe_date=?, recipe_update=?, recipe_origin=?, recipe_share_id=? where " + ( importRecipe ? "recipe_share_id=?" : "recipe_id = ?")))
        {
            recipe.setRecipeUpdate( new Date());
            ps.setString( 1, recipe.getRecipeTitle() );
            ps.setCharacterStream(2, new StringReader( recipe.getRecipe()));
            ps.setTimestamp(3, new Timestamp( recipe.getRecipeDate().getTime()));
            ps.setTimestamp(4, new Timestamp( recipe.getRecipeUpdate().getTime()));
            ps.setString( 5, recipe.getRecipeOrigin());
            ps.setString( 6, recipe.getRecipeShareId());
            if ( importRecipe )
            {
                ps.setString( 7, recipe.getRecipeShareId());    
            }
            else
            {
                ps.setInt( 7, recipe.getRecipeId());
            }
            ps.executeUpdate();
        }
        
        new TagDAO().updateTags( connection, recipe );
    }
    
    /**
     * Import changes on a recipe
     * @param connection
     * @param recipe
     * @throws SQLException 
     */
    public void importRecipe( Connection connection, Recipe recipe ) throws SQLException
    {
        int localId = getLocalId(connection, recipe.getRecipeShareId());
        if ( localId != -1 )
        {
            recipe.setRecipeId( localId );
            update( connection, recipe, true );
        }
        else
        {
            create( connection, recipe, true );
        }
    }
    
    /**
     * Returns the localId for a imported recipe.
     * @param connection
     * @param shareId
     * @return
     * @throws SQLException 
     */
    public int getLocalId( Connection connection, String shareId ) throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement( "select recipe_id from recipe where recipe_share_id = ?"))
        {
            ps.setString( 1, shareId );
            try ( ResultSet rs = ps.executeQuery())
            {
                if ( rs.next())
                {
                    return rs.getInt("recipe_id");
                }
                return -1;
            }
        }
    }
    
    public void delete ( Connection connection, int id  ) throws SQLException
    {
        new TagDAO().deleteRecipeTags( connection, id );
        try
            ( PreparedStatement ps = connection.prepareStatement("delete from recipe where recipe_id = ?"))
        {
            ps.setInt( 1, id );
            ps.executeUpdate();
        }
    }
    
    /**
     * Searchs for recipes using the tags passed as parameter.
     * @param connection the connections for the search
     * @param tags the tags list.
     * @param inclusive indicates if whe search for recipe with one of the tags, or the recipes with all the tags.
     * @return
     * @throws SQLException 
     */
    public List<Recipe> search( Connection connection, List<String> tags, boolean inclusive ) throws SQLException
    {
        if ( inclusive )
        {
            return inclusiveSearch( connection, tags );
        }
        return exclusiveSearch( connection, tags );
    }
    
    /**
     * Searchs for the recipes that have any of the tags passed as parameter. If no 
     * tags passed shows all recipes.
     * @param connection
     * @param tags
     * @return return a list with all the recipes but whihtout the recipe.
     * @throws SQLException 
     */
    private List<Recipe> inclusiveSearch( Connection connection, List<String> tags ) throws SQLException
    {
        String sql = "select a.recipe_id, a.recipe_title, a.recipe_date, "
            + "a.recipe_update, a.recipe_origin, a.recipe_share_id, b.tag from recipe a, recipe_tags b where "
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
     * Searchs for the recipes that have all of the tags passed as parameter. If no 
     * tags passed shows all recipes.
     * @param connection
     * @param tags
     * @return return a list with all the recipes but whihtout the recipe.
     * @throws SQLException 
     */
    private List<Recipe> exclusiveSearch( Connection connection, List<String> tags ) throws SQLException
    {
        String sql = "select a.recipe_id, a.recipe_title, a.recipe_date, "
            + "a.recipe_update, a.recipe_origin, a.recipe_share_id, b.tag from recipe a, recipe_tags b where "
            + "a.recipe_id = b.recipe_id ";
        List<Recipe> result = new ArrayList<>();
        if ( tags != null && !tags.isEmpty())
        {
            
            sql+= " and a.recipe_id in ( select f.recipe_id from ( select c.recipe_id, count ( * ) as cuenta "
                + " from recipe_tags c where tag in (";
            for( int i = 0; i < tags.size(); i ++ )
            {
                if ( i > 0 )
                {
                    sql+= ", ";
                }
                sql += "?";
            }
            sql += ") group by c.recipe_id ) f where cuenta >= ? )";
        }
        sql += " order by recipe_update desc";
        try
            (PreparedStatement ps = connection.prepareStatement(sql))
        {
            for ( int i = 0; i < tags.size(); i ++ )
            {
                ps.setString(i+1, tags.get( i ));
            }
            if ( tags.size() > 0 )
            {
                ps.setInt( tags.size() + 1, tags.size() );
            }
            ResultSet rs = ps.executeQuery();
            Recipe recipe = null;
            while ( rs.next())
            {
                if ( recipe == null || rs.getInt( "recipe_id" ) != recipe.getRecipeId() )
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

    public List<Recipe> shareIdSearch( Connection connection, List<String> shareIds ) throws SQLException
    {
        String sql = "select a.recipe_id, a.recipe_title, a.recipe, a.recipe_date, "
            + "a.recipe_update, a.recipe_origin, a.recipe_share_id, b.tag from recipe a, recipe_tags b where "
            + "a.recipe_id = b.recipe_id ";
        List<Recipe> result = new ArrayList<>();
        if ( shareIds != null && !shareIds.isEmpty())
        {
            
            sql+= " and a.recipe_share_id in ( ";
            for( int i = 0; i < shareIds.size(); i ++ )
            {
                if ( i > 0 )
                {
                    sql+= ", ";
                }
                sql += "?";
            }
            sql += ") ";
        }
        sql += " order by recipe_update desc, a.recipe_id, b.tag";
        try
            (PreparedStatement ps = connection.prepareStatement(sql))
        {
            for ( int i = 0; i < shareIds.size(); i ++ )
            {
                ps.setString(i+1, shareIds.get( i ));
            }
            ResultSet rs = ps.executeQuery();
            Recipe recipe = null;
            while ( rs.next())
            {
                if ( recipe == null || rs.getInt( "recipe_id" ) != recipe.getRecipeId() )
                {
                    recipe = readRecipe( rs, true );
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
        recipe.setRecipeOrigin( rs.getString("recipe_origin"));
        recipe.setRecipeShareId( rs.getString("recipe_share_id"));
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
