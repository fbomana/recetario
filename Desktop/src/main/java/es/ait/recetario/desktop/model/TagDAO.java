/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao object that encapsulate the BBDD operations related to the tables TAGS and RECIPE_TAGS.
 *
 * @author aitkiar
 */
public class TagDAO
{

    /**
     * Inserts all the new tags for the recipe, an delete the ones that no loner apply
     * @param connection The connection used for the transacction. Transacction depends on the connection autocomit status.
     * @param recipe a recipe object with at least the recipe_id and the tags filled.
     * @throws SQLException 
     */
    public void updateTags( Connection connection, Recipe recipe ) throws SQLException
    {
        deleteRecipeTags( connection, recipe.getRecipeId());
        
        for ( String tag : recipe.getTags() )
        {
            try
                (PreparedStatement ps = connection.prepareStatement( "insert into tags ( tag ) values ( ? )"))
            {
                ps.setString( 1, tag );
                ps.executeUpdate();
            }
            catch ( SQLException e )
            {
                // We ignore duplicate key errors.
                if (!(e.getErrorCode() == 20000 && "23505".equals(e.getSQLState())))
                {
                    throw e;
                }
            }
            
            try
                ( PreparedStatement ps = connection.prepareStatement("insert into recipe_tags ( recipe_id, tag ) values ( ?, ? )"))
            {
                ps.setInt( 1, recipe.getRecipeId());
                ps.setString( 2, tag );
                ps.executeUpdate();
            }
        }
    }
    
    /**
     * Delete al tags related to a recipe in the recpe_tags table and then clean
     * all orphan tags.
     * 
     * @param connection
     * @param recipeId
     * @throws SQLException 
     */
    public void deleteRecipeTags( Connection connection, int recipeId ) throws SQLException
    {
        try
            (PreparedStatement ps = connection.prepareStatement("delete from recipe_tags where recipe_id=?"))
        {
            ps.setInt( 1, recipeId );
            ps.executeUpdate();
        }
        
        try
            (PreparedStatement ps = connection.prepareStatement("delete from tags where tag not in ( select tag from recipe_tags )"))
        {
            ps.executeUpdate();
        }        
        
    }
    
    /**
     * Searchs for all the tags that aren't in the excluded tags list.
     * @param connection
     * @param excludedTags
     * @return A list of the tags alphabetivally ordered
     * @throws SQLException 
     */
    public List<String> searchTags( Connection connection, List<String> excludedTags ) throws SQLException
    {
        String sql = "select tag from tags";
        List<String> result = new ArrayList<>();
        if ( !excludedTags.isEmpty())
        {
            sql+= " where tag not in (";
            String separator = " ";
            for ( String tag : excludedTags )
            {
                sql+= separator + "?";
                separator = ", ";
            }
            sql += " )";
                    
        }
        sql += " order by tag";
        try
            (PreparedStatement ps = connection.prepareStatement( sql ))
        {
            int i = 1;
            for ( String tag : excludedTags )
            {
                ps.setString( i++, tag );
            }
            ResultSet rs = ps.executeQuery();
            while ( rs.next())
            {
                result.add( rs.getString("tag"));
            }
            rs.close();
        }
        return result;
    }
}
