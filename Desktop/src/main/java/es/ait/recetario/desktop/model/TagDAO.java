/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario.desktop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        try
            (PreparedStatement ps = connection.prepareStatement("delete from recipe_tags where recipe_id=?"))
        {
            ps.setInt( 1, recipe.getRecipeId());
            ps.executeUpdate();
        }
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
                System.out.println( e.getErrorCode() + " -- " + e.getSQLState() );
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
}
